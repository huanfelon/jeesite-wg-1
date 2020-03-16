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

function checkOrg(){
	var pkOrg=getMainOrgCode();
	if(pkOrg==null||pkOrg==""){
		js.showMessage('请先选择主组织！','','warning',3000);
		return false;
	}
	var href="/js/a/base/bdCustomer/form";
	js.addTabPage($('.cust'),$.trim('客户基本信息'||js.text("tabpanel.newTabPage")),href);
	return true;
}

/**
 * 批改业务员
 */
function batchChangePsn() {
	
	var selectRows=$('#dataGrid').dataGrid('getSelectRows');//获取行数组id
	if(selectRows==null||selectRows.length<=0){
		js.showMessage('请选择数据！','','info',3000);
		return ;
	}
	
	var selectpks=[];//存放单据主键
	var selectData=[]; //存放业务员pk
	for(var i=0;i<selectRows.length;i++){
		var dataselect=$('#dataGrid').getRowData(selectRows[i]);//获取行数据
		selectData.push(dataselect['pkPsndoc.id']);
		selectpks.push(selectRows[i]);
	}
	
	var selDatas = [];
	
	layer.open({//展开layer弹出框
		  type: 2,
		  shade: 0,
		  title: "人员选择",
		  area: ['900px', '650px'],
		  btn: ['确定','关闭'],
		  content: ['/js/a/base/bdPsndoc/bdPsndocSelect', 'yes'],//加载业务员信息并过滤
		  yes : function(index, layero){ //此处用于演示
		  
			 var selectData2 = $(layero).find('iframe')[0].contentWindow.getSelectData();
			 for(var key in selectData2){
				selDatas.push(selectData2[key].id);
			 }
			 
			 if(selDatas.length<=0){
				js.showMessage('未查到用户信息！','','warning',3000);
				return ;
			 }
			 if(selDatas.length>1){
					
				js.showMessage('不可多选！','','warning',3000);
				return ;
			}
			
			 var did=selDatas[0];
			 //校验人员pk是否重复
			 for(var i=0;i<selectData.length;i++){//获取第一条数据的类型和状态与第二条数据比较
				var pk_psnid=selectData[i];
				if(pk_psnid==did){
					js.showMessage('批改前后的业务员不能一致，请重新选择！','','warning',3000);
					return ;
				}
			}
			 
			 var queryData={'pk_psndoc':did,'pks':selectpks};
			 
			 $.ajax({
				//type : "post",
				url : "./batchPGPsndoc",
				contentType: 'application/json;charset=utf-8',
				traditional : true,//数组
				dataType : 'json',
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
