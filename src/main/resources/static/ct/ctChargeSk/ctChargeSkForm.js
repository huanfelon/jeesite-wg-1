var oldRefCode,oldRefName;//存放参照选择之前的值
$(function() {
	var pk=$("#pkChargeSk").val();
	if(pk==""||pk==null){
		$("#pkOrgCode").val(getMainOrgCode());
		$("#pkOrgName").val(getMainOrgName());
		
		//测试
	}
	//获取当前主组织主键
	function getMainOrgCode(){
		var obj=window.parent.document.getElementById('officeCode').innerText;
		return obj;
	}
	//获取当前主组织名称
	function getMainOrgName(){
		var obj=window.parent.document.getElementById('officeName').innerText;
		if(obj=='未选择主组织'){
			obj="";
		}
		return obj;
	}
	//参照字段选择前过滤
	$(".input-group").on("mousedown",function showDeptTreeRef(){
		var id=$(this).attr("id");
		oldRefName=$(this).context.children[0].value;
		oldRefCode=$(this).context.children[1].value;
		resetDataUrl(id);
	});
	
	$('#ctChargeSkBDataGridAddRowBtn').click(function (){
		$(".input-group").on("mousedown",function showDeptTreeRef(){
			var id=$(this).attr("id");
			oldRefName=$(this).context.children[0].value;
			oldRefCode=$(this).context.children[1].value;
			resetDataUrl(id);
		});
	});
	var istag=$("#isEdit")[0].innerHTML;
	var isCz=$("#isCz")[0].innerHTML;
	if(istag=="false"){

		$('.input-group-btn a').unbind('click');//移除参照选择
//		$('.input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('input').attr('style', 'background:#F6F6F6!important');
		$('select').attr('disabled','disabled');//select不可选择(disabled)
		$('textarea').attr('disabled','disabled');//textarea不可选择 (readonly,disabled)
//		$(".input-group-btn a").addClass("btn-ban");
	}else{
//		$('.input-group-btn a').html('<i class="fa fa-search"></i>');
		$('input').removeAttr('disabled','disabled');//input参照不可选择(disabled)
		$('input').removeAttr('style', 'background:#F6F6F6!important');
		$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
	
		$('#creatorDiv .input-group-btn a').unbind('click');//移除参照选择
//		$('#creatorDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#creatorDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#creatorDiv input').attr('style', 'background:#F6F6F6!important');
		
		$('#modifierDiv .input-group-btn a').unbind('click');//移除参照选择
//		$('#modifierDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#modifierDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#modifierDiv input').attr('style', 'background:#F6F6F6!important');
		
		$('#approverDiv .input-group-btn a').unbind('click');//移除参照选择
//		$('#approverDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#approverDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#approverDiv input').attr('style', 'background:#F6F6F6!important');
//		$(".cedit .input-group-btn a").addClass("btn-ban");
		if(isCz == "false"){
			// 页面只可修改 本次收款金额 + 表体的备注
			$('.input-group-btn a').unbind('click');//移除参照选择
			$('input').attr('disabled','disabled');//input参照不可选择(disabled)
			$('input').attr('style', 'background:#F6F6F6!important');
			$('select').attr('disabled','disabled');//select不可选择(disabled)
			$("#skdate").removeAttr("disabled");
			$('#skdate').attr('style', 'background:#fff!important');
			$("#bcTotalAmount").removeAttr("disabled");
			$('#bcTotalAmount').attr('style', 'background:#fff!important');
			$("#bankAccount").removeAttr("disabled");
			$('#bankAccount').attr('style', 'background:#fff!important');
			$("#paymentMethod").removeAttr("disabled");
		
	}
	}
	
	$("#btnSubmit1").click(function(){
	    setHtmlView2();
	    form_cleanup_jine();//去掉千分位
		$('#btnSubmit').click();
	})

	function setHtmlView2() {
		$('.input-group-btn a').html('<i class="fa fa-search"></i>');
		//$('.input-group-btn a').attr('disabled','disabled');//添加好像无效
		//$('input').attr('readOnly','readonly');//input不可选择
		$('input').removeAttr('disabled','disabled');//input参照不可选择(disabled)
		$('input').removeAttr('style', 'background:#F6F6F6!important');
		$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
		$('textarea').removeAttr('readOnly','readonly');//textarea不可选择 (readonly,disabled)

	}
	$("#bcTotalAmount").change(function(){
		form_cleanup_jine();//去掉千分位
		var ids=$('#ctChargeSkBDataGrid').dataGrid('getDataIDs');
		if(ids.length<=0){
			js.showMessage("请先填写表体应收金额和税率!",null,'warning');
			$("#bcTotalAmount").val("");
			return;
		}else{
			for(var i=0;i<ids.length;i++){
				var nysmny=$("#"+ids[i]+"_nysmny").val();
				var ntaxRate=$("#"+ids[i]+"_taxRate").val();
				if(nysmny == "" || nysmny == undefined){
					layer.tips('应收金额不可为空!', '#'+ids[i]+'_nysmny',{tips: [2,"#ff0000b5"],tipsMore: true});
					$("#bcTotalAmount").val("");
					return false;
				}else if(ntaxRate == "" || ntaxRate == undefined){
					layer.tips('税率不可为空!', '#'+ids[i]+'_taxRate',{tips: [2,"#ff0000b5"],tipsMore: true});
					$("#bcTotalAmount").val(""); 
					return false;
				}
			}
		}
		var bcTotal=$("#bcTotalAmount").val();
		var ids=$('#ctChargeSkBDataGrid').dataGrid('getDataIDs');
		var ny1=0;
		var ny2=0;
		if(ids!=null&&ids.length>0){
			for(var i=0;i<ids.length;i++){
				var nymny=getUf($("#"+ids[i]+"_nysmny").val());
				var nymny1=getUf($("#"+ids[i]+"_nys1mny").val());
				ny1=Number(nymny)+ny1;
				ny2=Number(nymny1)+ny2;
			}
		}
		if(Number(bcTotal)>(ny1-ny2)){
			js.showMessage("本次收款总金额不可大于应收未收金额!",null,'warning');
			$("#bcTotalAmount").val("");
			return;
		}else{
			if(ids!=null&&ids.length>0){
				for(var i=0;i<ids.length;i++){
					var nymny=getUf($("#"+ids[i]+"_nysmny").val());
					if(Number(bcTotal)>Number(nymny)){
						var ys=getUf($("#"+ids[i]+"_nys1mny").val());
						var bcsk=nymny-ys;
						$("#"+ids[i]+"_nbcskmny").val(bcsk);
						var taxRate=getUf($("#"+ids[i]+"_taxRate").val());
						var noTaxAmount=(bcsk/(1+taxRate/100)).toFixed(2);//无税金额
						$("#"+ids[i]+"_noTaxAmount").val(noTaxAmount);
						var taxAmount=(bcsk-noTaxAmount).toFixed(2);//税额
						$("#"+ids[i]+"_taxAmount").val(taxAmount);
						bcTotal=bcTotal-bcsk;
						continue;
					}else{
						var ys=getUf($("#"+ids[i]+"_nys1mny").val());
						var bcsk=nymny-ys;
						if(Number(bcTotal)>Number(bcsk)){
							$("#"+ids[i]+"_nbcskmny").val(bcsk);
							var taxRate=getUf($("#"+ids[i]+"_taxRate").val());
							if(Number(bcTotal)>0){
								var noTaxAmount=(bcsk/(1+taxRate/100)).toFixed(2);//无税金额
								var taxAmount=(bcsk-noTaxAmount).toFixed(2);//税额
								$("#"+ids[i]+"_noTaxAmount").val(noTaxAmount);
								$("#"+ids[i]+"_taxAmount").val(taxAmount);
								bcTotal=bcTotal-bcsk;
								continue;
							}else{
								var noTaxAmount=0.00;//无税金额
								var taxAmount=0.00;//税额
								$("#"+ids[i]+"_noTaxAmount").val(noTaxAmount);
								$("#"+ids[i]+"_taxAmount").val(taxAmount);
								continue;
							}
						}else{
							$("#"+ids[i]+"_nbcskmny").val(bcTotal);
							var taxRate=getUf($("#"+ids[i]+"_taxRate").val());
							if(Number(bcTotal)>0){
								var noTaxAmount=(bcTotal/(1+taxRate/100)).toFixed(2);//无税金额
								var taxAmount=(bcTotal-noTaxAmount).toFixed(2);//税额
								$("#"+ids[i]+"_noTaxAmount").val(noTaxAmount);
								$("#"+ids[i]+"_taxAmount").val(taxAmount);
								bcTotal=bcTotal-bcTotal;
								continue;
							}else{
								var noTaxAmount=0.00;//无税金额
								var taxAmount=0.00;//税额
								$("#"+ids[i]+"_noTaxAmount").val(noTaxAmount);
								$("#"+ids[i]+"_taxAmount").val(taxAmount);
								continue;
							}
						}
						
					}
				}
			}
		}
		form_jine();//金额格式化
	})
	//表体应收金额、税率控制表头
	/*var ids=$('#ctChargeSkBDataGrid').dataGrid('getDataIDs');
	if(ids.length<=0){
		return;
	}else{
		for(var i=0;i<ids.length;i++){
			var nysmny=$("#"+ids[i]+"_nysmny").val();
			var ntaxRate=$("#"+ids[i]+"_taxRate").val();
			if(nysmny != "" || nysmny != undefined){
				
				$("#"+ids[i]+"_nysmny").change(function(){
					$("#bcTotalAmount").val("");
				})
				return false;
			}else if(ntaxRate != "" || ntaxRate != undefined){
				$("#"+ids[i]+"_taxRate").change(function(){
					$("#bcTotalAmount").val("");
				})
				return false;
			}
		}
	}*/
	
	$(".box").on("change",".yspay",function(){
		$("#bcTotalAmount").val("");
		var ids=$('#ctChargeSkBDataGrid').dataGrid('getDataIDs');
		if(ids.length<=0){
			return;
		}else{
			for(var i=0;i<ids.length;i++){
				var nysmny=$("#"+ids[i]+"_nysmny").val();
				if(nysmny != "" || nysmny != undefined){
		         $("#"+ids[i]+"_nqsmny").val(nysmny);
				}
			}
		}
	});
	$(".box").on("change",".sltax",function(){
		$("#bcTotalAmount").val("");
	});
});

function getUf(obj){
	return obj==null||obj==""?0:obj;
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
	
	//预算项目根据组织集团过滤
	if(id.startsWith("pkSfProject_")){
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