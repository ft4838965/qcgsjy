fnResize();
window.addEventListener("resize", function () {
	fnResize()
}, false);
function fnResize() {
	var docWidth = document.documentElement.clientWidth,
		body = document.getElementsByTagName('html')[0];
	body.style.fontSize = docWidth / 30 + 'px';
}
//公用的错误提示
function errmsg(msg) {
	var obj = document.getElementById("toast");
	obj.innerHTML = msg;
	obj.style.display = 'block';
	window.setTimeout(function () { obj.style.display = 'none'; }, 2000);
}
//安卓下载链接

var downurl = "http://img.greatchef.com.cn/app/greatchef_v2.14.1.apk";

/*设置 cookie*/
function set_cookie(key, value, exp, path, domain, secure) {
	path = "/";
	var cookie_string = key + "=" + escape(value);
	if (exp) {
		var Days = 1;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
		cookie_string += "; expires=" + exp.toGMTString();
	}
	if (path)
		cookie_string += "; path=" + escape(path);
	if (domain)
		cookie_string += "; domain=" + escape(domain);
	if (secure)
		cookie_string += "; secure";
	document.cookie = cookie_string;
}

/*读取 cookie*/
function get_cookie(cookie_name) {
	var results = document.cookie.match('(^|;) ?' + cookie_name + '=([^;]*)(;|$)');
	if (results)
		return (unescape(results[2]));
	else
		return null;
}
/*删除 cookie*/
function del_cookie(cookie_name) {
	var cookie_date = new Date();
	//current date & time
	cookie_date.setTime(cookie_date.getTime() - 1);
	document.cookie = cookie_name += "=; expires=" + cookie_date.toGMTString();
}
function getshownum(num) {
	if (num > 99) {
		return '99<sup>+</sup>';
	} else {
		return num;
	}
}
//话题聚合页评论数量&赞数量
function getshownumtopic(num) {
	if (num > 99) {
		return '<span>99<span><sup>+</sup>';
	} else {
		return '<span>' + num + '<span>';
	}
}
function dateformat(time) {
	var now = new Date(parseInt(time) * 1000);
	var month = now.getMonth() + 1;
	var date = now.getDate();
	if (month < 10) {
		month = '0' + month;
	}
	if (date < 10) {
		date = '0' + date;
	}
	return month + "." + date;
}
window.onload = function () {
	var downclose = document.getElementById("down_close");
	if (downclose != null) {
		downclose.onclick = function () {
			document.getElementById('down_banner').style.display = 'none';
		}
	}

	
	//和大家一起讨论
	var objtittlbutton = document.getElementById("tittlbutton");
	if (objtittlbutton != null) {
		objtittlbutton.onclick = function () {
			downcommon(1);
		}
	}
	var closebtn = document.getElementById("close_btn");
	if (closebtn != null) {
		closebtn.onclick = function () {
			closelivedownload();
		}
	}
	var openinBrowserobj = document.getElementById("openinBrowser");
	if (openinBrowserobj != null) {
		openinBrowserobj.onclick = function () {
			document.getElementById("openinBrowser").style.display = "none";
		}
	}
	var downappobj = document.getElementById("downapp");
	if (downappobj != null) {
		downappobj.onclick = function () {
			location.href = downurl;
		}
	}
	// var updateapp = document.getElementById("updateapp");
	// if (updateapp != null) {
	// 	updateapp.onclick = function () {
	// 		if (/ipad|iphone|mac/i.test(navigator.userAgent)) {
	// 			location.href = "https://itunes.apple.com/cn/app/id1443466594?mt=8";
	// 		} else {
	// 			location.href = "http://18120650554.fx.sj.360.cn/qcms/view/t/detail?id=4021649";//
	// 		}
	// 	}
	// }
}
function showlivedownload() {
	document.getElementById("divlivedownloadbackgroud").style.display = "block";
	document.getElementById("divlivedownload").style.display = "block";
}
function closelivedownload() {
	document.getElementById("divlivedownloadbackgroud").style.display = "none";
	document.getElementById("divlivedownload").style.display = "none";
}
function downcommon(type) {
	var guide = "";
	var downmsg = "";
	if (/ipad|iphone|mac/i.test(navigator.userAgent)) {
		document.getElementById('openinBrowsershare').style.backgroundImage = "url(static/img/chef/share.png)";
		guide = 1;
	} else {
		document.getElementById('openinBrowsershare').style.backgroundImage = "url(static/img/chef/shareandroid.png)";
		guide = 2;
	}
	if (type == 'live') {
		downmsg = "下载星厨餐帮APP";
	} else {
		downmsg = "下载星厨餐帮APP";
	}
	document.getElementById("downmsg").innerHTML = downmsg;
	var ua = navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == "micromessenger") {
		document.getElementById("openinBrowser").style.display = "block";
	} else {
		var url = location.href;
		var Request = new Object();
		Request = GetRequest();
		var desarray = new Array("foodview", "newsview", "articlesview", "userview", "liveview", "liveshow", "markview", "dynamicview", "topicview", "live", "trialview", "videoview", "rank");
		var des = "";
		var skuid = "";
		for (var i = 0; i < desarray.length; i++) {
			if (url.indexOf(desarray[i]) != -1) {
				des = desarray[i];
				skuid = Request['id'];
				break;
			}
		}
		if (guide == 1) {
			var loadDateTime = new Date();
			// yrj,定时器暂时注释，以便直接弹出提示框，加上Scheme协议后需取消注释
			// window.setTimeout(function () {
				var timeOutDateTime = new Date();
				if (timeOutDateTime - loadDateTime < 5000) {
					downurl = "https://itunes.apple.com/cn/app/id1443466594?mt=8";
					showlivedownload();
				} else {
					window.close();
				}
			// },2500);
			// yrj,IOS Scheme协议（com.greatchef.notes://）打开APP界面,文档：https://www.jianshu.com/p/57f79fc83233
			// var iosjumpurl = 'com.greatchef.notes://';
			var iosjumpurl = '';
			if (des) {
				iosjumpurl += '?params={"des":"' + des + '","skuid":"' + skuid + '"}';
			}
			// yrj,跳转暂时注释，加上Scheme协议后需取消注释
			// window.location = iosjumpurl;
		} else {
			// yrj,跳转暂时注释，加上Scheme协议后需取消注释
			// if (des) {
			// 	androidjumpurl = 'cn.com.greatchef://?params={"des":"' + des + '","skuid":"' + skuid + '"}';
			// } else {
			// 	androidjumpurl = "cn.com.greatchef://?params=''";
			// }
			androidjumpurl = ""
			var ifr = document.createElement('iframe');
			ifr.src = androidjumpurl;
			ifr.style.display = 'none';
			// yrj,同IOS
			// document.body.appendChild(ifr);
			// window.setTimeout(function () {
				showlivedownload(); document.body.removeChild(ifr);
			// }, 2000)
		}
	}
}
function formatDate(time, type) {
	//console.log(time);
	var now = new Date(parseInt(time) * 1000);
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var date = now.getDate();
	var hour = now.getHours();
	var minute = now.getMinutes();
	var second = now.getSeconds();
	if (month < 10) {
		month = '0' + month;
	}
	if (date < 10) {
		date = '0' + date;
	}
	if (type == 1) {
		return hour + ":" + minute;
	} else {
		return year + "." + month + "." + date;
	}

}

var img = document.getElementsByTagName("img")
for (var i = 0; i < img.length; i++) {
	if (img[i].getAttribute('_src')) {
		img[i].setAttribute("src", img[i].getAttribute('_src'));
		img[i].removeAttribute('_src');
	}
}

function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}
//getjumpurl();
function getjumpurl() {
	var url = location.href;
	var Request = new Object();
	Request = GetRequest();
	var des = new Array("foodview", "newsview", "articlesview", "userview")
	for (var i = 0; i < des.length; i++) {
		if (url.indexOf(des[i]) != -1) {
			break;
		}
	}

}
function dateformats(timetmps) {
	var nowtime = Date.parse(new Date()) / 1000;
	var t = nowtime - timetmps;
	y = Math.floor(t / 60 / 60 / 24 / 365);
	d = Math.floor(t / 60 / 60 / 24);
	h = Math.floor(t / 60 / 60);
	m = Math.floor(t / 60);

	if (t < 60) {
		return '刚刚';
	} else if (m == 60) {
		return '1分钟前';
	} else if (m > 1 && m < 60) {
		return m + '分钟前';
	} else if (h == 1) {
		return '1小时前';
	} else if (h > 1 && h < 24) {
		return h + '小时前';
	} else if (d == 1) {
		return '昨天';
	} else if (d > 1 && y < 1) {
		return todate(timetmps);
	} else {
		return todate(timetmps, 1);
	}

}
function todate(time, type) {
	var now = new Date(parseInt(time) * 1000);
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var date = now.getDate();
	if (month < 10) {
		month = '0' + month;
	}
	if (date < 10) {
		date = '0' + date;
	}
	if (type) {
		return month + "-" + date;
	} else {
		return year + "-" + month + "-" + date;
	}
}
//获取职位
function getDuty(duty, role) {
	var rolearray = ['厨师', '经营者', '供应商', '美食爱好者'];
	//如果是厨师，有duty，返回duty
	if (role == 1 && duty) {
		return duty;
	}
	role = role - 1;
	//其他返回角色
	return rolearray[role];
}
/**
 * 视频时长转化成时 分 秒
 * @param int $s 视频时长 秒
 * @return string
 */
function timeformat(videolength, type) {
	var hour = Math.floor(videolength / 3600);
	var second = videolength - hour * 3600;//除去整小时之后剩余的时间
	var minute = Math.floor(second / 60);
	var second = videolength % 60;//除去整分钟之后剩余的时间
	if (second < 10) {
		second = '0' + second;
	}
	if (minute < 10) {
		minute = '0' + minute;
	}
	if (hour < 10) {
		hour = '0' + hour;
	}
	if (type == 2) {
		return minute + ':' + second;
	} else {
		return hour + ':' + minute + ':' + second;
	}

}
function getLiveTime(begintime) {
	var now = Date.parse(new Date()) / 1000;
	var today = GetDateStr(0);
	var tomorrow = GetDateStr(1);
	var begindate = formatDate(begintime);

	if (begindate == today) {
		return '今天';
	} else if (begindate == tomorrow) {
		return '明天';
	} else {
		var d = Math.floor((begintime - now) / 60 / 60 / 24);
		if (d > 0) {
			return d + '天后';
		} else {
			return "";
		}

	}
}
function GetDateStr(AddDayCount) {
	var dd = new Date();
	dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期 
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1;//获取当前月份的日期 
	var d = dd.getDate();
	if (m < 10) {
		m = '0' + m;
	}
	if (d < 10) {
		d = '0' + d;
	}
	return y + "-" + m + "-" + d;
}
function getmin(begin_time) {
	var nowtime = Date.parse(new Date()) / 1000;
	var t = nowtime - begin_time;
	return Math.floor(t / 60);;
}
function date_diff(EndTime) {
	console.log(EndTime);
	var NowTime = new Date();
	var t = EndTime * 1000 - NowTime.getTime();
	var d = 0;
	var h = 0;
	var f = 0;
	var s = 0;
	if (t >= 0) {
		d = Math.floor(t / 1000 / 60 / 60 / 24);
		h = Math.floor(t / 1000 / 60 / 60 % 24);
		f = Math.floor(t / 1000 / 60 % 60);
		s = Math.floor(t / 1000 % 60);
	} else {
		return '';
	}
	return d + '天 ' + h + '时 ' + f + '分';
}
