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
            listUrl: 'memberInfo/queryList',
            getById: 'memberInfo/queryById'
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
            $("#recharge").bind("click",function(){
                $("#userId").val("");
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行数据 ");
                }else{
                    module.add(data);
                }
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

        add:function (data) {
            var dataUpdate = util.getTokenData();
            dojo.mixin(dataUpdate, {id: data.id});
            validate.resetForm();
            $("#form")[0].reset();
            util.post(module.constants.getById, dataUpdate).then(function (res) {
                if (res.code == 200) {
                    var d = res.data;
                    util.openLayer({
                        area: ['500px', '600px'],
                        title: "新增",
                        type: 1,
                        content: $("#addWin"),
                        btn: ['添加', '关闭'],
                        success: function (layero, index) {
                            $("#form")[0].reset();
                            datastore.publishFormData($("#form"), d);
                            $("#expiredt").val(moment(d.expireDt).format("YYYY-MM-DD"));
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
                }
            });
        },
        validateForm: function () {
            var validate = $('#form').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    rechargePrice:{
                        required: true,
                        number:true
                    },
                    payWay:{
                        required: true
                    }
                },
                messages: {
                    rechargePrice:{
                        required: "请输入充值金额",
                        number:"请输入正确的金额"
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