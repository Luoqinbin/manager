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
            listUrl: 'courtInfo/queryList',
            getById: 'courtInfo/queryById',
            deleteUrl: 'courtInfo/delete'
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
            $("#yuding").bind("click",function(){
                var data = table.getRowData("#dataTables");
                if(data==undefined){
                    layer.msg("请选择一行数据 ");
                }else{
                    module.yud(data);
                }
            });
            $("#date").change(function () {
                var d = $('#date option:selected') .val();
                module.__selectByDate(d, $("#courtId").val());
            });
        },
        __initDataTable:function () {
            var aoColumns = [
                {"mData": "area"},
                {"mData": "name"},
                {"mData": "busyBasePrice"},
                {"mData": "emptyBasePrice"},
                {"mData": "busyCashPrice"},
                {"mData": "emptyCashPrice"}

            ];
            var aoColumnDefs = [{
                "aTargets": [1],
                "mRender": function (a, b, c, d) {
                    return  a+"号场";
                }
            },{
                "aTargets": [2],
                "mRender": function (a, b, c, d) {
                    return  "￥"+a;
                }
            },{
                "aTargets": [3],
                "mRender": function (a, b, c, d) {
                    return  "￥"+a;
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
            }
            ];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTableNoSeq($("#dataTables"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler,null);

        },

        handler:{
            __queryHandler:function(condition){
                var areaQuery = $("#areaQuery").val();
                if (util.assertNotNullStr(areaQuery)) condition.area = areaQuery;

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
        yud:function (data) {
            util.openLayer({
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "场地预定",
                type: 1,
                content: $("#addWin1"),
                btn: ['预定', '关闭'],
                success: function (layero, index) {
                    var date = new Date();
                    var y = date.getFullYear();
                    var m = date.getMonth()+1;
                    var d = date.getDate();
                    var dd = y+"-"+m+"-"+d;
                    $("#courtId").val(data.id);
                    module.__selectByDate(dd,data.id);
                },
                yes: function (layero, index) {

                    var chk_value =[];
                    $('input[name="check"]:checked').each(function(){
                        chk_value.push($(this).val());
                    });

                    if(chk_value.length==0){
                        layer.alert("你还没有选择时间!");
                        return;
                    }
                    layer.closeAll();
                     module.init(data);


                },
                cancel: function (index, layero) {
                    layer.close(index);
                }
            });
        },
        __selectByDate:function (date,id) {
            var data = util.getTokenData();
            data['date'] = date;
            data['id'] = id;
            util.post("bookCustomer/selectByData", data).then(function (data) {
                if(data.code==200){
                    var d = data.data;
                    var table = $("#table");
                    table.empty();
                    var trs = "";
                    var num = 0;
                    for(var i=0;i<d.length;i++) {
                        if (num % 4 == 0) {
                            if(num>0){
                                trs+="</tr>"
                            }
                            trs+="<tr>"
                        }
                        trs += "<td>";
                        if (d[i].state == 1) {
                            trs += "<input type='checkbox' name='check' value='" + d[i].id + ","+ moment(d[i].startTime).format("HH:mm")+","+ moment(d[i].endTime).format("HH:mm")+","+d[i].price+"'>";
                        }
                        trs += moment(d[i].startTime).format("HH:mm") + "-" + moment(d[i].endTime).format("HH:mm") + "<br/>￥" + d[i].price + "</td>";
                        num++;
                    }
                    //
                    table.append(trs+"</tr>");
                }
            });
        },

        init:function (data) {
            var chk_value =[];
            util.openLayer({
                area: '800px',
                shade: [0.8, '#393D49'],
                title: "预定",
                type: 1,
                content: $("#addWin2"),
                btn: ['预定', '关闭'],
                success: function (layero, index) {
                    //获得选中的场地
                    $('input[name="check"]:checked').each(function(){
                        chk_value.push($(this).val());
                    });

                    if(chk_value.length==0){
                        layer.alert("你还没有选择时间!");
                    }
                    var dateArray = new Array();
                    var sum = 0;
                    var ids = "";
                    $("#areaSpan").text(data.area+"-"+data.name+"号场");
                    for(var i=0;i<chk_value.length;i++){
                        var str = chk_value[i].split(",");
                        ids+=str[0]+",";
                        dateArray.push(str[1]);
                        dateArray.push(str[2]);
                        sum= sum + parseInt( str[3]);
                    }
                    /*$("#dateSpan").text(dateArray[0]+" - "+dateArray[dateArray.length-1]);*/
                    $("#startTimeInput").val(dateArray[0]);
                    $("#endTimeInput").val(dateArray[dateArray.length-1]);
                    $("#price").val(sum);
                    $("#areaId").val(ids.substring(0,ids.length-1));
                    $("#payType").val("");
                    $("#person").val("");
                    $("#phone").val("")
                },
                yes: function (layero, index) {
                    var data = util.getTokenData();
                    var payType = $("#payType").val();
                    var phone = $("#phone").val();
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
                            //d.name = d.name.replace("号场","");
                            d.area = d.area.replace("F","");
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
                        required: true,
                        digits:true
                    },
                    area: {
                        required: true,
                        digits:true
                    },
                    busyBasePrice: {
                        required: true,
                        number:true
                    },
                    emptyBasePrice: {
                        required: true,
                        number:true
                    },
                    busyCashPrice: {
                        required: true,
                        number:true
                    },
                    emptyCashPrice: {
                        required: true,
                        number:true
                    }
                },
                messages: {
                    name: {
                        required: "场地名称不能为空",
                        digits:"请输入整数"
                    },
                    area: {
                        required: "楼层不能为空",
                        digits:"请输入整数"
                    },
                    busyBasePrice: {
                        required: "忙时价不能为空",
                        number:"请输入准确的价格"
                    },
                    emptyBasePrice: {
                        required: "闲时价不能为空",
                        number:"请输入准确的价格"
                    },
                    busyCashPrice: {
                        required: "忙时现金价不能为空",
                        number:"请输入准确的价格"
                    },
                    emptyCashPrice: {
                        required: "闲时现金价不能为空",
                        number:"请输入准确的价格"
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