var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	var pk=$("#pkCustomer").val();
	if(pk==""||pk==null){
		$("#pkOrgCode").val(getMainOrgCode());
		$("#pkOrgName").val(getMainOrgName());
		//为表体第一行组织塞值
		/*var arrs=$("#bdCustomerKpxxDataGrid").getDataIDs();
		for(let i=0;i<arrs.length;i++){
			$("#office_"+arrs[i]+"_pkOrgCode").val(getMainOrgCode());
			$("#office_"+arrs[i]+"_pkOrgName").val(getMainOrgName());
		}*/
		//设置单据号
		setMaxBillno();
	}
	
	//参照字段选择前过滤
	$(".input-group").on("mousedown",function showDeptTreeRef(){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
	
	$('#bdCustomerProjDataGridAddRowBtn').click(function (){
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

	var orgcode=$("#pkOrgCode").val();
	var orgname=$("#pkOrgName").val();
	/*var arrs=$("#bdCustomerKpxxDataGrid").getDataIDs();
	for(let i=0;i<arrs.length;i++){
		$("#office_"+arrs[i]+"_pkOrgCode").val(orgcode);
		$("#office_"+arrs[i]+"_pkOrgName").val(orgname);
	}*/
	
	setMaxBillno();
}

/**
 * 设置最大单据号
 * @param id
 * @returns
 */
function setMaxBillno() {
	
	var pkOrg=$("#pkOrgCode").val();
	$.ajax({
		type : "post",
		url : "./getMaxBillno",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : pkOrg,
		async : false,
		success : function(data) {
			$('#code').val(data.message);
		},
	    error : function (data) {
	    	alert("请求失败！");
	    }
	});
}


function resetDataUrl(id){
	//表体
	if(id.startsWith("proj_")){
		//获取当前行组织值
		var pkOrg=$('#pkOrgCode').val();
		var newurl='/js/a/zl/zlProject/treeData';
		if(pkOrg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkOrg;
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
				if(inputid.startsWith("pkOrg")){
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
	
	//表体项目
	if(id.startsWith('proj_') && (act == 'ok' || act == 'clear')){//组织选择
		var rowid=id.split("_")[1];
		if(nodes!=null&&nodes!=undefined){
			var newpro=$("#"+id+"Code").val();
			if(oldRefCode==newpro){
				return;
			}
			//设置项目编码
			$("#"+rowid+"_code").val(nodes[0].code);
		}else{
			$("#"+rowid+"_code").val(null);
		}
	}
	
}

$("#ly_btnSubmit").click(function (){
	if($(".has-error").length>0){
		js.alert("有必输项未填，请检查！",'','info',3000);
		return ;
	}else{
		var pids=$('#bdCustomerProjDataGrid').dataGrid('getDataIDs');
		if(pids.length>0){
			var row=0;
			for(var i=0;i<pids.length;i++){
				row++;
				var pkpro=$("#proj_"+pids[i]+"_pkProjectCode").val();
				if(pkpro==undefined||pkpro==null||pkpro==""){
					js.alert("[项目信息页签]第"+row+"行项目不可为空！",'','info',3000);
					return ;
				}
			}
		}else{
			js.alert("[项目信息页签]不可为空，请至少维护一条数据！",'','info',3000);
			return ;
		}
	}
	$("#btnSubmit").click();
});
