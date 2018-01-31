<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>Receipt</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 고객목록 select 생성
			fnCustNmList();
			
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnPreviewReceipt();		
				$(this).attr("disabled", false);
			});
			
			//달력 버튼 이벤트
			$("#fromSellDt").datepicker({
				dateFormat: "yy-mm-dd",
				showOn: "both",							
				buttonImageOnly: true,	
				defaultDate: "+0d",
			    changeMonth: true,
			    changeYear: true,
			    numberOfMonths: 1,
			    onClose: function( selectedDate ) {}
			});
			$("#toSellDt").datepicker({
				dateFormat: "yy-mm-dd",
				showOn: "both",
				buttonImageOnly: true,	
				defaultDate: "+0d",
			    changeMonth: true,
			    changeYear: true,
			    numberOfMonths: 1,
			    onClose: function( selectedDate ) {}
			});
		});
		
		function fnPreviewReceipt(){
			//datalist 객체에서 value찾기
			var vCustNo = $('#srchCustNo').val();
			var vCustNoVal = $('#custList option').filter(function() {
				return this.value == vCustNo;
			}).data('value');
			if(vCustNo == ""){
				if(typeof vCustNoVal == "undefined" || null == vCustNoVal){
					vCustNoVal = "";
				} 				
			}
			commonAjax.setUrl("<c:url value='/receipt/previewReceipt.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("cust_no", vCustNoVal);
			commonAjax.addParam("fromSellDt", gfn_nvl($("#fromSellDt").val()));
			commonAjax.addParam("toSellDt", gfn_nvl($("#toSellDt").val()));
			commonAjax.setCallback("fnPreviewReceiptCallback");
			commonAjax.ajax();	
		}
		
		function fnPreviewReceiptCallback(result){
			var dataList;
			$("#board_list tbody").empty();
			
			$.each(result.list, function(i, val){
				
				dataList  = '<tr>';
				dataList += '<td>' + (i+1) + '</td>';
				dataList += '<td>' + gfn_nvl(val.prod_nm) + '</td>';			//품명
				dataList += '<td>' + gfn_nvl(val.sell_quan) + '</td>';			//수량
				dataList += '<td>' + gfn_nvl(val.unit_nm) + '</td>';			//단위
				dataList += '<td>' + gfn_nvl(val.prod_price) + '</td>';			//단가
				if("N" == gfn_nvl(val.tax_yn)){
					dataList += '<td>' + gfn_nvl(val.total_price) + '</td>';	//면세
					dataList += '<td></td>';
					dataList += '<td></td>';
					dataList += '<td></td>';
					dataList += '<td></td>';
				} else{
					dataList += '<td></td>';
					dataList += '<td>' + gfn_nvl(val.supply) + '</td>';			//공급가액
					dataList += '<td>' + gfn_nvl(val.tax) + '</td>';			//부가세
					dataList += '<td>' + gfn_nvl(val.total_price) + '</td>';	//계
					dataList += '<td>' + gfn_nvl(val.total_price) + '</td>';	//합계
				}
								
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});
		}
		
		function fnDownload(){
			//datalist 객체에서 value찾기
			var vCustNo = $('#srchCustNo').val();
			var vCustNoVal = $('#custList option').filter(function() {
				return this.value == vCustNo;
			}).data('value');
			if(vCustNo == ""){
				if(typeof vCustNoVal == "undefined" || null == vCustNoVal){
					vCustNoVal = "";
				} 				
			}
			commonSubmit.clearForm();
			commonSubmit.setUrl("<c:url value='/receipt/downloadReceipt.do' />");
			commonSubmit.addParam("cust_no", vCustNoVal);
			commonSubmit.addParam("fromSellDt", gfn_nvl($("#fromSellDt").val()));
			commonSubmit.addParam("toSellDt", gfn_nvl($("#toSellDt").val()));
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			영수증발행
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
						<td class="align-left date" colspan="2">
							<input type="text" class="search_input" id="fromSellDt" readonly />
							<label class="mid_date">~</label>
							<input type="text" class="search_input" id="toSellDt" readonly />
						</td>
						<th>고객명</th>
						<td colspan="2">
							<input type="text" id="srchCustNo" list="custList" autocomplete="on" maxlength="50">
							<datalist id="custList">
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
			<button class="btn_default" type="button" id="issueReceipt" onclick="javascript:fnDownload();">다운로드</button>
		</div>
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="20%">품명</th>
						<th width="5%">수량</th>
						<th width="5%">단위</th>
						<th width="10%">단가</th>
						<th width="10%">면세</th>
						<th width="10%">공급가액</th>
						<th width="10%">부가세</th>
						<th width="10%">계</th>
						<th width="15%">합계</th>
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
