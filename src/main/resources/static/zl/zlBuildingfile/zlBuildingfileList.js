$(function() {
	InitQuery();
	$('#dataGrid').dataGrid('refresh');
	var toRowData = {};
	$("#zuling").click(function(){
    	var rowId = $('#dataGrid').dataGrid('getSelectRow'); // 获取选择行
    	var rowData = $('#dataGrid').dataGrid('getRowData', rowId); // 获取行数据
    	
    	if($.isEmptyObject(rowData)){
    		js.alert('请选择楼栋数据！', {icon: 2});
    		return
    	}
    	//console.log(rowData);
    	if(rowData['isbuild']=='是'){
    		js.alert('该楼栋已建房！', {icon: 2});
    		return
    	}
    	//获取当前选中的行数据
        toRowData = {
    		Pro:rowData['pkProjectid.pkProject'],
    		Build:rowData['pkBuildingfile'],
			Lou:Number(rowData['nbuilding']),
			Dy: Number(rowData['nunit']),
			// 项目、楼栋
		};
        showMeSomething(toRowData);
        //div需要隐藏
        $(".child1").removeClass('hide');
        $(".child2").addClass('hide');
        $(".child3").addClass('hide');
        $(".child4").addClass('hide');
    	$('#childGuide').removeClass("hide");
		$(".pro1").val(rowData['pkProjectid.name']);
		$(".build1").val(rowData.name);
		$(".pro2").val(rowData['pkProjectid.name']);
		$(".build2").val(rowData.name);
        $('#childGuide').removeClass("hide");
        layer.open({
          type: 1,
          title: '<span>快速建房</span>',
          maxmin: true,
          shade:0,
          //shadeClose: false,
          area: ['870px', '450px'],
          content: $('#childGuide'),
          end: function () {
              console.log("关闭了layer")
              $('#childGuide').addClass("hide");
          }
        });
    });
 // 快速建房4-9
    function showMeSomething(RowData){
    	//Lou:7,Dy:4,
    	var tr = '';
    	for(var some=1;some<RowData.Dy+1;some++){
    		tr += '<tr><td><input type="text" value="'+some+'单元"></td>'+
	            '<td><input type="text" value="2"></td></tr>';
    	}
    	$("#dy_xinxi").html(tr);
    	var tr2 = '';
    	for(var some=1;some<RowData.Dy+1;some++){
    		tr2 += '<tr><td><input type="text" value="'+some+'单元"></td>'+
	            '<td><input type="text" value="60"></td></tr>';
    	}
    	$("#lou_xinxi").html(tr2);
    }

    $(".tips_btn").click(function () {
        var dataLei = $(this).attr("data-lei");
        console.log(dataLei)
        // 给其他的table隐藏
        $(".onePageTable").hide();
        // 给对应的table展示
        $("." + dataLei).show();
        // 给自己加上 hove 样式，并清空同级样式
        $(this).addClass("hove").siblings().removeClass("hove");

    });
    // 下一步
    $("#toSecond").click(function(){
        $(".child1").addClass('hide');
        $(".child2").removeClass('hide');
         // 获取真实数据
        var danyuantr = $("#dy_xinxi").find('tr');
        //shuju
        var shuju_arr = [];
        for(var s=0;s<danyuantr.length;s++){
            var danyuanname = danyuantr.eq(s).find('input').eq(0).val();
            var hushu = danyuantr.eq(s).find('input').eq(1).val();
            var obj = {
                danyuanname: danyuanname,
                hushu: hushu
            }
            shuju_arr.push(obj);
        }
        var danyuantr2 = $("#lou_xinxi").find('tr');
        //shuju
        var mianji = {};
        for(var c=0;c<danyuantr2.length;c++){
            var danyuanname2 = danyuantr2.eq(c).find('input').eq(0).val();
            //console.log(danyuanname2)
            var mianji2 = danyuantr2.eq(c).find('input').eq(1).val();
            mianji[danyuanname2] = mianji2;
        }
        
        var moni_obj = {
            louceng: toRowData.Lou,
            huxing:shuju_arr,
            space:mianji
        }
        jianfan_obj = moni_obj;// 4.8将数据保存到全局变量上，后面双击时有操作
        // 生成thead
        var thead1 = '<tr><th rowspan="2">楼层</th>';
        var thead2 = '<tr>'; // 序号 行
        var zonghushu = 0;
        for(var dy=0; dy<moni_obj.huxing.length; dy++){
            thead1 += '<th colspan="'+moni_obj.huxing[dy].hushu+'">'+moni_obj.huxing[dy].danyuanname+'</th>';
            zonghushu += Number(moni_obj.huxing[dy].hushu);
        }
        thead1 += '</tr>';
        for(var a = 0;a<zonghushu;a++){
            thead2 +=  '<th>序号</th>';
        }
        thead2 += '</tr>';
        var thead_tr = thead1 + thead2;
        $("#t_thead").html(thead_tr);

        // body的房间号
        var tbody_tr = '';
        var t_body = [];
        for(var b=0;b<moni_obj.huxing.length;b++){
            t_body.push(moni_obj.huxing[b]);
        }
        //console.log(t_body)
        //console.log(moni_obj)
        // 通过循环赋值
        for(var t=moni_obj.louceng;t>0;t--){
            tbody_tr += '<tr><td>'+t+'</td>';
            for(var b=0;b<t_body.length;b++){
                var num = Number(t_body[b].hushu); // 3 --> 801 802 803
                for(var n=0;n<num;n++){
                    var value = t*100+n+1;
                    // 判断面积大小
                    tbody_tr += '<td><input type="text" data-dy="'+t_body[b].danyuanname+'" value="'+value+'"></td>';
                }
            }
            tbody_tr += '</tr>';
        }
        $("#t_tbody").html(tbody_tr);
    });
    
    //toThree 第二步往第三步
    $("#toThree").click(function(){
    	$(".child2").addClass('hide');
        $(".child3").removeClass('hide');
        console.log(jianfan_obj);
        
        var thead1 = '<tr><th>楼层</th><th class="width_50">户型</th>';
        var zonghushu = 0;
        for(var dy=0; dy<jianfan_obj.huxing.length; dy++){
            thead1 += '<th colspan="'+jianfan_obj.huxing[dy].hushu+'">'+jianfan_obj.huxing[dy].danyuanname+'</th>';
            zonghushu += Number(jianfan_obj.huxing[dy].hushu);
        }
        thead1 += '</tr>';
        $("#t_thead3").html(thead1);

        // body的房间号
        var tbody_tr = '';
        var huxing = '';//户型行
        var t_body = [];
        for(var b=0;b<jianfan_obj.huxing.length;b++){
            t_body.push(jianfan_obj.huxing[b]);
        }
        //console.log(t_body)
        //console.log(moni_obj)
        // 通过循环赋值
        huxing += '<tr><td class="bc_gray"></td><td class="huxing" data-lx="huxing_all"><span class="cw_color"></span></td>';
        for(var t=jianfan_obj.louceng;t>0;t--){
            tbody_tr += '<tr><td>'+t+'</td><td class="huxing" data-lx="huxing_hang"><span class="cw_color"></span></td>';
            for(var b=0;b<t_body.length;b++){
            	var num = Number(t_body[b].hushu); // 3 --> 801 802 803
                for(var n=0;n<num;n++){
                    var value = t*100+n+1;
                    // 判断面积大小
                    tbody_tr += '<td class="huxing huxing_input" data-dy="'+t_body[b].danyuanname+'" data-fh="'+value+'" title="原生成：'+value+'"><span class="cw_color"></span></td>';
                }
            }
            tbody_tr += '</tr>';
        }
        var num = Number(t_body[0].hushu);
        for(let c=0;c<t_body.length;c++){
        	var num = Number(t_body[c].hushu); // 3 --> 801 802 803
            for(let n=0;n<num;n++){
                huxing += '<td class="huxing" data-lx="huxing_lie"><span class="cw_color"></span></td>';
            }
        }
        huxing += '</tr>';
        $("#t_tbody3").html(huxing + tbody_tr);
    });
    
    //触发区分单元格面积
    $("#t_tbody").on('dblclick','input',function(){
        //console.log($(this).html());
        var data_dy = $(this).attr('data-dy');
        var mianji = jianfan_obj.space;
        var dataDy = mianji[data_dy];
        alert('当前双击的是：'+$(this).attr('data-dy')+$(this).val()+'，有'+dataDy+'面积');
    });
    
    $("#toFirst").click(function(){
    	js.confirm('是否确定返回上一步，此页面数据会被清空',function(){
    		$(".child1").removeClass('hide');
            $(".child2").addClass('hide');
    	});
    });
    $("#backSecond").click(function(){
    	js.confirm('是否确定返回上一步，此页面数据会被清空',function(){
    		$(".child2").removeClass('hide');
    		$(".child3").addClass('hide');
    	});
    });

    //户型操作
    $("#t_tbody3").on("mousedown",".cw_color", function(){
    	console.log("单击td");
    	let inx = $(this).parent().index();
    	let that = this;
    	// 当前行变颜色，点击的自身加边框
    	$(that).parent().parent().addClass("tr_active").siblings().removeClass("tr_active");
    	$(that).parent().parent().siblings().find("td").removeClass("td_active td_bg");
    	$(that).parent().addClass("td_active").siblings().removeClass("td_active td_bg");
    	for(let y=0;y<$("#t_tbody3").find("tr").length;y++){
    		$("#t_tbody3").find("tr").eq(y).find("td").eq(inx).addClass("td_bg");
    	}
    });
    $("#t_tbody3").on("dblclick",".huxing", function(){
    	console.log("我点了全部");
    	var that = this;
    	var pkProjectid = toRowData["Pro"];
    	console.log(pkProjectid);
    	// 弹出一个
    	js.layer.open({
    		type: 2,
    		maxmin: true,
    		shadeClose: true,
    		title: '户型选择(根据项目过滤)',
    		area: ['600px', '400px'],
    		content: "/js/a/zl/zlFamilyfile/familySelect2?pkProjectid="+pkProjectid,
    		success: function(layero, index){
    			
    		},
    		btn: ['<i class="fa fa-check"></i> 确定'],
    		btn1: function(index, layero){
    			var win = layero.iframeWindow();
    			var selectData = win.getSelectData();
    			console.log(selectData);
    			
    			let code = "";
    			let name = "";
    			for(var k in selectData){
    				code = selectData[k]["pkFamilyfile"];
    				name = selectData[k]["name"];
    			}
    			$(that).attr("data-code", code);
    			$(that).attr("data-name", name);
    			$(that).find("span").html(name);
    			
    			//区分点的位置
    			let lx = $(that).attr("data-lx");
    			if(lx == "huxing_all"){// 全部
    				$("#t_tbody3").find("td").attr("data-code", code);
    				$("#t_tbody3").find("td").attr("data-name", name);
    				$("#t_tbody3").find("span").html(name);
    			}else if(lx == "huxing_hang"){// 一行
    				$(that).parent().find("td").attr("data-code", code);
    				$(that).parent().find("td").attr("data-name", name);
    				$(that).parent().find("span").html(name);
    			}else if(lx == "huxing_lie"){// 一列
    				let xb = $(that).index();
    				console.log(xb);
    				let t3_tr = $("#t_tbody3").find("tr");
    				for(let d=0;d< t3_tr.length; d++){
    					t3_tr.eq(d).find("td").eq(xb).find("td").attr("data-code", code);
    					t3_tr.eq(d).find("td").eq(xb).find("td").attr("data-name", name);
    					t3_tr.eq(d).find("td").eq(xb).find("span").html(name);
    				}
    			}else{
    				// 单点
    			}
    		}
    	});
    });

    
  //点击保存按钮数据传入后台
    $("#toSql").click(function(){
    	let t_hux = $("#t_tbody3").find(".huxing_input");
    	for(let p=0;p< t_hux.length; p++){
    		if(t_hux.eq(p).attr("data-name") == undefined){
    			js.showMessage("请维护好完整户型！","","error");
    			return
    		}
    	}
    	//console.log(jianfan_obj);
    	var sql_arr = [];
    	//遍历t_tbody
    	var input_arr = $("#t_tbody").find('input');
    	var huxing_arr = $("#t_tbody3").find('td');
    	var mianji_obj = jianfan_obj.space;// 面积对应
    	for(var r=0; r < input_arr.length; r++){
    		var dy = $(input_arr).eq(r).attr('data-dy');
    		var fh = $(input_arr).eq(r).val();
    		var lc = fh.slice(0,-2); // 注：如801，取8就是楼层
    		var mj = mianji_obj[dy]; // 获取面积
    		var hx = $(huxing_arr).eq(r).attr('data-code');
    		sql_arr.push([dy.replace('单元',''), fh, lc, mj,toRowData.Pro,toRowData.Build,hx]);// 默认要塞入项目pk和楼栋pk
    	}
    	//console.log(sql_arr)
    	console.log(sql_arr.sort());//排序后
    	//var JSONDATA={arr2:arr};
    	return;//测试
    	var index = js.layer.load();
		$.ajax({
	        url: '/js/a/zl/zlHousesource/build',//请求后台加载数据的方法
	        data: JSON.stringify(sql_arr),
	        type: 'post',
	        datatype: 'json',
	        contentType: 'application/json;charset=utf-8',
	        success: function (data) {
	        	js.layer.close(index);
	        	if(data.split(",")[0].split(":")[1]=true){
		        	$('.layui-layer-close').click();
		        	js.showMessage("建房成功！");
		        	$('#dataGrid').dataGrid('refresh');
	        	}
	        }
	    })
    });
    
});

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
function bianji33(dom, flag){
	console.log($(dom).attr("data-url"));
	let url = $(dom).attr("data-url");
	var pk=url.substring(url.indexOf("=")+1,url.indexOf("&"));
	 var newurl = url.slice(0,url.lastIndexOf('?'))
     var newre = url.replace("bianji","form");
     var newr = url.replace("bianji","xgform");
	var JSONDATA={'pkBuildingfile':pk,'isNewRecord':flag};
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
					js.addTabPage($("#edit"), "修改", newre, null, null);//弹出新的标签页
				}else{
					js.addTabPage($("#edit"), "修改", newr, null, null);//弹出新的标签页
				}
			}
		}
	});
}