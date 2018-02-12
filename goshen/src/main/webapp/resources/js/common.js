/**
 * NULL to String
 */
function gfn_nvl(str, replaceStr){
	if(gfn_isNull(str)){
		if(gfn_isNull(replaceStr)){
			return "";
		}else{
			return replaceStr;
		}
	}else{
		return str;
	}
}

/**
 * Form Submit
 */
var commonSubmit = new function() {
	
    this.url = "";
    this.formId =  "";
    this.method = "";

    this.setUrl = function setUrl(url){
        this.url = url;
    };
    
    this.clearForm = function clearForm(){
    	$(this.formId).empty();
    } 
    
    /**
     * Set the form tag for submit.
     */
    this.setForm = function setForm(formId) {
    	$(formId).empty();
    	this.formId = formId;
    };
    
    /**
     * Set the parameter for sending to server
     */
    this.addParam = function addParam(key, value){
    	if (gfn_isNull(this.formId)) {
    		$('#TempForm').remove();
        	$('body').append('<form id="TempForm" style="display:none"></form>');
        	this.formId = $("#TempForm");
    	}
        $(this.formId).append($("<input type='hidden' name='"+key+"' id='"+key+"' value='"+value+"' >"));
    };
    
    this.setMethod = function setMethod(method) {
    	this.method = method;
    }
    
    /**
     * Call URL using parameter and form tag.
     */
    this.submit = function submit(){
    	if (gfn_isNull(this.formId)) {
        	$('body').append('<form id="TempForm" style="display:none"></form>');
        	this.formId = $("#TempForm");
    	}
        var frm = $(this.formId)[0];
        frm.action = this.url;
        frm.method = this.method;
        frm.submit();   
    };
}

/**
 * fileUpload excel도 포함한다.
 */
var fileUpload = new function() {
	this.deafultUri = "";
	this.uri="";
	var uploadForm;

	this.setURI = function setURI(uri) {
		this.uri = uri;
	}
	
	this.setFormData = function setFormData(form) {
		this.uploadForm = form;
	}
	
	this.setCallback = function setCallback(callBack){
		gfv_ajaxCallback = callBack;		
	};
	
	
	this.ajax = function ajax(){	
		if (gfn_isNull(this.uri)) {
			this.uri=this.defaultUri;
		}
		if (gfn_isNull(this.uploadForm)) {
			var message = $("#golabefilenotexist").val() + " : " + this.fileName;
			gfn_messageDialog(message);
			return;
		}
			
		$.ajax({
			url : this.uri,
			type : "POST",   
			data : this.uploadForm,
			dataType : "json",
  	        processData: false,
		    contentType: false,			
		    enctype : "multipart/form-data",
			success : function(data, status) {
				if(typeof(gfv_ajaxCallback) == "function"){
					gfv_ajaxCallback(data);
				}
				else {
					eval(gfv_ajaxCallback + "(data);");
				}
		    }
		    ,error: OnError
		});
	};
}

/**
 * Global variable for AJAX call back processing
 */
var gfv_ajaxCallback = "";

/**
 * Ajax call
 */
var commonAjax = new function () {
	this.url = "";		
	this.param = "";
	this.async = false,
	
	this.setUrl = function setUrl(url){
		this.url = url;
	};
	
	this.setDataType = function setDataType(dataType) {
		this.dataType = dataType;
	}
	this.setCallback = function setCallback(callBack){
		gfv_ajaxCallback = callBack;
	};
	
	this.setAsyncl = function setAsyncl(async){
		this.async = async;
	};

	this.clearParam = function clearParam() {
		this.param = "";
	}
	
	this.addParam = function addParam(key,value){
		this.param = this.param + "&" + key + "=" + value; 
	}
	
	this.setJsonParam = function setJsonParam(param) {
		this.param = this.param + "&" + "jsonData=" + JSON.stringify(param);
	}
	
	this.setSerializeParam = function setSerializeParam(param){
		this.param = param;
	}
	
	this.ajax = function ajax(){	

		if ( gfn_isNull(this.dataType) == true) {
			this.dataType = "json";
		}
			
		$.ajax({
			url : this.url,
			type : "POST",   
			data : this.param,
			async : this.async,
			dataType : this.dataType,
			success : function(data, status) {
				if(typeof(gfv_ajaxCallback) == "function"){
					gfv_ajaxCallback(data);
				}
				else {
					eval(gfv_ajaxCallback + "(data);");					
				}
			}
		 ,error: OnError
		});
	};
}

function OnError(xhr, errorType, exception ) {
	//var curEnv = $("#curEnv").val();
    //var responseText="";
    //$("#errorMessageDialog").html("");

	//if (curEnv == "production") {
	//	responseText = $("#globalerrorMessage").val();
	//}
	//else {
	    try {
	        responseText = jQuery.parseJSON(xhr.responseText);
	        alert(responseText);
	        //$("#errorMessageDialog").append("<div><b>" + errorType + " " + exception + "</b></div>");
	        //$("#errorMessageDialog").append("<div><u>Exception</u>:<br /><br />" + responseText.ExceptionType + "</div>");
	        //$("#errorMessageDialog").append("<div><u>StackTrace</u>:<br /><br />" + responseText.StackTrace + "</div>");
	        //$("#errorMessageDialog").append("<div><u>Message</u>:<br /><br />" + responseText.Message + "</div>");
	    } catch (e) {
	        responseText = xhr.responseText;
	        if (gfn_isNull(responseText)) {
	        	//responseText =  $("#globalerrorMessage").val();
	        }
	    }		
	//}
	
   // $("#errorMessageDialog").html(responseText);
   /* $("#errorMessageDialog").dialog({
        title: "Error",
        width: 700,
	    modal: true,
	    draggable: true,
	    resizable: true,
        buttons: [{
            text: "닫기",
        	click: function () {
                $(this).dialog('close');
            }
        }]
    });
    $('.ui-dialog-titlebar').addClass('danger');
    */
}

/**
 * NULL 변환
 */
function gfn_isNull(str) {
    if (str == null) return true;
    if (str == "NaN") return true;
    if (new String(str).valueOf() == "undefined") return true;
    if(typeof(str) == "undefined") return true;
    var chkStr = new String(str);
    if( chkStr.valueOf() == "undefined" ) return true;
    if (chkStr == null) return true;    
    if (chkStr.toString().length == 0 ) return true;   
    return false; 
}

/**
 * 고객 목록 조회하기
 */
function fnCustNmList() {
	commonAjax.clearParam(); 
	commonAjax.setUrl("/com/custNmList.do");
	commonAjax.setDataType("json");
	commonAjax.setCallback("fnCustNmListCallback");
	commonAjax.ajax();	
}

/**
 * 고객 목록 조회하기 callback
 */
function fnCustNmListCallback(response) {
	var codeList = $("#custList");
	var codeOpt = "";
	codeList.empty();
	$.each(response.custNmList, function(i, val){
		codeOpt = $("<option></option>").attr("value", val.cust_nm).attr("data-value", val.cust_no);
		codeList.append(codeOpt);
	});
}

/**
 * 상품 목록 조회하기 
 */
function fnProdNmList() {
	commonAjax.clearParam(); 
	commonAjax.setUrl("/com/prodNmList.do");
	commonAjax.setDataType("json");
	commonAjax.setCallback("fnProdNmListCallback");
	commonAjax.ajax();	
}

/**
 * 상품 목록 조회하기 callback
 */
function fnProdNmListCallback(response) {
	var codeList = $("#prodList");
	var codeOpt = "";
	codeList.empty();
	$.each(response.prodNmList, function(i, val){
		codeOpt = $("<option></option>").attr("value", val.prod_nm).attr("data-value", val.prod_no);
		codeList.append(codeOpt);
	});
}

/**
 * 단위 목록 조회하기
 */
function fnUnitNmList() {
	commonAjax.clearParam(); 
	commonAjax.setUrl("/com/unitNmList.do");
	commonAjax.setDataType("json");
	commonAjax.setCallback("fnUnitNmListCallback");
	commonAjax.ajax();	
}

/**
 * 단위 목록 조회하기 callback
 */
function fnUnitNmListCallback(response) {
	var codeList = $("#unitList");
	var codeOpt = "";
	codeList.empty();
	$.each(response.unitNmList, function(i, val){
		codeOpt = $("<option></option>").attr("value", val.unit_nm).attr("data-value", val.unit);
		codeList.append(codeOpt);
	});
}

/**
 * 차량 목록 조회하기
 */
function fnTruckNmList() {
	commonAjax.clearParam(); 
	commonAjax.setUrl("/com/truckNmList.do");
	commonAjax.setDataType("json");
	commonAjax.setCallback("fnTruckNmListCallback");
	commonAjax.ajax();	
}

/**
 * 차량 목록 조회하기 callback
 */
function fnTruckNmListCallback(response) {
	var codeList = $("#truckList");
	var codeOpt = "";
	codeList.empty();
	$.each(response.truckNmList, function(i, val){
		codeOpt = $("<option></option>").attr("value", val.truck_nm).attr("data-value", val.truck_no);
		codeList.append(codeOpt);
	});
}