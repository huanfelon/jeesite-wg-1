var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	var pk=$("#pkPsndoc").val();
	if(pk==""||pk==null){
		$("#pkOrgCode").val(getMainOrgCode());
		$("#pkOrgName").val(getMainOrgName());
	}
	
	//参照字段选择前过滤
	$(".input-group").on("mousedown",function showDeptTreeRef(){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
	
	$('#bdPsndocJobDataGridAddRowBtn').click(function (){
		$(".input-group").on("mousedown",function showDeptTreeRef(){
			var id=$(this).attr("id");
			oldRefName=$(this).context.children[0].value;
			oldRefCode=$(this).context.children[1].value;
			resetDataUrl(id);
		});
	});
	
})

/**
 * 组织切换后
 */
function changeOrgAfter(){

}

function resetDataUrl(id){
	//表头
	if(id=="pkDeptDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl='/js/a/base/bdDept/treeData';
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	if(id=="pkDeptallDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl='/js/a/base/bdDept/treeData';
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	//表体
	if(id.endsWith("pkDeptcDiv")){
		//获取当前行组织值
		var pkOrg=id.replace('pkDeptcDiv','pkOrgCode');
		var pkorg=$('#'+pkOrg).val();
		var newurl='/js/a/base/bdDept/treeData';
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	if(id.endsWith("pkDeptcallDiv")){
		//获取当前行组织值
		var pkOrg=id.replace('pkDeptcallDiv','pkOrgCode');
		var pkorg=$('#'+pkOrg).val();
		var newurl='/js/a/base/bdDept/treeData';
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
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
		layer.confirm("改变组织将会清空后续数据，是否继续？", {icon: 3, title:'信息'}, function(){
			var inputs=document.getElementsByTagName('input');
			for(var i = 2; i < inputs.length; i++) {
				var inputid=inputs[i].id;
				if(inputid.startsWith("pkOrg")||inputid.startsWith("office_")){
					continue;
				}
				inputs[i].value = ""; // 将每一个input的value置为空
			}
			changeOrgAfter();
		},function(){
			$("#pkOrgCode").val(oldRefCode);
			$("#pkOrgName").val(oldRefName);
		});
	}
	
	//表体组织
	if(id.endsWith('_pkOrg') && (act == 'ok' || act == 'clear')){//组织选择
		if(nodes!=null&&nodes!=undefined){
			var newpro=$("#"+id+"Code").val();
			if(oldRefCode==newpro){
				return;
			}
		}
		
		//清空部门信息
		var dept=id.replace('pkOrg','pkDeptc');
		var deptall=id.replace('pkOrg','pkDeptcall');
		$("#"+dept+"Code").val(null);
		$("#"+dept+"Name").val(null);
		$("#"+deptall+"Code").val(null);
		$("#"+deptall+"Name").val(null);
	}
	
}


