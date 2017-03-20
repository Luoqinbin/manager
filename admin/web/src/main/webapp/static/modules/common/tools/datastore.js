/**
 * @file modules/common/tools/datastore.js
 * @desc 该文件主要用于处理页面数据节点数据
 * @auth ynshun
 * @version 1.0
 * @since 2016-09-19 10:43:00
 */
define([
	"dojo",
	"./util"
], function (dojo, util) {
	
	var module = {
		
		/**
		 * @title 将指定json对象赋值到相同节点（name | id）上
		 * @desc 该方法通过遍历data对象，将对应节点名对应的数据发布到对应节点上
		 * 
		 * @param parentNode 	父节点（用于控制域）
		 * @param data 			JSON对象
		 */
		publishFormData: function (/**Node*/parentNode, /**Object*/data) {
			if (!parentNode || !data) return;
			//console.log(data);
			for (var pro in data) {
				var childNode = $(parentNode).find("[name=" + pro + "]");
				if (!childNode) childNode = $(parentNode).find("#" + pro);
				if ($(childNode).attr("type") === 'radio') {
					dojo.forEach(childNode, function (n, i) {
						if ($(n).val() == data[pro]) {
							$(n).attr("checked", "checked");
						}
					});
				} else if($(childNode).attr("type") === 'checkbox'){
					$(childNode).attr("checked",true);
				}else {
					$(childNode).val(data[pro]);
				}
			}
		},
		
		/**
		 * @title 收集form数据
		 * @warn 只支持input|select|textarea
		 * 
		 * @param parentNode	父节点（用于控制域）
		 * 
		 */
		gatherFormData: function (/**Node*/parentNode) {
			var data = {};
			
			// 处理所有Input节点
			var inputs = $(parentNode).find("input");
			dojo.mixin(data, module.__gatherInputData(inputs));
			
			// 处理所有Select节点
			var selects = $(parentNode).find("select");
			dojo.mixin(data, module.__gatherSelectData(selects));
			
			// 处理所有Textarea节点
			var textareas = $(parentNode).find("textarea");
			dojo.mixin(data, module.__gatherTextareaData(textareas));
			
			return data;
		},
		
		/**
		 * @title 根据配置为指定节点赋值
		 * @warn 配置的target节点只能是节点ID
		 * 
		 * @param parentNode	父节点（用于控制域）
		 * @param cfgs 			配置信息
		 * 
		 * @eg cfgs:
		 * 		[
		 * 			{target: "customerName", value: "张三"},
		 * 			{target: "customerAge", value: 20},
		 * 			{target: "customerHeight", value: 1.75}
		 * 		]
		 * 
		 * @desc 示例说明：
		 * 		事例中cfg表示将指定父节点中
		 * 			id="name"对应的节点值取出来放入customerName节点中（若取不到值或为空则默认为张三）
		 * 			id="age"对应的节点值取出来放入customerAge节点中（若取不到值或为空则默认为20）
		 * 			id="height"对应的节点值取出来放入customerHeight节点中（若取不到值或为空则默认为null）
		 * 
		 */
		publishFormDataByConfig: function (/**Node*/parentNode, /**Object*/cfgs) {
			if (!cfgs) return;
			
			$.each(cfgs, function (idx, cfg) {
				$("#" + cfg.target).val(cfg.value);
			});
		},
		
		/**
		 * @title 根据配置收集form数据
		 * @warn 配置的source节点只能是节点ID
		 * 
		 * @param cfgs			配置信息
		 * @param ignoreNull	是否忽略Null值
		 * 
		 * @eg cfgs:
		 * 		[
		 * 			{source: "name", target: "customerName", def: "张三"},
		 * 			{source: "age", target: "customerAge", def: 20},
		 * 			{source: "height", target: "customerHeight"}
		 * 		]
		 * 
		 * @desc 示例说明：
		 * 		事例中cfg表示将指定父节点中
		 * 			id="name"对应的节点值取出来放入customerName节点中（若取不到值或为空则默认为张三）
		 * 			id="age"对应的节点值取出来放入customerAge节点中（若取不到值或为空则默认为20）
		 * 			id="height"对应的节点值取出来放入customerHeight节点中（若取不到值或为空则默认为null）
		 * 
		 */
		gatherFormDataByCfg: function (/**Array*/cfgs, /**Boolean*/ignoreNull) {
			var data = {};
			if (!cfgs) return data;
			
			$.each(cfgs, function (idx, cfg) {
				var value = $("#" + cfg.source).val();
				if (ignoreNull != true) return true;
				var target = cfg.target ? cfg.target : cfg.source;
				data[target] = value ? value : (cfg.def ? cfg.def : null);
			});
			return data;
		},
		
		/**
		 * 根据配置收集指定数据
		 * 
		 * @param data 通过getPageObject()生成的数据对象源
		 * @param cfg 需要收集数据的配置数组
		 * 
		 * ------------------华丽的分割线---------------
		 * @auth: ynshun
		 * @since: 2016-10-12 09:18:00
		 * @lastUpdateUser: ynshun
		 * @lastUpdateDate: 2016-10-12 09:18:00
		 * @version: ver1.0
		 * 
		 * ------------------华丽的分割线---------------
		 * @说明
		 * 		cfg: [
		 * 			{s: "username", t: "account"},
		 * 			{s: "age"},
		 * 			"height"
		 * 		]
		 * 
		 * 	cfg: 可以支持三种方式：
		 * 		1、完整方式，即{s: "", t: ""}，用于域对象数据字段名和最终需要的字段名不一致的情况
		 * 		2、半完整方式，即{s: ""}，用于域对象数据字段名和最终需要的字段名一致的情况
		 * 		3、字符串方式，即""，该方式是对<2、半完整方式>的一个简写
		 * 
		 */
		gatherData: function (/**Array by cfg*/cfg) {
			var _data = {};
			if (!cfg || cfg.length == 0) return _data;
			
			var s, t, item, value;
			for (var idx = 0; idx < cfg.length; idx++) {
				item = cfg[idx];
				value = null;
				if(typeof item === "string") {
					s = item;
					t = item;
				} else {
					s = item.s;
					t = item.t || s;
				}
				value = $("#" + s).val();
				if (util.assertNotNullStr(value)) _data[t] = value;
			}
			return _data;
		},
		
		/**
		 * 在节点上设置值
		 */
		setData: function (/**Node*/node, /**String*/key, /**Object*/data) {
			$(node).data(key, data);
		},
		
		/**
		 * 取出节点中的值
		 */
		getData: function (/**Node*/node, /**String*/key) {
            var data = $(node).data(key);
            return util.assertDataNull(data) ? null : JSON.parse(data);
		},
		
		/**
		 * 收集所有指定的Textarea节点值
		 * 
		 * @param textareas textarea节点数组
		 * 
		 */
		__gatherTextareaData: function (/**Array by textarea*/textareas) {
			var data = {};
			if (!textareas || textareas.length == 0) return data;
			$.each(textareas, function (idx, textarea) {
				var name = $(textarea).attr("name");
				if (!name) name = $(textarea).attr("id");
				if (!name) return; 
				
				data[name] = $(textarea).val();
			});
			return data;
		},
		
		
		/**
		 * 收集所有指定的Select节点值
		 * 
		 * @param selects select节点数组
		 * 
		 */
		__gatherSelectData: function (/**Array by select*/selects) {
			var data = {};
			if (!selects || selects.length == 0) return data;
			$.each(selects, function (idx, select) {
				var name = $(select).attr("name");
				if (!name) name = $(select).attr("id");
				if (!name) return; 
				
				data[name] = $(select).val();
			});
			return data;
		},
		
		/**
		 * 收集所有指定的Input标签值
		 * 
		 * @param inputs Input节点数组
		 * 
		 */
		__gatherInputData: function (/**Array by input*/inputs) {
			var data = {};
			if (!inputs || inputs.length == 0) return data;
			
			$.each(inputs, function (idx, input) {
				var type = $(input).attr("type");
				if (type === 'button' || type === 'reset' || type === 'submit') return; 
				
				var name = $(input).attr("name");
				if (!name) name = $(input).attr("id");
				if (!name) return; 
				
				data[name] = $(input).val();
			});
			return data;
		}
		
	};
	
	return module;
});