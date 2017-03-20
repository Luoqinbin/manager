define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore"
], function (dojo, util, table, datastore) {
    var validate = null;
    var module = {
        constants:{
            listUrl: 'testCrud/queryList',
            getById: 'testCrud/queryById',
            deleteUrl: 'testCrud/delete'
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
                {"mData": "testName1"},
                {"mData": "testName2"},
                {"mData": "testName3"}
            ];
            var aoColumnDefs = [];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },
        
        handler:{
            __queryHandler:function(condition){
                var testName1Query = $("#testName1Query").val();
                var testName2Query = $("#testName2Query").val();
                var testName3Query = $("#testName3Query").val();
                if (util.assertNotNullStr(testName1Query)) condition.testName1 = testName1Query;
                if (util.assertNotNullStr(testName2Query)) condition.testName2 = testName2Query;
                if (util.assertNotNullStr(testName3Query)) condition.testName3 = testName3Query;

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
                area: ['500px', '400px'],
                title: "新增",
                type: 1,
                content: $("#addWin"),
                btn: ['添加', '关闭'],
                success: function (layero, index) {
                    $("#form")[0].reset();
                    validate.resetForm();
                },
                yes: function (layero, index) {
                    if ($("#form").valid()) {
                        $("#form").ajaxSubmit({
                            success: function (d) {
                                if (d.code == 200) {
                                    layer.msg("添加数据成功");
                                    module.datatable.fnReloadAjax();
                                    layer.closeAll();
                                } else {
                                    layer.msg(d.data);
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
                        area: ['500px', '410px'],
                        title: "修改",
                        type: 1,
                        content: $("#addWin"),
                        btn: ['确定', '关闭'],
                        success:function(){
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
                    testName1: {
                        required: true
                    },
                    testName2: {
                        required: true
                    },
                    testName3: {
                        required: true
                    }
                },
                messages: {
                    testName1: {
                        required: "测试一不能为空!"
                    },
                    testName2: {
                        required: "测试二不能为空!"
                    },
                    testName3: {
                        required: "测试三不能为空!"
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
    };

    return module;
});