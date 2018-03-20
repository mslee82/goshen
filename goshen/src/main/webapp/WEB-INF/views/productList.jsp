<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>상품 내역  조회</title>
	<script type="text/javascript">
		$(document).ready(function(){	
			//common.js 상품목록 select 생성
			fnProdNmList();
			
			//조회
			$(".main_srch").on("click", function(){
				$(this).attr("disabled", true);				
				fnProductList();		
				$(this).attr("disabled", false);
			});
		
			//등록 버튼 이벤트
			$("#btnReg").on("click", function(){
				if(confirm("등록 하시겠습니까?")){
					fnSetRegProductInfo();
				}
			});
						
			//수정 버튼 이벤트
			$("#btnMod").on("click", function(){
				if(confirm("수정 하시겠습니까?")){
					fnModProductInfo();
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
		 * 상품 목록 조회하기
		 */
		function fnProductList(){
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
			commonAjax.setUrl("<c:url value='/product/productList.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("prod_no", vProdVal);
			
			commonAjax.setCallback("fnProductListCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 상품 목록 조회하기 callback
		 */
		function fnProductListCallback(result){
			var dataList;

			var vProdNm = "";
			var vSelectedY = "";
			var vSelectedN = "";
			
			var vSelectedA = "";
			var vSelectedB = "";
			var vSelectedC = "";
			var vSelectedD = "";			
			var vSelectedE = "";
			$("#board_list tbody").empty();

			$.each(result.list, function(i, val){
				vProdNm =  gfn_nvl(val.prod_nm).replace(/\"/g,"&quot;");
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listProdNo">' + gfn_nvl(val.prod_no) 	+ '</td>';	//상품번호
				dataList += '<td name="listProdNm"><input type="text" id="prodNm" list="prodList" autocomplete="on" maxlength="50" value="' + vProdNm + '"><datalist id="prodList"></datalist></td>';				//상품
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
				dataList += '</tr>';
				
				$("#board_list tbody").append(dataList);
			});

		}
		
		/**
		 * 등록
		 */
		function fnSetRegProductInfo() {			
			commonSubmit.setUrl("<c:url value='/product/productRegPage.do' />");			
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}
		
		/**
		 * 수정 
		 */
		function fnModProductInfo() {			
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//목록을 array에 담아 전달
			$(".block_list table tbody .active").each(function (i) {
				vJsonParam = { "prod_no" : this.children.listProdNo.textContent							//상품번호
								, "prod_nm" : encodeURIComponent(this.children.listProdNm.children.prodNm.value.replace(/\"/g,"&quot;")) //상품명
						  		, "tax_yn" : this.children.listTaxYn.children.taxYn.value				//과세여부
						  		, "prod_typ" : this.children.listProdTyp.children.prodTyp.value			//상품종류						  		
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/product/setProductList.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnModProductInfoCallback");
			commonAjax.ajax();	
		}

		function fnModProductInfoCallback(response) {
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
			상품 조회
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
						<th>상품</th>
						<td colspan="5">
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
						<th width="5%"></th>
						<th width="10%">상품번호</th>
						<th width="65%">상품명</th>
						<th width="6%">과세여부</th>
						<th width="6%">상품구분</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>			
		</div>
		<div class="list_btn_group" id="btnGroup">
			<button class="btn_default" id="btnReg">등록</button>
			<button class="btn_default" id="btnMod">수정</button>	
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
