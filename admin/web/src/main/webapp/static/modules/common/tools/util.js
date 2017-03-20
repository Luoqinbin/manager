/**
 * @file modules/common/tools/util.js
 * @desc 该文件主要用于处理常用工具
 * @auth ynshun
 * @version 1.0
 * @since 2016-09-19 11:53:00
 */
define([
	"dojo",
	"dojo/date", 
    "dojo/date/locale", 
    "dojo/number"
], function (dojo, date, locale, number) {
	var module = {
		
		/**
		 * @title 打开弹出层
		 * 
		 * @param opts	layer配置对象
		 */
		openLayer: function (/**Object*/opts) {
			opts = opts || {};
			dojo.mixin(opts, {
				area: opts.area || [ '500px', '500px' ],
				title: opts.title || '系统处理窗口',
				type : 1,
				btn: opts.btn || [ '保存', '关闭' ],
				yes: opts.yes || function(layero, index) {},
				cancel : opts.cancel || function(index, layero) { layer.close(index); }
			});
			layer.open(opts);
		},
		
		/**
		 * 关闭弹出层
		 * 
		 * @param index		弹出层序号
		 */
		closeLayer: function (/**int*/index) {
			layer.close(index);
		},
		
		confirm: function (/**String*/msg) {
			var dtd = new dojo.Deferred();
			layer.confirm(msg, function (index) {
				dtd.resolve(index);
	        }, function() {
	        });
			return dtd;
		},
		
		/**
		 * 消息框
		 * 
		 * @param msg	提示消息
		 * @param opts 	配置信息
		 * 
		 * @eg opts: 
		 * 	{
		 * 		time: 0, // 显示时间，超时后会被自动关闭 0自动关闭
		 * 		icon: 3, // 0 - !; 1 - success; 2 - fail; 3 - ?; 4 - lock; 5 - 哭脸; 6 - 笑脸;
		 * 		btn: ['确定','取消'],
		 * 		yes: function (index) {
		 *	    	alert(index);
		 *	    }
		 *  }
		 * 
		 */
		message: function (/**String*/msg, /**Object*/opts) {
			opts = opts || {};
			layer.msg(msg, opts);
		},
		
		post: function (/**String*/url, /**Object*/data) {
			var dtd = new dojo.Deferred();
			
			$.post(url, data, function (data, textStatus, jqXHR) {
				dtd.resolve(data);
			});
			return dtd;
		},
		
		
		get: function (/**String*/url, /**Object*/data) {
			var dtd = new dojo.Deferred();
			
			$.get(url, data, function (data, textStatus, jqXHR) {
				dtd.resolve(data);
			});
			return dtd;
		},
		
		ajax: function (/**Object*/opts) {
			var dtd = new dojo.Deferred();
			opts = opts || {};
			
			dojo.mixin(opts, {
				contentType: opts.contentType || 'application/json',
				dataType: opts.dataType || 'json',
				async: opts.async || true,
				cache: opts.cache || true,
				success: function (res) {
					dtd.resolve(res);
				},
				error: function () {
					dtd.reject('请求服务器异常，请联系系统管理员！');
				}
			});
			
			$.ajax(opts);
			return dtd;
		},
		
		/**
		 * 获取权限token
		 */
		getTokenData: function () {
			var csrfName = $("#csrfName").val();
			var csrfToken = $("#csrfToken").val();
			var data = {};
			data[csrfName] = csrfToken;
			return data;
		},
		
		/**
		 * 将指定字符串转换成日期对象
		 * 
		 * @param sdate 	字符串日期
		 */
		toDate : function(/**String*/sdate) {
            return this._toDateObject(sdate, 0);
        },

        /**
		 * 将指定字符串转换成时间对象
		 * 
		 * @param sdate 	字符串日期
		 */
        toTime : function(/**String*/sdate) {
            return this._toDateObject(sdate, 1);
        },

        /**
		 * 将指定字符串转换成日期时间对象
		 * 
		 * @param sdate 	字符串日期
		 */
        toDateTime : function(/**String*/sdate) {
            return this._toDateObject(sdate, 2);
        },
        
        /**
         * 根据传入日期格式化成字符串
         * @param date 日期数据
         * @param format 格式化模板  默认（yyyy-MM-dd hh:mm:ss）
         */
        dateFormat : function(/**Date*/date, /**String*/format) {
            format = format || "yyyy-MM-dd hh:mm:ss";
            return J.Util.formatDate(date, format);
        },

        /**
         * 将指定时间戳格式化成字符串
         * 
         * @param timeMillis	时间戳
         * @param format 		时间格式
         */
        toFormatDateByLong : function(timeMillis, format) {
            var date = new Date(timeMillis);
            var o = {
                "M+" : date.getMonth() + 1,
                "d+" : date.getDate(),
                "h+" : date.getHours(),
                "m+" : date.getMinutes(),
                "s+" : date.getSeconds(),
                "q+" : Math.floor((date.getMonth() + 3) / 3),
                "S" : date.getMilliseconds()
            }
            if (/(y+)/.test(format)) {
                format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(format)) {
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return format;
        },
        
        /**
         * 计算开始时间与结束时间多久
         * 
         * @startTime 	开始时间
         * @endTime		结束时间
         */
        dateBetween : function(/**Date*/startTime, /**Date*/endTime) {
            var betweenInfoFlag = true;
            var betweenInfo = "";
            var showTime = (endTime - startTime) / 1000 / 60;
            if (betweenInfoFlag && showTime > 0 & showTime < 60) {
                betweenInfoFlag = false;
                betweenInfo = parseInt(showTime) + "分钟前";
            }
            showTime = showTime / 60;
            if (betweenInfoFlag && showTime > 0 & showTime < 24) {
                betweenInfoFlag = false;
                betweenInfo = parseInt(showTime) + "小时前";
            }
            showTime = showTime / 24;
            if (betweenInfoFlag && showTime > 0 & showTime < 30) {
                betweenInfoFlag = false;
                betweenInfo = parseInt(showTime) + "天前";
            }
            showTime = showTime / 30;
            if (betweenInfoFlag && showTime > 0 & showTime < 12) {
                betweenInfoFlag = false;
                betweenInfo = parseInt(showTime) + "月前";
            }

            showTime = showTime / 12;
            if (betweenInfoFlag && showTime > 0) {
                betweenInfoFlag = false;
                betweenInfo = parseInt(showTime) + "年前";
            }
            return betweenInfo;
        },

        /**
         * 将指定字符串日期装换成日期对象
         * 
         * @param sdate		字符串日期
         * @param type		日期类型（0-yyyy-MM-dd | 1-HH:mm:ss | 2-yyyy-MM-dd HH:mm:ss）
         */
        _toDateObject : function(/**String*/sdate, /**int*/type) {
            var format = {selector: "date", datePattern: "yyyy-MM-dd", locale : "zh-cn"};
            switch (type) {
            case 0:
                sdate = sdate.substr(0, 10);
                break;
            case 1:
                sdate = sdate.substr(0, 8);
                format = {selector: "time", timePattern: "HH:mm:ss", locale : "zh-cn"};
                break;
            case 2:
                sdate = sdate.substr(0, 19);
                format = {selector: "full", datePattern: "yyyy-MM-dd", timePattern: "HH:mm:ss", locale : "zh-cn"};
                break;
            }
            try {
                return locale.parse(sdate, format);
            } catch(e) {
                return new Date();
            }
        },

        /**
         * 判断传入对象是否为空
         * 		包括 undefined | null | "null" | "undefined"
         * 
         * @param obj 需检查的对象
         */
        assertNotNull : function(/**Any Object*/obj) {
            if (( typeof obj == "undefined") || (obj == null) || (obj === "null") || (obj === "undefined"))
                return false;
            return true;
        },
        
        /**
         * 判断传入对象是否为空
         * 		包括 undefined | null | "null" | "undefined" | ""
         * 
         * @param obj 需检查的对象
         */
        assertNotNullStr : function(/**Any Object*/obj) {
            if (( typeof obj == "undefined") || (obj == null) || (obj === "null") || (obj === "undefined") || (obj === ""))
                return false;
            return true;
        },

        /**
         * 判断传入对象是否为空
         * 
         * @param obj 需检查的对象
         */
        assertDataNull : function(/*Any Object*/obj) {
            return (!module.assertNotNull(obj) || (obj.length == 0) || !module.assertNotNull(obj[0]));
        },

        /**
         * 判断两个字符串是否相等（忽略大小写）
         * 
         * @param a 字符串1
         * @param b 字符串2
         */
        equalIgnoreCase : function(/*String*/a, /*String*/b) {
            return (a.toUpperCase() === b.toUpperCase());
        },

        
        numRound : function(/*Number*/value, /*int*/places) {
            return number.round(Number(value), places || 2);
        },
        
        numFormat: function(/*Number*/value, /*int*/places) {
            try {
                return number.format(this.numRound(value, places), places || 2);                
            } catch(e) { return "0"; }
        },

        /**
         * 过滤对象为空的值
         */
        objectNotNullValue : function(/*Object*/obj) {
            for (var i in obj) {
                if ( typeof (obj[i]) == 'string' || typeof (obj[i]) == 'undefined' || typeof (obj[i]) == 'object') {
                    if (!obj[i] || obj[i].length == 0)
                        delete obj[i];
                }
            }
            return obj;
        },

        /**
         * 使用Math.random()作为随机码来产生一个GUID字符串
         *  */
        randomUUID : function() {
            var HEX_RADIX = 16;

            function _generateRandomEightCharacterHexString() {
                var random32bitNumber = Math.floor((Math.random() % 1) * Math.pow(2, 32));
                var eightCharacterHexString = random32bitNumber.toString(HEX_RADIX);
                while (eightCharacterHexString.length < 8) {
                    eightCharacterHexString = "0" + eightCharacterHexString;
                }
                return eightCharacterHexString;
            }

            var hyphen = "-";
            var versionCodeForRandomlyGeneratedUuids = "4", variantCodeForDCEUuids = "8";
            var a = _generateRandomEightCharacterHexString();
            var b = _generateRandomEightCharacterHexString();
            b = b.substring(0, 4) + hyphen + versionCodeForRandomlyGeneratedUuids + b.substring(5, 8);
            var c = _generateRandomEightCharacterHexString();
            c = variantCodeForDCEUuids + c.substring(1, 4) + hyphen + c.substring(4, 8);
            var d = _generateRandomEightCharacterHexString();
            var returnValue = a + hyphen + b + hyphen + c + d;
            returnValue = returnValue.toLowerCase();
            return returnValue;
            // String
        },

        randomNum : function(/*Boolean?*/needYear) {
            var currentTime = locale.format(new Date(), constants.dtOptions.time), needYear = (module.assertNotNull(needYear) ? needYear : false);
            currentTime = ( needYear ? (new Date().getFullYear() + "").substr(2, 2) : "") + currentTime.replace(/:|\./ig, "");
            return parseInt(currentTime);
        }
	};
	
	return module;
});