
$("body").delegate('.back', "click", function () {
    window.history.back(-1);
});
$("body").delegate('.back_shouye', "click", function () {
    window.location.href = './index.html';
});


//保存cookie
function setCookie(c_name, value, expiredays, path) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + expiredays);
    document.cookie = c_name + "=" + escape(value) +
        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString()) +
        ((path == null) ? "" : ";path=" + path)
}
// 获取cookie
function getCookie(c_name) {
    var res = null;
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != - 1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == - 1) c_end = document.cookie.length
            res = unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return res;
}


//获取url
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = (window.location.hash || window.location.search).substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}



//写一个申请人弹出框，模仿百度搜索页
function Fuzzyquery(element) {
    //获取人员数据
    var arr = [];
     var requestData = { content: { } };
     $.ajax({
         type: "post",
         url: "/mobileCarApp/carapply/getAllPsndocEx",
         contentType: "application/json;charset=UTF-8",
         dataType: 'json',
         data: JSON.stringify(requestData),
         async: false,
         success: function (data) {
             console.log(data);
             arr = data.content;
         }
     });
     console.log(arr);
    //获取name值
    var renyuan_arr_str = [];
    for (var j = 0; j < arr.length; j++) {
        var name = arr[j]["name"];
        renyuan_arr_str.push(name);
    }
    //做筛选
    function shaixuan(renyuan_arr_str, input) {
        //获取下标
        var xiabiao = [];
        //在开始的时候就要判断中文
        if (/^[\u4e00-\u9fa5]{0,}$/.test(input) == true) {
            for (var r = 0; r < renyuan_arr_str.length; r++) {
                if (renyuan_arr_str[r].indexOf(input) != -1) {
                    xiabiao.push(r);
                }
            }
        } else {
            var input_val = input.toUpperCase();
            //获取input_val的长度
            var leng_zt = input_val.split("'").length;

            //获取字母
            var renyuan_SZM = [];
            if (leng_zt > 1) {
                //用的是简拼
                var leng = input_val.split("'").join("").length;
                if (input_val.split("'")[0].length != 1) {
                    //正常输入
                    var leng = input_val.length;
                    for (var i = 0; i < renyuan_arr_str.length; i++) {
                        renyuan_SZM.push($("#fuzzy_query").val(renyuan_arr_str[i]).toPinyin().substr(0, leng));
                    }
                    //一个字符判断
                    for (var h = 0; h < renyuan_SZM.length; h++) {
                        if (renyuan_SZM[h].toUpperCase() == input_val) {
                            xiabiao.push(h)
                        }
                    }
                } else {
                    for (var i = 0; i < renyuan_arr_str.length; i++) {
                        var shouzimu_str = $("#fuzzy_query").val(renyuan_arr_str[i]).toPinyin();
                        //获取首字母 如 LiNanNan -> LNN
                        var new_szm = "";
                        for (var k = 0; k < shouzimu_str.length; k++) {
                            if (/[A-Z]/.test(shouzimu_str[k]) == true) {
                                new_szm += shouzimu_str[k];
                            }
                        }
                        renyuan_SZM.push(new_szm);
                    }

                    //一个字符判断
                    for (var h = 0; h < renyuan_SZM.length; h++) {
                        if (renyuan_SZM[h].toUpperCase().indexOf(input_val.split("'").join("")) != -1) {
                            xiabiao.push(h)
                        }
                    }
                }
            } else {
                //正常输入
                var leng = input_val.length;
                for (var i = 0; i < renyuan_arr_str.length; i++) {
                    renyuan_SZM.push($("#fuzzy_query").val(renyuan_arr_str[i]).toPinyin().substr(0, leng));
                }
                //一个字符判断
                for (var h = 0; h < renyuan_SZM.length; h++) {
                    if (renyuan_SZM[h].toUpperCase() == input_val) {
                        xiabiao.push(h)
                    }
                }
            }
        }
        //找到下标后，返回原数组对应得值
        var data_arr = [];
        for (var a = 0; a < xiabiao.length; a++) {
            data_arr.push(arr[xiabiao[a]]);
        }
        return data_arr
    }
    //element 就是input的id
    $(".xq_content").delegate(element, "input", function () {
        //获取相对页面的位置
        var input_t = $(this).offset().top;
        var input_l = $(this).offset().left;
        var input_w = $(this).outerWidth();//input宽度，这个需要
        var input_h = $(this).outerHeight();
        var input_h2 = input_t + input_h;//input底边框高度

        //获取input输入的值
        var input_val = $(this).val();
        if (input_val == " ") {
            var data_arr = arr;
        } else {
            //这是一个首字母展示的数据筛选
            var data_arr = shaixuan(renyuan_arr_str, input_val);
        }

        //将获取的数据填充到页面上
        var ul = "";
        for (var i = 0; i < data_arr.length; i++) {
            ul += "<li><span data-pk='"+data_arr[i]["pk_psndoc"]+"'>" + data_arr[i]["name"] + "-" + data_arr[i]["code"] + "</span></li>";
        }
        var sqr = '<div class="fuzzy_query" data-id="' + element + '" style="left:' + input_l + 'px;top:' + input_h2 + 'px;width:' + input_w + 'px;z-index:1005;"><ul>' + ul + '</ul></div>';
        //放在页面上
        $("#fuzzy_query").html(sqr);
    });
    //监听选中的数据
    $("#fuzzy_query").delegate('.fuzzy_query ul li', "click", function () {
        var new_sqr = $(this).find("span").html();
        //将数据写到input中
        var data_id = $(".fuzzy_query").attr("data-id");
        $(data_id).val(new_sqr.split("-")[0]);
        $(data_id).attr("data-pk",$(this).find("span").attr("data-pk"));
        $(".fuzzy_query").remove();
    });

    //监听点击页面隐藏
    $(".xiangqing").click(function () {
        $(".fuzzy_query").remove();
    });
};
var weixin = (function () {
    return {
        // 显示提示信息
        tips: function(msg,times) { // msg 错误信息 times 隐藏时间，如1s后隐藏
            $("div.msgTip").remove(); // 隐藏可能存在的提示信息
            var html = '<div class="msgTip">' + msg + '</div>';
            var times = times || 1500;
            $("body").prepend(html);
            if(typeof(msg) != "undefined"){
            	if(msg.length > 18){
                	$(".msgTip").css({
                		"width":"90%",
                		"font-size":"14px",
                		"height":"auto",
                		"white-space":"normal",
                		"word-break":"break-all",
                		"line-height":"25px",
                		"padding":"5px 15px",
                		"text-align":"left"
                	})
                }
            }
            var length = $(".msgTip").length;
            length > 0 ? $(".msgTip").text(msg) : "";
            setTimeout(weixin.delTips,times);
        },
        // 隐藏提示信息
        delTips: function() {
            $("div.msgTip").animate({"top":'-='+'30px',"opacity":"0"},400,function(){
                $("div.msgTip").remove();
            })
        },
        //结果为空提示
        notFoundTips: function(msg){
        	$("div.notfound-tip").remove();
        	var html='<div class="notfound-tip"><img src="../../resources/images/nofound.png" /><p style="color:#555;">'+msg+'</p></div>';
        	$("body").prepend(html);
        },
        center: function(obj) {
            var windowWidth = document.documentElement.clientWidth;
            var windowHeight = document.documentElement.clientHeight;
            var popupHeight = $(obj).height();
            var popupWidth = $(obj).width();
            var top = (windowHeight-popupHeight)/2+$(document).scrollTop() > 0 ? (windowHeight-popupHeight)/2+$(document).scrollTop() : 46;
            var index = parseInt(new Date().getTime()/1000);
            $(obj).css({
                "position": "absolute",
                "opacity": 0,
                "top": top-30,
                "left": (windowWidth-popupWidth)/2,
                "z-index":index
            }); 
            $(obj).css({"top":'+='+'30px',"opacity":"1"});
        },
        //加载中...
        loading: function(msg) {
            var html = '<div id="msg_loading" class="msgbox"><span class="loading"><em>&nbsp;</em><p style="color:#fff;margin-top: 10px;font-weight:normal;">'+msg+'</p></span><span class="msg_right"></span></div>';
            $("body").prepend(html);
            weixin.center($("#msg_loading"));
            weixin.mask();
        },
        //去除加载中
        removeLoad: function() {
            $("#msg_loading").remove();
            weixin.delMask();
        },
        //去除加载中
        removeLoading: function() {
            $("#msg_loading").remove();
        },
        //显示浮层
        mask: function() {
            var windowHeight = document.documentElement.clientHeight;
            $("body").height(windowHeight);
            $("body,html").addClass("overflow-body");
            if($("#filter").length <= 0) {
                var mybg = '<div id="filter"></div>';
                var h = $("body").height() > document.documentElement.clientHeight ? $("body").height():document.documentElement.clientHeight;
                $("body").append(mybg);
                $("#filter").click(function(){
                    $(".visitor-list").hide();
                    $("#typeContent").hide();
            		$("#typeList").html('');
                    $("#shareTip").hide();
                    weixin.delMask();
                    $("body,html").removeClass('overflow-body');
                });
//                $("div#filter").height(h);
            }
        },
        //隐藏浮层
        delMask: function() {
            $("#filter").remove();
            $("body").removeClass("overflow-body");
            $("body").height("auto");
            $("#cardTip").hide();$(".cardTip").hide();
            $("#examTip").hide();$("#cardAlertWindow").hide();
            $("#cardAlertWindow").hide();$("#exitExamTip").hide();
            $(".alertWindow").hide();$("#aroundType").hide();
        },
        // 保存cookie
    	setCookie:function(c_name, value, expiredays, path) {
    	    var exdate = new Date();
    	    exdate.setDate(exdate.getDate() + expiredays);
    	    document.cookie = c_name + "=" + escape(value) +
    	        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString()) +
    	        ((path == null) ? "" : ";path=" + path)
    	},
        // 得到cookie
    	getCookie:function(c_name) {
    	    var res = null;
    	    if (document.cookie.length > 0) {
    	        c_start = document.cookie.indexOf(c_name + "=")
    	        if (c_start != - 1) {
    	            c_start = c_start + c_name.length + 1;
    	            c_end = document.cookie.indexOf(";", c_start);
    	            if (c_end == - 1) c_end = document.cookie.length
    	            res = unescape(document.cookie.substring(c_start, c_end))
    	        }
    	    }
    	    return res;
    	},
    	//清除cookie
    	delCookie:function(name,path){
    		var exp = new Date();
    	    exp.setTime(exp.getTime() - 1);
    	    var cval = weixin.getCookie(name);
//    	    console.log(cval);
    	    if (cval != null)
    	        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() +
    	        ((path == null) ? "" : ";path=" + path);
    	},
        // 清除所有cookie
        clearCookie: function() {
            var cookies = document.cookie.split(";");  
            for (var i = 0; i < cookies.length; i++) {  
                var cookie = cookies[i];  
                var eqPos = cookie.indexOf("=");  
                var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;  
                document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";  
            }  
            if(cookies.length > 0)  {  
                for (var i = 0; i < cookies.length; i++) {  
                    var cookie = cookies[i];  
                    var eqPos = cookie.indexOf("=");  
                    var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;  
                var domain = location.host.substr(location.host.indexOf('.'));  
                    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; domain=" + domain;  
                }  
            }
        }
    }
} ());

