$(document).ready(function() {
	$(".input-group input").unbind("click");//移除参照选择
	$(".input-group input").attr("readonly",false);
	$(".input-group input").attr("autocomplete","off");
	
	var refName,
	refCode,
	refList=new Array();
	/****参照查询input框 start*****/
	$(".input-group input").bind("focus propertychange",function (){//鼠标点击input触发事件
		//查询区域初始化
		$("#showRefData").css("display","none");
		$("#showRefData").children().html("");
		refList=new Array();
		var refUrl=$(this).parent().attr("data-url");//参照路径
		if(refUrl.indexOf("familySelect")!=-1){
			refUrl="/js/a/zl/zlFamilyfile/listFamily"
		}else if(refUrl.indexOf("buildingSelect")!=-1){
			refUrl="/js/a/zl/zlBuildingfile/listBuild"
		}
		if(refUrl.startsWith("11")){//路径是列表的转换查数据路径，可重写模糊查询方法
			
		}
		refName=$(this)[0].id;//用于存放参照名称
		refCode=$(this)[0].nextElementSibling.id;//用于存放参照主键
		var JSONDATA={};
		$.ajax({
			url: refUrl,
			datatype: 'json',
			data: JSONDATA,
			contentType: 'application/json;charset=utf-8',
			success: function (data) {
				if(data!=null&&data.length>0){
					for(var j=0;j<data.length;j++){
						if(refName.startsWith("pkOrg")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].id,
									"name":data[j].name
								};
						}else if(refName.startsWith("chargeitem")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}else if(refName.startsWith("projectname")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}else if(refName.startsWith("pkProjectid")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}else if(refName.startsWith("pkFormattypeid")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}else if(refName.startsWith("pkFormattype")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}else if(refName.startsWith("pkFamilyfile")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}else if(refName.startsWith("buildname")){
							refList[j]={
									"id":data[j].id,
									"code":data[j].code,
									"name":data[j].name
								};
						}
					}
					var html_li="";
					for(var i=0;i<refList.length;i++){
						html_li+="<li>";
						html_li+="<span style='display:none'>"+refList[i].id+"</span>";//存放主键
						html_li+="<span style='display:none'>"+refList[i].code+"</span>";//存放编码
						html_li+="<span>"+refList[i].name+"</span>";//存放名称
						html_li+="</li>";
					}
					$("#showRefData").children().html(html_li);
				}
			}
		});
	});
	$(".input-group input").on("dblclick",function (){//inpu值双击事件
		var id=$("#"+refCode).val();
		for(var i=0;i<refList.length;i++){
			if(refList[i].id==id){
				$("#"+refName).val(refList[i].code);
				break;
			}
		}
	});
	$(".input-group input").bind("blur propertychange",function (){//input框光标移出事件
		var s_value = $(this).val();//input框值
		var count=0;
		for(var i=0;i<refList.length;i++){
			if(refList[i].code==s_value||refList[i].name==s_value){
				$("#"+refName).val(refList[i].name);
				$("#"+refCode).val(refList[i].id);
				count++;
			}
		}
		if(count!=1){
			$("#"+refName).val(null);
			$("#"+refCode).val(null);
		}
	});
	$(".input-group input").bind("input propertychange",function (){//input框改变值事件
		var s_value = $(this).val();//input框值
		if(s_value==null||s_value==""){
			$("#showRefData").css("display","none");
			return;
		}
	    //匹配值
	    var count=0;
	    $("#showRefData").children().children("li").each(function(){
	    	var code=$(this).children()[1].innerText;
	    	var name=$(this).children()[2].innerText;
	    	if(code.indexOf(s_value)!=-1||name.indexOf(s_value)!=-1){
	    		$(this).css("display","block");
	    		count++;
	    	}else{
	    		$(this).css("display","none");
	    	}
	    });
	    var obj=$(this);//用于定位
		var pageh= $(".wrapper").height();
		var scollh=document.documentElement.scrollTop;  //网页被卷起来的高
		var viewh=document.documentElement.offsetHeight; //网页可见区域高
		var thistop=parseInt(obj.offset().top+obj.outerHeight())-9;
		var refh=$("#showRefData").height();
		var canh=(pageh-thistop+scollh)<(viewh-thistop+scollh)?(pageh-thistop+scollh):(viewh-thistop+scollh);
		if(refh>canh){
			thistop=parseInt(obj.offset().top)-refh-11;
		}
		//显示模糊查询区域
		$("#showRefData").css("left",parseInt(obj.offset().left)+1);
	   	$("#showRefData").css("top",thistop+10);
	    $("#showRefData").css("min-width",parseInt(obj[0].parentNode.clientWidth));
	    $("#showRefData").css('display', 'block');
	    if(count==0){
	    	$("#showRefData").css("display","none");
	    }
	});
	$(".box-body,.box-head").bind("click propertychange",function (){//页面点击事件
		$("#showRefData").css("display","none");
	});
	$("#showRefData").on("click","li",function(){//模糊查询值区域点击事件
		$("#"+refCode).val($(this).children()[0].innerText);
		$("#"+refName).val($(this).children()[2].innerText);
		$("#showRefData").css("display","none");
		if(refCode=="chargeitemCode"){
			var proj = $(this).children()[0].innerText
			var JSONDATA={'pkChargeitemCode':proj};
			$.ajax({
				url: '/js/a/zl/zlCostproject/out',
				datatype: 'json',
				data: JSONDATA,
				async:false,
				contentType: 'application/json;charset=utf-8',
				success: function (msg) {
				     var selectData=msg.roundtype;
				     $("#roundform").val(selectData).trigger("change");
				}
			});
		}else if(refCode=="pkFamilyfileCode"){
			 var proj = $(this).children()[0].innerText;
				var JSONDATA={'pkFamilyfileCode':proj};
				$.ajax({
					url: '/js/a/zl/zlFamilyfile/out',
					datatype: 'json',
					data: JSONDATA,
					async:false,
					contentType: 'application/json;charset=utf-8',
					success: function (msg) {
					     $("#pkFormattypeName").val(msg.pkFormattypeid.name);
					     $("#pkFormattypeCode").val(msg.pkFormattypeid.id);
					     $("#buildarea").val(msg.builtuparea);
					     $("#innerarea").val(msg.innerarea);
					}
				});
		}
		 
	});
	/****参照查询input框 end*****/
});