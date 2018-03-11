<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>단가표  조회</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 고객목록 select 생성
			fnCustNmList();
			
			//common.js 상품목록 select 생성
			fnProdNmList();
			
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnProductPriceList();		
				$(this).attr("disabled", false);
			});
			
			$("#srchDt").datepicker({
				dateFormat: "yy-mm-dd",
				showOn: "both",
				buttonImageOnly: true,	
				defaultDate: "+0d",
			    changeMonth: true,
				dayNames: ['일', '월', '화', '수', '목', '금', '토'],
             	dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
             	monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
             	monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			    changeYear: true,
			    numberOfMonths: 1,
			    onClose: function( selectedDate ) {}
			});
			
			//수정 버튼 이벤트
			$("#btnMod").on("click", function(){
				if(confirm("수정 하시겠습니까?")){
					fnSetSellReturn();
				}
			});
			//삭제 버튼 이벤트
			$("#btnDel").on("click", function(){
				if(confirm("삭제 하시겠습니까?")){
					fnSetDelProductPrice();
				}
			});
			
			//그리드 선택
			$(document).on("click", "#board_list > tbody > tr > td", function(event){
				if (event.target.type == 'checkbox') return;
				var params = $(this).parents("tr");		
				if($(params).find("input[type='checkbox']").is(":checked")){
					$(params).removeClass('active');
					$(params).find("input[type=checkbox]").prop("checked", false);					
				} else{
					$(params).addClass('active');
					$(params).find("input[type=checkbox]").prop("checked", true);					
				}
			});	
		});
		
		/**
		 * 단가표 조회하기
		 */		
		function fnProductPriceList(){
			//datalist 객체에서 value찾기
			var vCustNo = $('#srchCustNo').val();
			var vCustNoVal = $('#custList option').filter(function() {
				return this.value == vCustNo;
			}).data('value');
			
			var vProdNo = $('#srchProdNo').val();
			var vProdVal = $('#prodList option').filter(function() {
				return this.value == vProdNo;
			}).data('value');
			
			if(vCustNo == ""){
				if(typeof vCustNoVal == "undefined" || null == vCustNoVal){
					vCustNoVal = "";
				} 				
			}
			if(vProdNo == ""){
				if(typeof vProdVal == "undefined" || null == vProdVal){
					vProdVal = "";
				} 				
			}
			commonAjax.setUrl("<c:url value='/product/productPriceList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("srch_dt", gfn_nvl($("#srchDt").val()));
			commonAjax.addParam("cust_no", vCustNoVal);
			commonAjax.addParam("prod_no", vProdVal);
			commonAjax.setCallback("fnProductPriceListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 단가표 조회하기 callback
		 */	
		function fnProductPriceListCallback(result){
			var dataList;
			$("#board_list tbody").empty();
			
			$.each(result.list, function(i, val){				
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listCustNm">' 	+ gfn_nvl(val.cust_nm) 	+ '</td>';			//고객
				dataList += '<td name="listProdNm">' 	+ gfn_nvl(val.prod_nm) 	+ '</td>';			//상품
				dataList += '<td name="listProdSeq">' 	+ gfn_nvl(val.prod_seq) + '</td>';			//일련번호
				dataList += '<td name="listStartDt">' 	+ gfn_nvl(val.start_dt) + '</td>';			//시작일자
				dataList += '<td name="listEndDt">' 	+ gfn_nvl(val.end_dt) 	+ '</td>';			//종료일자
				dataList += '<td name="listProdPrice">' + gfn_nvl(val.prod_price) + '</td>';		//단가
				dataList += '<td name="listUnitNm">' 	+ gfn_nvl(val.unit_nm) 	+ '</td>';			//단위
				dataList += '<td name="listUnit" style="display:none">' 	+ gfn_nvl(val.unit)    + '</td>';	//단위명
				dataList += '<td name="listCustNo" style="display:none">' 	+ gfn_nvl(val.cust_no) + '</td>';	//고객번호
				dataList += '<td name="listProdNo" style="display:none">' 	+ gfn_nvl(val.prod_no) + '</td>';	//상품번호
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});
		}
		
		/**
		 * 단가표 삭제 
		 */
		function fnSetDelProductPrice() {
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//선택된 반품 항목을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {			
				vJsonParam = { "cust_no" : this.children.listSellCustNo.textContent
						  		, "prod_no" : this.children.listProdNo.textContent
						  		, "prod_seq" : this.children.listProdSeq.textContent
						  		, "unit" : this.children.listUnit.textContent
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/product/setDelProductPrice.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnSetDelProductPriceCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 단가표 삭제 callback
		 */
		function fnSetDelProductPriceCallback(response) {
			if(response.result.returnMsg == ""){			
				alert(response.result.returnCnt + "건 삭제 완료 되었습니다.");
			} else{
				alert(response.result.returnMsg + " 확인이 필요합니다.");
			}
			fnSellList();
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			단가표 조회
		</div>
		<div class="search_Tblock">
			<table class="search_table">
				<colgroup>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>					
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>				
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th>기준 일자</th>
						<td class="align-left date" colspan="3">
							<input type="text" class="search_input" id="srchDt" readonly />
						</td>						
					</tr>	
					<tr class="search_row">		
						<th>고객명</th>
						<td>
							<input type="text" id="srchCustNo" list="custList" autocomplete="on" maxlength="50">
							<datalist id="custList">
						    </datalist>	
						</td>
						<th>상품</th>
						<td colspan="3">
							<input type="text" id="srchProdNo" list="prodList" autocomplete="on" maxlength="50">
							<datalist id="prodList">
							</datalist>				
						</td>
					</tr>				
				</tbody>
			</table>
			<div class="search_submit">
				<input class="main_srch" type="button" value="조회"/>
			</div>
		</div>
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="5%">고객</th>
						<th width="5%">상품</th>
						<th width="5%">일련번호</th>
						<th width="10%">시작일자</th>
						<th width="10%">종료일자</th>
						<th width="10%">단가</th>
						<th width="10%">단위</th>
						<th width="10%" style="display:none">단위명</th>
						<th width="5%" style="display:none">고객번호</th>
						<th width="5%" style="display:none">상품번호</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>
			
		</div>
		<div class="list_btn_group" id="btnGroup">			
			<button class="btn_default" id="btnDel">단가표 삭제</button>				
			<button class="btn_default" id="btnMod">단가표 수정</button>
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
