<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>반품 내역  조회</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 고객목록 select 생성
			fnCustNmList();
			
			//common.js 상품목록 select 생성
			fnProdNmList();
			
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnSellReturnList();		
				$(this).attr("disabled", false);
			});
			
			//달력 버튼 이벤트
			$("#returnDt").datepicker({
				dateFormat: "yy-mm-dd",
				showOn: "both",
				buttonImageOnly: true,	
				defaultDate: "+0d",
			    changeMonth: true,
			    changeYear: true,
			    numberOfMonths: 1,
			    onClose: function( selectedDate ) {}
			});
		
			//반품삭제 버튼 이벤트
			$("#btnReturnDel").on("click", function(){
				fnSetDelSellReturn();
			});
			
			//반품수량수정 버튼 이벤트
			$("#btnReturnMod").on("click", function(){
				fnSetModSellReturn();
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
		 * 반품 목록 조회하기
		 */
		function fnSellReturnList(){
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
			commonAjax.setUrl("<c:url value='/sellReturn/sellReturnList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("return_dt", gfn_nvl($("#returnDt").val()));
			commonAjax.addParam("cust_no", vCustNoVal);
			commonAjax.addParam("prod_no", vProdVal);
			
			commonAjax.setCallback("fnSellReturnListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 반품 목록 조회하기 callback
		 */
		function fnSellReturnListCallback(result){
			var dataList;
			$("#board_list tbody").empty();

			$.each(result.list, function(i, val){
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listSellSeq">' + gfn_nvl(val.sell_seq) 	+ '</td>';		//일련번호
				dataList += '<td name="listReturnDt">' + gfn_nvl(val.return_dt) 	+ '</td>';	//반품일자
				dataList += '<td name="listSellDt">' + gfn_nvl(val.sell_dt) 	+ '</td>';		//판매일자
				dataList += '<td>' + gfn_nvl(val.cust_nm) 	+ '</td>';							//고객명
				dataList += '<td>' + gfn_nvl(val.prod_nm) 	+ '</td>';							//상품
				dataList += '<td name="listSellQuan">' + gfn_nvl(val.sell_quan) + '(<input name="returnQuan" class="return_unit" value="'+ gfn_nvl(val.return_quan) +'"/>)</td>';		//수량
				dataList += '<td>' + gfn_nvl(val.unit_nm) 	+ '</td>';		//단위
				dataList += '<td>' + gfn_nvl(val.tax_free) 	+ '</td>';		//면세
				dataList += '<td>' + gfn_nvl(val.supply) 	+ '</td>';		//공급가액
				dataList += '<td>' + gfn_nvl(val.tax) 		+ '</td>';		//부가세
				dataList += '<td>' + gfn_nvl(val.total_price) + '</td>';	//계
				dataList += '<td name="listSellCustNo" style="display:none">' + gfn_nvl(val.cust_no) + '</td>';	//고객번호
				dataList += '<td name="listReturnSeq" style="display:none">' + gfn_nvl(val.return_seq) 	+ '</td>';	//일련번호
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});
		}

		/**
		 * 반품 삭제
		 */
		function fnSetDelSellReturn() {
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//선택된 반품 항목을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {			
				vJsonParam = { "return_dt" : this.children.listReturnDt.textContent
						  		, "return_seq" : this.children.listReturnSeq.textContent
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sellReturn/setDelSellReturn.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnSetDelSellReturnCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 반품삭제 callback
		 */
		function fnSetDelSellReturnCallback(response) {
			if(response.result.returnMsg == ""){			
				alert(response.result.returnCnt + "건 삭제 완료 되었습니다.");
			} else{
				alert(response.result.returnMsg + " 확인이 필요합니다.");
			}
			fnSellReturnList();
		}
		
		/**
		 * 반품수량 수정 
		 */
		function fnSetModSellReturn() {
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//선택된 반품 항목을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {			
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent
						  		, "cust_no" : this.children.listSellCustNo.textContent
						  		, "sell_seq" : this.children.listSellSeq.textContent
						  		, "return_quan" : this.children.listSellQuan.children.returnQuan.value
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sellReturn/setModSellReturn.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnSetSellReturnCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 반품수량 수정 callback
		 */
		function fnSetSellReturnCallback(response) {
			if(response.result.returnMsg == ""){			
				alert(response.result.returnCnt + " 건 수량 수정이 완료 되었습니다.");
			} else{
				alert(response.result.returnMsg + " 확인이 필요합니다.");
			}
			fnSellReturnList();
		}
		
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			반품 내역 조회
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
						<th>반품 일자</th>
						<td class="align-left date" colspan="5">
							<input type="text" class="search_input" id="returnDt" readonly />
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
						<th width="3%"></th>
						<th width="5%">일련<br>번호</th>
						<th width="7%">반품일자</th>
						<th width="7%">판매일자</th>
						<th width="15%">고객명</th>
						<th width="10%">상품</th>
						<th width="10%">판매수량<br>(반품수량)</th>
						<th width="5%">단위</th>
						<th width="5%">면세</th>
						<th width="10%">공급가액</th>
						<th width="10%">부가세</th>
						<th width="10%">계</th>
						<th width="10%" style="display:none">고객번호</th>
						<th width="10%" style="display:none">반품일련번호</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>			
		</div>
		<div class="list_btn_group" id="btnGroup">			
			<button class="btn_default" id="btnReturnDel">반품취소(삭제)</button>				
			<button class="btn_default" id="btnReturnMod">반품수량수정</button>
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
