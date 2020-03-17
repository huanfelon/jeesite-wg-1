var oldRefCode,oldRefName;//存放参照选择之前的值
$(document).ready(function() {
	var pk=$("#pkRentbill").val();
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
	
	$('#ctRentbillSrftDataGridAddRowBtn').click(function (){
		$(".input-group").on("mousedown",function showDeptTreeRef(){
			var id=$(this).attr("id");
			oldRefName=$(this).context.children[0].value;
			oldRefCode=$(this).context.children[1].value;
			resetDataUrl(id);
		});
	});
	
	$('#ctRentbillZjmxDataGridAddRowBtn').click(function (){
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
	
}

$('.box-main').on('change','.nzjyhmny' ,function(){
	form_cleanup_jine();//去掉千分位
	var rowId=$(this).attr('rowId');
	//计算税额和无税金额
	var sl=$('#'+rowId+"_taxrate").val();
	var yhmny=$('#'+rowId+"_nyhmny").val();
	var planmny=$('#'+rowId+"_nplanskmny").val();
	if(Number(planmny)-Number(yhmny)<0){
		js.showMessage('优惠金额不能超过计划收款金额！');
		$('#'+rowId+"_nyhmny").val(null);
		return ;
	}
	var ysmny=fixnum(Number(planmny)-Number(yhmny));
	$('#'+rowId+"_nysmny").val(ysmny);
	if(sl==null||sl==""){
		return ;
	}
	$('#'+rowId+"_nnotaxmny").val(fixnum(Number(ysmny)/(100+Number(sl))*100));
	$('#'+rowId+"_ntaxmny").val(fixnum(Number(ysmny)-Number($('#'+rowId+"_nnotaxmny").val())));
	form_jine();//金额格式化
});


