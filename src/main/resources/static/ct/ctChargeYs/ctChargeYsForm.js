$(function() {
	var pk=$("#pkChargeYs").val();
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
	var istag=$("#isEdit")[0].innerHTML;
	if(istag=="false"){

		$('.input-group-btn a').unbind('click');//移除参照选择
	//	$('.input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('input').attr('style', 'background:#eee!important');
		$('select').attr('disabled','disabled');//select不可选择(disabled)
		$('textarea').attr('readOnly','readonly');//textarea不可选择 (readonly,disabled)
	//	$(".input-group-btn a").addClass("btn-ban");
	}else{
		$('.input-group-btn a').html('<i class="fa fa-search"></i>');
		$('input').removeAttr('disabled','disabled');//input参照不可选择(disabled)
		$('input').removeAttr('style', 'background:#eee!important');
		$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
	
		$('#creatorDiv .input-group-btn a').unbind('click');//移除参照选择
		//$('#creatorDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#creatorDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#creatorDiv input').attr('style', 'background:#eee!important');
		
		$('#modifierDiv .input-group-btn a').unbind('click');//移除参照选择
		//$('#modifierDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#modifierDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#modifierDiv input').attr('style', 'background:#eee!important');
		
		$('#approverDiv .input-group-btn a').unbind('click');//移除参照选择
	//	$('#approverDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#approverDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#approverDiv input').attr('style', 'background:#eee!important');
	//	$(".cedit .input-group-btn a").addClass("btn-ban");
		
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
		$('input').removeAttr('style', 'background:#eee!important');
		$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
		$('textarea').removeAttr('readOnly','readonly');//textarea不可选择 (readonly,disabled)

	}
});

