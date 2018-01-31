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
					fnSetModProductInfo();
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
			
			commonAjax.setUrl("<c:url value='/sell/sellList.do'/>");
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
			$("#board_list tbody").empty();

			$.each(result.list, function(i, val){
				dataList  = '<tr class="list_row">';
				dataList += '<td><input type="checkbox" name="chkList"/></td>';
				dataList += '<td name="listProdNo">' + gfn_nvl(val.prod_no) 	+ '</td>';	//상품번호
				dataList += '<td name="listProdNm">' + gfn_nvl(val.prod_nm) 	+ '</td>';	//상품명
				dataList += '<td name="listProdTyp">' + gfn_nvl(val.prod_typ) 	+ '</td>';	//상품구분
				dataList += '<td name="listTaxYn">' + gfn_nvl(val.tax_yn) 	+ '</td>';		//과세여부
				dataList += '<td name="listUseYn">' + gfn_nvl(val.use_yn) + '</td>';		//사용여부			
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
		function fnSetModProductInfo() {			
			var chkCnt = 0;
			var vProdNo = "";

			$(".block_list table tbody .active").each(function (i) {			
				vProdNo = this.children.listProdNo.textContent;
				chkCnt++;
			});
			
			if(chkCnt > 1){
				alert("한건만 선택 하세요.");
				return;
			}
			
			commonSubmit.setUrl("<c:url value='/product/productModPage.do' />");
			commonSubmit.addParam("prod_no", vProdNo);
			commonSubmit.setMethod("post");
			commonSubmit.submit();
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
						<th width="55%">상품명</th>
						<th width="10%">상품구분</th>
						<th width="10%">과세여부</th>
						<th width="10%">사용여부</th>
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
