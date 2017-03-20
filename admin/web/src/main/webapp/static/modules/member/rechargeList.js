/**
 * Created by Luoqb on 2017/3/13.
 */
define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore",
    "modules/common/tools/moment"
], function (dojo, util, table, datastore,moment) {
    var module = {
        constants:{
            queryList: 'memberOrder/queryList'
        },
        initPage: function () {
            module.__gatherEvent();
            module.__initDataTable();
        },
        __gatherEvent:function () {
            $("#query").bind("click",function(){
                module.datatable.fnReloadAjax();
            });
        },
        __initDataTable:function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "name"},
                {"mData": "number"},
                {"mData": "phone"},
                {"mData": "balance"},
                {"mData": "source"},
                {"mData": "pay_type"},
                {"mData": "state"},
                {"mData": "operate_type"},
                {"mData": "created_dt"}
            ];
            var aoColumnDefs = [{
                "aTargets": [5],
                "mRender": function (a, b, c, d) {
                    return  a==1?"前台":"后台";
                }
            },{
                "aTargets": [6],
                "mRender": function (a, b, c, d) {
                    if(a==1){
                        return "现金支付";
                    }else if(a==2){
                        return "刷卡支付";
                    }else if(a==3){
                        return "转账-农商行0549";
                    }else if(a==4){
                        return "转账-中信";
                    }else if(a==5){
                        return "微信-中信";
                    }else if(a==6){
                        return "支付宝";
                    }
                }
            },{
                "aTargets": [7],
                "mRender": function (a, b, c, d) {
                    return  a==1?"有效":"无效";
                }
            },{
                "aTargets": [8],
                "mRender": function (a, b, c, d) {
                    return  a==1?"办卡":"充值";
                }
            }];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.queryList + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },

        handler:{
            __queryHandler:function(condition){
                var phoneQuery = $("#phoneQuery").val();
                var cardNoQuery = $("#cardNoQuery").val();
                var startTimeQuery = $("#startTimeQuery").val();
                if (util.assertNotNullStr(phoneQuery)) condition.phone = phoneQuery;
                if (util.assertNotNullStr(cardNoQuery)) condition.cardNo = cardNoQuery;
                if (util.assertNotNullStr(startTimeQuery)) condition.startTimeQuery = startTimeQuery;
            },
            __initHandler:function () {

            }
        },

    }
    return module;
});