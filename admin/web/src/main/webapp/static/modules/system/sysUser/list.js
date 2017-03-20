define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore"
], function (dojo, util, table, datastore) {
    var validate;
    var module = {
        constants: {
            listUrl: 'sysUser/queryList',
            getById: 'sysUser/queryById',
            deleteUrl: 'sysUser/deleteUser',
            queryRole: 'sysUser/querySysRole',
            restPwd: 'sysUser/restPwd'
        },
        initPage: function () {
            module.__initDataTable();
            module.__gatherEvent();
            validate = module.validateForm();
        },

        __loadSelect: function () {
            $('#roleId').select2({
                placeholder: "请选择角色",
                allowClear: true,
                ajax: {
                    url: module.constants.queryRole,
                    cache: true,
                    processResults: function (data) {
                        return {
                            results: data
                        };
                    }
                }
            });
        },
        __loadSelectUpdate: function () {
            $('#roleId').select2({
                placeholder: "请选择角色",
                allowClear: true,
                ajax: {
                    url: module.constants.queryRole,
                    cache: true,
                    processResults: function (data) {
                        return {
                            results: data
                        };
                    }
                }
            });
        },
        __gatherEvent: function () {
            //查询按钮
            $("#query").click(function () {
                module.datatable.fnReloadAjax();
            });
            //添加
            $("#add").bind("click", function () {
                $("#userId").val("");
                $("#passwordPwd").show();
               layer.open({
                   area: ['500px', '500px'],
                   title: "添加用户",
                   type: 1,
                   content: $("#addWin"),
                   btn: ['添加', '关闭'],
                   success: function (layero, index) {
                       $("#form")[0].reset();
                       validate.resetForm();
                       module.__loadSelect();
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
            });
            $("#update").bind("click",function () {
                var data = table.getRowData("#dataTables-example");
                if(data==undefined){
                    layer.msg("请选择一行");
                }else{
                    module.update(data);
                }
            });
            $("#del").bind("click",function () {
                var data = table.getRowData("#dataTables-example");
                if(data==undefined){
                    layer.msg("请选择一行");
                }else{
                    module.delete(data);
                }
            });
            $("#restPwd").bind("click",function () {
                var data = table.getRowData("#dataTables-example");
                if(data==undefined){
                    layer.msg("请选择一行");
                }else{
                    module.restPwd(data);
                }
            });

        },
        __initDataTable: function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "username"},
                {"mData": "name"},
                {"mData": "lastLogin"},
                {"mData": "loginIp"},
                {"mData": "outLoginTime"},
                {"mData": "roleName"}
            ];
            var aoColumnDefs = [];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            var t = $("#dataTables-example");
            module.datatable = table.initPageTable(t, module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);


        },
        delete:function (d) {
            var data = module.getTokenData();
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

        update:function(data){
            var dataUpdate = module.getTokenData();
            dojo.mixin(dataUpdate, {id: data.id});
            validate.resetForm();
            util.post(module.constants.getById, dataUpdate).then(function (data) {
                if (data.code == 200) {
                    var d = data.data;
                    $("#username").val(d.user.username);
                    $("#name").val(d.user.name);
                    $("#userId").val(d.user.id);
                    $("#passwordPwd").hide();
                    var option = "<option value='" + d.role.id + "' selected='selected'>" + d.role.role_name + "</option>";
                    $("#roleId").empty();
                    $("#roleId").append(option);
                    layer.open({
                        area: ['500px', '450px'],
                        title: "修改",
                        type: 1,
                        content: $("#addWin"),
                        btn: ['确定', '关闭'],
                        success: function (layero, index) {
                            module.__loadSelectUpdate();
                        },
                        yes: function (layero, index) {
                            if ($('#form').valid()) {
                                $("#form").ajaxSubmit({
                                    success: function (d) {
                                        if (d.code == 200) {
                                            layer.closeAll();
                                            module.datatable.fnReloadAjax();
                                        } else {
                                            layer.msg("更新数据失败");
                                        }
                                    }
                                })
                            }
                        },
                        cancel: function (index, layero) {
                            layer.close(index);
                        }
                    });
                } else {
                    layer.alert("获取数据失败");
                }

            });
        },
        restPwd:function (d) {
            var data = module.getTokenData();
            dojo.mixin(data, {id: d.id});
            layer.confirm("此操作会重置密码为：666666，是否继续？", function (index) {
                util.post(module.constants.restPwd, data).then(function (data) {
                    layer.msg("重置成功");
                    layer.close(index);
                }, "json");

            });
        },
        handler: {
            __queryHandler: function (condition) {
                var username = $("#usernameQuery").val();
                var name = $("#nameQuery").val();
                if (util.assertNotNullStr(username)) condition.username = username;
                if (util.assertNotNullStr(name)) condition.name = name;
            },
            __initHandler: function () {
                /**
                 * 删除
                 */
               /* $("#dataTables-example tbody").on("click", "button[name='del']", function () {
                    var table = $('#dataTables-example').DataTable();
                    var d = table.row($(this).parents('tr')).data();
                    var data = module.getTokenData();
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
                });*/
                /**
                 * 修改
                 */

                /**
                 * 重置密码
                 */
            }
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
        validateForm: function () {
            var validate = $('#form').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    username: {
                        required: true
                    },
                    name: {
                        required: true
                    },
                    password: {
                        required: true,
                        minlength: 6,
                        maxlength: 10
                    },
                    roleId: {
                        required: true
                    }
                },
                messages: {
                    username: {
                        required: "用户名不能为空!"
                    },
                    name: {
                        required: "姓名不能为空!"
                    },
                    password: {
                        required: "密码不能为空!"
                    },
                    roleId: {
                        required: "角色不能为空!"
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