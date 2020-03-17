var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	var isnew=$("#isNewRecord").val();
	if(isnew=="true"){//新增
		$("#pkOrgCode").val(getMainOrgCode());
		$("#pkOrgName").val(getMainOrgName());
		changeOrgAfter();
	}else{
		$("#modifierCode").val(getLoginUserCode());
		$("#modifierName").val(getLoginUserName());
		$("#modifiedtime").val(getNowFormatDateTime());
	}
	
	//参照字段选择前过滤
	$(".input-group").on("mousedown",function showDeptTreeRef(){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
})

/**
 * 组织切换后
 */
function changeOrgAfter(){
	$("#creatorCode").val(getLoginUserCode());
	$("#creatorName").val(getLoginUserName());
	$("#createdtime").val(getNowFormatDateTime());
}

/**
 * 重新定位参照URL地址
 * @param id
 */
function resetDataUrl(id){
	if(id=="pkCarDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl='/js/a/dfbase/bdCar/carSelect';
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id=="yjrDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl='/js/a/dfbase/bdDriver/driverSelect';
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
			if(oldRefCode==""||oldRefCode==newpro){
				return;
			}
		}
		layer.confirm("改变组织将会清空后续数据，是否继续？", {icon: 3, title:'信息'}, function(){
			var inputs=document.getElementsByTagName('input');
			for(var i = 2; i < inputs.length; i++) {
				var inputid=inputs[i].id;
				if(inputid.startsWith("pkOrg")||inputid.startsWith("vbillno")){
					continue;
				}
				inputs[i].value = ""; // 将每一个input的value置为空
			}
			changeOrgAfter();
		}, function(){
			$("#pkOrgCode").val(oldRefCode);
			$("#pkOrgName").val(oldRefName);
		});
	}
}

function listselectCallback(id, act, index, layero,selectData,nodes){
	if(id=='pkCar' && (act == 'ok' || act == 'clear')){//车牌号选择
		var newvue=$("#pkCarCode").val();
		if(oldRefCode==newvue){
			return;
		}
		if(selectData!=null&&selectData!=undefined&&selectData[newvue]!=undefined){
			var JSONDATA2={};
			JSONDATA2['pkCar']=newvue;
			$.ajax({
				url: '/js/a/dfbase/bdCar/selectCarByPk',
				datatype: 'json',
				data: JSONDATA2,
				contentType: 'application/json;charset=utf-8',
				success: function (data) {
					var dlist=data.bdCar.bdCarDriverList;
					var dcode="",dname="";
					if(dlist!=null&&dlist.length>0){
						for(var i=0;i<dlist.length;i++){
							if(dlist[i].isMr=="1"){
								dcode=dlist[i].pkDriver;
								dname=dlist[i].name;
								break;
							}
						}
						if(dcode==""){
							$("#yjrCode").val(null);
							$("#yjrName").val(null);
						}else{
							$("#yjrCode").val(dcode);
							$("#yjrName").val(dname);
						}
					}else{
						$("#yjrCode").val(null);
						$("#yjrName").val(null);
					}
				},
				error: function(e){
					console.log(e);
				}
			});
			if(selectData[newvue].pkDept!=undefined){
				$("#pkDeptCode").val(selectData[newvue].pkDept.pkDept);
				$("#pkDeptName").val(selectData[newvue].pkDept.deptName);
			}else{
				$("#pkDeptCode").val(null);
				$("#pkDeptName").val(null);
			}
		}else{//清空
			$("#yjrCode").val(null);
			$("#yjrName").val(null);
			$("#pkDeptCode").val(null);
			$("#pkDeptName").val(null);
		}
	}
}
