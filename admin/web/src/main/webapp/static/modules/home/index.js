/**
 * Created by Luoqb on 2017/5/2.
 */
define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/tools/datastore",
    "modules/common/tools/moment"
], function (dojo, util,datastore,moment) {
    var validate = null;
    var module = {
        initPage: function () {
            validate = module.validateForm();
            //办理新卡
            $("#newCard").bind("click",function () {
                $("#userId").val("");
                module.add();
            });
            //充值
            $("#recharge").bind("click",function () {
                module.initRecharge();
            });
            $("#type").bind("change",function () {
                var typeId = $(this).val();
                var data = util.getTokenData();
                dojo.mixin(data, {id: typeId});
                util.post("memberCard/queryById", data).then(function (data) {
                    if (data.code == 200) {
                        $("#number").val(data.data.maxNumber);
                        $("#carPrice").val(data.data.titleAccount);
                    }
                });
            });
            module.initTable();
        },
        initRecharge:function () {
            util.openLayer({
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "充值",
                type: 1,
                content: $("#rechargeInit"),
                btn: ['充值', '关闭'],
                success: function (layero, index) {
                    $("#rechargeInitForm")[0].reset();
                },
                yes: function (layero, index) {
                    var number = $("#numberCard").val();
                    var phone = $("#phoneCard").val();
                    var data = util.getTokenData();
                    dojo.mixin(data, {"number": number,"phone":phone});
                    util.post("home/queryInitRecharge", data).then(function (res) {
                        if (res.code == 200) {
                            var d = res.data;
                            layer.closeAll();
                            util.openLayer({
                                area: '800px',
                                shade: [0.8, '#393D49'],
                                title: "充值",
                                type: 1,
                                content: $("#recharge2"),
                                btn: ['充值', '关闭'],
                                success: function (layero, index) {
                                    $("#rechargeForm")[0].reset();
                                    datastore.publishFormData($("#rechargeForm"), d);
                                    $("#expiredt").val(moment(d.expireDt).format("YYYY-MM-DD"));
                                },
                                yes: function (layero, index) {
                                    if ($("#rechargeForm").valid()) {
                                        $("#rechargeForm").ajaxSubmit({
                                            success: function (d) {
                                                if (d.code == 200) {
                                                    layer.closeAll();
                                                    layer.msg("充值成功");

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
                cancel: function (index, layero) {
                    layer.close(index);
                }
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
                    $("#status").val("");
                },
                yes: function (layero, index) {
                    if ($("#form").valid()) {
                        $("#form").ajaxSubmit({
                            success: function (d) {
                                layer.msg(d.message);
                                if (d.code == 200) {
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
        initTable:function () {
            var data = util.getTokenData();
            dojo.mixin(data, {"area": "4F","time":"2017-05-02"});
            util.post("home/queryByHomeTable", data).then(function (res) {
                if (res.code==200){
                    var d = res.data;
                    var list4FArea = d.list4FArea;
                    var list4FProduct = d. list4FProduct;
                    var th = $("#th");
                    th.empty();
                    var thStr = "<th></th>";
                    for(var i=0;i<list4FArea.length;i++){
                        thStr+="<th>"+list4FArea[i].area+" - "+list4FArea[i].name+"</th>"
                    }
                    th.append(thStr);
                    var tbody = $("tbody");
                    tbody.empty();
                    var tbodyStr = "";
                    for(var i=0;i<list4FProduct.length;i++){
                         tbodyStr += "<tr>";
                        tbodyStr+= " <td>"+moment(list4FProduct[i].startTime).format("HH:mm")+"</td>" ;
                        for(var j=0;j<list4FArea.length;j++){

                            tbodyStr +="<td>1</td>";

                        }
                        tbodyStr+="</tr>";
                    }
                    tbody.append(tbodyStr);
                }
            });
        },
        validateForm: function () {
            var validate = $('#rechargeForm').validate({
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