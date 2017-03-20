define([
    "dojo", 
    "dojo/_base/declare",
    "dojo/Evented"
], function(dojo, declare, Evented) {
    
    var mapClass = declare("modules.common.tools.Map", [Evented], {
        __container : null,
        
        constructor : function() {
            this.__container = new Object();
        },
        
        put : function (/**String*/key, /**Object*/value) {
            this.__container[key] = value;
        },
        
        get : function (/**String*/key) {
            return this.__container[key];
        },
        
        keySet : function () {
            var keyset = new Array();
            var count = 0;
            for (var key in this.__container) {
                if (key == 'extend') continue; // 跳过object的extend函数
                keyset[count++] = key;
            }
            return keyset;
        },
        
        values : function () {
            var tempValues = new Array();
            var count = 0;
            for (var key in this.__container) {
                tempValues[count++] = this.__container[key];
            }
            return tempValues;
        },
        
        size : function () {
            var count = 0;
            for (var key in this.__container) {
                if (key == 'extend') continue; // 跳过object的extend函数
                count++;
            }
            return count;
        },
        
        remove : function (/*String*/key) {
            delete this.__container[key];
        },
        
        clear : function () {
            this.__container = new Object();
        },
        
        toString : function () {
            var str = "";
            for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
                str = str + keys[i] + "=" + this.__container[keys[i]] + ";\n";
            }
            return str;
        }
    });
    return mapClass;
});
