$(function() {
	var istag=$("#isEdit")[0].innerHTML;
	if(istag=="false"){

		$('.input-group-btn a').unbind('click');//移除参照选择
		$('.input-group-btn a').css("opacity","0.6");
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
		$('#creatorDiv .input-group-btn a').css("opacity","0.6");
//		$('#creatorDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#creatorDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#creatorDiv input').attr('style', 'background:#F6F6F6!important');
		
		$('#modifierDiv .input-group-btn a').unbind('click');//移除参照选择
		$('#modifierDiv .input-group-btn a').css("opacity","0.6");
//		$('#modifierDiv .input-group-btn a').html('<i class="fa fa-ban"></i>');
		$('#modifierDiv input').attr('disabled','disabled');//input参照不可选择(disabled)
		$('#modifierDiv input').attr('style', 'background:#F6F6F6!important');
//		$(".cedit .input-group-btn a").addClass("btn-ban");
	}

});

