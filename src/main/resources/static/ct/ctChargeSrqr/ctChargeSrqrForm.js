var oldRefCode,oldRefName;//存放参照选择之前的值
$(function() {
	var pk=$("#pkChargeSrqr").val();
	if(pk==""||pk==null){
		$("#pkOrgCode").val(getMainOrgCode());
		$("#pkOrgName").val(getMainOrgName());
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
	
	$('#ctChargeSrqrBDataGridAddRowBtn').click(function (){
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
		$('textarea').attr('readOnly','readonly');//textarea不可选择 (readonly,disabled)
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
//			// 页面只可修改 本次收款金额 + 表体的备注
//			$('.input-group-btn a').unbind('click');//移除参照选择
//			$('input').attr('disabled','disabled');//input参照不可选择(disabled)
//			$('input').attr('style', 'background:#F6F6F6!important');
//			$('select').attr('disabled','disabled');//select不可选择(disabled)
			setHtmlView4(["_nbcsrqrmny","_remarks"]);
			
		}
	}
	
	$("#btnSubmit1").click(function(){
	    setHtmlView2();
	    form_cleanup_jine();//去掉千分位
		$('#btnSubmit').click();
	})

	function setHtmlView2() {
//		$('.input-group-btn a').html('<i class="fa fa-search"></i>');
		//$('.input-group-btn a').attr('disabled','disabled');//添加好像无效
		//$('input').attr('readOnly','readonly');//input不可选择
		$('input').removeAttr('disabled','disabled');//input参照不可选择(disabled)
		$('input').removeAttr('style', 'background:#F6F6F6!important');
		$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
		$('textarea').removeAttr('readOnly','readonly');//textarea不可选择 (readonly,disabled)

	}
});
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
	if(id.startsWith("pkCustomer_")){
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
