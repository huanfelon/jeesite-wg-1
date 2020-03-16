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
	JSONDATA['pkContract']=rowIds[0];
	$.ajax({
		url: '/js/a/wght/wgContract/edit',
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
				js.addTabPage($(this), "修改合同管理", "/js/a/wght/wgContract/form?pkContract="+rowIds[0]+"&isEdit=true", null, null);//弹出新的标签页
			}
		}
	});
});
//计划金额
function btnPlanMny(){
	var rowIds = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowIds==null||rowIds.length!=1){
		js.showMessage("请选择一条数据查看！",'','info',5000);
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowIds[0]); // 获取选择行数据
	if(!rowData.vbillstatus.includes("审批通过")){
		js.showMessage("单据未审批，无数据！",'','info',5000);
		return;
	}
	//弹出查看界面
	js.layer.open({
    	id:"planMny",
    	type: 2, //此处以iframe举例
    	anim: 1,
    	title: "合同计划金额明细",
    	area: ['1100px', "564px"],
    	shade: 0.2,
    	shadeClose: false,
    	content: '/js/a/wght/wgContract/planMny?pk='+rowIds[0],
    	btn: ['确认'],
    	zIndex: layer.zIndex,
    });
}
//计划金额
function btnSrMny(){
	var rowIds = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowIds==null||rowIds.length!=1){
		js.showMessage("请选择一条数据查看！",'','info',5000);
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowIds[0]); // 获取选择行数据
	if(!rowData.vbillstatus.includes("审批通过")){
		js.showMessage("单据未审批，无数据！",'','info',5000);
		return;
	}
	//弹出查看界面
	js.layer.open({
    	id:"srMny",
    	type: 2, //此处以iframe举例
    	anim: 1,
    	title: "合同收入金额明细",
    	area: ['1100px', "564px"],
    	shade: 0.2,
    	shadeClose: false,
    	content: '/js/a/wght/wgContract/srMny?pk='+rowIds[0],
    	btn: ['确认'],
    	zIndex: layer.zIndex,
    });
}
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
//打印功能
$('#btnPrint2').click(function(){
	//获取行数组id
	var selectRows = $('#dataGrid').dataGrid('getSelectRow');
	if(selectRows==null||selectRows==""){
		js.showMessage('请选择一条数据！','','info',5000);
		return ;
	}
	var dataselect=$('#dataGrid').getRowData(selectRows);//获取行数据
	if(dataselect.vbillstatus!="1"&&!dataselect.vbillstatus.includes("审批通过")){
		js.showMessage('合同未审批不可打印！','','info',5000);
		return ;
	}
    var id=selectRows;
	js.layer.open({
		type: 1,
		area: '400px',
		title: '合同模板类型',
		resize: false,
		closeBtn: 0,
		shadeClose: false,
		scrollbar: false,
		content: js.template('printTempletSelectTpl'),
		success: function(layero, index){
			
		},
		btn: ['<i class="fa fa-check"></i> 确认',
			'<i class="fa fa-remove"></i> 取消'],
		yes: function(index, layero){
			var templet=$(layero).find("#httemplet").val();//获取模板名称
			if(templet==""){
				js.showMessage('请选择一个模板类型！','','info',5000);
				return false;
			}
			js.layer.open({//展开layer弹出框
				  type: 2,
				  shade: 0,
				  title: "合同条款打印",
				  area: ['900px', '400px'],
				 // btn: ['确定','关闭'],
				  content: ["./wght/wgContract/print?pkContract="+id+"&templetname="+templet, "yes"],//加载信息并过滤
				  yes : function(index, layero){},
				  btn2 : function(){}
			});
		},
		btn2: function(index, layero){
			layer.close(index);
		}
	});
});

/** 续约 **/
function btnResetHt(){
	var rowIds = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowIds==null||rowIds.length!=1){
		js.showMessage("请选择一条数据续约！",'','info',5000);
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowIds[0]); // 获取选择行数据
	if(!rowData.vbillstatus.includes("审批通过")){
		js.showMessage("单据未审批，不可续约！",'','info',5000);
		return;
	}
	if(rowData.htstatus.includes("退租")){
		js.showMessage("合同已经退租，不可续约！",'','info',5000);
		return;
	}
	var JSONDATA={};
	JSONDATA['pkContract']=rowIds[0];
	$.ajax({
		url: '/js/a/wght/wgContract/resetHt',
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
				js.addTabPage($(this), "修改合同管理", "/js/a/wght/wgContract/formXy?pkContract="+rowIds[0], null, null);//弹出新的标签页
			}
		}
	});
}

/** 税率变更 **/
function btnTaxChange(){
	var rowIds = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowIds==null||rowIds.length!=1){
		js.showMessage("请选择一条数据变更税率！",'','info',5000);
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowIds[0]); // 获取选择行数据
	if(!rowData.vbillstatus.includes("审批通过")){
		js.showMessage("单据未审批，不可变更！",'','info',5000);
		return;
	}
	if(rowData.htstatus.includes("退租")){
		js.showMessage("合同已经退租，不可变更！",'','info',5000);
		return;
	}
	js.layer.open({
		type: 1,
		area: '600px',
		title: '税率变更',
		resize: false,
		closeBtn: 0,
		shadeClose: false,
		scrollbar: false,
		content: js.template('htTaxChangeTpl'),
		success: function(layero, index){
			$(layero).find("#taxrate").change(function (){
				var data = $(this).val();
				var pretax=$(layero).find("#tax").val();
				if(data==pretax){
					$(layero).find("#taxrate").val(null);
					js.showMessage('税率不可与上次变更税率('+pretax+'%)相同，请重新选择！','','info',5000);
					return false;
				}
			});
			
			var JSONDATA={};
			JSONDATA['pkContract']=rowIds[0];
			$.ajax({
				url: '/js/a/wght/wgContract/getMaxTaxChangeDate',
				datatype: 'json',
				data: JSONDATA,
				contentType: 'application/json;charset=utf-8',
				success: function (msg) {
					$(layero).find("#dmin").val(msg.dmin);
					$(layero).find("#dmax").val(msg.dmax);
					$(layero).find("#tax").val(msg.tax);
				}
			});
		},
		btn: ['<i class="fa fa-save"></i> 保存',
			'<i class="fa fa-remove"></i> 取消'],
		yes: function(index, layero){
			var dstart=$(layero).find("#dstart").val();//获取变更开始日
			var taxrate=$(layero).find("#taxrate").val();//获取税率
			if(dstart==""){
				js.showMessage('请选择变更开始日期！','','info',5000);
				return false;
			}
			if(taxrate==""){
				js.showMessage('请选择税率！','','info',5000);
				return false;
			}
			var JSONDATA={};
			JSONDATA['pkContract']=rowIds[0];
			JSONDATA['dstart']=dstart;
			JSONDATA['taxrate']=taxrate;
			$.ajax({
				url: '/js/a/wght/wgContract/saveTaxChange',
				datatype: 'json',
				data: JSONDATA,
				contentType: 'application/json;charset=utf-8',
				success: function (msg) {
					if(msg!=null&&msg!=""){
						var tag=JSON.parse(msg).result;
						if(tag=="true"){
							js.showMessage(JSON.parse(msg).message);
							$('#dataGrid').dataGrid('refresh');
						}else if(tag=="false"){
							js.showMessage(JSON.parse(msg).message,'','info',5000);
							return false;
						}
					}
				}
			});
		},
		btn2: function(index, layero){
			layer.close(index);
		}
	});
}
/** 税率变更记录 **/
function btnTaxRecord(){
	var rowIds = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowIds==null||rowIds.length!=1){
		js.showMessage("请选择一条数据查看变更记录！",'','info',5000);
		return;
	}
	var rowData = $('#dataGrid').dataGrid('getRowData',rowIds[0]); // 获取选择行数据
	if(!rowData.vbillstatus.includes("审批通过")){
		js.showMessage("单据未审批，无变更记录！",'','info',5000);
		return;
	}
	//弹出查看界面
	js.layer.open({
    	id:"taxRecord",
    	type: 2, //此处以iframe举例
    	anim: 1,
    	title: "税率变更记录",
    	area: ['700px', $(top.window).height()*2/5 + "px"],
    	shade: 0.2,
    	shadeClose: false,
    	content: "/js/a/wght/wgContract/taxRecord?pkContract="+rowIds[0],
    	btn: ['确认'],
    	zIndex: layer.zIndex,
    });
}
