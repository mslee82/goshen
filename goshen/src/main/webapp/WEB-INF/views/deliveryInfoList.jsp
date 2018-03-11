<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>Receipt</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 차량목록 select 생성
			fnTruckNmList();
			
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
			
			//조회
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnDeliveryList();		
				$(this).attr("disabled", false);
			});
			
			//다운로드 버튼 이벤트
			$("#btnDownload").on("click", function(){
				fnDownload();
			});
		});
		
		/**
		 * 배달 목록 조회하기
		 */
		function fnDeliveryList(){
			//datalist 객체에서 value찾기
			var vTruckNo = $('#srchTruckNo').val();
			var vTruckNoVal = $('#truckList option').filter(function() {
				return this.value == vTruckNo;
			}).data('value');
			if(vTruckNo == ""){
				if(typeof vTruckNoVal == "undefined" || null == vTruckNoVal){
					vTruckNoVal = "";
				} 				
			}
			commonAjax.setUrl("<c:url value='/sell/deliveryInfoList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("truck_no", vTruckNoVal);
			commonAjax.addParam("sell_dt", gfn_nvl($("#sellDt").val()));
			commonAjax.setCallback("fnDeliveryListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 배달 목록 조회하기 callback
		 */
		function fnDeliveryListCallback(result){
			var dataList;
			$("#board_list tbody").empty();
			
			$.each(result.list, function(i, val){
				dataList  = '<tr>';
				dataList += '<td>' + (i+1) + '</td>';
				dataList += '<td>' + gfn_nvl(val.prod_nm) + '</td>';			//품명
				dataList += '<td>' + gfn_nvl(val.delivery_detail) + '</td>';	//수량						
				dataList += '</tr>';				
				$("#board_list tbody").append(dataList);
			});
		}

		function fnDownload(){
			//datalist 객체에서 value찾기
			var vTruckNo = $('#srchTruckNo').val();
			var vTruckNoVal = $('#truckList option').filter(function() {
				return this.value == vTruckNo;
			}).data('value');
			if(vTruckNo == ""){
				if(typeof vTruckNoVal == "undefined" || null == vTruckNoVal){
					vTruckNoVal = "";
				} 				
			}
			commonSubmit.setUrl("<c:url value='/sell/downloadDeliveryList.do' />");
			commonSubmit.addParam("truck_no", vTruckNoVal);
			commonSubmit.addParam("sell_dt", gfn_nvl($("#sellDt").val()));
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			배송기사를 위한 배송 내역 조회
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
						<th>판매 일자</th>
						<td class="align-left date">
							<input type="text" class="search_input" id="sellDt" readonly />
						</td>
						<th>차량구분</th>
						<td colspan="3">
							<input type="text" id="srchTruckNo" list="truckList" autocomplete="on" maxlength="50">
							<datalist id="truckList">
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
			<button class="btn_default" type="button" id="btnDownload">다운로드</button>
		</div>
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="15%">품명</th>
						<th width="80%">배달목록</th>						
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>
			
		</div>
		
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
