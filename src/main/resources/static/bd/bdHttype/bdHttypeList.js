$(document).ready(function() {
	InitQuery();
	$('#dataGrid').dataGrid('refresh');
})
//初始化查询条件
function InitQuery(){
	$(".form-group input").val("");
}

/***查询***/
function queryData(){
		$("#btnSearch").click();
		$('#dataGrid').dataGrid('refresh');
}

/**重置**/
function resetBtn(){
	InitQuery();
}
function bianji33(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("bianji","form");
     var newr = url.replace("bianji","xgform");
	var JSONDATA={'pkHttype':pk,'isNewRecord':flag};
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
