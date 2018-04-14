<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>수금 관리</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 고객목록 select 생성
			fnCustNmList();		
			
			//조회
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnCollMoneyList();		
				$(this).attr("disabled", false);
			});
			
			//달력 버튼 이벤트
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
		
			$("#regDt").datepicker({
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
			
			//반품 버튼 이벤트
			$("#btnSave").on("click", function(){
				if(confirm("저장 하시겠습니까?")){
					fnCollMoneySave();
				}
			});

			//삭제 버튼 이벤트
			$("#btnDel").on("click", function(){
				if(confirm("삭제 하시겠습니까?")){
					fnDelCollMoney();
				}
			});

			//그리드 선택
			$(document).on("click", "#board_list > tbody > tr", function(event){
				$("#regDt").val(this.children.listRegDt.textContent);
				$("#custNm").val(this.children.listCustNm.textContent);
				$("#regType").val(this.children.listRegType.textContent);
				$("#amt").val(this.children.listAmt.textContent);
				$("#custNo").val(this.children.listCustNo.textContent);
				
			});	
		});
		
		/**
		 * 수금 목록 조회하기
		 */
		function fnCollMoneyList(){
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
			
			$("#custNm").val(gfn_nvl($('#srchCustNo').val()));
			$("#custNo").val(vCustNoVal);
			
			commonAjax.setUrl("<c:url value='/sell/collMoneyList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("reg_dt", gfn_nvl($("#srchDt").val()));
			commonAjax.addParam("cust_no", vCustNoVal);
			
			commonAjax.setCallback("fnCollMoneyListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 수금 목록 조회하기 callback
		 */
		function fnCollMoneyListCallback(result){
			var dataList;
			$("#board_list tbody").empty();

			$.each(result.list, function(i, val){
				dataList  = '<tr class="list_row">';
				dataList += '<td name="listRegDt">' + gfn_nvl(val.reg_dt) + '</td>';						//등록일자
				dataList += '<td name="listCustNm">' + gfn_nvl(val.cust_nm) + '</td>';						//거래처 & 지점명
				dataList += '<td name="listRegTypeNm">' + gfn_nvl(val.reg_type_nm) + '</td>';				//수금, 미수금 구분
				dataList += '<td name="listAmt">' + gfn_nvl(val.amt) + '</td>';								//금액
				dataList += '<td name="listCustNo" style="display:none">' + gfn_nvl(val.cust_no) + '</td>';		//거래처번호
				dataList += '<td name="listRegType" style="display:none">' + gfn_nvl(val.reg_type) + '</td>';	//수금, 미수금 구분
				
				dataList += '</tr>';
								
				$("#board_list tbody").append(dataList);
			});

		}
		
		/**
		 * 수금정보 저장 및 수정
		 */
		function fnCollMoneySave() {			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sell/setCollMoney.do' />");
			commonAjax.setDataType("json");
			commonAjax.addParam("reg_dt", gfn_nvl($("#regDt").val()));
			commonAjax.addParam("cust_no", gfn_nvl($("#custNo").val()));
			commonAjax.addParam("reg_type", gfn_nvl($("#regType").val()));
			commonAjax.addParam("amt", gfn_nvl($("#amt").val()));
			commonAjax.setCallback("fnCollMoneySaveCallback");
			commonAjax.ajax();	
		}
		
		function fnCollMoneySaveCallback(response){
			alert("등록 완료");
			fnCollMoneyList();
		} 

		/**
		 * 수금정보 저장 삭제
		 */
		function fnDelCollMoney() {			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sell/delCollMoney.do' />");
			commonAjax.setDataType("json");
			commonAjax.addParam("reg_dt", gfn_nvl($("#regDt").val()));
			commonAjax.addParam("cust_no", gfn_nvl($("#custNo").val()));
			commonAjax.addParam("reg_type", gfn_nvl($("#regType").val()));
			commonAjax.setCallback("fnDelCollMoneyCallback");
			commonAjax.ajax();	
		}
		
		function fnDelCollMoneyCallback(response){
			alert("삭제 완료");
			fnCollMoneyList();
		} 
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			수금 관리
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
						<th>고객명</th>
						<td colspan="3">
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
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<thead>
					<tr>
						<th width="5%">등록일자</th>
						<th width="10%">거래처 & 지점명</th>
						<th width="5%">구분</th>
						<th width="10%">금액</th>
						<th width="7%" style="display:none">거래처번호</th>
						<th width="7%" style="display:none">구분</th>
					</tr>
				</thead>
				<tbody>
					<tr class="list_row">
						<td colspan="4"></td>
					</tr>
				</tbody>
			</table>			
		</div>
		<div style="height:40px;">
		</div>
		<div class="sub_title">
      		<h3>저장 및 삭제</h3>
		</div>
		<div class="block_list">
			<table id="board_reg" class="list_table">
				<colgroup>
					<col width="20%"/>
					<col width="50%"/>
					<col width="10%"/>
					<col width="20%"/>				
				</colgroup>
				<thead>
					<tr>
						<th>등록일자</th>
						<th>거래처 & 지점명</th>
						<th>구분</th>
						<th>금액</th>
						<th style="display:none">거래처번호</th>
					</tr>
				</thead>
				<tbody>
					<tr class="detail_view">
						<td class="list_td align-left date"><input type="text" class="search_input" id="regDt" readonly /></td>
						<td class="list_td"><input type="text" id="custNm" readonly /></td>
						<td class="list_td align-left"><select id="regType" style="width:100px;"><option value="1">미수금</option><option value="2">수금</option></select></td>
						<td class="list_td"><input type="text" id="amt" /></td>
						<td style="display:none"><input type="hidden" id="custNo" /></td>
					</tr>
				</tbody>
			</table>			
		</div>
		
		<div class="list_btn_group" id="btnGroup">
			<button class="btn_default" id="btnSave">저장</button>
			<button class="btn_default" id="btnDel">삭제</button>			
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
