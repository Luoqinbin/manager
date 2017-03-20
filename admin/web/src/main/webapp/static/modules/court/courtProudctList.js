define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore",
    "modules/common/tools/moment"
], function (dojo, util, table, datastore,moment) {
    var module = {
        constants:{
            listUrl: 'courtProduct/queryList'
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
                {"mData": "area"},
                {"mData": "addr"},
                {"mData": "startTime"},
                {"mData": "endTime"},
                {"mData": "state"},
                {"mData": "price"},
                {"mData": "cashPrice"}
            ];
            var aoColumnDefs = [{
                "aTargets": [3],
                "mRender": function (a, b, c, d) {
                    return  moment(a).format("YYYY-MM-DD HH:mm:ss");
                }
            },{
                "aTargets": [4],
                "mRender": function (a, b, c, d) {
                    return  moment(a).format("YYYY-MM-DD HH:mm:ss");
                }
            },{
                "aTargets": [5],
                "mRender": function (a, b, c, d) {
                    if(a==-1){
                        return "已取消";
                    }else if(a==1){
                        return "未预定";
                    }else if(a==9){
                        return "已过期";
                    }else if(a==2){
                        return "未付款";
                    }else if(a==3){
                        return "已支付";
                    }else if(a==4){
                        return "已使用";
                    }

                }
            }
            ];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },

        handler:{
            __queryHandler:function(condition){
                var courtIdQuery = $("#courtIdQuery").val();
                var startTimeQuery = $("#startTimeQuery").val();
                var endTimeQuery = $("#endTimeQuery").val();
                if (util.assertNotNullStr(courtIdQuery)) condition.courtId = courtIdQuery;
                if (util.assertNotNullStr(startTimeQuery)) condition.startTimeQuery = startTimeQuery;
                if (util.assertNotNullStr(endTimeQuery)) condition.endTimeQuery = endTimeQuery;
            },
            __initHandler:function () {

            }
        }

    };

    return module;
});