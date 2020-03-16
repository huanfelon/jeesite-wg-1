/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 
 * @author ThinkGem
 * @version 2019-1-6
 */

$("#username, #password").on("focus blur",function(){
	var a=this;
	setTimeout(function(){
		var b=$(a).css("borderColor");
		if(b!=""){
			$(a).prev().css("color",b)
		}
	},100)
}).blur();

$("#loginForm").validate({submitHandler:function(c){
	
	/*var Browser=myBrowser();
	var browserName=Browser.name;
	var browserVersion=Browser.version;
	//js.showMessage(browserName+"版本："+browserVersion);
	if(browserName=="手机浏览器"||browserVersion=="手机版本"){
		//判断如果浏览器版本过低直接提示不允许登录
		js.alert("当前系统暂不支持手机查看，为了更好地体验请移至PC端浏览器！"
				,{icon: 7},null); 
		return;
	}
	if(browserName=="未知的浏览器"||browserVersion=="未知的版本"){
		//判断如果浏览器版本过低直接提示不允许登录
		js.alert("当前浏览器不支持或版本过低，请下载最新版IE浏览器或Chrome浏览器！"
				,{icon: 7},null); 
		return;
	}*/
	var d=$("#username").val(),
		a=$("#password").val(),
		b=$("#validCode").val();
	if(secretKey!=""){
		$("#username").val(DesUtils.encode(d,secretKey));
		$("#password").val(DesUtils.encode(a,secretKey));
		$("#validCode").val(DesUtils.encode(b,secretKey))
	}
	js.ajaxSubmitForm($(c),function(f,e,g){
		if(f.isValidCodeLogin==true){
			$("#isValidCodeLogin").show();
			$("#validCodeRefresh").click()
		}
		if(f.result=="false"&&f.message.length>0){
			js.showMessage(f.message)
		}else{
			js.loading($("#btnSubmit").data("loading"));
			if(f.__url&&f.__url!=""){
				location=f.__url
			}else{
				location=ctx+"/index"
			}
		}
	},"json",true,
	$("#btnSubmit").data("loginValid"));$("#username").val(d);
	$("#password").val(a).select().focus();$("#validCode").val(b)
}});

/**
 * 获取当前浏览器版本
 */
function myBrowser() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var name="未知的浏览器";
    var version="未知的版本";
    var isMobile=userAgent.indexOf("Mobile") > -1;
    var isIE = userAgent.indexOf("compatible") > -1
            && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    var isChrome = userAgent.indexOf("Chrome") > -1
            && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器

    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion == 11) {
        	version="IE11";
        }
        name="IE";
      //IE版本过低
    }
    if (isChrome) {
    	var version0=userAgent.substring(userAgent.indexOf('Chrome')+'Chrome'.length+1,userAgent.lastIndexOf(' '));
    	if(version0.split(".")[0]<70){
    		version="未知的版本";
    	}else{
    		version=version0.split(".")[0];
    	}
    	name="Chrome";
    }
    if(isMobile){
    	version="手机版本";
    	name="手机浏览器";
    }
    var browser={name,version};
    return browser;
}

