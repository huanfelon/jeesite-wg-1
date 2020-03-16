$(document).ready(function() {
	InitQuery();
});
//初始化查询条件
function InitQuery(){
	$(".query-group input").val("");
	$("#pkOrgCode").val(getMainOrgCode());
	$("#pkOrgName").val(getMainOrgName());
}

/**列表参照获取界面选择数据(用作参照前方法，变更参照URL)**/
function listselectGetSelectData(id){
	//参照前过滤
	if(id=="pkBuilding"){
		var pkp=$("#pkProjectCode").val();
		var newurl='/js/a/zl/zlBuildingfile/buildingSelect';
		if(pkp==""){
			newurl+="?pkProjectid=' '"
		}else{
			newurl+="?pkProjectid="+pkp;
		}
		$('#'+id+'Div').attr('data-url',newurl);
	}
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
	if(id=='pkProject' && (act == 'ok' || act == 'clear')){//项目选择
		if(nodes==null||nodes==undefined){
			$('#pkBuildingCode').val(null);
			$('#pkBuildingName').val(null);
			$("#pkProject").val(null);
			$("#projectname").val(null);
			return;
		}
		var oldpro=$("#pkProject").val();
		var newpro=$("#pkProjectCode").val();
		if(oldpro!=newpro){
			$('#pkBuildingCode').val(null);
			$('#pkBuildingName').val(null);
			$("#pkProject").val(newpro);
			$("#projectname").val($("pkProjectName").val());
		}
	}
}

function showData(){
	var count=0;
	var pkOrg=$("#pkOrgCode").val();
	if (pkOrg=="") {
		layer.tips('请输入组织', '#pkOrgDiv',{tips: [2,"#ff0000b5"],tipsMore: true});
		count++;
	}
	var pkPro=$("#pkProjectCode").val();
	if(pkPro==""){
		layer.tips('请输入项目', '#pkProjectDiv',{tips: [2,"#ff0000b5"],tipsMore: true});
		count++;
	}
	var pkBuild=$("#pkBuildingCode").val();
	if(pkBuild==""){
		layer.tips('请输入楼栋', '#pkBuildingDiv',{tips: [2,"#ff0000b5"],tipsMore: true});
		count++;
	}
	if(count>0){
		return;
	}
	var new_fh = {};
	new_fh = {};
	var JSONDATA={};
	JSONDATA['pkOrg']=pkOrg;
	JSONDATA['pkPro']=pkPro;
	JSONDATA['pkBuild']=pkBuild;
	JSONDATA['busidate']=getBusiDate();
	$.ajax({
		url: '/js/a/zl/zlHouseTable/dataShow',
		datatype: 'json',
		data: JSONDATA,
		contentType: 'application/json;charset=utf-8',
		success: function (data) {
			if(data.length==0){
				$(".xk_page").addClass("hide");
				js.alert('该楼栋尚未建房！', {icon: 7});
				return;
			}
			$(".xk_page").removeClass("hide");
			var fh_obj2=[data.length];
			
			for(var i=0;i<data.length;i++){
				fh_obj2[i]={louceng:data[i].FLOORN,danyuan:data[i].UNIT,fangjianhao:data[i].ROOMNUMBER,name:data[i].ESTATENAME,
					mianji:data[i].BUILDAREA,pk_fangjian:data[i].PK_HOUSESOURCE,pk_customer:data[i].CUSTOMER,jfzt:data[i].JFZT};
			}
			
			$.each(fh_obj2, function(i,val){
				var louc_arr = [];
				//默认new_fh中是否有此楼层的键,如果存在,获取new_fh中的数据放到louc_arr中
				if(new_fh[val.louceng]!==undefined){
					louc_arr = new_fh[val.louceng];
				}
				// 不管存不存在都会塞值
				louc_arr.push(val);
				//添加成{13 : [louc_arr],12: []}
				new_fh[val.louceng+''] = louc_arr;
			});
			
			// 获取表头格式要通过colspan来固定单元号，默认取第一条数据[{},{}]，来判断单元
			function find_thead(arr){
				var thead_arr = {};
				for(var t=0;t<arr.length;t++){
					thead_arr[arr[t]['danyuan']] = Number(thead_arr[arr[t]['danyuan']])+1;
				}
				return thead_arr;
			}
			
			var thead2 = '<tr><th>楼层</th>';
			var tbody2 = [];
			var falg = true;
			var keshou = 0;
			// 楼层
			var jv_shangxiahebing = '';// 区别501-601房间的rowspan
			$.each(new_fh,function(i, val){
				if(falg){
					// 为了只执行一次
					var thead2_arr = find_thead(val); // 获取表头
					for(var c in thead2_arr){
						if(c>1){
							var width = (144+thead2_arr[c]+1)*thead2_arr[c];
							thead2 += '<th class="qufen" width="'+width+'" colspan="'+thead2_arr[c]+'"> '+c+' 单元</th>';
						}else{
							var width = (144+thead2_arr[c]+1)*thead2_arr[c];//此赋值会四舍五入
							thead2 += '<th width="'+width+'" colspan="'+thead2_arr[c]+'"> '+c+' 单元</th>';
						}
					}
					falg = false;
				}
				if(jv_shangxiahebing != ''){
					//将501-601 ，5楼的数据塞到6层来，并且赛完后清空
					val.push(jv_shangxiahebing);
					jv_shangxiahebing = '';
				}
				// 区分单元 new_fh是区分了楼层，但是单元没区分导致1201和2201可能存在1单元中
				var new_var = val.sort(function(a,b){return (a['name'] - b['name'])});// 从小到大排序
				var tr = "<tr><td><span>" + val[0].louceng + "层</span></td>";
				
				//测试4-28
				// 区分了单元
				var dy_arr = {};
				for(var y=0;y<val.length;y++){
					if(dy_arr[val[y]['danyuan']]===undefined){
						dy_arr[val[y]['danyuan']] = [val[y]];
					}else{
						dy_arr[val[y]['danyuan']].push(val[y]);
					}
				}
				// 单元
				var tbody_str = [];
				for(var r in dy_arr){
					var new_aa = {};
					// 区分房间
					for(var f=0;f<dy_arr[r].length;f++){
						var name_arr = dy_arr[r][f]['name'].split('-');
						new_aa[dy_arr[r][f]['name']] = [dy_arr[r][f]];
					}
					
					// 少了一个单元房间的排序4-30
					// 大佬操作，可惜没学会
					// 纯数字的要顺序就Object.keys(obj).sort((a, b) => b - a > 0).forEach(k => obj[k])
					// 通过key，重新排序，并把数字转成字符串
					var aa = Object.keys(new_aa).sort(function(a,b){
						return a.split('-')[0] - b.split('-')[0]
					})
					var new_fangjian = {};
					for(var u=0;u<aa.length;u++){
						new_fangjian['a'+aa[u]] =  new_aa[aa[u]];
					}
					// 房间
					var fangjian_str = [];
					var qufen_flag = {};
					for(var g in new_fangjian){
						var colspan = '';
						var rowspan = '';
						var td_class = '';
						// 区分单元样式，border-left加粗
						if(new_fangjian[g][0]['danyuan'] > 1){
							var quden_dayuan = qufen_flag[new_fangjian[g][0]['danyuan']];
							if(quden_dayuan == undefined){
								qufen_flag[new_fangjian[g][0]['danyuan']] = 1;
								td_class = 'qufen';
							}
						}
						
						var td = "<td class='"+td_class+"' rowspan='"+rowspan+"' colspan='"+colspan+"'><div class='cs_flex'>";
						for(var s=0;s<new_fangjian[g].length; s++){
							// 区分房间状态
							var zt_class = '';
							if (new_fangjian[g][s]['jfzt'] == 'N') {
								zt_class = "fjh qjfy";
								keshou++;
							}else {
								zt_class = "fjh yjfy";
							}
							if(rowspan == 2){
								zt_class += " flex_height";
							}
							var danyuange = new_fangjian[g][s]['danyuan']+'-'+new_fangjian[g][s]['louceng']+'-'+new_fangjian[g][s]['name'].split('-')[0].slice(-2);
							td += '<div class="'+zt_class+'" data-dy2="'+new_fangjian[g][s]['danyuan']+
							'" data-dyg="'+danyuange+'" data-zt="'+new_fangjian[g][s]['jfzt']+
							'" data-fhmj="'+new_fangjian[g][s]['mianji']+
							'" data-fhpk="'+new_fangjian[g][s]['pk_fangjian']+
							'" data-custpk="'+new_fangjian[g][s]['pk_customer']+
							'"><p>'+new_fangjian[g][s]['fangjianhao']+'</p></div>';
						}
						td += '</div></td>';
						fangjian_str.push(td);
					}
					tbody_str.push(fangjian_str.join(""));
				}
				tr += tbody_str.join("")
				
				tr +="</tr>";
				tbody2.push(tr);
			});
			//2、房间号总数汇总
			$(".zongTao").html(fh_obj2.length);
			$(".qianjiao").html(keshou);
			thead2 += "</tr>";
			$(".fh_thead").html(thead2);
			$("#fh_tbody").html(tbody2.reverse());
			//4-25 获取table的宽度，赋值给固定的头部div
			var fh_tbody_width = $("#fh_tbody").width();
			$(".gudingTab").width(fh_tbody_width-1);
			var diyige = $(".F_table .fh_thead").find('tr').eq(1).width();
			$(".fh_thead tr").eq(1).width(diyige);
			
		}
	});
}

$('#refresh').click(function(){
	showData();
});

var custPk=undefined;
//监听点击的房间号
$("#fh_tbody").on('click',".fjh",function () {
	$(".fjh").removeClass("selectedRoom")
	$(this).addClass("selectedRoom");
	//获取主键
	var pkCustomer=$(this).attr("data-custpk");
	custPk=pkCustomer;
});

function linkCustomer(){
	if(custPk=='undefined'){
		js.alert('该房间尚未关联住户！', {icon: 7});
		return;
	}
	/**
	 * 添加TAB页面（ class="addTabPage" ）
	 * @param $this 		点击的对象
	 * @param title 		提示标题
	 * @param url	 		访问的路径
	 * @param closeable	 	是否有关闭按钮，关闭页面回调方法：function onTablePageClose(tabId, title){}
	 * @param refresh 		打开后是否刷新重新加载
	 */
	js.addTabPage($("#"+custPk), "查看住户", "/js/a/zl/zlCustomer/form?pkCustomer="+custPk+"&isEdit=false", null, false);//弹出新的标签页
}

//获取当前主组织主键
function getMainOrgCode(){
	var obj=window.parent.document.getElementById('officeCode').innerText;//获取当前登录人主键
	return obj;
}
//获取当前主组织名称
function getMainOrgName(){
	var obj=window.parent.document.getElementById('officeName').innerText;//获取当前登录人名称
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