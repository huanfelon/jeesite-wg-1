var oldRefCode,oldRefName;//存放参照选择之前的值
var ishouseadd=false;//是否房产页签增行
$(document).ready(function() {
	var isnew=$("#isNewRecord").val();
	if(isnew=="true"){//新增
		$("#pkOrgCode").val(getMainOrgCode());
		$("#pkOrgName").val(getMainOrgName());
		$("#dqzdate").val(getNowFormatDate());
		//$("#wgContractCustDataGridAddRowBtn").click();
		$("#wgContractZltypeDataGridAddRowBtn").click();
		changeOrgAfter();
	}else{
		$("#modifierCode").val(getLoginUserCode());
		$("#modifierName").val(getLoginUserName());
		$("#modifiedtime").val(getNowFormatDateTime());
	}
	//租赁支付方式第一行不可删除
	var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids.length>0){
		$("#"+zltids[0]+"_dstartdate").val($("#dstartdate").val());
		$("#"+zltids[0]+"_dstartdate").attr("disabled","disabled");
		$("#"+zltids[0]+"_del").addClass("hide");
	}
	//物业支付方式第一行不可删除
	var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids.length>0){
		$("#"+wytids[0]+"_dstartdate").attr("disabled","disabled");
		$("#"+wytids[0]+"_del").addClass("hide");
	}
	
	form_jine();//增加千分位符
	//参照字段选择前过滤
	$(".box-main").on("mousedown", ".input-group",function (){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
	
	//租赁方式切换
	$("#renttype").change(function(){
		var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
		if(hids.length>0){
			for(var i=0;i<hids.length;i++){
				//重算租金
				var rowprice=toNumber($("#"+hids[i]+"_nprice").val());
				var rowarea=toNumber($("#"+hids[i]+"_narea").val());
				var rowtzmonth=toNumber($("#"+hids[i]+"_nmonthtzprice").val());
				var rowtzyear=toNumber($("#"+hids[i]+"_nyeartzprice").val());
				renScalRow(hids[i],rowprice,rowarea,rowtzmonth,rowtzyear);
			}
			rentScalHead();
			form_jine();//增加千分位符
		}
	});
	
	//表体增行方法绑定
	$("#jqgh_wgContractHouseDataGrid_rn a").click(addRowByHouse);
	$("#jqgh_wgContractZqfyDataGrid_rn a").click(addRowByZqfy);
	$("#jqgh_wgContractYjDataGrid_rn a").click(addRowByYj);
	$("#jqgh_wgContractZltypeDataGrid_rn a").click(addRowByZltype);
	$("#jqgh_wgContractWytypeDataGrid_rn a").click(addRowByWytype);
	
	//房产页签是否物业选择操作
	$("#wgContractHouseDataGrid").on("change", ".checkbox_isyes", function (){
		if(ishouseadd){
			ishouseadd=false;
			return;
		}
		var hrowid=$(this).attr("rowid");
		var buildCode=$("#hBuild_"+hrowid+"_pkBuildCode").val();
		var buildName=$("#hBuild_"+hrowid+"_pkBuildName").val();
		var houseCode=$("#hHouse_"+hrowid+"_pkHouseCode").val();
		var houseName=$("#hHouse_"+hrowid+"_pkHouseName").val();
		if(houseCode==""){
			js.showMessage("请先选择楼栋、房产名称！",'','info',3000);
			ishouseadd=true;
			$(this).select2('val','N');
			return;
		}
		var tag=$(this).select2("val");
		if(tag=="Y"){
			//传数据到物业信息
			var wyids=$('#wgContractWyfDataGrid').dataGrid('getDataIDs');
			if(wyids.length>0){
				for(var i=0;i<wyids.length;i++){
					var wyhouse=$("#wyHouse_"+wyids[i]+"_pkHouseCode").val();
					if(houseCode==wyhouse){
						return;
					}
				}
			}
			$('#wgContractWyfDataGridAddRowBtn').click();//增行
			var wyids2=$('#wgContractWyfDataGrid').dataGrid('getDataIDs');
			var wynewrow=wyids2[wyids2.length-1];
			$("#wyBuild_"+wynewrow+"_pkBuildCode").val(buildCode);
			$("#wyBuild_"+wynewrow+"_pkBuildName").val(buildName);
			$("#wyHouse_"+wynewrow+"_pkHouseCode").val(houseCode);
			$("#wyHouse_"+wynewrow+"_pkHouseName").val(houseName);
			$("#"+wynewrow+"_dstartdate").val($("#dstartdate").val());
			$("#"+wynewrow+"_ntaxrate").select2("val",[$("#"+wyids2[0]+"_ntaxrate").select2("val")]);
			$("#"+wynewrow+"_vdef1").val($("#"+hrowid+"_narea").val());
			var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
			if(wytids.length==0){//有物业数据，物业支付方式必须有一行
				$('#wgContractWytypeDataGridAddRowBtn').click();
				//物业支付方式第一行不可删除
				var wytids2=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
				$("#"+wytids2[0]+"_dstartdate").val($("#dstartdate").val());
				$("#"+wytids2[0]+"_dstartdate").attr("disabled","disabled");
				$("#"+wytids2[0]+"_del").addClass("hide");
			}
		}else{
			delByHouseChange(houseCode);
		}
	});
	//时间改变前存值
	$(".box-main").on("mousedown", "#dstartdate,#denddate,.datachangeafter1",function (){
		oldRefCode=$(this).val();
		oldRefName=$(this).attr("id");
	});
	//物业信息税率变更
	$("#wgContractWyfDataGrid").on("select2:select", ".wytaxchange",function (){
		var selectrow=$("#wgContractWyfDataGrid").dataGrid('getSelectRow');
		var thisval=$("#"+selectrow+"_ntaxrate").select2("val");
		var wyids=$("#wgContractWyfDataGrid").dataGrid('getDataIDs');
		for(var i=0;i<wyids.length;i++){
			if(selectrow==wyids[i]){
				continue;
			}
			$("#"+wyids[i]+"_ntaxrate").select2("val",[thisval]);//塞值要用[]括起来，多个直接放数组，否则长度不是一的值不生效
		}
	});
	
	
})

/**
 * 组织切换后
 */
function changeOrgAfter(){
	$("#dqzdate").val(getNowFormatDate());
	$("#creatorCode").val(getLoginUserCode());
	$("#creatorName").val(getLoginUserName());
	$("#createdtime").val(getNowFormatDateTime());
}

/**
 * 重新定位参照URL地址
 * @param id
 */
function resetDataUrl(id){
	if(id=="pkProjectDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/zl/zlProject/treeData?isShowCode=true";
		if(pkorg==""){
			newurl+="&pkOrg=' '";
		}else{
			newurl+="&pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id=="pkDeptDiv"){
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/base/bdDept/treeData";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("cCust")){//客户页签客户名称
		var pkpro=$("#pkProjectCode").val();
		var newurl="/js/a/base/bdCustomer/bdCustomerSelect";
		if(pkpro==""){
			newurl+="?pk_project=' '";
		}else{
			newurl+="?pk_project="+pkpro;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("hBuild")){//房产页签楼栋
		var pkpro=$("#pkProjectCode").val();
		var newurl="/js/a/zl/zlBuildingfile/buildingSelect";
		if(pkpro==""){
			newurl+="?pkProjectid=' '";
		}else{
			newurl+="?pkProjectid="+pkpro;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("hHouse")){//房产页签房产名称，已经被选择的不显示
		var rowid=id.split("_")[1];
		var pkbuild=$("#hBuild_"+rowid+"_pkBuildCode").val();
		var pkContract=$("#pkContract").val();
		var pkh="";
		var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
		if(hids.length>0){
			for(var i=0;i<hids.length;i++){
				if(rowid==hids[i]){
					continue;
				}
				pkh+=$("#hHouse_"+hids[i]+"_pkHouseCode").val()+",";
			}
		}
		if(pkContract==""){
			pkContract="null";
		}
		if(pkh==""){
			pkh="null";
		}
		var newurl="/js/a/zl/zlHousesource/housesourceSelect";
		if(pkbuild==""){
			newurl+="?buildname=' '";
		}else{
			newurl+="?buildname="+pkbuild+"&vdef1="+pkContract+"_"+pkh;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("wyFeescale")){//物业信息页签收费标准
		var rowid=id.split("_")[1];
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/bd/bdFeescale/feescaleSelect";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("zqYspro")){//其他周期费用页签预算项目
		var rowid=id.split("_")[1];
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/bd/bdProject/treeData";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("zqFeescale")){//其他周期费用页签收费标准
		var rowid=id.split("_")[1];
		var pkcostp=$("#zqYspro_"+rowid+"_pkYsprojectCode").val();
		var newurl="/js/a/bd/bdFeescale/feescaleSelect";
		if(pkcostp==""){
			newurl+="?pkProject=' '";
		}else{
			newurl+="?pkProject="+pkcostp;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("yjYspro")){//其他周期费用页签预算项目
		var rowid=id.split("_")[1];
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/bd/bdProject/treeData";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("zltYspro")){//租赁付款方式页签预算项目
		var rowid=id.split("_")[1];
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/bd/bdProject/treeData";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	if(id.startsWith("wytYspro")){//物业付款方式页签预算项目
		var rowid=id.split("_")[1];
		var pkorg=$("#pkOrgCode").val();
		var newurl="/js/a/bd/bdProject/treeData";
		if(pkorg==""){
			newurl+="?pkOrg=' '";
		}else{
			newurl+="?pkOrg="+pkorg;
		}
		$("#"+id).attr("data-url",newurl);
	}
}

$("#ly_btnSubmit").click(function (){
	if($(".has-error").length>0){
		js.alert("有必输项未填，请检查！",'','info',3000);
		return ;
	}else{
		var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
		if(zltids.length>0){
			var row=0;
			for(var i=0;i<zltids.length;i++){
				row++;
				var dyear=$("#"+zltids[i]+"_payyear").val();
				var dmonth=$("#"+zltids[i]+"_paymonth").val();
				var dday=$("#"+zltids[i]+"_payday").val();
				if(dyear==0&&dmonth==0&&dday==0){
					js.alert("[租赁支付方式页签]第"+row+"行付款方式年、月、日不可都为空！",'','info',3000);
					return ;
				}
			}
		}
		var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
		if(wytids.length>0){
			var row=0;
			for(var i=0;i<wytids.length;i++){
				row++;
				var dyear=$("#"+wytids[i]+"_payyear").val();
				var dmonth=$("#"+wytids[i]+"_paymonth").val();
				var dday=$("#"+wytids[i]+"_payday").val();
				if(dyear==0&&dmonth==0&&dday==0){
					js.alert("[物业支付方式页签]第"+row+"行付款方式年、月、日不可都为空！",'','info',3000);
					return ;
				}
			}
		}
		var zzids=$('#wgContractZzqDataGrid').dataGrid('getDataIDs');
		if(zzids.length>0){
			for(var i=0;i<zzids.length;i++){
				var zzrate=$("#"+zzids[i]+"_zzrate").val();
				var zzmny=$("#"+zzids[i]+"_nzzmny").val();
				if(zzrate==""&&zzmny==""){
					js.alert("增长比例、增长金额必须填写一个，请检查！",'','info',3000);
					return ;
				}
			}
		}
	}
	form_cleanup_jine();//去掉千分位
	$("#btnSubmit").click();
});

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
		layer.confirm("改变组织将会清空后续数据，是否继续？", {shadeClose:false,closeBtn:0,icon: 3, title:'提示'}, function(){
			var inputs=document.getElementsByTagName('input');
			for(var i = 2; i < inputs.length; i++) {
				var inputid=inputs[i].id;
				if(inputid.startsWith("pkOrg")||inputid.startsWith("vbillno")){
					continue;
				}
				inputs[i].value = ""; // 将每一个input的value置为空
			}
			changeOrgAfter();
			delAllBodyRows("org");
		});
	}
	if(id=='pkProject' && (act == 'ok' || act == 'clear')){//组织选择
		if(nodes!=null&&nodes!=undefined){
			var newpro=$("#pkProjectCode").val();
			if(oldRefCode==""||oldRefCode==newpro){
				return;
			}
		}
		layer.confirm("改变项目将会清空表体数据，是否继续？", {shadeClose:false,closeBtn:0,icon: 3, title:'提示'}, function(){
			delAllBodyRows("project");
		}, function(){$("#pkProjectCode").val(oldRefCode);$("#pkProjectName").val(oldRefName);});
	}
	if(id.startsWith('zqYspro') && (act == 'ok' || act == 'clear')){//其他周期费用页签预算项目选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			var zqids=$('#wgContractZqfyDataGrid').dataGrid('getDataIDs');
			if(zqids.length>1){
				for(var i=0;i<zqids.length;i++){
					if(rowid==zqids[i]){
						continue;
					}
					var rowyspro=$("#zqYspro_"+zqids[i]+"_pkYsprojectCode").val();
					if(rowyspro==newvue){
						js.showMessage("该预算项目已被选择，请重新选择！",'','info',3000);
						$("#zqYspro_"+rowid+"_pkYsprojectCode").val(oldRefCode);
						$("#zqYspro_"+rowid+"_pkYsprojectName").val(oldRefName);
						return;
					}
				}
			}
			$("#zqFeescale_"+rowid+"_pkFeescaleCode").val(null);
			$("#zqFeescale_"+rowid+"_pkFeescaleName").val(null);
			$("#"+rowid+"_vdef1").val(null);
			$("#"+rowid+"_vdef2").val(null);
			$("#"+rowid+"_nfeemny").val(null);
		}else{//清空
			$("#zqFeescale_"+rowid+"_pkFeescaleCode").val(null);
			$("#zqFeescale_"+rowid+"_pkFeescaleName").val(null);
			$("#"+rowid+"_vdef1").val(null);
			$("#"+rowid+"_vdef2").val(null);
			$("#"+rowid+"_nfeemny").val(null);
		}
	}
	if(id.startsWith('yjYspro') && (act == 'ok' || act == 'clear')){//押金页签预算项目选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			var yjids=$('#wgContractYjDataGrid').dataGrid('getDataIDs');
			if(yjids.length>1){
				for(var i=0;i<yjids.length;i++){
					if(rowid==yjids[i]){
						continue;
					}
					var rowyspro=$("#yjYspro_"+yjids[i]+"_pkYsprojectCode").val();
					if(rowyspro==newvue){
						js.showMessage("该预算项目已被选择，请重新选择！",'','info',3000);
						$("#yjYspro_"+rowid+"_pkYsprojectCode").val(oldRefCode);
						$("#yjYspro_"+rowid+"_pkYsprojectName").val(oldRefName);
						return;
					}
				}
			}
		}
	}
	if(id.startsWith('zltYspro') && (act == 'ok' || act == 'clear')){//租赁支付方式页签预算项目选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		var newname=$("#"+id+"Name").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
			if(zltids.length>1){
				for(var i=0;i<zltids.length;i++){
					if(rowid==zltids[i]){
						continue;
					}
					$("#zltYspro_"+zltids[i]+"_pkYsprojectCode").val(newvue);
					$("#zltYspro_"+zltids[i]+"_pkYsprojectName").val(newname);
				}
			}
		}else{//清空
			var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
			if(zltids.length>1){
				for(var i=0;i<zltids.length;i++){
					if(rowid==zltids[i]){
						continue;
					}
					$("#zltYspro_"+zltids[i]+"_pkYsprojectCode").val(null);
					$("#zltYspro_"+zltids[i]+"_pkYsprojectName").val(null);
				}
			}
		}
	}
	if(id.startsWith('wytYspro') && (act == 'ok' || act == 'clear')){//物业支付方式页签预算项目选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		var newname=$("#"+id+"Name").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
			if(wytids.length>1){
				for(var i=0;i<wytids.length;i++){
					if(rowid==wytids[i]){
						continue;
					}
					$("#wytYspro_"+wytids[i]+"_pkYsprojectCode").val(newvue);
					$("#wytYspro_"+wytids[i]+"_pkYsprojectName").val(newname);
				}
			}
		}else{//清空
			var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
			if(wytids.length>1){
				for(var i=0;i<wytids.length;i++){
					if(rowid==wytids[i]){
						continue;
					}
					$("#wytYspro_"+wytids[i]+"_pkYsprojectCode").val(null);
					$("#wytYspro_"+wytids[i]+"_pkYsprojectName").val(null);
				}
			}
		}
	}
}

function listselectCallback(id, act, index, layero,selectData,nodes){
	if(id.startsWith('cCust') && (act == 'ok' || act == 'clear')){//客户页签客户选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			changeCust(newvue, $("#"+id+"Name").val());
			if(selectData[newvue].idno!=undefined){
				$("#"+rowid+"_idno").val(selectData[newvue].idno);
			}else{
				$("#"+rowid+"_idno").val(null);
			}
			if(selectData[newvue].creditcode!=undefined){
				$("#"+rowid+"_shtyxydm").val(selectData[newvue].creditcode);
			}else{
				$("#"+rowid+"_shtyxydm").val(null);
			}
			var JsonData={};
			JsonData["pkCustomer"]=newvue;
			$.ajax({
				url: '/js/a/wght/wgContract/custkp',
				datatype: 'json',
				data: JsonData,
				async: false,
				contentType: 'application/json;charset=utf-8',
				success: function (data) {
					var kpxx=data.bdCustomerKpxxList;
					if(kpxx.length>0){
						if(kpxx[0].taxno!=undefined){
							$("#"+rowid+"_taxnum").val(kpxx[0].taxno);
						}else{
							$("#"+rowid+"_taxnum").val(null);
						}
						if(kpxx[0].addr!=undefined){
							$("#"+rowid+"_address").val(kpxx[0].addr);
						}else{
							$("#"+rowid+"_address").val(null);
						}
						if(kpxx[0].phone!=undefined){
							$("#"+rowid+"_tel").val(kpxx[0].phone);
						}else{
							$("#"+rowid+"_tel").val(null);
						}
						if(kpxx[0].bankname!=undefined){
							$("#"+rowid+"_openbank").val(kpxx[0].bankname);
						}else{
							$("#"+rowid+"_openbank").val(null);
						}
						if(kpxx[0].accnum!=undefined){
							$("#"+rowid+"_accountnum").val(kpxx[0].accnum);
						}else{
							$("#"+rowid+"_accountnum").val(null);
						}
					}else{
						$("#"+rowid+"_taxnum").val(null);
						$("#"+rowid+"_address").val(null);
						$("#"+rowid+"_tel").val(null);
						$("#"+rowid+"_openbank").val(null);
						$("#"+rowid+"_accountnum").val(null);
					}
				},
				error: function(e){
					console.log(e);
				}
			});
		}else{//清空
			$("#"+rowid+"_idno").val(null);
			$("#"+rowid+"_shtyxydm").val(null);
			$("#"+rowid+"_taxnum").val(null);
			$("#"+rowid+"_address").val(null);
			$("#"+rowid+"_tel").val(null);
			$("#"+rowid+"_openbank").val(null);
			$("#"+rowid+"_accountnum").val(null);
			changeCust(null, null);
		}
	}
	if(id.startsWith('hHouse') && (act == 'ok' || act == 'clear')){//房产页签房产选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		var iswy=$("#"+rowid+"_iswy").select2("val");
		if(iswy=="Y"){
			js.showMessage("该房产已经生成物业信息，不可修改！",'','info',3000);
			$("#"+id+"Code").val(oldRefCode);
			$("#"+id+"Name").val(oldRefName);
			return;
		}
		if(newvue!=null&&newvue!=""){
			var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
			for(var i=0;i<hids.length;i++){
				if(rowid==hids[i]){
					continue;
				}
				var rowhouse=$("#hHouse_"+hids[i]+"_pkHouseCode").val();
				if(newvue==rowhouse){
					js.showMessage("该房产已被选用，请重新选择！",'','info',3000);
					$("#"+id+"Code").val(oldRefCode);
					$("#"+id+"Name").val(oldRefName);
					return;
				}
			}
			//过滤已经被其他合同选择过的房产
			var JsonData={};
			JsonData["pkHouse"]=newvue;
			JsonData["pkContract"]=$("#pkContract").val();
			$.ajax({
				url: '/js/a/wght/wgContract/filterHouse',
				datatype: 'json',
				data: JsonData,
				contentType: 'application/json;charset=utf-8',
				success: function (msg) {
					if(JSON.parse(msg).message!=undefined&&JSON.parse(msg).message!=""){
						js.showMessage(JSON.parse(msg).message,'','info',3000);
						$("#"+id+"Code").val(oldRefCode);
						$("#"+id+"Name").val(oldRefName);
						return;
					}
				},
				error: function(e){
					console.log(e);
				}
			});
			$("#"+rowid+"_narea").val(selectData[newvue].buildarea);
			
			//重算租金
			var rowprice=toNumber($("#"+rowid+"_nprice").val());
			var rowarea=toNumber(selectData[newvue].buildarea);
			var rowtzmonth=toNumber($("#"+rowid+"_nmonthtzprice").val());
			var rowtzyear=toNumber($("#"+rowid+"_nyeartzprice").val());
			renScalRow(rowid,rowprice,rowarea,rowtzmonth,rowtzyear);
		}else{
			$("#"+rowid+"_narea").val(null);
			$("#"+rowid+"_ndayprice").val(null);
			$("#"+rowid+"_nmonthprice").val(null);
			$("#"+rowid+"_nyearprice").val(null);
		}
		rentScalHeadArea();
		rentScalHead();
	}
	if(id.startsWith('wyFeescale') && (act == 'ok' || act == 'clear')){//物业信息页签收费标准选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			var jsfs=selectData[newvue].caMethod;//计算方式
			var jsse=toNumber(selectData[newvue].caAmount);//计算数额
			var area=toNumber($("#"+rowid+"_vdef1").val());//租赁面积
			var nfee=0.0;
			if(jsfs=="0"){//按面积计算
				nfee=jsse*area;
			}else if(jsfs=="1"){//固定金额
				nfee=jsse;
			}
			$("#"+rowid+"_nfeemny").val(nfee);
			form_jine();//增加千分位符
		}else{//清空
			$("#"+rowid+"_nfeemny").val(null);
		}
	}
	if(id.startsWith('zqFeescale') && (act == 'ok' || act == 'clear')){//其他周期费用页签收费标准选择
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		if(newvue!=null&&newvue!=""){
			var jsfs=selectData[newvue].caMethod;//计算方式
			var jsse=toNumber(selectData[newvue].caAmount);//计算数额
			var num=toNumber($("#"+rowid+"_num").val());//数量
			if(num==null||num==""){
				num=0;
			}
			var nfee=0.0;
			if(jsfs=="0"){//按面积计算
				nfee=jsse*num;
			}else if(jsfs=="1"){//固定金额
				nfee=jsse;
			}
			$("#"+rowid+"_vdef1").val(jsfs);
			$("#"+rowid+"_vdef2").val(jsse);
			$("#"+rowid+"_nfeemny").val(nfee);
			form_jine();//增加千分位符
		}else{//清空
			$("#"+rowid+"_vdef1").val(null);
			$("#"+rowid+"_vdef2").val(null);
			$("#"+rowid+"_nfeemny").val(null);
		}
	}
}

//改变合同开始日期
function pickDateAfter(){
	var dstart=new Date($("#dstartdate").val());
	//判断是否清空相应表体
	var tag1="",tag2="",tag3="",tag4="";
	var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids.length>1){
		var dfirstrow1=new Date($("#"+zltids[1]+"_dstartdate").val());
		if(dstart.getTime()>dfirstrow1.getTime()){
			tag1="[租赁支付方式]";
		}
	}
	var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids.length>1){
		var dfirstrow2=new Date($("#"+wytids[1]+"_dstartdate").val());
		if(dstart.getTime()>dfirstrow2.getTime()){
			tag2="[物业支付方式]";
		}
	}
	var mzids=$('#wgContractMzqDataGrid').dataGrid('getDataIDs');
	if(mzids.length>0){
		var dfirstrow3=new Date($("#"+mzids[0]+"_dstartdate").val());
		if(dstart.getTime()>dfirstrow3.getTime()){
			tag3="[免租期]";
		}
	}
	var zzids=$('#wgContractZzqDataGrid').dataGrid('getDataIDs');
	if(zzids.length>0){
		var dfirstrow4=new Date($("#"+zzids[0]+"_dstartdate").val());
		if(dstart.getTime()>dfirstrow4.getTime()){
			tag4="[增长期]";
		}
	}
	if(tag1!=""||tag2!=""||tag3!=""||tag4!=""){
		layer.confirm("改变合同起始日将会清空"+tag1+tag2+tag3+tag4+"页签的数据，是否确认继续？", {shadeClose:false,closeBtn:0,icon: 3, title:'提示'}, function(){
			delAllBodyRows2(tag1,tag2,tag3,tag4);
		}, function(){
			$("#dstartdate").val(oldRefCode);
		});
	}
	//更新房产信息页签金额
	var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
	if(hids.length>0){
		for(var i=0;i<hids.length;i++){
			var rowprice=toNumber($("#"+hids[i]+"_nprice").val());
			var rowarea=toNumber($("#"+hids[i]+"_narea").val());
			var rowtzmonth=toNumber($("#"+hids[i]+"_nmonthtzprice").val());
			var rowtzyear=toNumber($("#"+hids[i]+"_nyeartzprice").val());
			renScalRow(hids[i],rowprice,rowarea,rowtzmonth,rowtzyear);
		}
		rentScalHead();
	}
	//自动带出首期付款日期
	var dstart=$("#dstartdate").val();
	var dfk=$("#dfirstfkdate").val();
	if(dfk==""){
		$("#dfirstfkdate").val(getNextDate(dstart,-toNumber($("#days").val())));
	}
	//更新租赁支付方式、物业支付方式第一行时间
	var zltids2=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids2.length>0){
		$("#"+zltids2[0]+"_dstartdate").val(dstart);
	}
	var wytids2=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids2.length>0){
		$("#"+wytids2[0]+"_dstartdate").val(dstart);
	}
}
//改变合同结束日期
function pickDateAfter2(){
	var dend=new Date($("#denddate").val());
	//判断是否清空相应表体
	var tag1="",tag2="",tag3="",tag4="";
	var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids.length>1){
		var dlastrow1=new Date($("#"+zltids[zltids.length-1]+"_dstartdate").val());
		if(dend.getTime()<dlastrow1.getTime()){
			tag1="[租赁支付方式]";
		}
	}
	var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids.length>1){
		var dlastrow2=new Date($("#"+wytids[wytids.length-1]+"_dstartdate").val());
		if(dend.getTime()<dlastrow2.getTime()){
			tag2="[物业支付方式]";
		}
	}
	var mzids=$('#wgContractMzqDataGrid').dataGrid('getDataIDs');
	if(mzids.length>0){
		var dlastrow3=new Date($("#"+mzids[mzids.length-1]+"_denddate").val());
		if(dend.getTime()<dlastrow3.getTime()){
			tag3="[免租期]";
		}
	}
	var zzids=$('#wgContractZzqDataGrid').dataGrid('getDataIDs');
	if(zzids.length>0){
		var dlastrow4=new Date($("#"+zzids[zzids.length-1]+"_denddate").val());
		if(dend.getTime()<dlastrow4.getTime()){
			tag4="[增长期]";
		}
	}
	if(tag1!=""||tag2!=""||tag3!=""||tag4!=""){
		layer.confirm("改变合同终止日将会清空"+tag1+tag2+tag3+tag4+"页签的数据，是否确认继续？", {shadeClose:false,closeBtn:0,icon: 3, title:'提示'}, function(){
			delAllBodyRows2(tag1,tag2,tag3,tag4);
		}, function(index){
			$("#denddate").val(oldRefCode);
			layer.close(index);
		});
	}
}
//表体日期选择后事件
function pickBodyDateAfter1(gridName){
	var rowId=oldRefName.split("_")[0];
	var item=oldRefName.split("_")[1];
	var rows=$("#"+gridName).dataGrid('getDataIDs');
	if(gridName=="wgContractZltypeDataGrid"||gridName=="wgContractWytypeDataGrid"||gridName=="wgContractZzqDataGrid"){//租赁支付方式、业务支付方式页签、增长期页签
		var ddate=new Date($("#"+rowId+"_"+item).val());
		if(rows.length>1){//只有一行不校验
			for(var i=0;i<rows.length;i++){
				if(rows[i]==rowId){//行Id相等校验上下行时间关系
					if(i>0){//不是第一行，校验上一行
						if($("#"+rows[i-1]+"_"+item).val()!=""){
							var predate=new Date($("#"+rows[i-1]+"_"+item).val());
							if(predate>=ddate){//上一行比当前行日期大，提示
								$("#"+rowId+"_"+item).val(oldRefCode);
								js.showMessage("当前日期不可比上一行开始日期小！",'','info',3000);
								return;
							}
						}
					}
					if(i<rows.length-1){//不是最后一行，校验下一行
						if($("#"+rows[i+1]+"_"+item).val()!=""){
							var nextdate=new Date($("#"+rows[i+1]+"_"+item).val());
							if(nextdate<=ddate){//下一行比当前行日期小，提示
								$("#"+rowId+"_"+item).val(oldRefCode);
								js.showMessage("当前日期不可比下一行开始日期大！",'','info',3000);
								return;
							}
						}
					}
				}
			}
		}
	}
	if(gridName=="wgContractMzqDataGrid"){//免租期页签
		if(item=="dstartdate"){//开始日期
			var start=new Date($("#"+rowId+"_dstartdate").val());
			var end=new Date($("#"+rowId+"_denddate").val())
			if($("#"+rowId+"_denddate").val()!=""&&start>=end){
				//比本行结束日期大，提示
				$("#"+rowId+"_dstartdate").val(oldRefCode);
				js.showMessage("当前行开始日期不可比结束日期大！",'','info',3000);
				return;
			}else{
				for(var i=0;i<rows.length;i++){
					if(rows[i]==rowId){//行Id相等校验上下行时间关系
						if(i>0){//不是第一行，校验上一行结束日期
							if($("#"+rows[i-1]+"_denddate").val()!=""){
								var predate=new Date($("#"+rows[i-1]+"_denddate").val());
								if(predate>=start){//上一行结束日期比当前行开始日期大，提示
									$("#"+rowId+"_dstartdate").val(oldRefCode);
									js.showMessage("当前日期不可比上一行结束日期小！",'','info',3000);
									return;
								}
							}
						}
						if(i<rows.length-1){//不是最后一行，校验下一行
							if($("#"+rows[i+1]+"_dstartdate").val()!=""){
								var nextdate=new Date($("#"+rows[i+1]+"_dstartdate").val());
								if(nextdate<=start){//下一行开始日期比当前行开始日期小，提示
									$("#"+rowId+"_dstartdate").val(oldRefCode);
									js.showMessage("当前日期不可比下一行开始日期大！",'','info',3000);
									return;
								}
							}
						}
					}
				}
			}
		}
		if(item=="denddate"){//结束日期
			var start=new Date($("#"+rowId+"_dstartdate").val());
			var end=new Date($("#"+rowId+"_denddate").val())
			if($("#"+rowId+"_dstartdate").val()!=""&&start>=end){
				//比本行开始日期小，提示
				$("#"+rowId+"_denddate").val(oldRefCode);
				js.showMessage("当前行结束日期不可比开始日期小！",'','info',3000);
				return;
			}else{
				for(var i=0;i<rows.length;i++){
					if(rows[i]==rowId){//行Id相等校验上下行时间关系
						if(i>0){//不是第一行，校验上一行结束日期
							if($("#"+rows[i-1]+"_denddate").val()!=""){
								var predate=new Date($("#"+rows[i-1]+"_denddate").val());
								if(predate>=end){//上一行结束日期比当前行结束日期大，提示
									$("#"+rowId+"_denddate").val(oldRefCode);
									js.showMessage("当前日期不可比上一行结束日期小！",'','info',3000);
									return;
								}
							}
						}
						if(i<rows.length-1){//不是最后一行，校验下一行
							if($("#"+rows[i+1]+"_dstartdate").val()!=""){
								var nextdate=new Date($("#"+rows[i+1]+"_dstartdate").val());
								if(nextdate<=end){//下一行开始日期比当前行结束日期小，提示
									$("#"+rowId+"_denddate").val(oldRefCode);
									js.showMessage("当前日期不可比下一行开始日期大！",'','info',3000);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
//改变客户
function changeCust(custCode, custName){
	var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
	if(hids.length>0){
		for(var i=0;i<hids.length;i++){
			$("#hCust_"+hids[i]+"_pkCustomerCode").val(custCode);
			$("#hCust_"+hids[i]+"_pkCustomerName").val(custName);
		}
	}
	var zqids=$('#wgContractZqfyDataGrid').dataGrid('getDataIDs');
	if(zqids.length>0){
		for(var i=0;i<zqids.length;i++){
			$("#zqCust_"+zqids[i]+"_pkCustomerCode").val(custCode);
			$("#zqCust_"+zqids[i]+"_pkCustomerName").val(custName);
		}
	}
}
//行租金重算
function renScalRow(rowId,rowprice,rowarea,rowtzmonth,rowtzyear){
	if(rowId==""||rowprice==""||rowarea==""){
		return;
	}
	var dstart=$("#dstartdate").val();
	if(dstart==""){
		dstart=new Date();
	}
	var dnext=getNextFormatDate2(dstart,1);//获取合同开始日期到下一年的前一天
	var days=getDaysBetween(dstart,dnext);//获取年区间的天数
	var renttype=$("#renttype").select2("val");
	var rowyearmny=0;
	if(renttype=="1"){//日/平方米
		rowyearmny=rowprice*rowarea*days;
	}else if(renttype=="2"){//日/套
		rowyearmny=rowprice*days;
	}else if(renttype=="3"){//月/平方米
		rowyearmny=rowprice*rowarea*12;
	}else if(renttype=="4"){//月/套
		rowyearmny=rowprice*12;
	}else if(renttype=="5"){//年/平方米
		rowyearmny=rowprice*rowarea;
	}else if(renttype=="6"){//年/套
		rowyearmny=rowprice;
	}
	$("#"+rowId+"_nmonthprice").val(rowyearmny/12);
	$("#"+rowId+"_nmonthtzhprice").val(rowyearmny/12+toNumber(rowtzmonth));
	$("#"+rowId+"_nyearprice").val((rowyearmny/12+toNumber(rowtzmonth))*12);
	$("#"+rowId+"_nyeartzhprice").val((rowyearmny/12+toNumber(rowtzmonth))*12+toNumber(rowtzyear));
	$("#"+rowId+"_ndayprice").val(toNumber($("#"+rowId+"_nyeartzhprice").val())/days);
	form_jine();//增加千分位符
}
//增长期行租金重算
function renScalRow2(rowId,item){
	if(rowId==""){
		return;
	}
	var noldprice=0;
	var zzids=$('#wgContractZzqDataGrid').dataGrid('getDataIDs');
	var firstrow=zzids[0];//增长期第一行
	if(rowId==firstrow){
		//第一行取表头金额
		noldprice=$("#nprice").val();
	}else{
		//不是第一行取上一行金额
		for(var i=0;i<zzids.length;i++){
			if(rowId==zzids[i]){
				noldprice=$("#"+zzids[i-1]+"_nzzprice").val();
			}
		}
	}
	var dstart=$("#"+rowId+"_dstartdate").val();
	if(dstart==""){
		$("#"+rowId+"_"+item).val(null);
		js.showMessage("请先维护增长起始日期！",'','info',3000);
		return;
	}
	var dnext=getNextFormatDate2(dstart,1);//获取合同开始日期到下一年的前一天
	var days=getDaysBetween(dstart,dnext);//获取年区间的天数
	var renttype=$("#renttype").select2("val");
	var nzzyearmny=0;
	var narea=toNumber($("#nallarea").val());
	var nzzrate=$("#"+rowId+"_zzrate").val();//增长比例
	var nzzmny=$("#"+rowId+"_nzzmny").val();//增长金额
	if(nzzrate==""&&nzzmny==""){
		return;
	}
	var nnewprice=0;//增长后单价
	if(nzzmny!=""){
		nnewprice=toNumber(noldprice)+toNumber(nzzmny);
	}else{
		nnewprice=toNumber(noldprice)*(1+toNumber(nzzrate)/100);
	}
	if(renttype=="1"){//日/平方米
		nzzyearmny=nnewprice*narea*days;
	}else if(renttype=="2"){//日/套
		nzzyearmny=nnewprice*days;
	}else if(renttype=="3"){//月/平方米
		nzzyearmny=nnewprice*narea*12;
	}else if(renttype=="4"){//月/套
		nzzyearmny=nnewprice*12;
	}else if(renttype=="5"){//年/平方米
		nzzyearmny=nnewprice*narea;
	}else if(renttype=="6"){//年/套
		nzzyearmny=nnewprice;
	}
	var rowtzmonth=$("#"+rowId+"_nzzmonthptz").val();
	var rowtzyear=$("#"+rowId+"_nzzyearptz").val();
	$("#"+rowId+"_nzzprice").val(nnewprice);
	$("#"+rowId+"_nzzmonthp").val(nzzyearmny/12);
	$("#"+rowId+"_nzzmonthptzh").val(nzzyearmny/12+toNumber(rowtzmonth));
	$("#"+rowId+"_nzzyearp").val(toNumber($("#"+rowId+"_nzzmonthptzh").val())*12);
	$("#"+rowId+"_nzzyearptzh").val(toNumber($("#"+rowId+"_nzzyearp").val())+toNumber(rowtzyear));
	$("#"+rowId+"_nzzdayp").val(toNumber($("#"+rowId+"_nzzyearptzh").val())/days);
	form_jine();//增加千分位符
}
//表头金额重算
function rentScalHead(){
	var tolprice=0,toldaymny=0,tolmonthmny=0,tolyearmny=0;
	var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
	for(var i=0;i<hids.length;i++){
		var rowprice2=toNumber($("#"+hids[i]+"_nprice").val());
		var rowdaymny2=toNumber($("#"+hids[i]+"_ndayprice").val());
		var rowmonthmny2=toNumber($("#"+hids[i]+"_nmonthtzhprice").val());
		var rowyearmny2=toNumber($("#"+hids[i]+"_nyeartzhprice").val());
		tolprice+=rowprice2;
		toldaymny+=rowdaymny2;
		tolmonthmny+=rowmonthmny2;
		tolyearmny+=rowyearmny2;
	}
	$("#nprice").val(tolprice/hids.length);
	$("#ndayprice").val(toldaymny);
	$("#nmonthprice").val(tolmonthmny);
	$("#nyearprice").val(tolyearmny);
	form_jine();//增加千分位符
}

//表头面积重算
function rentScalHeadArea(){
	var tolarea=0;
	var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
	for(var i=0;i<hids.length;i++){
		var rowarea=toNumber($("#"+hids[i]+"_narea").val());
		tolarea+=rowarea;
	}
	$("#nallarea").val(tolarea);
	form_jine();//增加千分位符
}

//删行方法（手动触发）
function delRowData(pkname,rowid,panelname){
	if($("#"+rowid+"_"+pkname).val()!=""){//不是新增行
		$("#"+panelname).dataGrid("setRowData",rowid,null,{display:"none"});
		$("#"+rowid+"_status").val("1");
	}else{
		$("#"+panelname).dataGrid('delRowData', rowid);
	}
}
//房产信息变更后方法
function delByHouseChange(pkHouse){
	var wyids=$('#wgContractWyfDataGrid').dataGrid('getDataIDs');
	if(wyids.length>0){
		for(var i=0;i<wyids.length;i++){
			var wyhouse=$("#wyHouse_"+wyids[i]+"_pkHouseCode").val();
			if(pkHouse==wyhouse){
				delRowData("pkContractWyf",wyids[i],"wgContractWyfDataGrid");
			}
		}
	}
	var wyids2=$('#wgContractWyfDataGrid').dataGrid('getDataIDs');
	if(wyids2.length==0){//物业费无数据，清除物业支付方式
		var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
		if(wytids.length>0){
			for(var i=0;i<wytids.length;i++){
				delRowData("pkContractWytype",wytids[i],"wgContractWytypeDataGrid");
			}
		}
	}
}
//清除表体所有数据
function delAllBodyRows(type){
	if(type=="org"){
		var cids=$('#wgContractCustDataGrid').dataGrid('getDataIDs');
		if(cids.length>0){
			for(var i=0;i<cids.length;i++){
				delRowData("pkContractCust",cids[i],"wgContractCustDataGrid");
			}
			$("#wgContractCustDataGridAddRowBtn").click();
		}
	}
	var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
	if(hids.length>0){
		for(var i=0;i<hids.length;i++){
			delRowData("pkContractHouse",hids[i],"wgContractHouseDataGrid");
		}
	}
	var wyids=$('#wgContractWyfDataGrid').dataGrid('getDataIDs');
	if(wyids.length>0){
		for(var i=0;i<wyids.length;i++){
			delRowData("pkContractWyf",wyids[i],"wgContractWyfDataGrid");
		}
	}
	var zqids=$('#wgContractZqfyDataGrid').dataGrid('getDataIDs');
	if(zqids.length>0){
		for(var i=0;i<zqids.length;i++){
			delRowData("pkContractZqfy",zqids[i],"wgContractZqfyDataGrid");
		}
	}
	var yjids=$('#wgContractYjDataGrid').dataGrid('getDataIDs');
	if(yjids.length>0){
		for(var i=0;i<yjids.length;i++){
			delRowData("pkContractYj",yjids[i],"wgContractYjDataGrid");
		}
	}
	var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids.length>0){
		for(var i=0;i<zltids.length;i++){
			delRowData("pkContractZltype",zltids[i],"wgContractZltypeDataGrid");
		}
		$("#wgContractZltypeDataGridAddRowBtn").click();
		//租赁支付方式第一行不可删除
		var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
		if(zltids.length>0){
			$("#"+zltids[0]+"_dstartdate").attr("disabled","disabled");
			$("#"+zltids[0]+"_del").addClass("hide");
		}
	}
	var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids.length>0){
		for(var i=0;i<wytids.length;i++){
			delRowData("pkContractWytype",wytids[i],"wgContractWytypeDataGrid");
		}
	}
	var mzids=$('#wgContractMzqDataGrid').dataGrid('getDataIDs');
	if(mzids.length>0){
		for(var i=0;i<mzids.length;i++){
			delRowData("pkContractMzq",mzids[i],"wgContractMzqDataGrid");
		}
	}
	var zzids=$('#wgContractZzqDataGrid').dataGrid('getDataIDs');
	if(zzids.length>0){
		for(var i=0;i<zzids.length;i++){
			delRowData("pkContractZzq",zzids[i],"wgContractZzqDataGrid");
		}
	}
}
//清除表体所有数据
function delAllBodyRows2(tag1,tag2,tag3,tag4){
	if(tag1!=""){
		var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
		if(zltids.length>1){
			for(var i=1;i<zltids.length;i++){
				delRowData("pkContractZltype",zltids[i],"wgContractZltypeDataGrid");
			}
		}
	}
	if(tag2!=""){
		var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
		if(wytids.length>1){
			for(var i=1;i<wytids.length;i++){
				delRowData("pkContractWytype",wytids[i],"wgContractWytypeDataGrid");
			}
		}
	}
	if(tag3!=""){
		var mzids=$('#wgContractMzqDataGrid').dataGrid('getDataIDs');
		if(mzids.length>0){
			for(var i=0;i<mzids.length;i++){
				delRowData("pkContractMzq",mzids[i],"wgContractMzqDataGrid");
			}
		}
	}
	if(tag4!=""){
		var zzids=$('#wgContractZzqDataGrid').dataGrid('getDataIDs');
		if(zzids.length>0){
			for(var i=0;i<zzids.length;i++){
				delRowData("pkContractZzq",zzids[i],"wgContractZzqDataGrid");
			}
		}
	}
}

//房产页签增行事件
function addRowByHouse(){
	var cids=$('#wgContractCustDataGrid').dataGrid('getDataIDs');
	var cust=$("#cCust_"+cids[0]+"_pkCustomerCode").val();
	var hids=$('#wgContractHouseDataGrid').dataGrid('getDataIDs');
	var newrow=hids[hids.length-1];
	if(cust==""){
		js.showMessage("请先维护客户信息页签！",'','info',3000);
		$('#wgContractHouseDataGrid').dataGrid('delRowData', newrow);
		return;
	}
	var custCode=$("#cCust_"+cids[0]+"_pkCustomerCode").val();
	var custName=$("#cCust_"+cids[0]+"_pkCustomerName").val();
	$("#hCust_"+newrow+"_pkCustomerCode").val(custCode);
	$("#hCust_"+newrow+"_pkCustomerName").val(custName);
	ishouseadd=true;
	$("#"+newrow+"_iswy").select2('val','N');
}
//其他周期费用页签增行事件
function addRowByZqfy(){
	var cids=$('#wgContractCustDataGrid').dataGrid('getDataIDs');
	var cust=$("#cCust_"+cids[0]+"_pkCustomerCode").val();
	var zqids=$('#wgContractZqfyDataGrid').dataGrid('getDataIDs');
	var newrow=zqids[zqids.length-1];
	if(cust==""){
		js.showMessage("请先维护客户信息页签！",'','info',3000);
		$('#wgContractZqfyDataGrid').dataGrid('delRowData', newrow);
		return;
	}
	var custCode=$("#cCust_"+cids[0]+"_pkCustomerCode").val();
	var custName=$("#cCust_"+cids[0]+"_pkCustomerName").val();
	$("#zqCust_"+newrow+"_pkCustomerCode").val(custCode);
	$("#zqCust_"+newrow+"_pkCustomerName").val(custName);
	$("#"+newrow+"_dstartdate").val($("#dstartdate").val());
}
//押金页签增行事件
function addRowByYj(){
	var yjids=$('#wgContractYjDataGrid').dataGrid('getDataIDs');
	var newrow=yjids[yjids.length-1];
	/*if(yjids.length>1){
		js.showMessage("只能有一行押金！",'','info',3000);
		$('#wgContractYjDataGrid').dataGrid('delRowData', newrow);
		return;
	}*/
	var cids=$('#wgContractCustDataGrid').dataGrid('getDataIDs');
	var cust=$("#cCust_"+cids[0]+"_pkCustomerCode").val();
	if(cust==""){
		js.showMessage("请先维护客户信息页签！",'','info',3000);
		$('#wgContractYjDataGrid').dataGrid('delRowData', newrow);
		return;
	}
	var custCode=$("#cCust_"+cids[0]+"_pkCustomerCode").val();
	var custName=$("#cCust_"+cids[0]+"_pkCustomerName").val();
	$("#yjCust_"+yjids[yjids.length-1]+"_pkCustomerCode").val(custCode);
	$("#yjCust_"+yjids[yjids.length-1]+"_pkCustomerName").val(custName);
	$("#"+yjids[yjids.length-1]+"_drecdate").val(getNowFormatDate());
}
//租赁支付方式增行事件
function addRowByZltype(){
	var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids.length>1){
		$("#zltYspro_"+zltids[zltids.length-1]+"_pkYsprojectCode").val($("#zltYspro_"+zltids[0]+"_pkYsprojectCode").val());
		$("#zltYspro_"+zltids[zltids.length-1]+"_pkYsprojectName").val($("#zltYspro_"+zltids[0]+"_pkYsprojectName").val());
	}
}
//物业支付方式增行事件
function addRowByWytype(){
	//物业支付方式第一行不可删除
	var wytids2=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids2.length==1){
		$("#"+wytids2[0]+"_dstartdate").val($("#dstartdate").val());
		$("#"+wytids2[0]+"_dstartdate").attr("disabled","disabled");
		$("#"+wytids2[0]+"_del").addClass("hide");
	}else{
		$("#wytYspro_"+wytids2[wytids2.length-1]+"_pkYsprojectCode").val($("#wytYspro_"+wytids2[0]+"_pkYsprojectCode").val());
		$("#wytYspro_"+wytids2[wytids2.length-1]+"_pkYsprojectName").val($("#wytYspro_"+wytids2[0]+"_pkYsprojectName").val());
	}
}
//页面disabled字段可编辑，保存方法前调用
function canEdit(){
	$("#htstatus").removeAttr("disabled");
	$("#vbillstatus").removeAttr("disabled");
	$("#billtype").removeAttr("disabled");
	var zltids=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids.length>0){
		$("#"+zltids[0]+"_dstartdate").removeAttr("disabled");
	}
	var wytids=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids.length>0){
		$("#"+wytids[0]+"_dstartdate").removeAttr("disabled");
	}
}
//页面disabled字段不可编辑，保存方法失败后调用
function notEdit(){
	$("#htstatus").attr("disabled","disabled");
	$("#vbillstatus").attr("disabled","disabled");
	$("#billtype").attr("disabled","disabled");
	var zltids2=$('#wgContractZltypeDataGrid').dataGrid('getDataIDs');
	if(zltids2.length>0){
		$("#"+zltids2[0]+"_dstartdate").attr("disabled","disabled");
	}
	var wytids2=$('#wgContractWytypeDataGrid').dataGrid('getDataIDs');
	if(wytids2.length>0){
		$("#"+wytids2[0]+"_dstartdate").attr("disabled","disabled");
	}
}
