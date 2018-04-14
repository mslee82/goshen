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
			
			//고객목록 select 생성
			fnMainCustNmList();
			
			//common.js 차량목록 select 생성
			fnTruckNmList();
			
			if("U" == '${reg_mode}'){
				//상세 내용을 조회해서 input에 셋팅함.
				fnCustInfo();
			} 			
		
			//저장 버튼 이벤트
			$("#btnSave").on("click", function(){
				fnSave();
			});
		});
		
		/**
		 * 본점 연결을 위한 고객 목록 조회하기
		 */
		function fnMainCustNmList() {
			commonAjax.clearParam(); 
			commonAjax.setUrl("/com/custNmList.do");
			commonAjax.setDataType("json");
			commonAjax.setCallback("fnMainCustNmListCallback");
			commonAjax.ajax();	
		}

		/**
		 * 고객 목록 조회하기 callback
		 */
		function fnMainCustNmListCallback(response) {
			var codeList = $("#mainCustList");
			var codeOpt = "";
			codeList.empty();
			$.each(response.custNmList, function(i, val){
				codeOpt = $("<option></option>").attr("value", val.cust_nm).attr("data-value", val.cust_no);
				codeList.append(codeOpt);
			});
		}

		/**
		 * 저장
		 */
		function fnSave(){
			//datalist 객체에서 value찾기
			var vMainCustNo = $('#mainCustNo').val();
			var vMainCustNoVal = $('#mainCustList option').filter(function() {
				return this.value == vMainCustNo;
			}).data('value');
					
			var vTruckNo = $('#truckNo').val();
			var vTruckNoVal = $('#truckList option').filter(function() {
				return this.value == vTruckNo;
			}).data('value');
			
			if(vMainCustNo == ""){
				if(typeof vMainCustNoVal == "undefined" || null == vMainCustNoVal){
					vMainCustNoVal = "";
				} 				
			}
			
			if(vTruckNo == ""){
				if(typeof vTruckNoVal == "undefined" || null == vTruckNoVal){
					vTruckNoVal = "";
				} 				
			}
			commonAjax.setUrl("<c:url value='/customer/setCustomer.do' />");
			
			commonAjax.addParam("cust_nm"		, gfn_nvl($("#custNm").val()));		//거래처명
			commonAjax.addParam("branch_nm"	, gfn_nvl($("#branchNm").val()));	//지점명
			commonAjax.addParam("cust_al"		, gfn_nvl($("#custAl").val()));		//별칭
			commonAjax.addParam("receipt_lv"	, gfn_nvl($("#receiptLv").val()));						//영수증레벨
			commonAjax.addParam("truck_no"	, vTruckNoVal);											//배차
			commonAjax.addParam("mainstore_yn", gfn_nvl($("#mainstoreYn").val()));					//본점여부
			
			if(vMainCustNoVal != ""){				
				commonAjax.addParam("mainstore_cust_no", vMainCustNoVal);								//본점고객번호
			}
			
			commonAjax.addParam("mode"		, $("#mode").val());									//입력 I, 수정 U
			
			if("U" == $("#mode").val()){
				commonAjax.addParam("cust_no"	, gfn_nvl($("#cust_no").val()));						//고객번호
			}
			commonAjax.setCallback("fnSaveCallback");
			commonAjax.ajax();
		}
		
		/**
		 * 고객정보 저장 callback
		 */
		function fnSaveCallback(result){	
			commonSubmit.setUrl("<c:url value='/customer/customerListPage.do' />");
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}
		
		/**
		 * 고객정보 조회하기
		 */
		function fnCustInfo(){
			commonAjax.setUrl("<c:url value='/customer/getCustomerInfo.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("cust_no", "${cust_no}");
			commonAjax.setCallback("fnCustInfoCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 고객정보 조회하기 callback
		 */
		function fnCustInfoCallback(result){	
			$("#custNm").val(result.custInfo.cust_nm);
			$("#branchNm").val(result.custInfo.branch_nm);
			$("#custAl").val(result.custInfo.cust_al);
			$("#receiptLv").val(result.custInfo.receipt_lv);
			$("#truckNo").val($('#truckList [data-value='+result.custInfo.truck_no+']').val());
			$("#mainstoreYn").val(result.custInfo.mainstore_yn);
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			고객 등록 및 수정
		</div>
		
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<colgroup>
					<col width="20%"/>
					<col width="30%"/>
					<col width="20%"/>
					<col width="30%"/>
				</colgroup>
				<tbody>
					<tr class="detail_view">
						<th class="list_th">거래처명</th>
						<td class="list_td align-left">
							<input type="text" class="search_input" id="custNm" maxlength="25" />
						</td>
						<th class="list_th">지점명</th>
						<td class="list_td align-left">
							<input type="text" class="search_input" id="branchNm" maxlength="25">
						</td>
					</tr>
					<tr class="detail_view">
						<th class="list_th">별칭</th>
						<td class="list_td align-left">
							<input type="text" class="search_input" id="custAl" maxlength="5">
						</td>
						<th class="list_th">영수증 발행처</th>
						<td class="list_td align-left">
							<select id="receiptLv" style="width:120px">
								<option value="1">고센</option>
								<option value="2">흥인</option>
								<option value="3">고센&흥인</option>
							</select>
						</td>
					</tr>
					<tr class="detail_view">
						<th class="list_th">차량정보</th>
						<td class="list_td align-left">
							<input type="text" id="truckNo" list="truckList" autocomplete="on" maxlength="50">
							<datalist id="truckList">
						    </datalist>	
						</td>
						<th class="list_th">본점여부</th>
						<td class="list_td align-left">
							<select id="mainstoreYn" style="width:120px">
								<option value="Y">본점</option>
								<option value="N">지점</option>
							</select>
						</td>
					</tr>
					<tr class="detail_view">
						<th class="list_th">본점 연결</th>
						<td class="list_td align-left" colspan="3">
							<input type="text" id="mainCustNo" list="mainCustList" autocomplete="on" maxlength="50">
							<datalist id="mainCustList">
						    </datalist>	
						</td>
					</tr>
				</tbody>
			</table>	
			<input type="hidden" id="mode" />
			<input type="hidden" id="cust_no" />
		</div>
		<div class="list_btn_group" id="btnGroup">			
			<button class="btn_default" id="btnSave">저장</button>				
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
