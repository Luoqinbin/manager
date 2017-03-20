define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore",
    "static/js/moment"
], function (dojo, util, table, datastore,moment) {
    var validate;
    var module = {
        constants: {
            listUrl: 'resource/queryResources',
            queryResource: 'resource/queryResourceParent',
            deleteUrl: 'resource/delResource',
            getById: 'resource/queryResourceForId'
        },
        initPage: function () {
            module.__initDataTable();
            module.__gatherEvent();
            validate = module.validateForm();
        },
        __loadSelect:function(){
            $('#resource_parentSelect').select2({
                placeholder: "请选择上级资源",
                allowClear: true,
                ajax: {
                    url: module.constants.queryResource,
                    data:module.getTokenData(),
                    cache: true,
                    dataType : "json",
                    data: function (term) {  // 请求参数（GET）
                        return { "ffData": term.term };
                    },
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
                var resourceNameQuery = $("#resourceNameQuery").val();
                if (util.assertNotNullStr(resourceNameQuery)){
                    //刷新
                    $.ui.fancytree.getTree("#dataTables-example").reload([
                        {title:resourceNameQuery}
                    ]);

                }else{
                    $.ui.fancytree.getTree("#dataTables-example").reload();
                }

            });
            //添加
            $("#add").bind("click", function () {
                $("#resource_parentDiv").hide();
                var node = $("#dataTables-example").fancytree("getActiveNode");
                var resource_parent = "";
                if(node==null){
                    resource_parent = "0";
                }else{
                    resource_parent = node.data.id;
                }
                $("#resource_parent").val(resource_parent);
                layer.open({
                    area: ['600px', '750px'],
                    title: "新增资源",
                    type: 1,
                    content: $("#addWin"),
                    btn: ['添加', '关闭'],
                    success: function (layero, index) {
                        $("#form")[0].reset();
                        $("#userId").val("");
                        validate.resetForm();
                    },
                    yes: function (layero, index) {
                        if ($("#form").valid()) {
                            $("#form").ajaxSubmit({
                                success: function (d) {
                                    if (d.code == 200) {
                                        layer.msg("保存数据成功");
                                        $.ui.fancytree.getTree("#dataTables-example").reload();
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

            $("#delete").bind("click",function(){
                var node = $("#dataTables-example").fancytree("getActiveNode");
                if(node==null){
                    layer.msg("请选择一条数据");
                }else{
                    module.delete(node.data.id);
                    $.ui.fancytree.getTree("#dataTables-example").reload();
                }

            });
            $("#update").bind("click",function(){
                var node = $("#dataTables-example").fancytree("getActiveNode");
                if(node==null){
                    layer.msg("请选择一条数据");
                }else{
                    module.update(node.data.id);
                }
            });

        },
        __initDataTable:function(){
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
           // module.datatable = table.initPageTable($("#dataTables-example"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);
           var glyph_opts = {
                map: {
                    doc: "glyphicon glyphicon-file",
                    docOpen: "glyphicon glyphicon-file",
                    checkbox: "glyphicon glyphicon-unchecked",
                    checkboxSelected: "glyphicon glyphicon-check",
                    checkboxUnknown: "glyphicon glyphicon-share",
                    dragHelper: "glyphicon glyphicon-play",
                    dropMarker: "glyphicon glyphicon-arrow-right",
                    error: "glyphicon glyphicon-warning-sign",
                    expanderClosed: "glyphicon glyphicon-menu-right",
                    expanderLazy: "glyphicon glyphicon-menu-right",  // glyphicon-plus-sign
                    expanderOpen: "glyphicon glyphicon-menu-down",  // glyphicon-collapse-down
                    folder: "glyphicon glyphicon-folder-close",
                    folderOpen: "glyphicon glyphicon-folder-open",
                    loading: "glyphicon glyphicon-refresh glyphicon-spin"
                }
            };
            $("#dataTables-example").fancytree({
                extensions: ["dnd", "edit", "glyph", "table"],
                checkbox: false,
                glyph: glyph_opts,
                source: {url: "resource/queryResources?"+data},
                table: {
                    nodeColumnIdx: 0
                },

                activate: function(event, data) {
                   // console.log(data);
                },
                renderColumns: function(event, data) {
                    var d = data.node.data;
                    var node = data.node,
                        $tdList = $(node.tr).find(">td");
                        $tdList.eq(1).text(d.resource_type);
                        $tdList.eq(2).text(d.resource_desc);
                        $tdList.eq(3).text(d.resource_path);
                        $tdList.eq(4).text(d.enable==1?"可用":"不可用");
                        $tdList.eq(5).text(d.order_no);
                        $tdList.eq(6).text(d.resource_level);
                        $tdList.eq(7).text(moment(d.create_time).format('YYYY-MM-DD HH:mm:ss'));
                }
            });
        },
        handler: {
            __initHandler:function(){


            },
            __queryHandler: function (condition) {
                var resourceNameQuery = $("#resourceNameQuery").val();
                if (util.assertNotNullStr(resourceNameQuery)) condition.resource_name = resourceNameQuery;
            }
        },

        delete:function(id){
            var data = module.getTokenData();
            dojo.mixin(data, {id: id});
            layer.confirm("你确定要删除该数据吗？", function (index) {
                util.post(module.constants.deleteUrl, data).then(function (data) {
                    layer.msg(data.message);
                    if (data.code == 200) {
                        $.ui.fancytree.getTree("#dataTables-example").reload();
                        layer.msg("删除成功！");
                    }
                });
            });
        },

        update:function(id){
            var dataUpdate = module.getTokenData();
            dojo.mixin(dataUpdate, {id: id});
            validate.resetForm();
            $("#resource_parentDiv").show();
            util.post(module.constants.getById, dataUpdate).then(function (data) {
                if (data.code == 200) {
                    var d = data.data;
                    layer.open({
                        area: ['600px', '750px'],
                        title: "修改",
                        type: 1,
                        content: $("#addWin"),
                        btn: ['确定', '关闭'],
                        success: function (layero, index) {
                            datastore.publishFormData($("#addWin"), d);
                            var option = "";
                            if(d.parentName==null){
                                option = "<option value='0' selected='selected'>根节点</option>";
                            }else{
                                option = "<option value='" + d.resource_parent + "' selected='selected'>" + d.parentName + "</option>";
                            }
                            var p = $("#resource_parentSelect");
                            p.empty();
                            p.append(option);
                            module.__loadSelect();
                        },
                        yes: function (layero, index) {
                            if ($('#form').valid()) {
                                $("#form").ajaxSubmit({
                                    success: function (d) {
                                        if (d.code == 200) {
                                            layer.closeAll();
                                            $.ui.fancytree.getTree("#dataTables-example").reload();
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
                resource_type: {
                    required: true
                },
                resource_name: {
                    required: true,
                },
                resource_desc: {
                    required: true
                },
                resource_path: {
                    required: true
                },
                resource_level: {
                    required: true,
                    number:true
                },
                order_no: {
                    number:true
                }
            },
            messages: {
                resource_type: {
                    required: "资源类型不能为空"
                },

                resource_name: {
                    required: "资源名称不能为空",
                },
                resource_desc: {
                    required: "资源描述不能为空"
                },
                resource_path: {
                    required: "资源路径不能为空"
                },
                resource_level: {
                    required: "资源等级不能为空",
                    number:"请输入合法的数字"
                },
                order_no: {
                    number:"请输入合法的数字"
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