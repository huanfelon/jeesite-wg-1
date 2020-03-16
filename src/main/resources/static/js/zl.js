
var url = window.location.href;


//树表结构页面加载完成后 编码字段监听事件 
const morenCdu  =  $("#code").val().length;//页面加载时获取的默认值长度
console.log('yuan'+morenCdu);
const morenVal = $("#code").val();//页面加载时获取的默认值
console.log('zhi'+morenVal);
var oninput_str = $("#code").val();//上一次保存值
var flag = false;// 判断新增还是修改

if(url.indexOf("pk") == -1){
	// 说明没找到pk
	
}else{
	flag = true
}

document.getElementById("code").oninput = function () {
	var changdu = this.value.length;//编辑时获取当前长度
	var aa=this.value;
	console.log(changdu);

	var reg = /^[0-9a-zA-Z]+$/
		if(!reg.test(aa)&&aa!=""){
			$("#code").val(oninput_str)
			return ;
		}
	if(flag== true){
		// 如果是修改不是新增 可以删除2格
		try{
			var new_val = morenVal.slice(0,-2);
			if(this.value.slice(0,(morenCdu-2)) != new_val){
				console.log('只能在初始值前删除2位再修改！');
				$("#code").val(new_val);
				return
			}
			if(changdu > morenCdu){
				console.log("输入超出初始长度，return")
				$("#code").val(this.value.slice(0, -1));
				return
			}
			if(changdu < new_val.length){
				console.log("最多修改默认值的后2位")
				$("#code").val(oninput_str)
				return 
			}
		}catch(e){
			console.log(e)
		}
		oninput_str = this.value;
	}else{
		try {
			//判断 修改值的前 slice 是否与初始值相同
			if (this.value.slice(0, morenCdu) != morenVal) {
				console.log('只能在初始值后增加2位！');
				$("#code").val(morenVal);
				return
			}
			if (changdu < morenCdu) {
				//当前长度小于默认长度，塞初始值
				$("#code").val(morenVal);
			} else if (changdu > (morenCdu + 2)) {
				//js.alert('编码格式不对');
				$("#code").val(this.value.slice(0, -1));
			}
			oninput_str = this.value;
	
		} catch (e) {
	
		}
	}
	
}

//编码字段触发事件 复制粘贴导致字段超出
$("#code").change(function(){
	let newchange = $(this).val().length;
	if(newchange > (morenCdu+2)){
		js.alert("编码数值超出，请重新填写");
		$("#code").val(morenVal);
	}
});
$("#btnSubmit4").click(function(){
	let newchange = $("#code").val().length;
	if(flag){
		if(newchange < morenCdu){
			js.showMessage('编码为上级编码和两位字符的组合!','','warning',3000);
			return;
		}
	}else{
		if(newchange < (morenCdu+2)){
			js.showMessage('编码为上级编码和两位字符的组合!','','warning',3000);
			return;
		}
	}
    setHtmlView2();
	$('#btnSubmit').click();
})
function bianji33(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("bianji","form");
	var JSONDATA={'pkCostproject':pk,'isNewRecord':flag};
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
				js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
			}
		}
	});
}
function setHtmlView2() {
//	$('.input-group-btn a').html('<i class="fa fa-search"></i>');
	//$('.input-group-btn a').attr('disabled','disabled');//添加好像无效
	//$('input').attr('readOnly','readonly');//input不可选择
	$('input').removeAttr('disabled','disabled');//input参照不可选择(disabled)
	$('input').removeAttr('style', 'background:#F6F6F6!important');
	$('select').removeAttr('disabled','disabled');//select不可选择(disabled)
	$('textarea').removeAttr('readOnly','readonly');//textarea不可选择 (readonly,disabled)

}
