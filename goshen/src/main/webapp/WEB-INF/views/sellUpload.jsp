<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>Home</title>
	<script type="text/javascript">
		$(document).ready(function(){
					
			//저장 버튼 이벤트
			$("#btnSave").on("click", function(){
				fnSellSave();
			});			

		});
		
		function checkFileType(filePath){
			var fileFormat = filePath.split(".");
			if(fileFormat.indexOf("xls") > -1){
				return true;
			}else if(fileFormat.indexOf("xlsx") > -1){
				return true;
			}else{
				return false;
			}
		}

		/**
		 * 저장전 미리보기
		 */
		function fnUploadPrev(){
			var file = $("#excel").val();
			if(file == "" || file == null){
				alert("파일을 선택하세요.");
				return false;
			} else if(!checkFileType(file)){
				alert("엑셀 파일만 업로드 가능합니다.");
				return false;
			} else {
				
				//저장없이 미리보기만
				var oForm = new FormData();
				if(!gfn_isNull(excel.files[0])){
					oForm.append("excel", excel.files[0]);
			    }
				fileUpload.setURI("<c:url value='/sell/sellUploadExePrev.do'/>");
				fileUpload.setFormData(oForm);
				fileUpload.setCallback(fnUploadPrevCallBack);
				fileUpload.ajax();
			}
		}

		/**
		 * 업로드 미리보기 callback
		 */
		function fnUploadPrevCallBack(result){
			var dataList;
			$("#board_list tbody").empty();
			var vSeq = 0;
			var vBfCustNm = "";
			var vCurrCustNm = "";
			var vSelectedY = "";
			var vSelectedN = "";
			
			var vSelectedA = "";
			var vSelectedB = "";
			var vSelectedC = "";
			var vSelectedD = "";
			
			var vSelectedE = "";
			$.each(result.list, function(i, val){
				vCurrCustNm = gfn_nvl(val.cust_nm);
				if(vCurrCustNm != vBfCustNm){
					vSeq = 1;
				}
				dataList  = '<tr class="list_row">';				
				dataList += '<td name="listNewYn">' + gfn_nvl(val.new_yn) + '</td>';			//미등록 상품
				dataList += '<td name="listSellDt">' + gfn_nvl(val.sell_dt) 	+ '</td>';		//판매일자
				dataList += '<td name="listCustNm">' + gfn_nvl(val.cust_nm) 	+ '</td>';		//고객명
				dataList += '<td name="listBranchNm">' + gfn_nvl(val.branch_nm) + '</td>';		//지점명
				dataList += '<td name="listSellSeq">' + vSeq + '</td>';		//일련번호
				
				//미입/교환
				if("E" == gfn_nvl(val.sell_type)){
					vSelectedE = "selected";
					vSelectedN = "";
					vSelectedY = "";
				} else if("N" == gfn_nvl(val.sell_type)){
					vSelectedE = "";
					vSelectedN = "selected";
					vSelectedY = "";
				} else{
					vSelectedE = "";
					vSelectedN = "";
					vSelectedY = "selected";
				}
				dataList += '<td name="listSellType"><select name="sellType"><option value="E"'+vSelectedE+'>교환</option><option value="N"'+vSelectedN+'>미입</option><option value=""'+vSelectedY+'></option></select></td>';		//미입/교환
				
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
				dataList += '<td name="listGrpType" style="display:none">' + gfn_nvl(val.grp_type) + '</td>';		//작업표 정렬조건
				dataList += '</tr>';
				
				vBfCustNm = gfn_nvl(val.cust_nm);
				vSeq++;
				$("#board_list tbody").append(dataList);
			});
			
			fnProdNmList();
			fnUnitNmList();
			fnSetGridActive();
		}

		/**
		 * 미등록 상품 라인색 강조
		 */
		function fnSetGridActive(){
			var vParents = "";
			$("#board_list > tbody > tr > td[name='listNewYn']").each(function(){
				if($(this).text() == "N"){
					//$(this).parents('.lnb_nav > ul > li').addClass('active');
					vParents = $(this).parents("tr");	
					$(vParents).addClass('active');
				}
			});
		}
		
		/**
		 * 미리보기 이후 등록
		 */
		function fnSellSave() {			
			var arrChecked = [];
			var vJsonParam = "";
			var chkCnt = 0;
			
			//목록을 array에 담아 전달
			$(".block_list table tbody tr").each(function (i) {			
				vJsonParam = { "sell_dt" : this.children.listSellDt.textContent							//판매일자
						  		, "cust_nm" : this.children.listCustNm.textContent						//고객명
						  		, "branch_nm" : this.children.listBranchNm.textContent					//지점명
						  		, "sell_seq" : this.children.listSellSeq.textContent					//일련번호
						  		, "sell_type" : this.children.listSellType.children.sellType.value		//미입/교환구분
						  		, "prod_nm" : this.children.listProdNm.children.prodNm.value			//상품명
						  		, "sell_quan" : this.children.listSellQuan.children.sellQuan.value		//판매수량
						  		, "unit_nm" : this.children.listUnitNm.children.unitNm.value			//단위명
						  		, "prod_price" : this.children.listProdPrice.children.prodPrice.value	//금액
						  		, "tax_yn" : this.children.listTaxYn.children.taxYn.value				//과세여부
						  		, "prod_typ" : this.children.listProdTyp.children.prodTyp.value			//종류
						  		, "grp_type" : this.children.listGrpeType.textContent					//작업표정렬구분
				}				
				arrChecked.push(vJsonParam);
				chkCnt++;
			});
			
			//전달된 파라미터는 CommUtil.json2List 기능을 통해 List Object로 전환해서 사용한다. 
			var vJsonString = arrChecked;
			
			commonAjax.clearParam(); 
			commonAjax.setUrl("<c:url value='/sell/setSellList.do' />");
			commonAjax.setDataType("json");
			commonAjax.setJsonParam(vJsonString);
			commonAjax.setCallback("fnSellSaveCallback");
			commonAjax.ajax();	
		}
		
		function fnSellSaveCallBack(response) {
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
			판매내역 업로드
		</div>
		<div class="container_wrap">
			<input id="excel" name="excel" class="file" type="file" multiple data-show-upload="false" data-show-caption="true" />
			<button type="button" id="excelUp" onclick="javascript:fnUploadPrev();">미리보기</button>
		</div>
		
		<div class="sub_title">
			미리보기
		</div>
		<div class="block_list">
			<table id="board_list" class="list_table colwidth">
				<thead>
					<tr>
						<th width="5%">등록<br>여부</th>		
						<th width="5%">판매일자</th>
						<th width="14%">고객명</th>
						<th width="14%">지점명</th>					
						<th width="5%">일련<br>번호</th>
						<th width="5%">미입<br>교환</th>
						<th width="20%">상품명</th>
						<th width="7%">수량</th>
						<th width="8%">단위</th>
						<th width="10%">개당가격</th>
						<th width="6%">과세여부</th>
						<th width="6%">종류</th>
						<th width="6%" style="display:none">작업표정렬조건</th>
					</tr>
				</thead>
				<tbody>
			
				</tbody>
			</table>			
		</div>
		<div class="list_btn_group" id="btnGroup">
			미등록 상품이 있을 경우 오타가 아닌지 확인해주세요. 등록하실때 새로운 상품으로 등록됩니다.
			<button class="btn_default" id="btnSave">판매내역 등록</button>		
		</div>
		
	</div>
	
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
