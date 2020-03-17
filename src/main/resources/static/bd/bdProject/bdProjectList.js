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
$("#btnRefreshTree").click(function(){
	InitQuery();
})
function bianji33(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("bianji","form");
     var newr = url.replace("bianji","xgform");
	var JSONDATA={'pkProject':pk,'isNewRecord':flag};
	$.ajax({
		url: newurl,
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
			}else{
				if(msg=="isXj"){
					js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}
function bianji34(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("bianji","form1");
     var newr = url.replace("bianji","xgform1");
	var JSONDATA={'pkProject':pk,'isNewRecord':flag};
	$.ajax({
		url: newurl,
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
			}else{
				if(msg=="isXj"){
					js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}
/**修改**/
function c_editBtn1(url){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据修改！",null,'warning');
		return;
	}else{
		var data=$('#dataGrid').dataGrid('getRowData', rowId);
		if(data['pkOrg.officeName']=="万谷集团"){
			js.showMessage("集团数据不可修改！",null,'warning');
			return;
		}
	}
	// /js/a/bd/bdHttype/bianji?pkHttype
	var url_list = url.split("?");
	var JSONDATA={};
	JSONDATA[url_list[1]]=rowId;
	JSONDATA['isEdit']=true;
	var url_list1=url+"="+rowId+"&isEdit=true"
    var newre = url_list1.replace("bianji","form");
    var newr = url_list1.replace("bianji","xgform");
	$.ajax({
		url: url_list[0],
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
    	var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
			}else{
				if(msg=="isXj"){
					js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}
//无多选框 单个删除
function c_delete1(name){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据删除！",null,'warning');
		return;
	}else{
		var data=$('#dataGrid').dataGrid('getRowData', rowId);
		if(data['pkOrg.officeName']=="万谷集团"){
			js.showMessage("集团数据不可删除！",null,'warning');
			return;
		}
	}
//	var rowData = $('#dataGrid').dataGrid('getRowData',rowId); // 获取选择行数据
//	if(rowData.status.includes("停用")){
//		js.showMessage("已经停用数据不可删除！",null,'warning');
//		return;
//	}
	var JSONDATA={};
	JSONDATA[name]=rowId;
	$.ajax({
		//type : "post",
		url : "./delete",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSONDATA,
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else{
				js.showMessage(data.message);
			}
		},
	    error : function (data) {
	    	js.showMessage(data.responseJSON.message,'','error',3000);
	    }
	});
}
/**集团修改**/
function c_editBtn12(url){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据修改！",null,'warning');
		return;
	}
	// /js/a/bd/bdHttype/bianji?pkHttype
	var url_list = url.split("?");
	var JSONDATA={};
	JSONDATA[url_list[1]]=rowId;
	JSONDATA['isEdit']=true;
	var url_list1=url+"="+rowId+"&isEdit=true"
    var newre = url_list1.replace("bianji","form1");
    var newr = url_list1.replace("bianji","xgform1");
	$.ajax({
		url: url_list[0],
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
    	var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
			}else{
				if(msg=="isXj"){
					js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}
function xinzen33(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("xinzen","form");
	 var newr=newre.replace("pkProject","parentCode");
	var JSONDATA={'pk':pk,'isNewRecord':flag};
	$.ajax({
		url: newurl,
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
				return ;
			}else{
				if(msg=="isXz"){
					js.addTabPage(null, "新增下级", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}
