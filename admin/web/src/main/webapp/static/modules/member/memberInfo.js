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
    var validateUpdate = null;
    var module = {
        constants:{
            listUrl: 'memberInfo/queryList',
            getById: 'memberInfo/queryById',
            deleteUrl: 'memberInfo/delete',
            getCardeUrl: 'memberCard/queryById'
        },
        initPage: function () {
            module.__gatherEvent();
            module.__initDataTable();
            validate = module.validateForm();
            validateUpdate = module.validateUpdateForm();
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
            $("#import").bind("click",function () {
                $("#uploadForm")[0].reset();
                    util.openLayer({
                        area: '800px',
                        shade: [0.8, '#393D49'],
                        title: "导入",
                        type: 1,
                        content: $("#uploadWin"),
                        btn: ['导入', '关闭'],
                        yes: function (layero, index) {
                            var file = $("#xlsFile").val();
                            var fileext=file.substring(file.lastIndexOf("."),file.length)
                            fileext=fileext.toLowerCase();
                            if (fileext=='.xls'){
                                var header = "X-CSRF-TOKEN";
                                var token = $("#csrfToken").val();
                                $("#uploadForm").ajaxSubmit({
                                    beforeSend: function (xhr) {
                                        xhr.setRequestHeader(header, token);
                                    },
                                    success: function (d) {
                                        if (d.code == 200) {
                                            module.datatable.fnReloadAjax();
                                            layer.closeAll();
                                            if(d.data!=""){
                                                layer.alert("第"+d.data+"数据导入失败");
                                            }else{
                                                layer.msg("数据导入成功");
                                            }
                                        } else {
                                            layer.msg(d.data);
                                        }
                                    }
                                })
                            }else{
                                layer.msg("对不起，导入数据格式必须是xls格式文件哦，请您调整格式后重新上传，谢谢 ！");
                            }

                        },
                        cancel: function (index, layero) {
                            layer.close(index);
                        }
                    });
            });

            $("#delete").bind("click",function () {
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行数据 ");
                }else{
                    module.delete(data);
                }
            });
            $("#type").bind("change",function () {
               var typeId = $(this).val();
                var data = util.getTokenData();
                dojo.mixin(data, {id: typeId});
                util.post(module.constants.getCardeUrl, data).then(function (data) {
                    if (data.code == 200) {
                       $("#number").val(data.data.maxNumber);
                        $("#carPrice").val(data.data.titleAccount);
                    }
                });
            });
        },
        __initDataTable:function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "number"},
                {"mData": "typeName"},
                {"mData": "phone"},
                {"mData": "name"},
                {"mData": "cratedDt"},
                {"mData": "expireDt"},
                {"mData": "carPrice"},
                {"mData": "account"},
                {"mData": "weichat"},
                {"mData": "comments"}
            ];
            var aoColumnDefs = [{
                "aTargets": [5],
                "mRender": function (a, b, c, d) {
                    return  moment(a).format("YYYY-MM-DD HH:mm:ss");
                }
            },{
                "aTargets": [6],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        return  moment(a).format("YYYY-MM-DD HH:mm:ss");
                    else
                        return "";
                }
            },{
                "aTargets": [9],
                "mRender": function (a, b, c, d) {
                    if (util.assertNotNullStr(a))
                        return "已绑定";
                    else
                        return "未绑定";
                }
            }];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },

        handler:{
            __queryHandler:function(condition){
                var phoneQuery = $("#phoneQuery").val();
                var typeQuery = $("#typeQuery").val();
                var numberQuery = $("#numberQuery").val();
                if (util.assertNotNullStr(phoneQuery)) condition.phone = phoneQuery;
                if (util.assertNotNullStr(typeQuery)) condition.type = typeQuery;
                if (util.assertNotNullStr(numberQuery)) condition.number = numberQuery;

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
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "新增",
                type: 1,
                content: $("#addWin"),
                btn: ['添加', '关闭'],
                success: function (layero, index) {
                    $("#form")[0].reset();
                    validate.resetForm();
                    $("#type").val("");
                    //$("#cratedDt").val(moment(new Date()).format("YYYY-MM-DD"));
                },
                yes: function (layero, index) {
                    if ($("#form").valid()) {
                        $("#form").ajaxSubmit({
                            success: function (d) {
                                layer.msg(d.message);
                                if (d.code == 200) {
                                    module.datatable.fnReloadAjax();
                                    layer.closeAll();
                                } else {

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
            validateUpdate.resetForm();
            $("#updateform")[0].reset();
            util.post(module.constants.getById, dataUpdate).then(function (res) {
                if (res.code == 200) {
                    var d = res.data;
                    layer.open({
                        area: '800px',
                        shade: [0.8, '#393D49'],
                        title: "修改",
                        type: 1,
                        content: $("#updateWin"),
                        btn: ['确定', '关闭'],
                        success:function(){
                            datastore.publishFormData($("#updateWin"), d);
                        },
                        yes: function (layero, index) {
                            if ($('#updateform').valid()) {
                                $("#updateform").ajaxSubmit({
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
        validateUpdateForm: function () {
            var validate = $('#updateform').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {

                    phone: {
                        required: true,
                        minlength : 11,
                        isMobile: true
                    },
                    name: {
                        required: true
                    }
                },
                messages: {

                    phone: {
                        required: "请输入电话号码",
                        minlength : "确认手机不能小于11个字符",
                        isMobile: "请输入正确的手机号"
                    },
                    name: {
                        required: "请输入姓名"
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
        },
        validateForm: function () {
            var validate = $('#form').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    type:{
                        required: true
                    },
                    phone: {
                        required: true,
                        minlength : 11,
                        isMobile: true
                    },
                    name: {
                        required: true
                    },
                    payWay:{
                        required: true
                    }
                },
                messages: {
                    type:{
                        required: "请选择卡类型"
                    },
                    phone: {
                        required: "请输入电话号码",
                        minlength : "确认手机不能小于11个字符",
                        isMobile: "请输入正确的手机号"
                    },
                    name: {
                        required: "请输入姓名"
                    },
                    payWay:{
                        required: "请选择支付方式"
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