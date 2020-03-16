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
						}else if(refName.startsWith("custtype")||refName.startsWith("pkProject")){
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
				$("#"+refCode).val(refList[i].id);
				$("#"+refName).val(refList[i].name);
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
		var refName=$(this)[0].id;//用于存放参照名称
		var obj=$(this);//用于定位
		//显示模糊查询区域
		$("#showRefData").css("left",parseInt(obj.offset().left)-9);
	    $("#showRefData").css("top",parseInt(obj.offset().top+obj.outerHeight())-9);
	    $("#showRefData").css("width",parseInt(obj[0].parentNode.clientWidth));
	    $("#showRefData").css('display', 'block');
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
		if(refName.startsWith("pkProject")){
			if(refName.indexOf("_")!=-1){//表体项目
				var rowid=refName.split("_")[1];
				$('#'+rowid+'_projectcode').val($(this).children()[1].innerText);
				$('#'+rowid+'_pkpro').val($(this).children()[0].innerText);
				$('#'+rowid+'_proname').val($(this).children()[2].innerText);
			}
		}
		$("#showRefData").css("display","none");
	});
	/****参照查询input框 end*****/
});