$(function(){
	setChart1();
	setChart2();
	setChart3();
});

function setChart1() {
	var myChart = echarts.init(document.getElementById('barShow'));
    myChart.clear();
	// 基于准备好的dom，初始化echarts图表
    option = {
    	    tooltip : {
    	        trigger: 'axis'
    	    },
    	    legend: {//分类标签
    	        data:['蒸发量','降水量']
    	    },
    	    grid: {//数据区域大小
		        left: '3%',
		        right: '8%',
		        bottom: '3%',
		        containLabel: true	//包含标签
		    },
    	    toolbox: {//工具条
    	        show : true,
    	        feature : {
    	            dataView : {show: true, readOnly: false},	//展示数据列表
    	            magicType : {show: true, type: ['line']},	//展示可切换展示类型：line折线，bar柱状
    	            restore : {show: true},	//还原
    	            saveAsImage : {show: true}	//保存为图
    	        }
    	    },
    	    calculable : true,
    	    xAxis : [
    	        {
    	            type : 'category',
    	            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
    	        }
    	    ],
    	    yAxis : [
    	        {
    	            type : 'value'
    	        }
    	    ],
    	    series : [
    	        {
    	            name:'蒸发量',
    	            type:'bar',
    	            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
    	            markPoint : {
    	                data : [
    	                    {type : 'max', name: '最大值'},
    	                    {type : 'min', name: '最小值'}
    	                ]
    	            },
    	            markLine : {
    	                data : [
    	                    {type : 'average', name: '平均值'}
    	                ]
    	            }
    	        },
    	        {
    	            name:'降水量',
    	            type:'bar',
    	            data:[2.6, 5.9, 9.0, 26.4, 70, 110, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
    	            markPoint : {
    	                data : [
    	                    {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183},
    	                    {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
    	                ]
    	            },
    	            markLine : {
    	                data : [
    	                    {type : 'average', name : '平均值'}
    	                ]
    	            }
    	        }
    	    ]
    	};
    //使用制定的配置项和数据显示图表
    myChart.setOption(option);
};
function setChart2() {
	var myChart = echarts.init(document.getElementById('lineShow'));
    myChart.clear();
	// 基于准备好的dom，初始化echarts图表
	option = {
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
		    },
		    grid: {
		        left: '3%',
		        right: '8%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		        	dataView : {show: true, readOnly: true},
    	            magicType : {show: true, type: ['bar']},
    	            restore : {show: true},
		            saveAsImage: {show: true}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: ['周一','周二','周三','周四','周五','周六','周日']
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name:'邮件营销',
		            type:'line',
		            stack: '总量',
		            data:[120, 132, 101, 134, 90, 230, 210]
		        },
		        {
		            name:'联盟广告',
		            type:'line',
		            stack: '总量',
		            data:[220, 182, 191, 234, 290, 330, 310]
		        },
		        {
		            name:'视频广告',
		            type:'line',
		            stack: '总量',
		            data:[150, 232, 201, 154, 190, 330, 410]
		        },
		        {
		            name:'直接访问',
		            type:'line',
		            stack: '总量',
		            data:[320, 332, 301, 334, 390, 330, 320]
		        },
		        {
		            name:'搜索引擎',
		            type:'line',
		            stack: '总量',
		            data:[820, 932, 901, 934, 1290, 1330, 1320]
		        }
		    ]
		};
		//使用制定的配置项和数据显示图表
	    myChart.setOption(option);
}
function setChart3() {
	var myChart = echarts.init(document.getElementById('pieShow'));
    myChart.clear();
	// 基于准备好的dom，初始化echarts图表
    var data = genData(10);
    option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {	//标签属性
            type: 'scroll',
            orient: 'vertical',
            right: 10,
            top: 20,
            bottom: 20,
            data: data.legendData,	//所有标签
            selected: data.selected	//被选中标签
        },
        series : [	//展示数据属性
            {
                name: '详情',	//悬浮框标题
                type: 'pie',
                radius : '80%',	//大小
                center: ['30%', '50%'],	//位置
                data: data.seriesData,	//展示数据
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,	//阴影扩散效果
                        shadowOffsetX: 20,	//阴影偏移量
                        shadowColor: 'rgba(0, 0, 0, 0.5)'	//阴影颜色透明度
                    },
                    //color: '#c23531'	//设置每块不同的颜色
                }
            }
        ]
    };
    function genData(count) {
        var nameList = [
            '赵', '钱', '孙', '李', '周', '吴', '郑', '王', '冯', '陈', '褚', '卫', '蒋', 
            '沈', '韩', '杨', '朱', '秦', '尤', '许', '何', '吕', '施', '张', '孔', '曹', 
            '严', '华', '金', '魏', '陶', '姜', '戚', '谢', '邹', '喻', '柏', '水', '窦', 
            '章', '云', '苏', '潘', '葛', '奚', '范', '彭', '郎', '鲁', '韦', '昌', '马', 
            '苗', '凤', '花', '方', '俞', '任', '袁', '柳', '酆', '鲍', '史', '唐', '费'
        ];
        var legendData = [];
        var seriesData = [];
        var selected = {};
        for (var i = 0; i < count; i++) {
            name = Math.random() > 0.75 ? makeWord(4, 0) : makeWord(3, 0);
            legendData.push(name);
            //数据区域塞值
            seriesData.push({
                name: name,
                value: Math.round(Math.random() * 100000)
            });
            selected[name] = i < 6;
        }

        return {
            legendData: legendData,
            seriesData: seriesData,
            selected: selected
        };

        function makeWord(max, min) {
            var nameLen = Math.ceil(Math.random() * max + min);
            var name = [];
            for (var i = 0; i < nameLen; i++) {
                name.push(nameList[Math.round(Math.random() * nameList.length - 1)]);
            }
            return name.join('');
        }
    }
	//使用制定的配置项和数据显示图表
	myChart.setOption(option);
}

$('#refresh1').click(function(){
	setChart1();
});
$('#refresh2').click(function(){
	setChart2();
});
$('#refresh3').click(function(){
	setChart3();
});
