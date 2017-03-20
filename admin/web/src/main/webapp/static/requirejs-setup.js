/**
 * NOTE: 在使用该脚本时必须使用绝对路径，而不能是相对路径
 *  */
if (typeof syscfg == "undefined") window.syscfg = {};
var requirejsArgs = {
	dojoLocation : "./dojo/dojo",
	rootPath : (function() {
		var scripts = document.getElementsByTagName("script");
		for ( var src, match = false, i = 0; i < scripts.length && !match; i++) {
			if (((src = scripts[i].getAttribute("src")) && (match = src.match(/(.*)\/?(?:dojo|xforms|screen|requirejs-setup)[\w|-]*\.(?:css|js)(\W|$)/i)))) {
				var index = src.indexOf("/form/xforms/");
				index = (index > 0)? index : src.indexOf("/requirejs-setup");
				var params = src.split("?");
				for (var i = 0; i < params.length; i++) {
					var match = /v=(.+)/i.exec(params[i]);
					if (match != null) {
						window.syscfg.ver = match[1]; break;
					}
				}
				return src.substring(0, index);
			}
		}
	})()
};
if (typeof syscfg.ver == "undefined") syscfg.ver = "1.0";
requirejsArgs.dojoLocation = requirejsArgs.rootPath + "/dojo/1.10.4/dojo";

var dojoConfig = {
	parseOnLoad: true, async : 1, baseUrl : requirejsArgs.baseUrl || ".",  tlmSiblingOfDojo: 0,
	packages : [{
		name : "dojo",
		location : requirejsArgs.dojoLocation
	}, {
    	name : "modules",
    	location : requirejsArgs.dojoLocation + "/../../../modules"
    }, {
        name : "static",
        location : requirejsArgs.dojoLocation + "/../../../../static"
    }],
	callback: function() { }
};

if( typeof require != "undefined") {
	(function() {
		for(var p in require) dojoConfig[p] = require[p];
	})();
}