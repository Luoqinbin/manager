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
            module.initTable('4F',$("#date").val());
            $("#date").bind("change",function () {
                var area = "";
                $(".nav-tabs").find("li").each(function(){
                    if($(this).attr("class")=="active"){
                        area = $.trim($(this).find("a").text());
                    }
                });
                module.initTable(area,$(this).val());
            });

            $("#fixOrder").bind("click",function () {
                var courtId = "";
                var startTime = "";
                var endTime ="";
                var area ="";
                var name ="";
                var pid="";
                $("#tbody").find("tr").each(function(i,o){
                    $(this).find("td").each(function(i){
                        var clicked =  $(this).attr("clicked");
                        if(clicked=='true'){
                            courtId = $(this).attr("data-id");
                            startTime= $(this).attr("data-starttime");
                            endTime= $(this).attr("data-endtime");
                            area= $(this).attr("data-area");
                            name= $(this).attr("data-name");
                            pid = $(this).attr("data-pid");
                        }
                    })
                });
                if(courtId==""){
                    layer.alert("请选择一个时间");
                    return;
                }
                var time = $("#date").val();

                module.yud(pid,time,startTime,endTime,area,name);
            });
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
        yud:function (pid,time,startTime,endTime,area,name) {
            util.openLayer({
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "预定",
                type: 1,
                content: $("#addWin2"),
                btn: ['预定', '关闭'],
                success: function (layero, index) {
                    //获得选中的场地
                    $("#areaSpan").text(area+"-"+name+"号场");
                    $("#startTimeInput").val(startTime);
                    $("#endTimeInput").val(endTime);
                    $("#price").val("");
                    $("#areaId").val(pid);
                    $("#payType").val("");
                    $("#person").val("");
                    $("#phone").val("")
                },
                yes: function (layero, index) {
                    var data = util.getTokenData();
                    var payType = $("#payType").val();
                    var phone = $("#phone2").val();
                    data['areaId'] = $("#areaId").val();
                    data['price']=$("#price").val();
                    data['payType']=payType;
                    data['phone']=phone;
                    data['date']=$("#date").val();
                    data['person']=$("#person").val();
                    data['startTime']=$("#startTimeInput").val();
                    data['endTime']=$("#endTimeInput").val();
                    if (payType==""){
                        layer.msg("请选择支付方式！");
                        return;
                    }
                    console.log(data);
                    if(!(/^1[3|4|5|7|8][0-9]{9}$/.test(phone))){
                        layer.msg("手机号码有误，请重填");
                        return;
                    }
                    util.post("bookCustomer/addOrder", data).then(function (d) {
                        if (d.code == 200) {
                            layer.closeAll();
                        }
                        layer.alert(d.message);
                    });

                },
                cancel: function (index, layero) {
                    layer.close(index);
                }
            });
        },
        initTable:function (area,time) {
            var data = util.getTokenData();
            dojo.mixin(data, {"area": area,"time":time});
            util.post("home/queryByHomeTable", data).then(function (res) {
                if (res.code==200){
                    var d = res.data;
                    var list4FArea = d.list4FArea;
                    var list4FProduct = d. list4FProduct;
                    var listProduct = d.listProduct;
                    var customerList = d.customerList;
                    var th = $("#th");
                    th.empty();
                    var thStr = "<th></th>";
                    for(var i=0;i<list4FArea.length;i++){
                        thStr+="<th>"+list4FArea[i].area+" - "+list4FArea[i].name+"</th>"
                    }
                    th.append(thStr);

                    var tbody = $("#tbody");
                    tbody.empty();
                    var tbodyStr = "";
                    for(var i=0;i<list4FProduct.length;i++){
                         tbodyStr += "<tr>";
                         var tt = moment(list4FProduct[i].startTime).format("HH:mm");
                         var ett = moment(list4FProduct[i].endTime).format("HH:mm");
                         tbodyStr+= " <td>"+tt+"-"+ett+"</td>" ;
                        for(var j=0;j<list4FArea.length;j++){
                            var index = i * list4FArea.length + j;
                            tbodyStr +="<td class='tdContent' data-area='"+list4FArea[j].area+"' data-name='"+list4FArea[j].name+"'" +
                                " data-startTime='"+tt+"' data-endTime ='"+ett+"' data-id='"+listProduct[index].courtId+"' data-pid='"+listProduct[index].pid+"'></td>";

                        }
                        tbodyStr+="</tr>";
                    }
                    tbody.append(tbodyStr);
                    $("#tbody").find("tr").each(function(i,o){
                        $(this).find("td").each(function(i){
                            var pid = ($(this).attr("data-pid"));
                            for(var h = 0;h<customerList.length;h++){
                                if(customerList[h].pid == pid){
                                    $(this).attr("bgcolor","#CB5A5E");
                                    $(this).attr("fix","true");
                                    break;
                                }
                            }
                        })
                    });
                    $(".tdContent").click(function(){
                        var courtId = ($(this).attr("data-id"));
                        var clicked =  $(this).attr("clicked");
                        if(clicked == 'true'){
                            $(this).attr("clicked", false).css("background-color", "");
                        }else{
                            $("#tbody").find("tr").each(function(i,o){
                                $(this).find("td").each(function(i){
                                    $(this).css("background-color", "");
                                })
                            });
                            $(this).attr("clicked", true).css("background-color", "#CB5A5E");
                        }

                    });
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