﻿/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 项目自定义的公共JavaScript，可覆盖jeesite.js里的方法
 */
js.layer.config({
	  anim: 1, //默认动画风格
	  extend: 'myskin/style.css', //加载您的扩展样式
	  skin: 'demo-class' //自定义皮肤
});

$(".c_close").click(function(){
	$("#btnSearch").click();
});
//去掉enter保存事件
document.onkeydown=function(event){
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if(e && e.keyCode==13){ // enter 键
		if(event.currentTarget.URL.indexOf("login") != -1||event.currentTarget.URL.indexOf("list") != -1){
			return
		}
		return false;
	}
};
// form页面金额格式化  （加row 区分表体中的金额控制)
// 项目结束时，统一测试时去掉，并检查金额逻辑：先调用form_cleanup_jine()->计算->完成后再form_jine()
// .number 离开事件 加上千分位
$(".cw_box").on("blur", ".number", function(){
	var val = $(this).val();
	var number = (val + '').replace(/,/g, '');
	var new_num = number_format(number, 2, ".", ",", "floor");
	$(this).val(new_num);
});
// 给页面中 所有的.number 加上千分位
function form_jine(){
	for(var h=0;h<$(".number").length; h++){
		$(".number").eq(h).val(number_format($(".number").eq(h).val(), 2, ".", ",", "floor"));
	}
	//保留三位小数
	for(var h=0;h<$(".number3").length; h++){
		$(".number3").eq(h).val(number_format($(".number3").eq(h).val(), 3, ".", ",", "floor"));
	}
};
//移除页面中所有的千分位
function form_cleanup_jine(){
	for(var h=0;h<$(".number").length; h++){
		var val = $(".number").eq(h).val();
		if(val == ""){
			continue
		}
        if(val.indexOf("E") == -1){
            var number = (val + '').replace(/,/g, '');
            val = Number(number).toFixed(2);
        }else{
        	var number = Number((val + '').replace(/[^0-9+-Ee.]/g, ''));
            val = Number(number).toFixed(2);
        }
		$(".number").eq(h).val(val);
	}
	for(var h=0;h<$(".number3").length; h++){
		var val = $(".number3").eq(h).val();
		if(val == ""){
			continue
		}
        if(val.indexOf("E") == -1){
            var number = (val + '').replace(/,/g, '');
            val = Number(number).toFixed(2);
        }else{
        	var number = Number((val + '').replace(/[^0-9+-Ee.]/g, ''));
            val = Number(number).toFixed(2);
        }
		$(".number3").eq(h).val(val);
	}
}

function toNumber(val){
	if(val==""){
		val=0;
	}
	return parseFloat((val+"").replace(/,/g, ''));
}

//顶部栏保存单据
$("#c_btnSubmit").click(function(){
    form_cleanup_jine();//去掉千分位
	$("#btnSubmit").click();
});
$("#c_btnSubmit1").click(function(){
    form_cleanup_jine();//去掉千分位
	$("#btnSubmit1").click();
});

function dateFor(fmt, date) { //author: meizz 
    var o = {
        "M+": date.getMonth() + 1,     //月份 
        "d+": date.getDate(),     //日 
        "h+": date.getHours(),     //小时 
        "m+": date.getMinutes(),     //分 
        "s+": date.getSeconds(),     //秒 
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
        "S": date.getMilliseconds()    //毫秒 
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//创建时间格式化显示
function creatTime(value) {
    var time = new Date(value);
    return top.dateFor("yyyy-MM-dd hh:mm:ss", time);//直接调用公共JS里面的时间类处理的办法  
}

//获取当前登录人主键
function getLoginUserCode(){
	var obj=window.parent.document.getElementById('userCode').innerText;
	return obj;
}
//获取当前登录人名称
function getLoginUserName(){
	var obj=window.parent.document.getElementById('userName').innerText;
	return obj;
}
//获取当前登录人关联人员主键
function getUserPsndocCode(){
	var obj=window.parent.document.getElementById('psndocCode').innerText;
	return obj;
}
//获取当前登录人关联人员名称
function getUserPsndocName(){
	var obj=window.parent.document.getElementById('psndocName').innerText;
	return obj;
}
//获取当前人员关联部门主键
function getPsndocDeptCode(){
	var obj=window.parent.document.getElementById('deptCode').innerText;
	return obj;
}
//获取当前人员关联部门名称
function getPsndocDeptName(){
	var obj=window.parent.document.getElementById('deptName').innerText;
	return obj;
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
//获取当前业务日期
function getBusiDate(){
	var obj=window.parent.document.getElementById('dateChange').innerText;
	return obj;
}
//获取当前日期，格式YYYY-MM-DD
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
//获取两个日期之间的天数差
function getDaysBetween(date1,date2){
	var s1=new Date(date1);
	var s2=new Date(date2);
	var time = s2.getTime() - s1.getTime();
	var days = parseInt(time / (1000 * 60 * 60 * 24))+1;
	return days;
};
//获取日期的后N天
function getNextDate(date,day) {
	var dd = new Date(date);
	dd.setDate(dd.getDate() + day);
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
	var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
	return y + "-" + m + "-" + d;
};

//获取当前日期的nextyearN年之后日期，格式YYYY-MM-DD by tcl
function getNextFormatDate(nextyear) {
    var date = new Date();
    date.setFullYear(date.getFullYear()+nextyear);
    date.setDate(date.getDate()-1);
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

//获取当前日期的nextyearN年之后日期，格式YYYY-MM-DD by tcl
function getNextFormatDate2(str,nextyear) {
    var date = new Date(str);
    date.setFullYear(date.getFullYear()+nextyear);
    date.setDate(date.getDate()-1);
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

//获取当前时间，格式YYYY-MM-DD HH:mm:ss
function getNowFormatDateTime() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var strHour = date.getHours();
    if(strHour<10){
    	strHour="0"+strHour;
    }
    var strMinu = date.getMinutes();
    if(strMinu<10){
    	strMinu="0"+strMinu;
    }
    var strSec = date.getSeconds();
    if(strSec<10){
    	strSec="0"+strSec;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + strHour + seperator2 + strMinu + seperator2 + strSec;
    return currentdate;
}

// 新增下级
function c_xinzenDown(url){
	// '/js/a/bd/bdFormattype/form'
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据新增下级！",null,'warning');
		return;
	}
	var JSONDATA={};
	JSONDATA['parentCode']=rowId;
	JSONDATA['isEdit']=true;
	$.ajax({
		url: url,
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			js.addTabPage($("#xinzen"), "新增合同类型", url + "?parentCode="+rowId+"&isEdit=true", null, null);//弹出新的标签页
		}
	});
//	js.addTabPage($("#xinzen"), "新增合同类型", url + "?parentCode="+rowId+"&isEdit=true", null, null);//弹出新的标签页
}

//组织新增下级判断
function c_xinzenDownZz(url){
	// '/js/a/bd/bdFormattype/form'
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id、
	var JSONDATA={};
	if(rowId==null){
		js.showMessage("请选择一条数据新增下级！",null,'warning');
		return;
	}else{
		var data = $('#dataGrid').dataGrid('getRowData', rowId);
		if(data['pkOrg.officeName']=="万谷集团"){
			JSONDATA['pk']=rowId;
			var newurl=url+"?pk="+rowId+"&isNewRecord=true"
			var newp=url+"?parentCode="+rowId+"&isEdit=true"
		}else{
			JSONDATA['parentCode']=rowId;
			var newurl=url+"?parentCode="+rowId+"&isNewRecord=true"
			var newp=url+"?parentCode="+rowId+"&isEdit=true"
		}
	}
    var newre = newp.replace("xinzen","form");
	JSONDATA['isNewRecord']=true;
	$.ajax({
		url: url,
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
				return ;
			}else{
				if(msg=="isXz"){
					js.addTabPage(null, "新增下级", newre, null, null);//弹出新的标签页
				}
			}
		}
	});
//	js.addTabPage($("#xinzen"), "新增合同类型", url + "?parentCode="+rowId+"&isEdit=true", null, null);//弹出新的标签页
}

/**修改**/
function c_editBtn(url){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据修改！",null,'warning');
		return;
	}
	// /js/a/bd/bdHttype/bianji?pkHttype
	var url_list = url.split("?");
	var JSONDATA={};
	JSONDATA[url_list[1]]=rowId;
	JSONDATA['isEdit']=true;
	var url_list1=url+"="+rowId+"&isEdit=true"
    var newre = url_list1.replace("bianji","form");
    var newr = url_list1.replace("bianji","xgform");
	$.ajax({
		url: url_list[0],
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (msg) {
			var new_msg = msg.split(",");
			console.log(new_msg)
			if(new_msg.length>1){
				js.showMessage(JSON.parse(msg).message);
			}else{
				if(msg=="isXj"){
					js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}

// 无多选框 单个删除
function c_delete(name){
	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行Id
	if(rowId==null){
		js.showMessage("请选择一条数据删除！",null,'warning');
		return;
	}
//	var rowData = $('#dataGrid').dataGrid('getRowData',rowId); // 获取选择行数据
//	if(rowData.status.includes("停用")){
//		js.showMessage("已经停用数据不可删除！",null,'warning');
//		return;
//	}
	var JSONDATA={};
	JSONDATA[name]=rowId;
	$.ajax({
		//type : "post",
		url : "./delete",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSONDATA,
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else{
				js.showMessage(data.message);
			}
		},
	    error : function (data) {
	    	js.showMessage(data.responseJSON.message,'','error',3000);
	    }
	});
}

//批量删除功能
$('#btnDelAll').click(function(){
	
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		selectData.push(dataselect.id);
	}
	
	$.ajax({
		type : "post",
		url : "./deleteAllData",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSON.stringify(selectData),
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else{
				js.showMessage(data.message);
			}
		},
	    error : function (data) {
	    	console.log("后台批量删除抛出异常！");
	    }
	});
});

//审批功能
$('#btnApproveAll').click(function(){
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0/*||selectRows.length>1*/){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		if(dataselect.vbillstatus=="1"||dataselect.vbillstatus.indexOf("审批通过")!=-1){
			js.showMessage('单据已审批，不可再次审批！','','warning',5000);
			return ;
		}
		selectData.push(dataselect.id);
	}
	
	$.ajax({
		type : "post",
		url : "./approveAll",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSON.stringify(selectData),
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else if(data.result == Global.FALSE){
				js.showMessage(data.message,'','warning',3000);
			}
		},
	    error : function (data) {
	    	console.log("抛出异常！");
	    }
	});
});

//取消审批功能
$('#btnUnApproveAll').click(function(){
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0/*||selectRows.length>1*/){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		if(dataselect.vbillstatus=="0"||dataselect.vbillstatus==""||dataselect.vbillstatus.indexOf("自由态")!=-1){
			js.showMessage('单据已取消审批，不可再次取消审批！','','warning',5000);
			return ;
		}
		selectData.push(dataselect.id);
	}
	
	$.ajax({
		type : "post",
		url : "./unapproveAll",
		contentType : "application/json;charset=UTF-8",
		dataType : 'json',
		data : JSON.stringify(selectData),
		async : false,
		success : function(data) {
			if(data.result == Global.TRUE){
				js.showMessage(data.message);
				$('#dataGrid').dataGrid('reloadGrid');
			}else if(data.result == Global.FALSE){
				js.showMessage(data.message,'','warning',3000);
			}
		},
	    error : function (data) {
	    	console.log("抛出异常！");
	    }
	});
});

//导出功能
$('#btnExport').click(function(){
	js.ajaxSubmitForm($('#searchForm'), {
		url:'./exportData',
		downloadFile:true
	});
});

//导入功能
$('#btnImport').click(function(){
	js.layer.open({
		type: 1,
		area: ['400px'],
		title: '导入数据',
		resize: false,
		scrollbar: true,
		content: js.template('importTpl'),
		success: function(layero, index){
			layero.find('input[type="checkbox"]').iCheck();
		},
		btn: ['<i class="fa fa-check"></i> 导入',
			'<i class="fa fa-remove"></i> 取消'],
		btn1: function(index, layero){
			var form = {
				inputForm: layero.find('#inputForm'),
				file: layero.find('#file').val()
			};
		    if (form.file == '' || (!js.endWith(form.file, '.xls') && !js.endWith(form.file, '.xlsx'))){
		    	js.showMessage("${text('文件不正确，请选择后缀为 “xls”或“xlsx”的文件。')}", null, 'warning');
		        return false;
		    }
			js.ajaxSubmitForm(form.inputForm, function(data){
				js.showMessage(data.message);
				if(data.result == Global.TRUE){
					js.layer.closeAll();
				}
				page();
			}, "json");
			return true;
		}
	});
});

//打印功能
$('#btnPrint').click(function(){
	//获取行数组id
	var selectRows = $('#dataGrid').dataGrid('getSelectRow');
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
    var id=selectRows;
    // 获取节点名称
    var dataselect=$('#dataGrid').getRowData(selectRows);
    //每个节点都应该有 所属组织  吧
    var c_name = dataselect["actions"].split("?")[1].split("=")[0];
    console.log(c_name);
	layer.open({//展开layer弹出框
		  type: 2,
		  shade: 0,
		  title: "单据打印",
		  area: ['900px', '400px'],
		 // btn: ['确定','关闭'],
		  content: ['./print?'+c_name+'='+id, 'yes'],//加载用户信息并过滤
		  yes : function(index, layero){},
		  btn2 : function(){}
	});
});

//刷新功能
/*$('#btnRefresh').click(function(){
	// 系统管理-业务单元-左边的组织机构-点击刷新按钮（会报错）
	$('#dataGrid').dataGrid('refresh');
});*/
$('#c_btnRefresh').click(function(){
	$('#dataGrid').dataGrid('refresh');
});

//附件功能
$('#btnFileUpload').click(function(){
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
	var dataselect=$('#dataGrid').getRowData(selectRows[0]);//获取行数据
	var vbillno=dataselect.vbillno;
	if(vbillno==null||vbillno==""||vbillno==undefined){
		js.showMessage('单据号不存在！','','warning',3000);
		return ;
	}
	
	var type="addImg";
	if(dataselect.vbillstatus=="1"||dataselect.vbillstatus.indexOf("审批通过")!=-1){
		type="viewImg";
	}
	
	js.layer.open({//展开layer弹出框
		  type: 2,
		  shade: 0,
		  title: "附件管理",
		  area: ['800px', '550px'],
		 // btn: ['确定','关闭'],
		  content: ['/js/a/filemanager/'+type+'?appid=nc&isWin=true&vbillno='+vbillno, 'yes'],
		 /* yes : function(index, layero){},
		  btn2 : function(){}*/
	});
});

$('.cw_btn_taggle').click(function() {
	//触发下面button按钮 
	var isShow = $(this).find('.btn_taggle').eq(0).attr('data-show');
	if(isShow == 'true'){
		//$(this).parent().css('margin','8px 10px 14px 10px');
		$(this).find('.btn_taggle').eq(0).find('i').attr('class','fa fa-plus');
		$(this).next().addClass('c_noheight');
		$(this).find('.btn_taggle').eq(0).attr('data-show','false');
	}else{
		$(this).find('.btn_taggle').eq(0).find('i').attr('class','fa fa-minus');
		$(this).next().removeClass('c_noheight');
		$(this).find('.btn_taggle').eq(0).attr('data-show','true');
	}
});

//格式化限制数字文本框输入，只能数字或者两位小数
function checknum(obj){
  // 清除"数字"和"."以外的字符
  obj.value = obj.value.replace(/[^\d.]/g,"");
  // 验证第一个字符是数字
  obj.value = obj.value.replace(/^\./g,"");
  // 只保留第一个, 清除多余的
  obj.value = obj.value.replace(/\.{2,}/g,".");
  obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
  // 只能输入两个小数
  obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
  
};

function formatnum(obj){
	 obj.value = Number(obj.value).toFixed(2);
};

function fixnum(obj){
	 return Number(obj).toFixed(2);
};

//查询弹框的位移事件
searchScoll();
function searchScoll(){
	var Mydiv = document.getElementsByClassName("c_search")[0];
	if(Mydiv != null ){
		Mydiv.onmousedown = function (ev) {
	        var e = window.event || ev;
	        var div=document.getElementById("searchForm");
	        //获取到鼠标点击的位置距离div左侧和顶部边框的距离；
	        var oX = e.clientX - div.offsetLeft;
	        var oY = e.clientY - div.offsetTop;
	        //当鼠标移动，把鼠标的偏移量付给div
	        document.onmousemove = function (ev) {
	            //计算出鼠标在XY方向上移动的偏移量，把这个偏移量加给DIV的左边距和上边距，div就会跟着移动
	            var e = window.event || ev;
	            div.style.left = e.clientX - oX + "px";
	            div.style.top = e.clientY - oY + "px";
	        }
	        //当鼠标按键抬起，清除移动事件
	        document.onmouseup = function () {
	            document.onmousemove = null;
	            document.onmouseup = null;
	        }
	    }
	}
    
}

//列表界面默认选中事件
function getSelectRow(data,cookiename){
	if(data.list!=undefined&&data.list!=null&&data.list.length>0){
		var rowid = js.cookie(cookiename);
		if(rowid!=undefined&&rowid!=null){
			$('#dataGrid').dataGrid('setSelectRow', rowid, false);
		}else{
			var dt=data.list;
			$('#dataGrid').dataGrid('setSelectRow', data.list[data.list.length-1].id, false);
		}
	}
}
function setSelectRow(pkid,cookiename){
	if(pkid==undefined||pkid==""){
		js.cookie(cookiename, null);
	}else{
		js.cookie(cookiename, pkid);
	}
};

//保留2位小数的千分号
function number_format(number, decimals, dec_point, thousands_sep,roundtag) {
	if(number == ""){
		return ""
	}
    /*
    * 参数说明：
    * number：要格式化的数字
    * decimals：保留几位小数
    * dec_point：小数点符号
    * thousands_sep：千分位符号
    * roundtag:舍入参数，默认 "ceil" 向上取,"floor"向下取,"round" 四舍五入
    * */
    number = (number + '').replace(/[^0-9+-Ee.]|,/g, '');
    roundtag = roundtag || "ceil"; //"ceil","floor","round"
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
 
            var k = Math.pow(10, prec);
            console.log();
 
            return '' + parseFloat(Math[roundtag](parseFloat((n * k).toFixed(prec*2))).toFixed(prec*2)) / k;
        };
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    var re = /(-?\d+)(\d{3})/;
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, "$1" + sep + "$2");
    }
 
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
};

var gridName;//存放当前编辑页签名称
//cw测试
$(".c_nav").on("click", "li", function() {
	//给当前元素加上css (c_active)
	$(this).addClass("c_active").siblings().removeClass("c_active");
	// 给相对应的table展示
	var index = $(this).index();
	$(this).parent().next().find(".ml10.mr10").eq(index).removeClass("hide").siblings().addClass("hide");
	gridName=$(this).parent().next().find(".ml10.mr10").eq(index).find("table").eq(1).attr("id");
});

