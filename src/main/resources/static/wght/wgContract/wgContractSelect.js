var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	InitQuery();
	
	//参照字段选择前过滤
	$(".input-group").on("mousedown",function showDeptTreeRef(){
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
	if(id=="pkProjectDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl='/js/a/zl/zlProject/treeData?isShowCode=true';
		if(pkorg==""){
			newurl+="&pkOrg=' '";
		}else{
			newurl+="&pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
}

//初始化查询条件
function InitQuery(){
	$(".form-group input").val("");
	$(".select2-hidden-accessible").select2("val", " "); 
	$("#pkOrgCode").val(getMainOrgCode());
	$("#pkOrgName").val(getMainOrgName());
}
/**查询条件必输项校验**/
function checkQueryNotNull(){
	var num=0;
	var pkOrg=$("#pkOrgCode").val();
	if (pkOrg=="") {
		layer.tips('请输入组织', '#pkOrgDiv',{tips: [2,"#c7360c"],tipsMore: true});
		num++;
	}
	return num;
}
/***查询***/
function queryData(){
	var num=checkQueryNotNull();
	if(num==0){
		$("#btnSearch").click();
		$('#dataGrid').dataGrid('refresh');
	}
}
/**刷新**/
function refreshData(){
	$('#dataGrid').dataGrid('refresh');
}
/**重置**/
function resetBtn(){
	InitQuery();
}

