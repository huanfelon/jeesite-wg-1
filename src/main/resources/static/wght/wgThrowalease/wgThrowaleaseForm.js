var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	var isnew=$("#isNewRecord").val();
	if(isnew=="false"){//修改
		$("#modifierCode").val(getLoginUserCode());
		$("#modifierName").val(getLoginUserName());
		$("#modifiedtime").val(getNowFormatDateTime());
	}
	
	form_jine();//增加千分位符
	//参照字段选择前过滤
	$(".box-main").on("mousedown", ".input-group",function (){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
	
})

/**
 * 重新定位参照URL地址
 * @param id
 */
function resetDataUrl(id){
	if(id=="pkDeptDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/base/bdDept/treeData";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
}

$("#ly_btnSubmit").click(function (){
	if($(".has-error").length>0){
		js.alert("有必输项未填，请检查！",'','info',3000);
		return ;
	}else{
		
	}
	form_cleanup_jine();//去掉千分位
	$("#btnSubmit").click();
});

//改变合同开始日期
function pickDateAfter(){
	var dend=new Date($("#denddate").val());
	var dtz=new Date($("#dtzdate").val());
	if(dtz.getTime()<dend.getTime()){
		//提前退租
		$("#tztype").select2('val',"1");
	}else{
		//正常退租
		$("#tztype").select2('val',"0");
	}
}
