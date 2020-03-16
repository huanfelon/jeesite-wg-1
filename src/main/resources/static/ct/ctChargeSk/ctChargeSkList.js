$(document).ready(function() {
	InitQuery();
	//参照字段选择前过滤
	$(".input-group").on("mousedown",function showDeptTreeRef(){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
	$('#dataGrid').dataGrid('refresh');
})
//初始化查询条件
function InitQuery(){
	$(".form-group input").val("");
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
	var pkPro=$("#pkProjectCode").val();
	if(pkPro==""){
		layer.tips('请输入项目', '#pkProjectDiv',{tips: [2,"#ff0000b5"],tipsMore: true});
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

/**重置**/
function resetBtn(){
	InitQuery();
}

function resetDataUrl(id){
	//项目根据组织过滤
	if(id==("pkProjectDiv")){
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
	//客户根据组织过滤
	if(id==("pkCustomerDiv")){
		//获取当前行组织值
		var pkOrg=$('#pkProjectCode').val();
		var newurl='/js/a/base/bdCustomer/bdCustomerSelect';
		if(pkOrg==""){
			newurl+="?pk_project=' '";
		}else{
			newurl+="?pk_project="+pkOrg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	//部门根据组织过滤
	if(id==("pkDeptDiv")){
		//获取当前行组织值
		var pkOrg=$('#pkOrgCode').val();
		var newurl='/js/a/base/bdDept/treeData';
		if(pkOrg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkOrg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	//楼栋根据项目过滤
	if(id==("pkBuildDiv")){
		//获取当前行组织值
		var pkProject=$('#pkProjectCode').val();
		var newurl='/js/a/zl/zlBuildingfile/buildingSelect';
		if(pkProject==""){
			newurl+="?pkProjectid=' '";
		}else{
			newurl+="?pkProjectid="+pkProject;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	//房产根据楼栋过滤
	if(id==("pkHouseDiv")){
		//获取当前行组织值
		var pkProject=$('#pkBuildCode').val();
		var newurl='/js/a/zl/zlHousesource/housesourceSelect';
		if(pkProject==""){
			newurl+="?buildname=' '";
		}else{
			newurl+="?buildname="+pkProject;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
}

/**列表参照获取界面选择数据(用作参照前方法，变更参照URL)**/
function listselectGetSelectData(id){
	
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
	
	if(id=='pkOrg' && (act == 'ok' || act == 'clear')){//项目选择
		if(nodes==null||nodes==undefined){
			$('#pkDeptCode').val(null);
			$('#pkDeptName').val(null);
			$('#pkProjectCode').val(null);
			$('#pkProjectName').val(null);
			$('#pkCustomerCode').val(null);
			$('#pkCustomerName').val(null);
			$('#pkHouseCode').val(null);
			$('#pkHouseName').val(null);
			$('#pkBuildCode').val(null);
			$('#pkBuildName').val(null);
			return;
		}
		var newpro=$("#pkProjectCode").val();
		if(newpro!=oldRefCode){
			$('#pkDeptCode').val(null);
			$('#pkDeptName').val(null);
			$('#pkProjectCode').val(null);
			$('#pkProjectName').val(null);
			$('#pkCustomerCode').val(null);
			$('#pkCustomerName').val(null);
			$('#pkHouseCode').val(null);
			$('#pkHouseName').val(null);
			$('#pkBuildCode').val(null);
			$('#pkBuildName').val(null);
		}
	}
	
	if(id=='pkProject' && (act == 'ok' || act == 'clear')){//项目选择
		if(nodes==null||nodes==undefined){
			$('#pkHouseCode').val(null);
			$('#pkHouseName').val(null);
			$('#pkBuildCode').val(null);
			$('#pkBuildName').val(null);
			$('#pkCustomerCode').val(null);
			$('#pkCustomerName').val(null);
			return;
		}
		var newpro=$("#pkProjectCode").val();
		if(newpro!=oldRefCode){
			$('#pkHouseCode').val(null);
			$('#pkHouseName').val(null);
			$('#pkBuildCode').val(null);
			$('#pkBuildName').val(null);
			$('#pkCustomerCode').val(null);
			$('#pkCustomerName').val(null);
		}
	}
}
function listselectCallback(id, act, index, layero,selectData,nodes){
	if(id=='pkBuild' && (act == 'ok' || act == 'clear')){//房产选择
		if(selectData==null){
			$('#pkHouseCode').val(null);
			$('#pkHouseName').val(null);
			return;
		}
		var newval=$("#pkBuildCode").val();
		if(newval!=oldRefCode){
			$('#pkHouseCode').val(null);
			$('#pkHouseName').val(null);
		}
	}
}

/**收款单参照应收单**/
function refPageYs(){
	//多窗口模式，层叠置顶
    js.layer.open({
    	id:'gatherref1',
    	type: 2, //此处以iframe举例
    	anim: 1,
    	title: '收款单参照应收单',
    	area: ['1100px', $(top.window).height()*3/4 + "px"],
    	shade: 0.2,
    	maxmin: true,
    	content: '/js/a/ct/ctChargeYs/reflist',
    	btn: ['确认', '取消'],
    	yes: function(index, layero){
    		$('#dataGrid').dataGrid('reloadGrid');//刷新当前列表数据区域
    		var data = $(layero).find("iframe").contents().find("#dataGrid");//获取弹出框内dataGrid
    		var selectRows = data.dataGrid('getSelectRows');
    		if(selectRows.length<=0){
    			//layer.msg('请选择一条数据！',{icon: 7});
    			js.showMessage("请至少选择一条数据！",null,'warning');
    			return;
    		}
    		var selectData=[]; 
    		for(var i=0;i<selectRows.length;i++){
    			var status=data.dataGrid('getRowData', selectRows[i]);
    			 if(status.vbillstatus=="自由态"){
    				js.showMessage("单据还未审批，请先审批!",null,'warning');
    				return;
    			}
    			 selectData.push(status);
    		}
    		var pkOrg_arr=[];
    		var pkProject_arr=[];
    		for(var i=0;i<selectData.length;i++){
    			var pkOrg=selectData[i]["pkOrg.officeName"];
    			var pkProject=selectData[i]["pkProject.name"];
    			pkOrg_arr.push(pkOrg);
    			pkProject_arr.push(pkProject);
    		}
    		for(var i=0;i<pkOrg_arr.length-1;i++){
    			for(var j=i+1;j<pkOrg_arr.length;j++){
    				if(pkOrg_arr[i]!=pkOrg_arr[j]){
    					js.showMessage('所选单据组织不同，不可制单!','','warning',3000);
    					return  ;
    				}
    			}
    		}
    		for(var i=0;i<pkProject_arr.length-1;i++){
    			for(var j=i+1;j<pkProject_arr.length;j++){
    				if(pkProject_arr[i]!=pkProject_arr[j]){
    					js.showMessage('所选单据项目不同，不可制单!','','warning',3000);
    					return  ;
    				}
    			}
    		}
    		var pks = selectRows.join("-");
    	    js.addTabPage($(this), "新增收款单", "/js/a/ct/ctChargeSk/refToYs?pkChargeYs="+pks, null, null);//弹出新的标签页
    		//layer.close(index); //如果设定了yes回调，需进行手工关闭
    	},
    	btn2: function(index, layero){
    		layer.close(index);
    	},
    	zIndex: layer.zIndex,
    	end: function(){
    		//无论保不保存，弹框关闭就刷新列表
    		$('#dataGrid').dataGrid('reloadGrid');
    	 	
    	},
    });
}
