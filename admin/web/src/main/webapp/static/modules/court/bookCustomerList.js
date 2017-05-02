define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore",
    "modules/common/tools/moment"
], function (dojo, util, table, datastore,moment) {
    var module = {
        constants:{
            listUrl: 'bookCustomer/queryList'
        },

        initPage: function () {
            module.__gatherEvent();
            module.__initDataTable();
        },
        __gatherEvent:function () {
            $("#query").bind("click",function(){
                module.datatable.fnReloadAjax();
            });
            $("#add").bind("click",function(){
                $("#userId").val("");
                module.add();
            });

        },
        __initDataTable:function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "type"},
                {"mData": "area"},
                {"mData": "name"},
                {"mData": "startTime"},
                {"mData": "endTime"},
                {"mData": "person"},
                {"mData": "price"},
                {"mData": "phone"},
                {"mData": "id"},
                {"mData": "createdDt"},
                {"mData": "memberNum"},
                {"mData": "source"}
            ];
            var aoColumnDefs = [{
                "aTargets": [1],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        if(a==1){
                            return "普通场";
                        }else{
                            return"固定场";
                        }
                    else
                        return "";
                }
            },{
                "aTargets": [3],
                "mRender": function (a, b, c, d) {
                    return a+"号场";
                }
            },{
                "aTargets": [4],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        return  moment(a).format("YYYY-MM-DD HH:mm:ss");
                    else
                        return "";
                }
            },{
                "aTargets": [5],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        return  moment(a).format("YYYY-MM-DD HH:mm:ss");
                    else
                        return "";
                }
            },{
                "aTargets": [10],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        return  moment(a).format("YYYY-MM-DD");
                    else
                        return "";
                }
            },{
                "aTargets": [10],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        return  moment(a).format("YYYY-MM-DD");
                    else
                        return "";
                }
            },{
                "aTargets": [12],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        if(a==1){
                            return "前台";
                        }else{
                            return "后台";
                        }
                    else
                        return "";
                }
            }];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },

        handler:{
            __queryHandler:function(condition){
                var phoneQuery = $("#phoneQuery").val();
                var createdDtQuery = $("#createdDtQuery").val();
                var areaQuery = $("#areaQuery").val();
                var memberNumQuery = $("#memberNumQuery").val();
                if (util.assertNotNullStr(phoneQuery)) condition.phone = phoneQuery;
                if (util.assertNotNullStr(createdDtQuery)) condition.createdDtQuery = createdDtQuery;
                if (util.assertNotNullStr(areaQuery)) condition.areaQuery = areaQuery;
                if (util.assertNotNullStr(memberNumQuery)) condition.memberNum = memberNumQuery;
            },
            __initHandler:function () {

            }
        },

        add:function () {
            util.openLayer({
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "",
                type: 1,
                content: $("#addWin"),
                btn: ['预定', '关闭'],
                success: function (layero, index) {
                    var date = new Date();
                    var y = date.getFullYear();
                    var m = date.getMonth()+1;
                    var d = date.getDate();
                    var dd = y+"-"+m+"-"+d;
                    module.__selectByDate(dd);
                },
                yes: function (layero, index) {
                    layer.closeAll();
                   // module.init();


                },
                cancel: function (index, layero) {
                    layer.close(index);
                }
            });
        },
        __selectByDate:function (date) {
            var data = util.getTokenData();
            data['date'] = date;
            util.post("bookCustomer/selectByData", data).then(function (data) {
                if(data.code==200){
                    var d = data.data;
                    var tbody = "";
                    var table = $("#table");
                    var tr = "";
                    for(var i=0;i<d.length;i++){
                        if(i%4==0){
                            tr = "<tr>";
                        }else{
                            tr+="<td></td>";
                        }

                    }
                }
            });
        },

        init:function () {
            var d = $('#date option:selected') .val();//日期
            var startTime = $('#startTime1 option:selected') .val();//开始时间
            var startTime2 = $('#startTime2 option:selected') .val();//开始时间
            var endTime = $('#endTime option:selected') .val();//结束时间

            var area =  $('#area option:selected').val();//区域
            util.openLayer({
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "预定",
                type: 1,
                content: $("#addWin1"),
                btn: ['预定', '关闭'],
                success: function (layero, index) {
                    var areas = area.split(",");

                    $("#areaId").val(areas[0]);
                    $("#areaSpan").text(areas[1]);
                    $("#dateSpan").text(d+"  "+startTime+":"+startTime2+"-"+endTime);
                    $("#startDate").val(d+" "+startTime+":"+startTime2);
                    $("#endDate").val(d+" "+endTime);
                    $("#phone").val("");
                },
                yes: function (layero, index) {
                    var areaId = $("#areaId").val();
                    var price = $("#price").val();
                    var payType = $("#payType").val();
                    var phone = $("#phone").val();
                    var startDate = $("#startDate").val();
                    var endDate = $("#endDate").val();
                    var data = util.getTokenData();
                    data['areaId']=areaId;
                    data['price']=price;
                    data['payType']=payType;
                    data['phone']=phone;
                    data['startDate']=startDate;
                    data['endDate']=endDate;
                    data['person']=$("#person").val();
                    console.log(data);
                    util.post("bookCustomer/addOrder", data).then(function (data) {
                        layer.msg(data.message);
                       /* if (data.code == 200) {
                            module.datatable.fnReloadAjax();
                            layer.msg("删除成功！");
                        }*/
                    });
                    layer.closeAll();
                },
                cancel: function (index, layero) {
                    layer.close(index);
                }
            });
        }
    };

    return module;
});