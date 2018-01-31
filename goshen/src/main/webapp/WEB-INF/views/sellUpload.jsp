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
				//dataList += '<td name="listProdNm"><input name="prodNm" class="price" value="' + gfn_nvl(val.prod_nm) 	+ '" /><button type="button" class="search_btn" id="btnSchProd"><i class="fa fa-search"></i></button></td>';		//상품
				dataList += '<td name="listProdNm"><input type="text" id="prodNm" list="prodList" autocomplete="on" maxlength="50" value="' + gfn_nvl(val.prod_nm) 	+ '"><datalist id="prodList"></datalist></td>';		//상품
				dataList += '<td name="listSellQuan"><input type="text" class="price" id="sellQuan" value="' + gfn_nvl(val.sell_quan) + '" /></td>';		//수량
				dataList += '<td name="listUnitNm">' + gfn_nvl(val.unit_nm) 	+ '</td>';		//단위
				
				dataList += '<td name="listProdPrice"><input name="prodPrice" class="price" value="' + gfn_nvl(val.prod_price) + '"/></td>';	//가격
				if("Y" == gfn_nvl(val.tax_yn)){
					vSelectedY = "selected";
					vSelectedN = "";
				} else{
					vSelectedY = "";
					vSelectedN = "selected";
				}
				dataList += '<td name="listTaxYn"><select name="taxYn"><option value="N"'+vSelectedN+' >면세</option><option value="Y"'+vSelectedY+'>과세</option></select></td>';	//과세 여부
				dataList += '</tr>';
				
				vBfCustNm = gfn_nvl(val.cust_nm);
				vSeq++;
				$("#board_list tbody").append(dataList);
			});
			
			fnProdNmList();
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
						  		, "prod_nm" : this.children.listProdNm.children.prodNm.value			//상품명
						  		, "sell_quan" : this.children.listSellQuan.children.sellQuan.value		//판매수량
						  		, "unit_nm" : this.children.listUnitNm.textContent						//단위명
						  		, "prod_price" : this.children.listProdPrice.children.prodPrice.value	//금액
						  		, "tax_yn" : this.children.listTaxYn.children.taxYn.value				//과세여부
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
		
		function fnSellSaveCallBack(){
			alert("등록 완료");
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
						<th width="15%">고객명</th>
						<th width="15%">지점명</th>					
						<th width="5%">일련<br>번호</th>
						<th width="20%">상품명</th>
						<th width="5%">수량</th>
						<th width="5%">단위</th>
						<th width="10%">개당가격</th>
						<th width="10%">과세여부</th>
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
