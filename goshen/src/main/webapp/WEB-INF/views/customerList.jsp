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
				if(confirm("수정 하시겠습니까?")){
					fnSetModSellInfo();
				}
			});
			//삭제 버튼 이벤트
			$("#btnDel").on("click", function(){
				if(confirm("삭제 하시겠습니까?")){
					fnSetSellReturn();
				}
			});
			//반품 버튼 이벤트
			$("#btnReturn").on("click", function(){
				if(confirm("반품 하시겠습니까?")){
					fnSetSellReturn();
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
				dataList += '<td name="listCustNo">' + gfn_nvl(val.cust_no) + '</td>';						//거래처번호
				dataList += '<td name="listCustNm">' + gfn_nvl(val.cust_nm) + '</td>';						//판매일자
				dataList += '<td name="listCustAl">' + gfn_nvl(val.cust_al) + '</td>';						//약칭
				dataList += '<td name="listCustAddr">' + gfn_nvl(val.cust_addr) + '</td>';					//주소
				dataList += '<td name="listCustPhone">' + gfn_nvl(val.cust_phone) + '</td>';				//대표번호
				dataList += '<td name="listCustMobile">' + gfn_nvl(val.cust_mobile) + '</td>';				//휴대전화
				dataList += '<td name="listCustFax">' + gfn_nvl(val.cust_fax) + '</td>';					//팩스
				dataList += '<td name="listOwnerNm">' + gfn_nvl(val.owner_nm) + '</td>';					//대표자명
				dataList += '<td name="listStaffPhone1">' + gfn_nvl(val.staff_phone1) + '</td>';			//스탭번호1
				dataList += '<td name="listStaffPhone2">' + gfn_nvl(val.staff_phone2) + '</td>';			//스탭번호2
				dataList += '<td name="listStaffPhone3">' + gfn_nvl(val.staff_phone3) + '</td>';			//스탭번호3
				
				dataList += '<td name="listReceiptLv">' + gfn_nvl(val.receipt_lv) + '</td>';				//영수증 발행 레벨
				dataList += '<td name="listTruckNo">' + gfn_nvl(val.truck_no) + '</td>';					//배달차량
				dataList += '<td name="listMainstoreYn">' + gfn_nvl(val.mainstore_yn) + '</td>';			//본지점사용여부
				dataList += '<td name="listMainstoreCustNo">' + gfn_nvl(val.mainstore_cust_no) + '</td>';	//본점번호
				dataList += '<td name="listBranchNm">' + gfn_nvl(val.branch_nm) + '</td>';					//지점명
				dataList += '<td name="listUseYn">' + gfn_nvl(val.use_yn) + '</td>';						//사용여부
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
			commonAjax.setUrl("<c:url value='/sell/setSellReturn.do' />");
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
		 * 수정 
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
						<th width="10%">거래처명</th>
						<th width="5%">약칭</th>
						<th width="5%">주소</th>
						<th width="10%">대표번호</th>
						<th width="7%">휴대전화</th>
						<th width="5%">팩스</th>
						<th width="5%">대표자명</th>
						<th width="5%">스탭번호1</th>
						<th width="5%">스탭번호2</th>
						<th width="5%">스탭번호3</th>
						<th width="5%">영수증<br>발행레벨</th>
						<th width="5%">배달<br>차량</th>
						<th width="5%">본지점사용여부</th>
						<th width="5%">본점<br>번호</th>
						<th width="5%">지점명</th>
						<th width="5%">사용여부</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>			
		</div>
		<div class="list_btn_group" id="btnGroup">
			<button class="btn_default" id="btnMod">수정</button>	
			<button class="btn_default" id="btnDel">삭제</button>			
			<button class="btn_default" id="btnReturn">반품</button>				
			<button class="btn_default" id="btnChange">교환</button>
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
