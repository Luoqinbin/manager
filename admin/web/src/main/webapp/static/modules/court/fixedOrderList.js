/**
 * Created by Luoqb on 2017/3/16.
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
            listUrl: 'fixedOrder/queryList',

            deleteUrl: 'fixedOrder/delete'
        },

        initPage: function () {
            module.__gatherEvent();
            module.__initDataTable();
            validate = module.validateForm();
        },
        __gatherEvent:function () {
            $("#add").bind("click",function(){
                $("#userId").val("");
                module.add();
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
                {"mData": "startDate"},
                {"mData": "endDate"},
                {"mData": "startTime"},
                {"mData": "endTime"},
                {"mData": "cycle"},
                {"mData": "areaStr"},
                {"mData": "noStr"},
                {"mData": "type"},
                {"mData": "price"},
                {"mData": "consume"},
                {"mData": "name"},
                {"mData": "phone"},
                {"mData": "payWay"},
                {"mData": "comments"}
            ];
            var aoColumnDefs = [{
                "aTargets": [1],
                "mRender": function (a, b, c, d) {
                    return  moment(a).format("YYYY-MM-DD");
                }
            },
            {
                "aTargets": [2],
                "mRender": function (a, b, c, d) {
                    return  moment(a).format("YYYY-MM-DD");
                }
            },{
                "aTargets": [5],
                "mRender": function (a, b, c, d) {
                    var index = a.indexOf(",");
                    var str ;
                    if(index>-1){
                         str = a.split(",");
                         var date = "";
                         for (var i=0;i<str.length;i++){
                             date += module.strDay(str[i])+"/";
                         }
                         return date.substring(0,date.length-1);
                    }else{
                        return module.strDay(a);
                    }
                }
            },{
                    "aTargets": [8],
                    "mRender": function (a, b, c, d) {
                        if(a==1){
                            return "固定场";
                        }else if(a==2){
                            return "培训场";
                        }else if(a==3){
                            return "赛事场";
                        }
                    }
            },{
                    "aTargets": [13],
                    "mRender": function (a, b, c, d) {
                        if(a==1){
                            return "现金";
                        }else if(a==2){
                            return "刷卡";
                        }else if(a==3){
                            return "转账-农商行0549";
                        }else if(a==4){
                            return "转账-中信";
                        }else if(a==5){
                            return "支付宝";
                        }else if(a==6){
                            return "储值卡";
                        }
                    }
                }];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },

        strDay:function (index) {
            if(index ==1){
                return "周一";
            }else if(index ==2){
                return "周二";
            }else if(index ==3){
                return "周三";
            }else if(index ==4){
                return "周四";
            }else if(index ==5){
                return "周五";
            }else if(index ==6){
                return "周六";
            }else if(index ==7){
                return "周日";
            }
        },

        handler:{
            __queryHandler:function(condition){


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
            $("#form")[0].reset();
            validate.resetForm();
            $("#startTime").select2('val','');
            $("#endTime").select2('val','');
            $("#courtInfoId").select2('val','');
            $("#type").select2('val','');
            $("#payWay").select2('val','');
            util.openLayer({
                area: ['550px', '800px'],
                title: "新增",
                type: 1,
                content: $("#addWin"),
                btn: ['添加', '关闭'],
                success: function (layero, index) {
                    $("#startTime").empty();
                    var option = "";
                    for(var i=9;i<23;i++){
                        option+="<option value='"+i+"'>"+i+":00</option>";
                    }
                    $("#startTime").append(option);
                    option = "";
                    for(var i=9;i<23;i++){
                        option+="<option value='"+i+"'>"+i+":00</option>";
                    }
                    $("#endTime").empty();
                    $("#endTime").append(option);
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
                                    layer.alert(d.message);
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


        validateForm: function () {
            var validate = $('#form').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    cycle: {
                        required: true
                    },
                    startDateStr: {
                        required: true
                    },
                    endDateStr: {
                        required: true
                    },
                    startTime: {
                        required: true
                    },
                    endTime: {
                        required: true
                    },
                    courtInfoId:{
                        required: true
                    },
                   /* area: {
                        required: true
                    },
                    no: {
                        required: true
                    },*/
                    type: {
                        required: true
                    },
                    price: {
                        required: true,
                        number: true
                    },
                    consume: {
                        number: true
                    },
                    name: {
                        required: true
                    },
                    phone: {
                        required: true,
                        minlength : 11,
                        isMobile: true
                    },
                    payWay: {
                        required: true
                    }
                },
                messages: {
                    cycle: {
                        required: "周期不能为空"
                    },
                    startDateStr: {
                        required: "开始日期不能为空"
                    },
                    endDateStr: {
                        required: "结束日期不能为空"
                    },
                    startTime: {
                        required: "开始时间不能为空"
                    },
                    endTime: {
                        required: "结束时间不能为空"
                    },
                    courtInfoId:{
                        required: "区域不能为空"
                    },
                    /*area: {
                        required: "区域不能为空"
                    },
                    no: {
                        required: "场地不能为空"
                    },*/
                    type: {
                        required: "类型不能为空"
                    },
                    price: {
                        required: "单价不能为空",
                        number: "请输入正确的价格"
                    },
                    consume: {
                        number: "请输入正确的金额"
                    },
                    name: {
                        required: "预定人不能为空"
                    },
                    phone: {
                        required: "请输入电话号码",
                        minlength : "确认手机不能小于11个字符",
                        isMobile: "请输入正确的手机号"
                    },
                    payWay: {
                        required: "支付方式不能为空"
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