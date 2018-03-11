<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>고객 등록 및 수정</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//입력 수정 판단
			$("#mode").val('${reg_mode}');
			$("#sellSeq").val("");
			
			//common.js 고객목록 select 생성
			fnCustNmList();
			
			//common.js 상품목록 select 생성
			fnProdNmList();
			
			//common.js 단위목록 select 생성
			fnUnitNmList();
			
			//common.js 차량목록 select 생성
			fnTruckNmList();
			
			if("U" == '${reg_mode}'){
				//상세 내용을 조회해서 input에 셋팅함.
				fnSellInfo();
			} 
			
			
			//달력 버튼 이벤트
			$("#sellDt").datepicker({
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
				fnSave();
			});
		});
		
		/**
		 * 저장
		 */
		function fnSave(){
			//datalist 객체에서 value찾기
			var vCustNo = $('#custNo').val();
			var vCustNoVal = $('#custList option').filter(function() {
				return this.value == vCustNo;
			}).data('value');
		
			var vProdNo = $('#prodNo').val();
			var vProdVal = $('#prodList option').filter(function() {
				return this.value == vProdNo;
			}).data('value');			
			
			var vUnit = $('#unit').val();
			var vUnitVal = $('#unitList option').filter(function() {
				return this.value == vUnit;
			}).data('value');
			
			var vTruckNo = $('#truckNo').val();
			var vTruckNoVal = $('#truckList option').filter(function() {
				return this.value == vTruckNo;
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
			if(vUnit == ""){
				if(typeof vUnitVal == "undefined" || null == vUnitVal){
					vUnitVal = "";
				} 				
			}
			if(vTruckNo == ""){
				if(typeof vTruckNoVal == "undefined" || null == vTruckNoVal){
					vTruckNoVal = "";
				} 				
			}
			commonSubmit.setUrl("<c:url value='/sell/setSell.do' />");
			commonSubmit.addParam("sell_dt"		, gfn_nvl($("#sellDt").val()));			//판매일자
			commonSubmit.addParam("cust_no"		, vCustNoVal);							//고객번호
			commonSubmit.addParam("prod_no"		, vProdVal);							//상품
			commonSubmit.addParam("sell_quan"	, gfn_nvl($("#sellQuan").val()));		//수량
			commonSubmit.addParam("unit"		, vUnitVal);							//단위
			commonSubmit.addParam("truck_no"	, vTruckNoVal);							//배차
			commonSubmit.addParam("mode"		, $("#mode").val());					//입력 I, 수정 U
			if("U" == $("#mode").val()){
				commonSubmit.addParam("sell_seq", gfn_nvl($("#sellSeq").val()));		//단위
			}
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}

		/**
		 * 판매 내역 조회하기
		 */
		function fnSellInfo(){
			commonAjax.setUrl("<c:url value='/sell/getSellInfo.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("sell_dt", "${sell_dt}");
			commonAjax.addParam("cust_no", "${cust_no}");
			commonAjax.addParam("sell_seq", "${sell_seq}");			
			commonAjax.setCallback("fnSellInfoCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 판매 내역 조회하기 callback
		 */
		function fnSellInfoCallback(result){	
			$("#sellDt").val(result.sellInfo.sell_dt);
			$("#custNo").val($('#custList [data-value="'+result.sellInfo.cust_no+'"]').val());
			$("#prodNo").val($('#prodList [data-value='+result.sellInfo.prod_no+']').val());
			$("#truckNo").val($('#truckList [data-value='+result.sellInfo.truck_no+']').val());
			$("#sellQuan").val(result.sellInfo.sell_quan);
			$("#unit").val($('#unitList [data-value='+result.sellInfo.unit+']').val());
			$("#sellSeq").val(result.sellInfo.sell_seq);
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			판매 등록 및 수정
		</div>
		
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<colgroup>
					<col width="10%"/>
					<col width="25%"/>
					<col width="10%"/>
					<col width="25%"/>
					<col width="10%"/>
					<col width="20%"/>
				</colgroup>
				<tbody>
					<tr class="detail_view">
						<th class="list_th">판매일자</th>
						<td class="list_td align-left">
							<input type="text" class="search_input" id="sellDt" readonly />
						</td>
						<th class="list_th">고객명</th>
						<td class="list_td align-left">
							<input type="text" id="custNo" list="custList" autocomplete="on" maxlength="50">
							<datalist id="custList">
						    </datalist>	
						</td>
						<th class="list_th">배차</th>
						<td class="list_td align-left">
							<input type="text" id="truckNo" list="truckList" autocomplete="on" maxlength="50">
							<datalist id="truckList">
						    </datalist>	
						</td>
					</tr>
					<tr class="detail_view">
						<th class="list_th">상품</th>
						<td class="list_td align-left">
							<input type="text" id="prodNo" list="prodList" autocomplete="on" maxlength="50">
							<datalist id="prodList">
							</datalist>
						</td>
						<th class="list_th">수량</th>
						<td class="list_td align-left">
							<input type="text" class="search_input" id="sellQuan" />
						</td>
						<th class="list_th">단위</th>
						<td class="list_td align-left">
							<input type="text" id="unit" list="unitList" autocomplete="on" maxlength="50">
							<datalist id="unitList">
							</datalist>
						</td>
					</tr>
				</tbody>
			</table>	
			<input type="hidden" id="mode" />
			<input type="hidden" id="sellSeq" />
		</div>
		<div class="list_btn_group" id="btnGroup">			
			<button class="btn_default" id="btnSave">저장</button>				
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
