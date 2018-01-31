<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>상품 등록 및 수정</title>
	<script type="text/javascript">
		$(document).ready(function(){
			//입력 수정 판단
			$("#mode").val('${reg_mode}');
			$("#prodNo").val("");
			
			if("U" == '${reg_mode}'){
				//상세 내용을 조회해서 input에 셋팅함.
				fnProductInfo();
			} 
			
			//저장 버튼 이벤트
			$("#btnSave").on("click", function(){
				fnSave();
			});
		});
		
		/**
		 * 저장
		 */
		function fnSave(){			
			commonSubmit.setUrl("<c:url value='/product/setProduct.do' />");
			commonSubmit.addParam("prod_nm"		, gfn_nvl($("#prodNm").val()));		//상품명
			commonSubmit.addParam("prod_typ"	, gfn_nvl($("#prodTyp").val()));	//상품구분
			commonSubmit.addParam("tax_yn"		, gfn_nvl($("#taxYn").val()));		//과세여부
			commonSubmit.addParam("use_yn"		, gfn_nvl($("#useYn").val()));		//사용여부
			commonSubmit.addParam("mode"		, $("#mode").val());				//입력 I, 수정 U
			if("U" == $("#mode").val()){
				commonSubmit.addParam("prod_no", gfn_nvl($("#prodNo").val()));		//상품번호
			}
			commonSubmit.setMethod("post");
			commonSubmit.submit();
		}

		/**
		 * 상품 내역 조회하기
		 */
		function fnProductInfo(){
			commonAjax.setUrl("<c:url value='/product/getProductInfo.do'/>");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.addParam("prod_no", "${prodNo}");
			commonAjax.setCallback("fnProductInfoCallback");
			commonAjax.ajax();	
		}
		
		/**
		 * 상품 내역 조회하기 callback
		 */
		function fnProductInfoCallback(result){	
			$("#prodNo").val(result.productInfo.prod_no);
			$("#prodNm").val(result.productInfo.prod_nm);
			$("#prodTyp").val(result.productInfo.prod_typ);
			$("#taxYn").val(result.productInfo.tax_yn);
			$("#useYn").val(result.productInfo.use_yn);
		}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			상품 등록 및 수정
		</div>
		
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<colgroup>
					<col width="15%"/>
					<col width="35%"/>
					<col width="15%"/>
					<col width="35%"/>
				</colgroup>
				<tbody>
					<tr class="detail_view">						
						<th class="list_th">상품명</th>
						<td class="list_td align-left">
							<input type="text" id="prodNm" maxlength="50">
						</td>
						<th class="list_th">상품구분</th>
						<td class="list_td align-left">
							<select id="prodTyp">
								<option value="A">야채</option>
								<option value="B">공산품</option>
							</select>
						</td>
					</tr>
					<tr class="detail_view">
						<th class="list_th">과세여부</th>
						<td class="list_td align-left">
							<select id="taxYn">
								<option value="Y">과세</option>
								<option value="N">면세</option>
							</select>
						</td>
						<th class="list_th">사용여부</th>
						<td class="list_td align-left">
							<select id="useYn">
								<option value="Y">사용</option>
								<option value="N">해제</option>
							</select>
						</td>
					</tr>
				</tbody>
			</table>	
			<input type="hidden" id="mode" />
			<input type="hidden" id="prodNo" />
		</div>
		<div class="list_btn_group" id="btnGroup">			
			<button class="btn_default" id="btnSave">저장</button>				
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
