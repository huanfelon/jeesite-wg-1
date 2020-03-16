function setHtmlView() {
	
	$('.input-group-btn a').unbind('click');//移除参照选择
//	$('.input-group-btn a').html('<i class="fa fa-ban"></i>');
	//$('.input-group-btn a').attr('disabled','disabled');//添加好像无效
	//$('input').attr('readOnly','readonly');//input不可选择
	$('input').attr('disabled','disabled');//input参照不可选择(disabled)
	$('input').attr('style', 'background:#F6F6F6!important');
	$('select').attr('disabled','disabled');//select不可选择(disabled)
	$('textarea').attr('readOnly','readonly');//textarea不可选择 (readonly,disabled)
}
function setHtmlView2() {
//	$('.input-group-btn a').html('<i class="fa fa-search"></i>');
	//$('.input-group-btn a').attr('disabled','disabled');//添加好像无效
	//$('input').attr('readOnly','readonly');//input不可选择
	$('input').removeAttr('disabled','disabled');//input参照不可选择(disabled)
	$('input').removeAttr('style', 'background:#F6F6F6!important');
	$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
	$('textarea').removeAttr('readOnly','readonly');//textarea不可选择 (readonly,disabled)
}

function setHtmlView3(arr) {
	//arr 是可编辑的id 如 setHtmlView3(["pkCartype","dgrdate","carstatus"])
	for(let i=0;i< $("input").length;i++){
		var ceshi = $("input").eq(i).attr("id");
		// 隐藏的字段不算
		if($("input").eq(i).attr("type")!="hidden"){
			let id = $("input").eq(i).attr("id");
			let new_id = id.replace(/^Div|Name|Code$/,"");//去掉Div|Name|Code
			if(arr.indexOf(new_id) == -1){
				$("input").eq(i).attr('disabled','disabled');//input参照不可选择(disabled)
				$("input").eq(i).attr('style', 'background:rgba(246, 246, 246, 1) !important');
				try{
					$("input").eq(i).parent().find('.input-group-btn a').unbind('click');//移除参照选择
					$("input").eq(i).parent().find('.input-group-btn a').css("opacity","0.6");
//					$("input").eq(i).parent().find('.input-group-btn a').html('<i class="fa fa-ban"></i>');
				}catch(e){
					console.log(e)
				}
			}
		}
	}
	for(let j=0;j< $("select").length;j++){
		if($("select").eq(j).attr("type")!="hidden"){
			let id = $("select").eq(j).attr("id");
			let new_id = id.replace(/^Div|Name|Code$/,"");//去掉Div|Name|Code
			if(arr.indexOf(new_id) == -1){
				$("select").eq(j).attr('disabled','disabled');//select不可选择(disabled
			}
			
		}
	}
	for(let n=0;n< $("textarea").length;n++){
		if($("textarea").eq(n).attr("type")!="hidden"){
			let id = $("textarea").eq(n).attr("id");
			let new_id = id.replace(/^Div|Name|Code$/,"");//去掉Div|Name|Code
			if(arr.indexOf(new_id) == -1){
				$("textarea").eq(n).attr('disabled','disabled');//select不可选择(disabled
			}
			
		}
	}
	
}

// 加强版：区分表体可编辑字段
function setHtmlView4(arr) {
	//arr 是可编辑的id 如 setHtmlView3(["pkCartype","dgrdate","carstatus","_nnnn"]) 表体的id  加上_,如： "_nyhmny"
	for(let i=0;i< $("input").length;i++){
		var ceshi = $("input").eq(i).attr("id");
		// 隐藏的字段不算
		if($("input").eq(i).attr("type")!="hidden"){
			let id = $("input").eq(i).attr("id");
			let flag_id = id.split("_");
			let new_id = "";
			if(flag_id.length>1){
				//有，那是表体的字段
				new_id = "_" +  flag_id[flag_id.length-1];
			}else{
				new_id = id.replace(/^Div|Name|Code$/,"");//去掉Div|Name|Code
			}
			if(arr.indexOf(new_id) == -1){
				$("input").eq(i).attr('disabled','disabled');//input参照不可选择(disabled)
				$("input").eq(i).attr('style', 'background:rgba(246, 246, 246, 1) !important');
				try{
					$("input").eq(i).parent().find('.input-group-btn a').unbind('click');//移除参照选择
					$("input").eq(i).parent().find('.input-group-btn a').css("opacity","0.6");
//					$("input").eq(i).parent().find('.input-group-btn a').html('<i class="fa fa-ban"></i>');
				}catch(e){
					console.log(e)
				}
			}
			
		}
	}
	for(let j=0;j< $("select").length;j++){
		if($("select").eq(j).attr("type")!="hidden"){
			let id = $("select").eq(j).attr("id");
			let flag_id = id.split("_");
			let new_id = "";
			if(flag_id.length>1){
				//有，那是表体的字段
				// select2-1194871692826726400_isQc-container
				new_id = "_" +  flag_id[flag_id.length-2];
			}else{
				new_id = id.replace(/^Div|Name|Code$/,"");//去掉Div|Name|Code
			}
			if(arr.indexOf(new_id) == -1){
				$("select").eq(j).attr('disabled','disabled');//select不可选择(disabled
			}
			
		}
	}
	for(let n=0;n< $("textarea").length;n++){
		if($("textarea").eq(n).attr("type")!="hidden"){
			let id = $("textarea").eq(n).attr("id");
			let flag_id = id.split("_");
			let new_id = "";
			if(flag_id.length>1){
				//有，那是表体的字段
				// select2-1194871692826726400_isQc-container
				new_id = "_" +  flag_id[flag_id.length-1];
			}else{
				new_id = id.replace(/^Div|Name|Code$/,"");//去掉Div|Name|Code
			}
			if(arr.indexOf(new_id) == -1){
				$("textarea").eq(n).attr('disabled','disabled');//select不可选择(disabled
			}
			
		}
	}
	
}
//点击审核后不可再次点击
/*function DoNotClickAgain(click, num){
	$(click).attr('disabled','disabled');
	$(click).css('cursor','not-allowed');
	$(click).removeClass('btn-primary').addClass('btn-default');
	$(click).unbind('click');
	if(num == 2){
		$('#select2-vbillstatus-container').html('待复核');
		$('#select2-vbillstatus-container').attr('title','待复核');
	}else if(num == 4){
		$('#select2-vbillstatus-container').html('完成');
		$('#select2-vbillstatus-container').attr('title','完成');
	}else if(num == 6){
		$('#select2-vbillstatus-container').html('作废');
		$('#select2-vbillstatus-container').attr('title','作废');
	}
}*/
function DoNotClickAgain(arr, num, newAction){
	setHtmlView2();  //先取消页面不可编辑，获取数据
	//ajax方法获取到页面数据后台接收不到，所以调用自带from获取数据方法然后进行方法截取替换成自己方法
	var action = $("#inputForm").attr('action');
	var new_action = action.slice(0,action.lastIndexOf('/')+1) + newAction;//拼接方法
	console.log(new_action);
	$("#inputForm").attr('action',new_action)
	$('#btnSubmit').click();
	setHtmlView();//页面不可编辑
	for(var i=0;i<arr.length;i++){
		$(arr[i]).attr('disabled','disabled');
		$(arr[i]).css('cursor','not-allowed');
		$(arr[i]).removeClass('btn-primary').addClass('btn-default');
		$(arr[i]).unbind('click');
	}
	
	if(num == 2){
		$('#select2-vbillstatus-container').html('待复核');
		$('#select2-vbillstatus-container').attr('title','待复核');
	}else if(num == 4){
		$('#select2-vbillstatus-container').html('完成');
		$('#select2-vbillstatus-container').attr('title','完成');
	}else if(num == 6){
		$('#select2-vbillstatus-container').html('作废');
		$('#select2-vbillstatus-container').attr('title','作废');
	}
}

function DoNotClickAgain2(click){
	$(click).attr('disabled','disabled');
	$(click).css('cursor','not-allowed');
	$(click).removeClass('btn-primary').addClass('btn-default');
	$(click).unbind('click');
}

/*//20190705 添加作废按钮后，与审核按钮互反
$("#btnSubmit1").click(function(){
	DoNotClickAgain(["#btnSubmit1","#btnSubmit3"], 2, 'exa');
});*/

//复核
$("#btnSubmit2").click(function(){
	DoNotClickAgain(["#btnSubmit2","#btnSubmit3"], 4, "check");

});
//作废
$("#btnSubmit3").click(function(){
	DoNotClickAgain(["#btnSubmit1","#btnSubmit2","#btnSubmit3"], 6, "can");
});
$("#btnSubmit0").click(function(){
	form_cleanup_jine();//去掉千分位
	setHtmlView2();
	$("#btnSubmit").click();
});
