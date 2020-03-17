var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	InitQuery();
	$('#dataGrid').dataGrid('refresh');
	
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

/** 参照合同管理 **/
$("#btnRefHt").click(function (){
	//多窗口模式，层叠置顶
	var width = $(top.window).width()*0.8;
	var height = $(top.window).height()*0.9;
    js.layer.open({
    	id:'refHt',
    	type: 2, //此处以iframe举例
    	title: '参照合同管理',
    	area: [width+'px', height+'px'],
    	shade: 0.2,
    	maxmin: true,
    	content: '/js/a/wght/wgContract/tzReflist',
    	btn: ['确认', '取消'],
    	yes: function(index, layero){
    		$('#dataGrid').dataGrid('reloadGrid');//刷新当前列表数据区域
    		var data = $(layero).find("iframe").contents().find("#dataGrid");//获取弹出框内dataGrid
    		var rowId = data.dataGrid('getSelectRow');
    		if(rowId==null||rowId==""){
    			js.showMessage('请选择一条数据！','','info',3000);
    			return false;
    		}
    		js.addTabPage($(this), "新增退租管理", "/js/a/wght/wgThrowalease/tzRefToAdd?pk="+rowId, null, null);//弹出新的标签页
    	},
    	btn2: function(index, layero){
    		layer.close(index);
    	},
    	zIndex: layer.zIndex,
    });
});
/**修改**/
$("#btnEdit").click(function (){
	var rowIds = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowIds==null||rowIds.length!=1){
		js.showMessage("请选择一条数据修改！",'','info',5000);
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowIds[0]); // 获取选择行数据
	if(!rowData.vbillstatus.includes("自由")){
		js.showMessage("单据状态不是自由态，不可修改！",'','info',5000);
		return;
	}
	var JSONDATA={};
	JSONDATA['pkThrowalease']=rowIds[0];
	$.ajax({
		url: '/js/a/wght/wgThrowalease/edit',
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			if(msg!=null&&msg!=""){
				var tag=JSON.parse(msg).result;
				if(tag=="true"){
					js.showMessage(JSON.parse(msg).message);
				}else if(tag=="false"){
					js.showMessage(JSON.parse(msg).message,'','info',5000);
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
				js.addTabPage($(this), "修改合同管理", "/js/a/wght/wgThrowalease/form?pkThrowalease="+rowIds[0]+"&isEdit=true", null, null);//弹出新的标签页
			}
		}
	});
});
//审批功能
$('#btnApproveAll2').click(function(){
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0/*||selectRows.length>1*/){
		js.showMessage('请选择数据！','','info',5000);
		return ;
	}
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		if(dataselect.vbillstatus=="1"||dataselect.vbillstatus.includes("审批通过")){
			js.showMessage('单据已审批，不可再次审批！','','info',5000);
			return ;
		}
		selectData.push(dataselect.id);
	}
	
	$.ajax({
		type : "post",
		url : "./approveAll",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSON.stringify(selectData),
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else if(data.result == Global.FALSE){
				js.showMessage(data.message,'','info',5000);
			}
		},
	    error : function (data) {
	    	alert("请求失败！");
	    }
	});
});

//取消审批功能
$('#btnUnApproveAll2').click(function(){
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0/*||selectRows.length>1*/){
		js.showMessage('请选择数据！','','info',5000);
		return ;
	}
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		if(dataselect.vbillstatus=="0"||dataselect.vbillstatus==""||dataselect.vbillstatus.includes("自由")){
			js.showMessage('自由态单据，不可再次取消审批！','','info',5000);
			return ;
		}
		selectData.push(dataselect.id);
	}
	
	$.ajax({
		type : "post",
		url : "./unapproveAll",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSON.stringify(selectData),
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else if(data.result == Global.FALSE){
				js.showMessage(data.message,'','info',5000);
			}
		},
	    error : function (data) {
	    	alert("请求失败！");
	    }
	});
});
