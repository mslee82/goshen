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
			
			//저장 버튼 이벤트
			$("#btnSave").on("click", function(){
				if(confirm("저장 하시겠습니까?")){
					fnComProductPriceSave();
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
			
			$(document).on("keydown", "#board_list > tbody > tr > td[name='listBuyProdPrice'] > input[name='buyProdPrice']", function(event){
				if (event.keyCode == 9) {
					alert("1");
				}
			});		
		});
		
		/**
		 * 단가표 조회하기
		 */		
		function fnProductPriceList(){
			//datalist 객체에서 value찾기
			var vProdNo = $('#srchProdNo').val();
			var vProdVal = $('#prodList option').filter(function() {
				return this.value == vProdNo;
			}).data('value');
			
			if(vProdNo == ""){
				if(typeof vProdVal == "undefined" || null == vProdVal){
					vProdVal = "";
				} 				
			}
			commonAjax.setUrl("<c:url value='/product/comProductPriceList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("srch_dt", gfn_nvl($("#srchDt").val()));
			commonAjax.addParam("prod_no", vProdVal);
			commonAjax.setCallback("fnProductPriceListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 단가표 조회하기 callback
		 */	
		function fnProductPriceListCallback(result){
			var dataList;
			$("#board_list_l tbody").empty();
			$("#board_list_r tbody").empty();
						
			$.each(result.list, function(i, val){				
				dataList  = '<tr class="list_row">';
				dataList += '<td name="listProdNm">' 	+ gfn_nvl(val.prod_nm) 	+ '</td>';						//상품
				dataList += '<td name="listSellQuan">' 	+ gfn_nvl(val.sell_quan) + '</td>';						//수량합계
				dataList += '<td name="listUnitNm">' 	+ gfn_nvl(val.unit_nm) 	+ '</td>';						//단위명
				dataList += '<td name="listBuyProdPrice"><input type="text" name="buyProdPrice" class="price" value="' + gfn_nvl(val.buy_prod_price) + '"/></td>';		//구매단가				
				dataList += '<td name="listSellProdPrice"><input type="text" name="sellProdPrice" class="price" value="' + gfn_nvl(val.sell_prod_price) + '"/></td>';	//판매단가
				dataList += '<td name="listProdNo" style="display:none">' + gfn_nvl(val.prod_no) + '</td>';		//상품번호
				dataList += '<td name="listUnit" style="display:none">'	+ gfn_nvl(val.unit) 	+ '</td>';		//단위
				dataList += '<td name="listSellDt" style="display:none">' + gfn_nvl(val.sell_dt) + '</td>';		//판매일자
				dataList += '</tr>';
				if(i % 2 == 0){
					$("#board_list_l tbody").append(dataList);
				} else{
					$("#board_list_r tbody").append(dataList);
				}	
			});
			
			//숫자만.
			$(".price").numeric();
		}
		
		/**
		 * 단가표 저장
		 */
		function fnComProductPriceSave() {			
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//목록을 array에 담아 전달
			$("#board_list_l tbody tr").each(function (i) {			
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent							//판매일자
								, "prod_no" : this.children.listProdNo.textContent						//상품번호
								, "unit" : this.children.listUnit.textContent							//단위
						  		, "buy_prod_price" : this.children.listBuyProdPrice.children.buyProdPrice.value	//구매금액
						  		, "sell_prod_price" : this.children.listSellProdPrice.children.sellProdPrice.value	//판매금액						  		
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			$("#board_list_r tbody tr").each(function (i) {			
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent							//판매일자
								, "prod_no" : this.children.listProdNo.textContent						//상품번호
								, "unit" : this.children.listUnit.textContent							//단위
						  		, "buy_prod_price" : this.children.listBuyProdPrice.children.buyProdPrice.value	//구매금액
						  		, "sell_prod_price" : this.children.listSellProdPrice.children.sellProdPrice.value	//판매금액						  		
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/product/setComProductPrice.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnComProductPriceSaveCallback");
			commonAjax.ajax();	
		}
		
		function fnComProductPriceSaveCallback(response) {
			if(response.result.returnYn == "Y"){			
				alert("완료 되었습니다.");
			} else{
				alert("확인이 필요합니다.");
			}
		} 		
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			공통 단가표 조회
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
						<td class="align-left date">
							<input type="text" class="search_input" id="srchDt" readonly />
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
		<div class="list_btn_group" id="btnGroup">		
			<button class="btn_default" id="btnSave">단가표 저장</button>
		</div>
		<div class="block_list">
			<div class="match_board floatL">
				<table id="board_list_l" class="list_table colwidth">
					<thead>
						<tr>
							<th width="10%">상품</th>
							<th width="10%">수량합계</th>
							<th width="10%">단위</th>
							<th width="10%">구매단가</th>
							<th width="10%">판매단가</th>
							<th width="5%" style="display:none">상품번호</th>
							<th width="5%" style="display:none">단위</th>
							<th width="5%" style="display:none">판매일자</th>
						</tr>
					</thead>
					<tbody>
				
					</tbody>
				</table>	
			</div>	
			<div class="match_board floatR">
				<table id="board_list_r" class="list_table colwidth">
					<thead>
						<tr>
							<th width="10%">상품</th>
							<th width="10%">수량합계</th>
							<th width="10%">단위</th>
							<th width="10%">구매단가</th>
							<th width="10%">판매단가</th>
							<th width="5%" style="display:none">상품번호</th>
							<th width="5%" style="display:none">단위</th>
							<th width="5%" style="display:none">판매일자</th>
						</tr>
					</thead>
					<tbody>
				
					</tbody>
				</table>	
			</div>	
		</div>
		
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
