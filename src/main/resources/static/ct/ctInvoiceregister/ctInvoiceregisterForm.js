var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	var pk=$("#pkInvoiceapply").val();
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
	
	$('#ctInvoiceregisterSkmxDataGridAddRowBtn').click(function (){
		$(".input-group").on("mousedown",function showDeptTreeRef(){
			var id=$(this).attr("id");
			oldRefName=$(this).context.children[0].value;
			oldRefCode=$(this).context.children[1].value;
			resetDataUrl(id);
		});
	});
	
	$('#ctInvoiceregisterKpxxDataGridAddRowBtn').click(function (){
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
	
	//客户根据项目过滤
	if(id.startsWith("cust_")){
		//获取当前行组织值
		var pkOrg=$('#pkProjectCode').val();
		var newurl='/js/a/base/bdCustomer/bdCustomerSelect';
		if(pkOrg==""){
			newurl+="?pk_project=***";
		}else{
			newurl+="?pk_project="+pkOrg;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	//房产根据楼栋过滤
	if(id.startsWith("house_")){
		var rowid=id.split("_")[1];
		//获取当前行组织值
		var pkProject=$('#build_'+rowid+"_pkBuildingCode").val();
		var newurl='/js/a/zl/zlHousesource/housesourceSelect';
		if(pkProject==""){
			newurl+="?buildname=' '";
		}else{
			newurl+="?buildname="+pkProject;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	//楼栋根据项目过滤
	if(id.startsWith("build_")){
		var rowid=id.split("_")[1];
		var pkProject=$('#pkProjectCode').val();
		var newurl='/js/a/zl/zlBuildingfile/buildingSelect';
		if(pkProject==""){
			newurl+="?pkProjectid=' '";
		}else{
			newurl+="?pkProjectid="+pkProject;
		}
		$("#"+id).attr("data-url",newurl);
	}
	
	//预算项目根据组织集团过滤
	if(id.startsWith("ysproj_")){
		//获取当前行组织值
		var pkOrg=$('#pkOrgCode').val();
		var newurl='/js/a/bd/bdProject/treeData';
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
	
	if(id=='pkProject' && (act == 'ok' || act == 'clear')){
		var rowid=id.split("_")[1];
		if(nodes!=null&&nodes!=undefined){
			var newpro=$("#"+id+"Code").val();
			if(oldRefCode==newpro){
				return;
			}else{
				var inputs=document.getElementsByTagName('input');
				for(var i = 2; i < inputs.length; i++) {
					var inputid=inputs[i].id;
					if(inputid.startsWith("build_")||inputid.startsWith("house_")){
						inputs[i].value = ""; // 将每一个input的value置为空
					}
				}
			}
		}else{
			var inputs=document.getElementsByTagName('input');
			for(var i = 2; i < inputs.length; i++) {
				var inputid=inputs[i].id;
				if(inputid.startsWith("build_")||inputid.startsWith("house_")){
					inputs[i].value = ""; // 将每一个input的value置为空
				}
			}
		}
	}
	
}

function listselectCallback(id, act, index, layero,selectData,nodes){
	
	//表体楼栋
	if(id.startsWith('build_') && (act == 'ok' || act == 'clear')){
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		$("#house_"+rowid+"_pkHouseName").val(null);
		$("#house_"+rowid+"_pkHouseCode").val(null);
	}
	
	if(id.startsWith('cust_') && (act == 'ok' || act == 'clear')){
		var rowid=id.split("_")[1];
		var newvue=$("#"+id+"Code").val();
		if(oldRefCode==newvue){
			return;
		}
		//第一行客户修改更新开票信息
		var arrs=$("#ctInvoiceregisterSkmxDataGrid").getDataIDs();
		if(rowid!=arrs[0]){
			return ;
		}
		if(selectData!=null&&selectData!=undefined&&selectData[newvue]!=undefined){
			var JSONDATA2={};
			$.ajax({
				type : "post",
				url: '/js/a/base/bdCustomer/selectKpxxByCustomer?pkCustomer='+newvue,
				datatype: 'json',
				data: JSONDATA2,
				contentType: 'application/json;charset=utf-8',
				success: function (data) {
					//清空信息
					var arrs=$("#ctInvoiceregisterKpxxDataGrid").getDataIDs();
					for(let i=0;i<arrs.length;i++){
						$("#ctInvoiceregisterKpxxDataGrid").dataGrid('delRowData',arrs[i]);
					}
					
					var dlist=data.bdCustomerKpxxList;
					if(dlist!=null&&dlist.length>0){
						//$("#ctInvoiceapplyKpxxDataGrid").dataGrid('addRow');
						var did=dlist[0];
						var context={pkOrg:did.pkOrg,taxno:did.taxno,phone:did.phone,addr:did.addr,bankname:did.bankname,accnum:did.accnum};
						//往页面塞值
						var rowid = new Date().getTime()+Math.random().toString().substr(2,7); 
						$("#ctInvoiceregisterKpxxDataGrid").jqGrid('addRowData', rowid, context, 'last');
					}else{
					}
				},
				error: function(e){
					console.log(e);
				}
			});
		}else{
			//清空开票信息
			var arrs=$("#ctInvoiceregisterKpxxDataGrid").getDataIDs();
			for(let i=0;i<arrs.length;i++){
				$("#ctInvoiceregisterKpxxDataGrid").dataGrid('delRowData',arrs[i]);
			}
		}
	}
}

$('.box-main').on('change','.nkpmny' ,function(){
	form_cleanup_jine();//去掉千分位
	var rowId=$(this).attr('rowId');
	var flag=$('#isRef').html();
	//如果是参照校验是否大于剩余开票金额
	var mny=$(this).val();
	if(mny==null||mny==""){
		return ;
	}
	if(flag=="true"){
		var sykp=$('#'+rowId+"_nsykpmny").val();
		if(mny-sykp>0){
			js.showMessage('开票金额不能大于剩余可开票金额!');
			$('#'+rowId+"_nkpmny").val(null);
			return ;
		}
	}
	//计算税额和无税金额
	var sl=$('#'+rowId+"_taxrate").val();
	if(sl==null||sl==""){
		$('#'+rowId+"_ntaxmny").val(0.00);
		$('#'+rowId+"_nnotaxmny").val(mny);
	}else{
		$('#'+rowId+"_nnotaxmny").val(fixnum(Number(mny)/(100+Number(sl))*100));
		$('#'+rowId+"_ntaxmny").val(fixnum(Number(mny)-Number($('#'+rowId+"_nnotaxmny").val())));
	}
	
	//计算表头合计
	var arrs=$("#ctInvoiceregisterSkmxDataGrid").getDataIDs();
	var hjmny=0.00;
	for(let i=0;i<arrs.length;i++){
		var sqkpmny=$("#"+arrs[i]+"_nkpmny").val();
		hjmny=Number(hjmny)+Number(sqkpmny);
	}
	$('#nkpmny').val(fixnum(hjmny));
	form_jine();//金额格式化
});


$('.box-main').on('change','.taxrate' ,function(){
	form_cleanup_jine();//去掉千分位
	var rowId=$(this).attr('rowId');
	var flag=$('#isRef').html();
	//计算税额和无税金额
	var sl=$('#'+rowId+"_taxrate").val();
	if(sl==null||sl==""){
		return ;
	}
	var sqmny=$('#'+rowId+"_nkpmny").val();
	if(sqmny==null||sqmny==""){
		return ;
	}else{
		$('#'+rowId+"_nnotaxmny").val(fixnum(Number(sqmny)/(100+Number(sl))*100));
		$('#'+rowId+"_ntaxmny").val(fixnum(Number(sqmny)-Number($('#'+rowId+"_nnotaxmny").val())));
	}
	form_jine();//金额格式化
});


