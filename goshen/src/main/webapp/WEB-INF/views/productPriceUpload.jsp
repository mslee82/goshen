<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>단가표 업로드</title>
	<script type="text/javascript">
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

		function fnUpload(){
			var file = $("#excel").val();
			if(file == "" || file == null){
				alert("파일을 선택");
				return false;
			} else if(!checkFileType(file)){
				alert("엑셀 파일만 업로드");
				return false;
			}
			var oForm = new FormData();
			if(!gfn_isNull(excel.files[0])){
				oForm.append("excel", excel.files[0]);
		    }
			//oForm.append("excel", excel.files[0]);
			fileUpload.setURI("<c:url value='/product/productUploadExe.do'/>");
			fileUpload.setFormData(oForm);
			fileUpload.setCallback(fnUploadCallback);
			fileUpload.ajax();
		}

		function fnUploadCallback(){
			alert("끝");
		} 
				
	</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navi.jsp" %>
	<div class="container_wrap">
		<div class="sub_title">
			단가표 업로드
		</div>
		<div class="container_wrap">
			<input id="excel" name="excel" class="file" type="file" multiple data-show-upload="false" data-show-caption="true" />
			<button type="button" id="excelUp" onclick="javascript:fnUpload();">등록</button>
		</div>
	</div>
<%@ include file="/WEB-INF/include/tailer.jspf" %>	
</body>
</html>
