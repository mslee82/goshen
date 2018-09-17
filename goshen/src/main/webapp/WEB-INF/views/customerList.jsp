<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>고객 목록 조회</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 고객목록 select 생성
			fnCustNmList();

			//조회
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnCustList();		
				$(this).attr("disabled", false);
			});
		
			//신규 버튼 이벤트
			$("#btnCre").on("click", function(){
				if(confirm("새로 등록 하시겠습니까?")){
					fnSetCustInfo();
				}
			});
						
			//수정 버튼 이벤트
			$("#btnMod").on("click", function(){
				if(confirm("수정 하시겠습니까?")){
					fnSetModCustInfo();
				}
			});
			
			//삭제 버튼 이벤트
			$("#btnDel").on("click", function(){
				if(confirm("삭제 하시겠습니까?")){
					fnDelCustInfo();
				}
			});

			//그리드 선택
			$(document).on("click", "#board_list > tbody > tr > td", function(event){
				var params = $(this).parents("tr");		
				if (event.target.type == 'checkbox'){
					$(params).find("input[type=checkbox]").prop("checked", false);
					return;
				}
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
		 * 고객 목록 조회하기
		 */
		function fnCustList(){
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
			commonAjax.setUrl("<c:url value='/customer/customerList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();

			commonAjax.setCallback("fnCustListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 고객 목록 조회하기 callback
		 */
		function fnCustListCallback(result){
			var dataList;
			$("#board_list tbody").empty();

			$.each(result.list, function(i, val){
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listCustNo">' + gfn_nvl(val.cust_no) + '</td>';						//거래처번호
				dataList += '<td name="listCustNm">' + gfn_nvl(val.cust_nm) + '</td>';						//거래처명
				dataList += '<td name="listBranchNm">' + gfn_nvl(val.branch_nm) + '</td>';					//지점명
				dataList += '<td name="listCustAl">' + gfn_nvl(val.cust_al) + '</td>';						//약칭
				dataList += '<td name="listReceiptLv">' + gfn_nvl(val.receipt_lv) + '</td>';				//영수증 발행 레벨
				dataList += '<td name="listTruckNm">' + gfn_nvl(val.truck_nm) + '</td>';					//배달차량
				dataList += '<td name="listMainstoreYn">' + gfn_nvl(val.mainstore_yn) + '</td>';			//본지점사용여부
				dataList += '<td name="listMainstoreCustNm">' + gfn_nvl(val.mainstore_cust_nm) + '</td>';	//본점명칭
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});

		}
		
		/**
		 * 신규
		 */
		function fnSetCustInfo() {			
			commonSubmit.setUrl("<c:url value='/customer/customerRegPage.do' />");
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}
		
		/**
		 * 수정 
		 */
		function fnSetModCustInfo() {			
			var chkCnt = 0;
			var vCustNo = "";

			$(".block_list table tbody .active").each(function (i) {			
				vCustNo = this.children.listCustNo.textContent;
				chkCnt++;
			});
			
			if(chkCnt > 1){
				alert("한건만 선택 하세요.");
				return;
			}
			
			commonSubmit.setUrl("<c:url value='/customer/customerModPage.do' />");
			commonSubmit.addParam("cust_no", vCustNo);
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}

		/**
		 * 삭제
		 */
		function fnDelCustInfo() {			
			var chkCnt = 0;
			var vCustNo = "";

			$(".block_list table tbody .active").each(function (i) {			
				vCustNo = this.children.listCustNo.textContent;
				chkCnt++;
			});
			
			if(chkCnt > 1){
				alert("한건만 선택 하세요.");
				return;
			}
			
			commonAjax.setUrl("<c:url value='/customer/delCustomer.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("cust_no", vCustNo);
			commonAjax.setCallback("fnDelCustInfoCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 삭제 Callback
		 */
		function fnDelCustInfoCallback(){
			alert("삭제 완료");
			fnCustList();
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			고객 목록 조회
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
						<th>고객명</th>
						<td colspan="5">
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
						<th width="3%"></th>
						<th width="5%">거래처<br>번호</th>
						<th width="20%">거래처명</th>
						<th width="15%">지점명</th>
						<th width="10%">약칭</th>
						<th width="10%">영수증<br>발행레벨</th>
						<th width="10%">배달<br>차량</th>
						<th width="5%">본점<br>여부</th>
						<th width="17%">본점<br>명칭</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>			
		</div>
		<div class="list_btn_group" id="btnGroup">
			<button class="btn_default" id="btnCre">등록</button>
			<button class="btn_default" id="btnMod">수정</button>	
			<button class="btn_default" id="btnDel">삭제</button>			
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
