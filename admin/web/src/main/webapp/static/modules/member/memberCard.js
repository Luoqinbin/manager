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
    var validate = null;
    var module = {
        constants:{
            listUrl: '/memberCard/queryList',
            getById: 'memberCard/queryById',
            deleteUrl: 'memberCard/delete'
        },
        initPage: function () {
            module.__gatherEvent();
            module.__initDataTable();
            validate = module.validateForm();
        },
        __gatherEvent:function () {
            $("#query").bind("click",function(){
                module.datatable.fnReloadAjax();
            });
            $("#add").bind("click",function(){
                $("#userId").val("");
                module.add();
            });
            $("#update").bind("click",function () {
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行数据 ");
                }else{
                    module.update(data);
                }
            });

            $("#delete").bind("click",function () {
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行数据 ");
                }else{
                    module.delete(data);
                }
            });
        },
        __initDataTable:function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "name"},
                {"mData": "emptyDiscount"},
                {"mData": "busyDiscount"},
                {"mData": "titleAccount"},
                {"mData": "price"},
                {"mData": "payDate"},
                {"mData": "last"},
                {"mData": "status"}
            ];
            var aoColumnDefs = [{
                "aTargets": [2],
                "mRender": function (a, b, c, d) {
                    var s = a*10;
                    return  Math.round(s*100)/100+"折";
                }
            },{
                "aTargets": [3],
                "mRender": function (a, b, c, d) {
                    var s = a*10;
                    return  Math.round(s*100)/100+"折";
                }
            },{
                "aTargets": [4],
                "mRender": function (a, b, c, d) {
                    return  "￥"+a;
                }
            },{
                "aTargets": [5],
                "mRender": function (a, b, c, d) {
                    return  "￥"+a;
                }
            },{
                "aTargets": [6],
                "mRender": function (a, b, c, d) {
                    return  a+"天";
                }
            },{
                "aTargets": [7],
                "mRender": function (a, b, c, d) {
                    return  a+"个月";
                }
            },{
                "aTargets": [8],
                "mRender": function (a, b, c, d) {
                    if(a=='1'){
                        return "可售";
                    }else{
                        return "不可售";
                    }

                }
            }];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },

        handler:{
            __queryHandler:function(condition){
                var nameQuery = $("#nameQuery").val();
                var testName2Query = $("#testName2Query").val();
                var testName3Query = $("#testName3Query").val();
                if (util.assertNotNullStr(nameQuery)) condition.name = nameQuery;

            },
            __initHandler:function () {

            }
        },

        delete:function (d) {
            var data = util.getTokenData();
            dojo.mixin(data, {id: d.id});
            layer.confirm("你确定要删除该数据吗？", function (index) {
                util.post(module.constants.deleteUrl, data).then(function (data) {
                    layer.msg(data.message);
                    if (data.code == 200) {
                        module.datatable.fnReloadAjax();
                        layer.msg("删除成功！");
                    }
                });
            });
        },
        add:function () {
            util.openLayer({
                area: ['530px', '600px'],
                title: "新增",
                type: 1,
                content: $("#addWin"),
                btn: ['添加', '关闭'],
                success: function (layero, index) {
                    $("#form")[0].reset();
                    validate.resetForm();
                    $("#status").val("");
                },
                yes: function (layero, index) {
                    if ($("#form").valid()) {
                        $("#form").ajaxSubmit({
                            success: function (d) {
                                layer.msg(d.message);
                                if (d.code == 200) {
                                    module.datatable.fnReloadAjax();
                                    layer.closeAll();
                                }
                            }
                        })
                    }
                },
                cancel: function (index, layero) {
                    layer.close(index);
                }
            });
        },
        update:function (data) {
            var dataUpdate = util.getTokenData();
            dojo.mixin(dataUpdate, {id: data.id});
            validate.resetForm();
            util.post(module.constants.getById, dataUpdate).then(function (res) {
                if (res.code == 200) {
                    var d = res.data;
                    layer.open({
                        area: '800px',
                        shade: [0.8, '#393D49'],
                        title: "修改",
                        type: 1,
                        content: $("#addWin"),
                        btn: ['确定', '关闭'],
                        success:function(){
                            var ed = d.emptyDiscount*10;
                            var bd = d.busyDiscount*10;
                            d.emptyDiscount = Math.round(ed * 100) / 100;
                            d.busyDiscount = Math.round(bd * 100) / 100;
                            datastore.publishFormData($("#addWin"), d);

                        },
                        yes: function (layero, index) {
                            if ($('#form').valid()) {
                                $("#form").ajaxSubmit({
                                    success: function (d) {
                                        if (d.code == 200) {
                                            module.datatable.fnReloadAjax();
                                            layer.closeAll();
                                        } else {
                                            layer.msg(d.message);
                                        }
                                    }
                                })
                            }
                        },
                        cancel: function (index, layero) {
                            layer.close(index);
                        }
                    });
                }else{
                    layer.msg("更新数据失败");
                }
            });
        },

        validateForm: function () {
            var validate = $('#form').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    name: {
                        required: true
                    },
                    rank: {
                        required: true
                    },
                    emptyDiscount: {
                        required: true,
                        number: true
                    },
                    busyDiscount: {
                        required: true,
                        number: true
                    },
                    titleAccount: {
                        number: true
                    },
                    price: {
                        number: true
                    },
                    payDate: {
                        digits: true
                    },
                    last: {
                        digits: true
                    }
                },
                messages: {
                    name: {
                        required: "请输入名称 "
                    },
                    rank: {
                        required: "请输入前缀 "
                    },
                    emptyDiscount: {
                        required: "请输入闲时折扣",
                        number: "必须合法的数字"
                    },
                    busyDiscount: {
                        required: "请输入忙时折扣",
                        number: "必须合法的数字"
                    },
                    titleAccount: {
                        number: "必须合法的数字"
                    },
                    price: {
                        number: "必须合法的数字"
                    },
                    payDate: {
                        digits: "必须输入整数"
                    },
                    last: {
                        number: "必须合法的数字"
                    }
                },
                highlight: function (element) { // hightlight error inputs
                    $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
                },
                unhighlight: function (element) { // revert the change done by hightlight
                    $(element).closest('.form-group').removeClass('has-error'); // set error class to the control group
                },
                success: function (label) {
                    label.closest('.form-group').removeClass('has-error');
                },
                errorPlacement: function (error, element) {
                    if (element.parent(".input-group").size() > 0) {
                        error.insertAfter(element.parent(".input-group"));
                    } else if (element.attr("data-error-container")) {
                        error.appendTo(element.attr("data-error-container"));
                    } else if (element.parents('.radio-list').size() > 0) {
                        error.appendTo(element.parents('.radio-list').attr("data-error-container"));
                    } else if (element.parents('.radio-inline').size() > 0) {
                        error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
                    } else if (element.parents('.checkbox-list').size() > 0) {
                        error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
                    } else if (element.parents('.checkbox-inline').size() > 0) {
                        error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
                    } else {
                        error.insertAfter(element); // for other inputs, just perform default behavior
                    }
                }
            });
            return validate;
        }
    }
    return module;
});