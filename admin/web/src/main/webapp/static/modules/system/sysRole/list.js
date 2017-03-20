define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore"
], function (dojo, util, table, datastore) {
    var validate;
    var module = {
        constants: {
            listUrl: 'sysRole/queryList',
            deleteUrl: 'sysRole/delete',
            getById: 'sysRole/getById',
            jsTree: 'sysRole/getTree'
        },
        initPage: function () {
            module.__gatherEvent();
            module.__initDataTable();
            validate = module.validateForm();
        },
        __gatherEvent: function () {
            //查询按钮
            $("#query").click(function () {
                module.datatable.fnReloadAjax();
            });
            $("#add").click(function () {
                $("#userId").val("");
                layer.open({
                    area: ['500px', '400px'],
                    title: "新增角色",
                    type: 1,
                    content: $("#win"),
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
            });

            $("#update").bind("click",function () {
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行");
                }else{
                    module.update(data);
                }
            });
            $("#del").bind("click",function () {
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行");
                }else{
                    module.delete(data);
                }
            });
            $("#settings").bind("click",function () {
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行");
                }else{
                    module.showTree(data);
                }
            });

        },
        __initDataTable: function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "role_name"},
                {"mData": "role_desc"},
                {"mData": "role_auth"}
            ];
            var aoColumnDefs = [];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);
        },

        update:function(data){
            var dataUpdate = module.getTokenData();
            dojo.mixin(dataUpdate, {id: data.id});
            validate.resetForm();
            util.post(module.constants.getById, dataUpdate).then(function (data) {
                if (data.code == 200) {
                    var d = data.data;
                    $("#userId").val(d.id);
                    $("#role_name").val(d.role_name);
                    $("#role_desc").val(d.role_desc);
                    $("#role_auth").val(d.role_auth);
                    layer.open({
                        area: ['500px', '410px'],
                        title: "修改",
                        type: 1,
                        content: $("#win"),
                        btn: ['确定', '关闭'],
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
                } else {
                    layer.msg("更新数据失败");
                }
            });
        },
        delete:function(d){
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
        showTree:function (data) {
            module.loadTree(data.id);
        },

        handler: {
            __queryHandler: function (condition) {
                var roleName = $("#roleNameQuery").val();
                if (util.assertNotNullStr(roleName)) condition.role_name = roleName;
            },
            __initHandler: function () {

            }
        },
         ztreeSetting :function(roleId) {
             var setting = {
                 check: {
                     enable: true
                 },
                 data: {
                     simpleData: {
                         enable: true,
                         idKey: "id",
                         pIdKey: "pId",
                         rootPId: null
                     }
                 },
                 async: {
                     enable: true,//开启异步加载
                     url:"data.jsp",//设置异步获取节点的 URL 地址
                     autoParam:["roleId"]//设置父节点数据需要自动提交的参数
                 },
                 callback: {
                     onClick: function(event, treeId, treeNode){//点击事件的触发函数
                         module.showButtonResource(event, treeId, treeNode)
                     }
                 }
             };
             return setting;
        },
        loadTree:function (id) {
            $("#buttonTitle").empty();
            $('#showBtnDiv').empty();
            $("#roleId").val(id);
            //加载资源
            var csrfName = $("#csrfName").val();
            var csrfToken = $("#csrfToken").val();
            var data = {};
            data[csrfName] = csrfToken;
            data["roleId"] = id;
            $.ajax({
                type: "POST",
                dataType: "json",
                async: false,
                url: "sysRole/getResource",
                data:data,
                success: function (data) {
                    if (data.code == 200) {
                        zNodes = data.data;
                    }
                }
            });
            //加载菜单
            $.fn.zTree.init($("#treeDemo"), module.ztreeSetting(), zNodes);
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.expandAll(true);
            var betBot = layer.open({
                area: ['600px', '500px'],
                title: "角色权限",
                type: 1,
                content: $("#treeWin"),
                btn: ['保存', '关闭'],

                yes: function (layero, index) {
                    //保存触发
                    //1.获取菜单资源
                    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
                    var nodes = treeObj.getCheckedNodes(true);
                    var resourceValue = new Array();
                    for (var i = 0; i < nodes.length; i++) {
                        resourceValue.push(nodes[i].id);
                    }
                    //2.获取按钮资源
                    var btnValue = new Array();//定义一个数组
                    ;
                    $("input[name='resourceBtn']:checked").each(function () {//遍历每一个名字为interest的复选框，其中选中的执行函数
                        btnValue.push($(this).val());//将选中的值添加到数组chk_value中
                    });
                    var csrfName = $("#csrfName").val();
                    var csrfToken = $("#csrfToken").val();
                    var data = {};
                    data[csrfName] = csrfToken;
                    data["roleId"] = id;
                    data["resourceValue"] = resourceValue;
                    data["resourceId"] = $("#resourceId").val();
                    data["btnValue"] = btnValue;
                    //保存
                    $.ajax({
                        type: 'post',
                        traditional: true,
                        url: 'sysRole/saveResource',
                        data: data,
                        success: function (data) {
                            if (data.code == 200) {
                                layer.msg('保存成功');
                            }
                        }
                    });
                },
                cancel: function (index, layero) {
                    //关闭触发
                    layer.close(index);
                }
            });
        },
        showButtonResource:function (event, treeId, treeNode) {
            $("#buttonDiv").show();
            $("#buttonTitle").html(treeNode.name);
            $("#resourceId").val(treeNode.id);
            //加载按钮资源
            var data = {};
            var csrfName = $("#csrfName").val();
            var csrfToken = $("#csrfToken").val();
            data[csrfName] = csrfToken;
            data["partentId"] = treeNode.id;
            data["roleId"] = $("#roleId").val();
            $.post("sysRole/getResourceBtn", data, function (data) {
                if (data.code == 200) {
                    $('#showBtnDiv').empty();
                    var btnList = data.data;
                    if (btnList.length > 0) {
                        var html = "";
                        for (var i = 0; i < btnList.length; i++) {
                            if (btnList[i].isCheck == 1) {
                                html += "<div class=\"checkbox\"><label><input name=\"resourceBtn\" checked=\"checked\" type=\"checkbox\" value=\"" + btnList[i].id + "\"/>" + btnList[i].name + "</label><br/></div>";
                            } else {
                                html += "<div class=\"checkbox\"><label><input name=\"resourceBtn\" type=\"checkbox\" value=\"" + btnList[i].id + "\"/>" + btnList[i].name + "</label><br/></div>";
                            }
                        }
                        $('#showBtnDiv').append(html);
                    } else {
                        $('#showBtnDiv').append("<label>无数据</label>");
                    }
                }
            }, "json");
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
                    role_name: {
                        required: true
                    },
                    role_auth: {
                        required: true
                    }
                },
                messages: {
                    role_name: {
                        required: "角色名称不能为空!"
                    },
                    role_auth: {
                        required: "角色代码不能为空!"
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
})