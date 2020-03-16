$(document).ready(function() {
	InitQuery();
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

/**修改**/
function editBtn(){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据修改！",null,'warning');
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowId); // 获取选择行数据
	if(rowData.status.includes("停用")){
		js.showMessage("已经停用数据不可修改！",null,'warning');
		return;
	}
	var JSONDATA={};
	JSONDATA['pkPsndoc']=rowId;
	$.ajax({
		url: '/js/a/base/bdPsndoc/edit',
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			if(msg!=null&&msg!=""){
				var tag=JSON.parse(msg).result;
				if(tag=="true"){
					js.showMessage(JSON.parse(msg).message);
				}else if(tag=="false"){
					js.showMessage(JSON.parse(msg).message,null,"warning");
				}
			}else{
				/**
				 * 添加TAB页面（ class="addTabPage" ）
				 * @param $this 		点击的对象
				 * @param title 		提示标题
				 * @param url	 		访问的路径
				 * @param closeable	 	是否有关闭按钮，关闭页面回调方法：function onTablePageClose(tabId, title){}
				 * @param refresh 		打开后是否刷新重新加载
				 */
				js.addTabPage($("#edit"), "修改人员基本信息", "/js/a/base/bdPsndoc/form?pkPsndoc="+rowId, null, null);//弹出新的标签页
			}
		}
	});
}
/**删除**/
function delBtn(){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行
	if(rowId==null){
		js.showMessage("请选择一条数据删除！",null,'warning');
		return;
	}
	var JSONDATA={};
	JSONDATA['pkPsndoc']=rowId;
	$.ajax({
		url: '/js/a/base/bdPsndoc/delete',
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			if(msg!=null){
				var tag=JSON.parse(msg).result;
				if(tag=="true"){
					js.showMessage(JSON.parse(msg).message);
					$('#dataGrid').dataGrid('reloadGrid');//刷新当前列表数据区域
				}else if(tag=="false"){
					js.showMessage(JSON.parse(msg).message,null,"warning");
				}
			}
		}
	});
}
/**启用**/
function enableBtn(){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行
	if(rowId==null){
		js.showMessage("请选择一条数据启用！",null,'warning');
		return;
	}
	layer.confirm("是否确认启用？", {icon: 3, title:'信息'}, function(){
		var rowData = $('#dataGrid').dataGrid('getRowData',rowId); // 获取选择行数据
		if(rowData.status.includes("正常")){
			js.showMessage("正常数据无需启用！",null,'warning');
			return;
		}
		var JSONDATA={};
		JSONDATA['pkPsndoc']=rowId;
		$.ajax({
			url: '/js/a/base/bdPsndoc/enable',
			datatype: 'json',
			data: JSONDATA,
			contentType: 'application/json;charset=utf-8',
			success: function (msg) {
				if(msg!=null){
					var tag=JSON.parse(msg).result;
					if(tag=="true"){
						js.showMessage(JSON.parse(msg).message);
						$('#dataGrid').dataGrid('reloadGrid');//刷新当前列表数据区域
					}else if(tag=="false"){
						js.showMessage(JSON.parse(msg).message,null,"warning");
					}
				}
			}
		});
	});
}
/**停用**/
function stopBtn(){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行
	if(rowId==null){
		js.showMessage("请选择一条数据停用！",null,'warning');
		return;
	}
	layer.confirm("是否确认停用？", {icon: 3, title:'信息'}, function(){
		var rowData = $('#dataGrid').dataGrid('getRowData',rowId); // 获取选择行数据
		if(rowData.status.includes("停用")){
			js.showMessage("数据已停用！",null,'warning');
			return;
		}
		var JSONDATA={};
		JSONDATA['pkPsndoc']=rowId;
		$.ajax({
			url: '/js/a/base/bdPsndoc/disable',
			datatype: 'json',
			data: JSONDATA,
			contentType: 'application/json;charset=utf-8',
			success: function (msg) {
				if(msg!=null){
					var tag=JSON.parse(msg).result;
					if(tag=="true"){
						js.showMessage(JSON.parse(msg).message);
						$('#dataGrid').dataGrid('reloadGrid');//刷新当前列表数据区域
					}else if(tag=="false"){
						js.showMessage(JSON.parse(msg).message,null,"warning");
					}
				}
			}
		});
	});
}
