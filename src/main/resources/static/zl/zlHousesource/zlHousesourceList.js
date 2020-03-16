$(document).ready(function() {
	InitQuery();
	$('#dataGrid').dataGrid('refresh');
})
//初始化查询条件
function InitQuery(){
	$(".form-group input").val("");
	$("#pkOrgCode").val(getMainOrgCode());
	$("#pkOrgName").val(getMainOrgName());
}
/**查询条件必输项校验**/
function checkQueryNotNull(){
	var num=0;
	var pkOrg=$("#pkOrgCode").val();
	if (pkOrg=="") {
		layer.tips('请输入组织', '#pkOrgDiv',{tips: [2,"#c7360c"],tipsMore: true});
		num++;
	}
	return num;
}
/***查询***/
function queryData(){
	var num=checkQueryNotNull();
	if(num==0){
		$("#btnSearch").click();
		$('#dataGrid').dataGrid('refresh');
	}
}

/**重置**/
function resetBtn(){
	InitQuery();
}

function correct() {//批改
	
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
	
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		selectData.push(dataselect);
	}
	console.log(selectData);
	var pname=selectData[0]["projectname.name"];
	var pcode=selectData[0]["projectname.pkProject"];
	//批改校验，已租房源不可以批改  housestate
	var project_arr=[];
	for(var i=0;i<selectData.length;i++){
		var housestatus=selectData[i].housestate;
		var projectfile = selectData[i]["projectname.name"];
		project_arr.push(projectfile);
		if(housestatus=="已租"){
			js.showMessage('包含已租状态房源，不允许批改!','','warning',3000);
			return  ;
		}
	}
	for(var j =0;j<project_arr.length;j++){
		var project =project_arr[0];
		var project1=project_arr[j];
		if(project!=project1){
			js.showMessage('项目类型不同，不允许批改!','','warning',3000);
			return  ;
		}
	}
/*	for(var i =0;i<family_arr.length;i++){
		var family =family_arr[0];
		var family1=family_arr[i];
		if(family!=family1){
			js.showMessage('户型信息不同，不允许批改!','','warning',3000);
			return  ;
		}
	}*/
	var selDatas = [];
	
	layer.open({//展开layer弹出框
		  type: 2,
		  shade: 0,
		  title: "房源批改",
		  area: ['800px', '350px'],
		  btn: ['确定','关闭'],
		  content: ['/js/a/zl/zlHousesource/currectHouseSelect?projectname='+pcode, 'yes'],//加载用户信息并过滤
		  yes : function(index, layero){
			     var filename1 = $(layero).find("iframe")[0].contentWindow.document.getElementById("pkFamilyfileName");
			     var filecode1 = $(layero).find("iframe")[0].contentWindow.document.getElementById("pkFamilyfileCode");
			     var buildarea1 = $(layero).find("iframe")[0].contentWindow.document.getElementById("buildarea");
			     var innerarea1 = $(layero).find("iframe")[0].contentWindow.document.getElementById("innerarea");
			     var hstatus1 = $(layero).find("iframe")[0].contentWindow.document.getElementById("housestate");
		         var filename=filename1.value;
		         var filecode=filecode1.value;
		         var buildarea=buildarea1.value;
		         var innerarea=innerarea1.value;
		         var hstatus=hstatus1.value;
				 	var si_obj={};
				 	for(var i=0;i<selectRows.length;i++){
				 		si_obj[i] = selectRows[i];
				 	}
				 	var dat=[];
			 	var data = {'filename':filename,'filecode':filecode,'buildarea':buildarea,'innerarea':innerarea,'hstatus':hstatus};

			    if(data.filename==""||data.filecode==""){
			   	 js.showMessage('户型为空，请选择户型','','error',3000);
			   	 return;
			    }
			 	    dat.push(data);
			 	var queryData={'housecode':si_obj,'houseList':dat};
					 $.ajax({
						type : "post",
						url : "/js/a/zl/zlHousesource/currect",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						dataType : 'json',
						//data : JSON.stringify(queryData),//将对象或者数组转换为一个 JSON字符串，后台为
						data : queryData,
						async : false,
						success : function(data) {
							if(data.result=="true"){
								js.showMessage(data.message,'','success',3000);
								$('#dataGrid').dataGrid('reloadGrid');
							}else{
								js.showMessage(data.message,'','error',3000);
							}
						},
					    error : function () {
					    	alert("请求失败！");
					    }
							
					});
		  }, 
		  btn2 : function(){
			  
		  }
		  });
	
	
}

function split() {//拆分
	
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}else if(selectRows.length>1){
		js.showMessage('请选择一条数据!','','info',3000);
		return ;
	}else{
		for(var i=0;i<selectRows.length;i++){
			var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
			if(dataselect.housestate=="已租"){
				js.showMessage('已租房源不可以拆分!','','warning',3000);
				return ;
			}
		}
		//var queryData={'pks':selectRows};
		var flag = false;
		//校验有没有被合同管理房产页签引用
		$.ajax({
			type:"post",
			url: '/js/a/zl/zlHousesource/splitData?pk='+selectRows,
			datatype: 'json',
			//data: queryData,
			async:false,
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success: function (msg) {
				if(msg!="split"){
					var msg1=$.parseJSON(msg);
					var pk1=msg1.data;
					for(var i=0;i<selectRows.length;i++){
						var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
						if(selectRows[i]==pk1){
							js.showMessage(dataselect.estatename+msg1.message,'','warning',3000);
							flag = true;
							return ;
						}
					}
				}
			},
			error: function (msg){
				console.log($.parseJSON(msg.responseText).message);
			}
		});
	}
	if(flag){
		return;
	}
	
	var selectData=[]; 
	for(var i=0;i<selectRows.length;i++){
		var pk1=selectRows[i];
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		selectData.push(dataselect);
	}
	console.log(selectData);
	var mr_buildarea=selectData[0]["buildarea"];
	var mr_innerarea=selectData[0]["innerarea"];
	
	layer.open({//展开layer弹出框
		  type: 2,
		  shade: 0,
		  title: "房源拆分",
		  area: ['1200px', '400px'],
		  btn: ['确定','关闭'],
		  content: ['/js/a/zl/zlHousesource/splitHouseSelect?pk='+pk1, 'yes'],//加载用户信息并过滤
		  yes : function(index, layero){
			    var data=$(layero).find("iframe").contents().find("#dataGrid");//弹出框内行数据
				var splitData =data.dataGrid('getRowData');
		    	//var splitData=$('#dataGrid').dataGrid('getParam', "splitData1");
			  
				var ocode=selectData[0]["pkOrg.officeCode"];
				var oname=selectData[0]["pkOrg.officeName"];
				var pname=selectData[0]["projectname.name"];
				var pcode=selectData[0]["projectname.pkProject"];
				var bname=selectData[0]["buildname.name"];
				var bcode=selectData[0]["buildname.pkBuildingfile"];
				var fcode=selectData[0]["pkFormattype.pkFormattype"];
				var fname=selectData[0]["pkFormattype.name"];
				var mcode=selectData[0]["pkFamilyfile.pkFamilyfile"];
				var mname=selectData[0]["pkFamilyfile.name"];
				//var bunit=selectData[0]["unit"];
				//var bfloorn=selectData[0]["floorn"];
				var bestatecode=selectData[0]["estatecode"];
				var bestatename=selectData[0]["estatename"];
				var hstatus=selectData[0]["housestate"];
				
		    	var sj_arr=[];
			    for(var i=1;i<=splitData.length;i++){
			    	/*var estate_code_id=i+"_estatecode"
			    	var estate_sj_c1=$("#"+estate_code_id).val();
			    	var estate_sj_c = $(layero).find("iframe")[0].contentWindow.document.getElementById(estate_code_id);*/
			    /*	if(splitData[i].estatecode==null){
			    		break;
			    	}*/
			    	var roomnumber_id=i+"_roomnumber"
			    	var roomnumber_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(roomnumber_id);
			    	var unit_id=i+"_unit"
			    	var unit_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(unit_id);
			    	var floorn_id=i+"_floorn"
			    	var floorn_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(floorn_id);
			    	var buildarea_id=i+"_buildarea"
			    	var buildarea_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(buildarea_id);
			    	var innerarea_id=i+"_innerarea"
			    	var innerarea_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(innerarea_id);
			    	
			    	var roomnumber=roomnumber_sj.value;
			    	var bunit=unit_sj.value;
			    	var bfloorn=floorn_sj.value;
			    	var buildarea=buildarea_sj.value;
			    	var innerarea=innerarea_sj.value;
			    	
			    	/*var roomnumber=$("#"+i+"_roomnumber").val();
			    	var bunit=$("#"+i+"_unit").val();
			    	var bfloorn=$("#"+i+"_floorn").val();
			    	var buildarea=$("#"+i+"_buildarea").val();
			    	var innerarea=$("#"+i+"_innerarea").val();*/
			    	
			    	//必输项与校验
			    	if(roomnumber==""){
			    		js.showMessage("房间号不可以为空!","","warning",3000);
			    		return false;
			    	}else if(buildarea==""){
			    		js.showMessage("租赁面积必输项不可为空!","","warning",3000);
			    		return false;
			    	}else if(innerarea==""){
			    		js.showMessage("产证面积必输项不可为空!","","warning",3000);
			    		return false;
			    	}else if(new Number(buildarea)=="0"||new Number(buildarea)=="0"){
			    		js.showMessage("租赁面积与产证面积不可为零!","","warning",3000);
			    		return false;
			    	}
			    	
			    	//将数据循环放进数组
			    	var sj = {'orgCode':ocode,'orgName':oname,'projectCode':pcode,'projectName':pname,'buildCode':bcode,'buildName':bname,'estatecode':bestatecode,'estatename':bestatename,'formatCode':fcode,'formatName':fname,'unit':bunit,'floorn':bfloorn,'roomnumber':roomnumber,'buildarea':buildarea,'innerarea':innerarea,'familyCode':mcode,'familyName':mname,'hstatus':hstatus};
			    	    sj_arr.push(sj);
			    
			          }
			        //数组转换为对象
			    	var sj_obj={};
			    	var sj_build =0;
			    	var sj_inner =0;
					for(var j=0;j<sj_arr.length;j++){
			    		sj_obj[j]=sj_arr[j];
			    		 sj_build += Number(sj_obj[j].buildarea);
			    		 sj_inner += Number(sj_obj[j].innerarea);
			    		
			    	}if(sj_build!=mr_buildarea){
			    		js.showMessage("拆分后租赁面积和产证面积之和必须等于拆分前面积!","","warning",3000);
			    		return false;
		    		}else if(sj_inner!=mr_innerarea){
		    			js.showMessage("拆分后租赁面积和产证面积之和必须等于拆分前面积!","","warning",3000);
		    			return false;
		    		}
					
			 	var queryData={'houseCode':pk1,'houseList':sj_obj};
					 $.ajax({
						type : "post",
						url : "/js/a/zl/zlHousesource/split",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						dataType : 'json',
				   	//  data : JSON.stringify(queryData),
						data : queryData,
						async : false,
						success : function(data) {
							if(data.result=="true"){
								js.showMessage(data.message,'','success',3000);
								$('#dataGrid').dataGrid('reloadGrid');
							}else{
								js.showMessage(data.message,'','error',3000);
							}
						},
					    error : function () {
					    	alert("请求失败！");
					    }
							
					});
		  }, 
		  btn2 : function(){
			  
		  }
		  });
	
	
}
function merge() {//合并
	
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}else if(selectRows.length<=1){
		js.showMessage('至少选择两条数据!','','info',3000);
		return ;
	}else{
		for(var i=0;i<selectRows.length;i++){
			var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
			if(dataselect.housestate=="已租"){
				js.showMessage('已租房源不可以合并!','','warning',3000);
				return ;
			}
		}
		var pks = selectRows.join('-');
		var queryData={'pks':pks};
		var flag = false;
		//校验有没有被合同引用
		$.ajax({
			type:"post",
			url: '/js/a/zl/zlHousesource/mergeData',
			datatype: 'json',
			data: queryData,
			async:false,
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success: function (msg) {
				if(msg!="merge"){
					var msg1=$.parseJSON(msg);
					var pk1=msg1.data;
					for(var i=0;i<selectRows.length;i++){
						var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
						if(selectRows[i]==pk1){
							js.showMessage(dataselect.estatename+msg1.message,'','warning',3000);
							flag = true;
							return ;
						}
					}
				}
			},
			error: function (msg){
				console.log($.parseJSON(msg.responseText).message);
			}
		});
	}
	if(flag){
		return;
	}
	var selectData=[]; 
	var buildsum =0;
	var innersum =0;
	var project_arr=[];
	var build_arr=[];
	var zst1=[];
	var pks1=[];
	for(var i=0;i<selectRows.length;i++){
		var pk1=selectRows[0];
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		selectData.push(dataselect);
		var projectfile = selectData[i]["projectname.name"];
		var buildfile = selectData[i]["bildname.name"];
		project_arr.push(projectfile);
		build_arr.push(buildfile);
		buildsum += Number(selectData[i]["buildarea"]);
		innersum += Number(selectData[i]["innerarea"]);
		zst1.push($('#dataGrid').getRowData(selectRows[i]).roomnumber);
		//如果主实体房号为空 则塞主键
	/*	if(dataselect.zstpk!=null&&dataselect.zstpk!=""){
			pks1.push(dataselect.zstpk);
		}
		pks1.push(selectRows[i]);*/
		//排序
		if(dataselect.zstpk==null||dataselect.zstpk==""){
			pks1.push(selectRows[i]);
		}else{
			pks1.push(dataselect.zstpk);
			pks1.push(selectRows[i]);
		}
			
	}
	
	var zst=zst1.join('-');
	var pkz=pks1.join('-');
	for(var j =0;j<project_arr.length;j++){
		var project =project_arr[0];
		var project1=project_arr[j];
		if(project!=project1){
			js.showMessage('项目类型不同，不允许合并!','','warning',3000);
			return  ;
		}
	}
	for(var i =0;i<build_arr.length;i++){
		var build =build_arr[0];
		var build1=build_arr[i];
		if(build!=build1){
			js.showMessage('楼栋不同，不允许合并!','','warning',3000);
			return  ;
		}
	}
	var builds=String(buildsum);
	var inners=String(innersum);
	console.log(selectData);

	
	layer.open({//展开layer弹出框
		  type: 2,
		  shade: 0,
		  title: "房源合并",
		  area: ['1200px', '400px'],
		  btn: ['确定','关闭'],
		  content: ['/js/a/zl/zlHousesource/mergeHouseSelect?pk='+pk1+'&bsum='+builds+'&isum='+inners+'&zst='+zst, 'yes'],//加载用户信息并过滤
		  yes : function(index, layero){
			    var data2=$('#dataGrid').getRowData(selectRows[0]);//获取行数据
			    var data=$(layero).find("iframe").contents().find("#dataGrid");//弹出框内行数据
			    var rowId=data.dataGrid('getSelectRow');
				var select = data.dataGrid('getRowData',rowId); // 获取选择行	
			  
				var pname=selectData[0]["projectname.name"];
				var pcode=selectData[0]["projectname.pkProject"];
				var bname=selectData[0]["buildname.name"];
				//var bunit=selectData[0]["unit"];
				//var bfloorn=selectData[0]["floorn"];
				var bestatecode=selectData[0]["estatecode"];
				var bestatename=selectData[0]["estatename"];
				var bcode=selectData[0]["buildname.pkBuildingfile"];
				var fcode=selectData[0]["pkFormattype.pkFormattype"];
				var fname=selectData[0]["pkFormattype.name"];
				var zstroom=selectData[0]["zstroom"];
				var hbcfs=select.hbcfstatus;
				var parentr=select.parentroom;
				var ocode=selectData[0]["pkOrg.officeCode"];
				var oname=selectData[0]["pkOrg.officeName"];
				var hstatus=selectData[0]["housestate"];
				var buildarea=selectData[0]["buildarea"];
				var innerarea=selectData[0]["innerarea"];
				

		    	var sj_arr=[];
			    for(var i=1;i<99;i++){
			    var roomnumber_id=i+"_roomnumber"
			    	var roomnumber_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(roomnumber_id);
			    	if(roomnumber_sj==null){
			    		break;
			    	}
			    	var unit_id=i+"_unit"
			    	var unit_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(unit_id);
			    	var floorn_id=i+"_floorn"
			    	var floorn_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(floorn_id);
			    	var family_code_id="pkFamilyfile_"+i+"_pkFamilyfileCode"
			    	var family_code_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(family_code_id);
			    	var family_name_id="pkFamilyfile_"+i+"_pkFamilyfileName"
			    	var family_name_sj = $(layero).find("iframe")[0].contentWindow.document.getElementById(family_name_id);
			    	
			    	var roomnumber=roomnumber_sj.value;
			    	var bunit=unit_sj.value;
			    	var bfloorn=floorn_sj.value;
			    	var familyCode=family_code_sj.value;
			    	var familyName=family_name_sj.value;
			    	//必输项与校验
			    	if(roomnumber==""){
			    		js.showMessage("房间号不可以为空!","","warning",3000);
			    		return false;
			    	}
			    	
			    	//将数据放进数组
			    	var sj = {'orgCode':ocode,'orgName':oname,'projectCode':pcode,'projectName':pname,'buildCode':bcode,'buildName':bname,'estatecode':bestatecode,'estatename':bestatename,'formatCode':fcode,'formatName':fname,'hbcfstatus':hbcfs,'parentroom':parentr,'zstroom':zstroom,'unit':bunit,'floorn':bfloorn,'roomnumber':roomnumber,'buildarea':builds,'innerarea':inners,'familyCode':familyCode,'familyName':familyName,'hstatus':hstatus,'zstpk':pkz};
			    	    sj_arr.push(sj);
			    
			    }
			        //数组转换为对象
			    	var sj_obj={};
					for(var j=0;j<sj_arr.length;j++){
			    		sj_obj[j]=sj_arr[j];
			    	}
					
				 	var pk_arr={};
				 	for(var i=0;i<selectRows.length;i++){
				 		pk_arr[i] = selectRows[i];
				 	}
			 	var queryData={'housecode':pk_arr,'houseList':sj_obj};
					 $.ajax({
						type : "post",
						url : "/js/a/zl/zlHousesource/merge",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						dataType : 'json',
				   	//  data : JSON.stringify(queryData),
						data : queryData,
						async : false,
						success : function(data) {
							if(data.result=="true"){
								js.showMessage(data.message,'','success',3000);
								$('#dataGrid').dataGrid('reloadGrid');
							}else{
								js.showMessage(data.message,'','error',3000);
							}
						},
					    error : function () {
					    	js.showMessage("编码或房号不符合唯一性，请重新填写!",'','error',3000);
					    }
							
					});
		  }, 
		  btn2 : function(){
			  
		  }
		  });
	
	
}
/**删除**/
function delData(){
	
	var rowId = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行
	if(rowId==null||rowId.length<=0){
		js.showMessage("请选择一条数据删除！",null,'warning');
		return;
	}
	var pks = rowId.join('-');
	var queryData={'pks':pks};
	var index = js.layer.load();
/*	js.layer.open({
		type:3,
	});*/
	$.ajax({
		type:"post",
		url: '/js/a/zl/zlHousesource/deleteData',
		datatype: 'json',
		data: queryData,
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		success: function (msg) {
			if(msg!=null){
				//layer.closeAll();
				js.layer.close(index);
				js.showMessage(JSON.parse(msg).message);
				$('#dataGrid').dataGrid('reloadGrid');//刷新当前列表数据区域
			}
		},
		error: function (msg){
			console.log($.parseJSON(msg.responseText).message);
		}
	});
}
//获取url
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = (window.location.hash || window.location.search).substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

$("#btnSubmit1").click(function(){
    setHtmlView2();
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
/**修改**/
function c_editBtn2(url){
	var rowId = $('#dataGrid').dataGrid('getSelectRows'); // 获取选择行Id
	if(rowId==null||rowId.length>1){
		js.showMessage("请选择一条数据修改！",null,'warning');
		return;
	}else{
		row=rowId[0];
	}
	// /js/a/bd/bdHttype/bianji?pkHttype
	var url_list = url.split("?");
	var JSONDATA={};
	JSONDATA[url_list[1]]=row;
	JSONDATA['isEdit']=true;
	var url_list1=url+"="+row+"&isEdit=false"
	var url_list2=url+"="+row+"&isEdit=true"
    var newre = url_list1.replace("bianji","form");
    var newr = url_list2.replace("bianji","xgform");
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
function bianji33(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("bianji","form");
     var newre1 = newre.replace("true","false");
     var newr = url.replace("bianji","xgform");
	var JSONDATA={'pkHousesource':pk,'isNewRecord':flag};
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
				if(msg=="isXj"){
					js.addTabPage($("#edit"), "修改", newre1, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}
$("#projectnameButton,#projectnameName").unbind();
$("#projectnameButton,#projectnameName").click(function(){
	if ($("#projectnameButton").hasClass("disabled")){
		return true;
	}
	var options = {
		type: 2,
		maxmin: true,
		shadeClose: true,
		title: '项目选择',
		area: ['300px', '400px'],
		content: '/js/tags/treeselect',
		contentFormData: {
			url: $('#projectnameDiv').attr('data-url'),
			checkbox: 'true',
			chkboxType: "",
			expandLevel: '-1',
			selectCodes: $("#projectnameCode").val(),
			isReturnValue: 'false'
		},
		success: function(layero, index){
			if ($(js.layer.window).width() < 300
				|| $(js.layer.window).height() < 400){
				js.layer.full(index);
			}
		},
		btn: ['<i class="fa fa-check"></i> 确定'],
		btn1: function(index, layero){
			var win = layero.iframeWindow();
			win.$('#keyword').val('').change(); 
			var codes = [], names = [], nodes ,nodes1,nodes2;
			if ("true" == "true"){
				nodes1 = win.tree.getCheckedNodes(true);
				nodes2 = win.tree.getSelectedNodes();
				nodes=nodes1.concat(nodes2);
			}else{
				nodes = win.tree.getSelectedNodes();
			}
			for(var i=0; i<nodes.length; i++) {
				var code = nodes[i]['false'=='true'?'value':'id'], name = nodes[i]['name'];
				codes.push(String(code).replace(/^u_/g,''));
				names.push(String(name).replace(/\([0-9]*\)/g,''));
			}
			if(typeof treeselectCheck == 'function'){
				if (!treeselectCheck('projectname', nodes)){
					return false;
				}
			}
			$("#projectnameCode").val(codes.join(',')).change();
			$("#projectnameName").val(names.join(',')).change();
			try{$('#projectnameCode,#projectnameName').resetValid();}catch(e){}
			if(typeof treeselectCallback == 'function'){
				treeselectCallback('projectname', 'ok', index, layero, nodes);
			}
		}
	};
	options.btn.push('<i class="fa fa-eraser"></i> 清除');
	options['btn'+options.btn.length] = function(index, layero){
		$("#projectnameCode").val('').change();
		$("#projectnameName").val('').change();
		try{$('#projectnameCode,#projectnameName').resetValid();}catch(e){}
		if(typeof treeselectCallback == 'function'){
			treeselectCallback('projectname', 'clear', index, layero);
		}
	};
	options.btn.push('<i class="fa fa-close"></i> 关闭');
	options['btn'+options.btn.length] = function(index, layero){
		if(typeof treeselectCallback == 'function'){
			treeselectCallback('projectname', 'cancel', index, layero);
		}
	};
	options.btn.push('<i class="fa fa-eraser"></i> 全选');
	options['btn'+options.btn.length] = function(index, layero){
		
		$(layero).find("iframe").contents().find("#tree").find('[treenode_check]').click();
		//$(layero).find("iframe").contents().find("#tree").find('[treenode_check]').attr('class','button chk checkbox_true_full');
		//$(layero).find("iframe").contents().find("#tree").find('[treenode_a]').removeClass('curSelectedNode').addClass('curSelectedNode');
		/*var win = layero.iframeWindow();
		var nodes = win.tree.getCheckedNodes(true);
		var codes = [], names = [];
		for(var i=0; i<nodes.length; i++) {
			var code = nodes[i]['false'=='true'?'value':'id'], name = nodes[i]['name'];
			codes.push(String(code).replace(/^u_/g,''));
			names.push(String(name).replace(/\([0-9]*\)/g,''));
		}
		$("#projectnameCode").val(codes.join(',')).change();
		$("#projectnameName").val(names.join(',')).change();*/
		return false;
	};
	/*options.btn.push('<i class="fa fa-eraser"></i> 全消');
	options['btn'+options.btn.length] = function(index, layero){
		
		$(layero).find("iframe").contents().find("#tree").find('[treenode_check]').click();
		//$(layero).find("iframe").contents().find("#tree").find('[treenode_check]').attr('class','button chk checkbox_true_full');
		//$(layero).find("iframe").contents().find("#tree").find('[treenode_a]').removeClass('curSelectedNode').addClass('curSelectedNode');
		var win = layero.iframeWindow();
		var nodes = win.tree.getCheckedNodes(true);
		var codes = [], names = [];
		for(var i=0; i<nodes.length; i++) {
			var code = nodes[i]['false'=='true'?'value':'id'], name = nodes[i]['name'];
			codes.push(String(code).replace(/^u_/g,''));
			names.push(String(name).replace(/\([0-9]*\)/g,''));
		}
		$("#projectnameCode").val(codes.join(',')).change();
		$("#projectnameName").val(names.join(',')).change();
		return false;
	};*/
	js.layer.open(options);
});
//版本查看
function btnCk(){
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}else if(selectRows.length>1){
		js.showMessage('请选择一条数据!','','info',3000);
		return ;
	}
	for(var i=0;i<selectRows.length;i++){
		var pk1=selectRows[i];
	}
	//多窗口模式，层叠置顶
    js.layer.open({//前面要加js.否则参照弹出异常
    	id:'bodydetail',
    	type: 2, //此处以iframe举例
    	title: '房源版本信息',
    	area: [width,height],
    	shade: 0.2,
    	content: 'zl/zlHousesource/houseBodyList?pkHousesource='+pk1,//+'&isCk=true'
    	zIndex: layer.zIndex,
    	end: function(){
    		//无论保不保存，弹框关闭就刷新列表
    		$('#dataGrid').dataGrid('reloadGrid');
    	},
    });
}
var width=$(top.window).width()*7/10+"px";
var height=$(top.window).height()*3/5+"px";

$('#splitHouseChildDataGridAddRowBtn').click(function (){

	var rowData=$('#dataGrid').dataGrid('getRowData','2');//获取行数组id
	var selectRow1=$('#dataGrid').dataGrid('getSelectRow');//获取行数组id
		var id=$(this).attr("id");
	
});
