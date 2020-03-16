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

/**
 * 选择回调方法
 * @param id  标签的id
 * @param act 动作事件：ok、clear、cancel
 * @param index layer的索引号
 * @param layero layer内容的jQuery对象
 * @param nodes 当前选择的树节点数组
 */
function treeselectCallback(id, act, index, layero, nodes){
	if(id=='pkOrg' && (act == 'ok' || act == 'clear')){//组织选择
		if(nodes!=null&&nodes!=undefined){
			var newpro=$("#pkOrgCode").val();
			if(oldRefCode==newpro){
				return;
			}
		}
		$("#pkProjectCode").val(null);
		$("#pkProjectName").val(null);
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
	var pkpro=$("#pkProjectCode").val();
	if (pkpro=="") {
		layer.tips('请输入项目', '#pkProjectDiv',{tips: [2,"#c7360c"],tipsMore: true});
		num++;
	}
	return num;
}
/***查询***/
function queryData(){
	var num=checkQueryNotNull();
	if(num==0){
		$('#dataGrid').dataGrid('refresh');
		$("#btnExport").removeClass("hide");
	}
}
/**重置**/
function resetBtn(){
	InitQuery();
}

/** 查看计划金额 **/
function showPlanMny(pk){
	//弹出查看界面
	js.layer.open({
    	id:"planMny",
    	type: 2, //此处以iframe举例
    	anim: 1,
    	title: "合同计划金额明细",
    	area: ['1100px', "564px"],
    	shade: 0.2,
    	shadeClose: false,
    	content: '/js/a/reports/fzWyhttz/planMny?pk='+pk,
    	btn: ['确认'],
    	zIndex: layer.zIndex,
    });
}
