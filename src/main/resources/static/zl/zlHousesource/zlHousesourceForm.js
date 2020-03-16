$(function() {
	var pk=$("#pkHousesource").val();
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
