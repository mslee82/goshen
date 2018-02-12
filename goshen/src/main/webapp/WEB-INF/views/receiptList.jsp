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
			
			//판매가 수정 버튼 이벤트
			$("#btnPriceMod").on("click", function(){
				if(confirm("판매정보가 수정됩니다. 수정하시겠습니까?")){
					fnSellSave();
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
				
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listSellDt">' + gfn_nvl(val.sell_dt) 	+ '</td>';		//판매일자
				dataList += '<td name="listCustNm">' + gfn_nvl(val.cust_nm) 	+ '</td>';		//고객명
				dataList += '<td name="listBranchNm">' + gfn_nvl(val.branch_nm) + '</td>';		//지점명
				dataList += '<td name="listSellSeq">' + gfn_nvl(val.sell_seq) 	+ '</td>';		//일련번호
				dataList += '<td name="listProdNm"><input type="text" id="prodNm" list="prodList" autocomplete="on" maxlength="50" value="' + gfn_nvl(val.prod_nm) 	+ '"><datalist id="prodList"></datalist></td>';		//상품
				dataList += '<td name="listSellQuan"><input type="text" class="price" id="sellQuan" value="' + gfn_nvl(val.sell_quan) + '" /></td>';		//수량
				dataList += '<td name="listUnitNm"><input type="text" class="unit" id="unitNm" list="unitList" autocomplete="on" maxlength="5" value="' + gfn_nvl(val.unit_nm) 	+ '"><datalist id="unitList"></datalist></td>';		//단위
				dataList += '<td name="listProdPrice"><input type="text" name="prodPrice" class="price" value="' + gfn_nvl(val.prod_price) + '"/></td>';	//가격
				
				//과세 여부
				if("Y" == gfn_nvl(val.tax_yn)){
					vSelectedY = "selected";
					vSelectedN = "";
				} else{
					vSelectedY = "";
					vSelectedN = "selected";
				}
				dataList += '<td name="listTaxYn"><select name="taxYn"><option value="N"'+vSelectedN+'>면세</option><option value="Y"'+vSelectedY+'>과세</option></select></td>';	
				
				//상품종류
				if("A" == gfn_nvl(val.prod_typ)){
					vSelectedA = "selected";
					vSelectedB = "";
					vSelectedC = "";
					vSelectedD = "";
				} else if("B" == gfn_nvl(val.prod_typ)){
					vSelectedA = "";
					vSelectedB = "selected";
					vSelectedC = "";
					vSelectedD = "";
				} else if("C" == gfn_nvl(val.prod_typ)){
					vSelectedA = "";
					vSelectedB = "";
					vSelectedC = "selected";
					vSelectedD = "";
				} else if("D" == gfn_nvl(val.prod_typ)){
					vSelectedA = "";
					vSelectedB = "";
					vSelectedC = "";
					vSelectedD = "selected";
				}
				dataList += '<td name="listProdTyp"><select name="prodTyp"><option value="A"'+vSelectedA+'>야채</option><option value="B"'+vSelectedB+'>공산품</option><option value="C"'+vSelectedC+'>수산</option><option value="D"'+vSelectedD+'>고기</option></select></td>';
				dataList += '<td name="listSellCustNo" style="display:none">' + gfn_nvl(val.cust_no) + '</td>';		//고객번호		
				dataList += '<td name="listReturnSeq" style="display:none">' + gfn_nvl(val.return_seq) + '</td>';	//반품일련번호
				dataList += '<td name="listProdSeq" style="display:none">' + gfn_nvl(val.prod_seq) + '</td>';		//상품일련번호		
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});
			
			fnProdNmList();
			fnUnitNmList();
		}
		
		/**
		 * 판매정보 수정
		 */
		function fnSellSave() {			
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;

			//목록을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent							//판매일자
								, "cust_nm" : this.children.listCustNm.textContent	
								, "cust_no" : this.children.listSellCustNo.textContent					//고객명
						  		, "sell_seq" : this.children.listSellSeq.textContent					//일련번호
						  		, "return_seq" : this.children.listReturnSeq.textContent				//반품일련번호
						  		, "prod_nm" : this.children.listProdNm.children.prodNm.value			//상품명
						  		, "sell_quan" : this.children.listSellQuan.children.sellQuan.value		//판매수량
						  		, "unit_nm" : this.children.listUnitNm.children.unitNm.value			//단위명
						  		, "prod_price" : this.children.listProdPrice.children.prodPrice.value	//금액
						  		, "tax_yn" : this.children.listTaxYn.children.taxYn.value				//과세여부
						  		, "prod_typ" : this.children.listProdTyp.children.prodTyp.value			//종류
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/receipt/setSellList.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnSellSaveCallback");
			commonAjax.ajax();	
		}
		
		function fnSellSaveCallBack(response){
			alert("등록 완료");
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
						<th width="5%">판매<br/>일자</th>
						<th width="14%">고객명</th>
						<th width="14%">지점명</th>					
						<th width="5%">일련<br>번호</th>
						<th width="20%">상품명</th>
						<th width="7%">수량</th>
						<th width="8%">단위</th>
						<th width="10%">개당가격</th>
						<th width="6%">과세<br/>여부</th>
						<th width="6%">종류</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>
		</div>
		<div class="list_btn_group" id="btnGroup">
			<button class="btn_default" id="btnMod">판매 내용 수정</button>	
			<button class="btn_default" id="btnDel">판매 내용 삭제</button>		
			<button class="btn_default" id="btnPriceMod">판매가 수정</button>
			<button class="btn_default" id="btnReturnCancel">반품취소</button>				
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
