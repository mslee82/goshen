<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>판매 내역  조회</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//common.js 고객목록 select 생성
			fnCustNmList();
			
			//common.js 상품목록 select 생성
			fnProdNmList();
			
			//조회
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnSellList();		
				$(this).attr("disabled", false);
			});
			
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
		
			//수정 버튼 이벤트
			$("#btnMod").on("click", function(){
				if(confirm("판매내역 수정 페이지로 이동합니다.\n진행 하시겠습니까?")){
					fnSetModSellInfo();
				}
			});
			
			//삭제 버튼 이벤트
			$("#btnDel").on("click", function(){
				if(confirm("삭제 하시겠습니까?")){
					fnDelSellList();				
				}
			});
			
			//반품 버튼 이벤트
			$("#btnReturn").on("click", function(){
				if(confirm("반품 하시겠습니까?")){
					fnSetSellReturn();
				}
			});
			
			//반품 취소 버튼 이벤트
			$("#btnReturnCancel").on("click", function(){
				if(confirm("반품 취소 하시겠습니까?")){
					fnSetSellReturn();
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
		 * 판매 목록 조회하기
		 */
		function fnSellList(){
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
			commonAjax.setUrl("<c:url value='/sell/sellList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("sell_dt", gfn_nvl($("#sellDt").val()));
			commonAjax.addParam("cust_no", vCustNoVal);
			commonAjax.addParam("prod_no", vProdVal);
			
			commonAjax.setCallback("fnSellListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 판매 목록 조회하기 callback
		 */
		function fnSellListCallback(result){
			var dataList;
			$("#board_list tbody").empty();

			$.each(result.list, function(i, val){
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listSellSeq">' + gfn_nvl(val.sell_seq) 	+ '</td>';		//일련번호
				dataList += '<td name="listSellDt">' + gfn_nvl(val.sell_dt) 	+ '</td>';		//판매일자
				dataList += '<td name="listCustNm">' + gfn_nvl(val.cust_nm) 	+ '</td>';		//고객명
				dataList += '<td name="listProdNm">' + gfn_nvl(val.prod_nm) 	+ '</td>';		//상품
				dataList += '<td name="listSellQuan">' + gfn_nvl(val.sell_quan) + '(<input name="returnQuan" class="return_unit" value="'+ gfn_nvl(val.return_quan) +'"/>)</td>';		//수량
				dataList += '<td name="listUnitNm">' + gfn_nvl(val.unit_nm) 	+ '</td>';		//단위
				dataList += '<td name="listTaxFree">' + gfn_nvl(val.tax_free) 	+ '</td>';	//면세
				dataList += '<td name="listSupply">' + gfn_nvl(val.supply) 		+ '</td>';	//과세
				dataList += '<td name="listTax">' 	 + gfn_nvl(val.tax) 		+ '</td>';		//부가세
				dataList += '<td name="listTotalPrice">' + gfn_nvl(val.total_price) + '</td>';	//계
				dataList += '<td name="listSellCustNo" style="display:none">' + gfn_nvl(val.cust_no) + '</td>';		//고객번호		
				dataList += '<td name="listReturnSeq" style="display:none">' + gfn_nvl(val.return_seq) + '</td>';	//반품일련번호		
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});

		}

		/**
		 * 반품 
		 */
		function fnSetSellReturn() {
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			var vReqChkCnt = 0;
			//선택된 반품 항목을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {			
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent
						  		, "cust_no" : this.children.listSellCustNo.textContent
						  		, "sell_seq" : this.children.listSellSeq.textContent
						  		, "return_quan" : this.children.listSellQuan.children.returnQuan.value
				}	
				 
				if(this.children.listSellQuan.children.returnQuan.value == ""){
					vReqChkCnt ++;
				}
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			if(chkCnt == 0){
				vReqChkCnt ++;
				alert("반품 내역을 선택하세요");
				return;
			}
			
			if(vReqChkCnt > 0){
				vReqChkCnt ++;
				alert("반품 수량을 입력하세요");
				return;
			}
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sellReturn/setSellReturn.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnSetSellReturnCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 반품 callback
		 */
		function fnSetSellReturnCallback(response) {
			if(response.result.returnMsg == ""){			
				alert(response.result.returnCnt + " 건 반품처리가 완료 되었습니다.");
			} else{
				alert(response.result.returnMsg + " 확인이 필요합니다.");
			}
			fnSellList();
		}
		
		/**
		 * 반품 취소
		 */
		function fnCancelSellReturn() {
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//선택된 반품 항목을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {			
				vJsonParam = { "return_dt" : this.children.listSellDt.textContent
						  		, "return_quan" : this.children.listReturnSeq.textContent
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
			commonAjax.setCallback("fnCancelSellReturnCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 반품 취소 callback
		 */
		function fnCancelSellReturnCallback(response) {
			if(response.result.returnMsg == ""){			
				alert(response.result.returnCnt + " 건 반품 취소 완료 되었습니다.");
			} else{
				alert(response.result.returnMsg + " 확인이 필요합니다.");
			}
			fnSellList();
		}
		
		/**
		 * 판매 내용 수정 
		 */
		function fnSetModSellInfo() {			
			var chkCnt = 0;
			var vSellDt = "";
			var vCustNo = "";
			var vSellSeq = "";

			$(".block_list table tbody .active").each(function (i) {			
				vSellDt = this.children.listSellDt.textContent;
				vCustNo = this.children.listSellCustNo.textContent;
				vSellSeq = this.children.listSellSeq.textContent;
				chkCnt++;
			});
			
			if(chkCnt == 0){
				alert("내역을 선택 하세요.");
				return;
			}
			
			if(chkCnt > 1){
				alert("한건만 선택 하세요.");
				return;
			}
			
			commonSubmit.setUrl("<c:url value='/sell/sellModPage.do' />");
			commonSubmit.addParam("sell_dt", vSellDt);
			commonSubmit.addParam("cust_no", vCustNo);
			commonSubmit.addParam("sell_seq", vSellSeq);
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}

		/**
		 * 판매내역 삭제
		 */
		function fnDelSellList() {
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//선택된 반품 항목을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {			
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent
						  		, "cust_no" : this.children.listSellCustNo.textContent
						  		, "sell_seq" : this.children.listSellSeq.textContent
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sell/delSellList.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnDelSellListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 판매내역 삭제 callback
		 */
		function fnDelSellListCallback(response) {
			if(response.result.returnMsg == ""){			
				alert(response.result.returnCnt + " 건 삭제 완료 되었습니다.");
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
			판매 내역 조회
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
						<td class="align-left date" colspan="5">
							<input type="text" class="search_input" id="sellDt" readonly />
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
						<th width="7%">판매일자</th>
						<th width="15%">고객명</th>
						<th width="10%">상품</th>
						<th width="10%">판매수량<br>(반품수량)</th>
						<th width="5%">단위</th>
						<th width="5%">면세</th>
						<th width="10%">과세</th>
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
			<button class="btn_default" id="btnMod">판매 내용 수정</button>	
			<button class="btn_default" id="btnDel">판매 내용 삭제</button>			
			<button class="btn_default" id="btnReturn">반품</button>
			<button class="btn_default" id="btnReturnCancel">반품취소</button>				
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
