var IndexMain = function () {
    var menuSelect = function(){
        var menu = $('.page-sidebar-menu');
         $("a[name='childMenu']").each(function () {
             var that = $(this);
             $(this).click(function () {
                 menu.find('li.active').removeClass('active');
                 $("li.open").addClass("active");
                 that.parent().addClass("active");
                 gotoMenu($(this).attr("data-url"),$(this).attr("data-menu"),$(this).attr("data-id"));
             });
         });
        $("a[name='noChild']").each(function () {
            var that = $(this);
            $(this).click(function () {
                menu.find('li.active').removeClass('active');
                that.parent().addClass("active");
                gotoMenu($(this).attr("data-url"),$(this).attr("data-menu"),$(this).attr("data-id"));
            });
        });
    };

    var gotoMenu = function (url,names,id) {
        var breadcrumb = $("#breadcrumb");
        breadcrumb.empty();
        var name = names.split(",");
        var li= "<li name=\"navigation\"><i class=\"fa fa-home\"></i> <a href=\"http://www.baidu.com\">Home</a> <i class=\"fa fa-angle-right\"></i></li>";
        var j = 0;
        for(var i=1;i<=name.length;i++){
             li += "<li> <a href=\"javascript:void(0)\">"+name[j]+"</a>";
             if(i<name.length){
                 li+="<i class=\"fa fa-angle-right\"></i>";
             }
             li+="</li> ";
             j++;
        }
        breadcrumb.append(li);
        $("#myiframe").attr("src",url+"?menuId="+id);
    }
    var changePwd = function(){
        $("#changPwd").bind("click",function () {
            var s = layer.open({
                type: 1,
                content: $("#changePasswordWarp"),
                title: '密码修改',
                area: ['600px', '350px'],
                shadeClose: true,
                success: function () {
                    $("#changePasswordForm")[0].reset();
                    validate.resetForm();
                }
                , btn: ['确定', '取消'],
                yes: function (index) {
                    if($("#changePasswordForm").valid()){
                        $("#changePasswordForm").ajaxSubmit({
                            success: function (d) {
                                layer.msg(d.message);
                                if (d.code == 200) {
                                    layer.close(index);
                                }
                            }
                        });
                    }
                },
            })
        });
    };
    var validate;
    var validateForm = function () {
        validate = $('#changePasswordForm').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                oldPassword: {
                    required: true,
                    minlength:6,
                    maxlength:10
                },
                newPassword: {
                    required: true,
                    minlength:6,
                    maxlength:10
                },
                confirmNewPassword: {
                    required: true,
                    equalTo:"#newPassword",
                    minlength:6,
                    maxlength:10
                }
            },
            messages: {
                oldPassword: {
                    required: "旧密码不能为空!"
                },
                newPassword: {
                    required: "新密码不能为空!"
                },
                confirmNewPassword: {
                    required: "确认密码不能为空!"
                }
            },
            highlight: function (element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
            },
            unhighlight: function (element) { // revert the change done by hightlight
                $(element) .closest('.form-group').removeClass('has-error'); // set error class to the control group
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
    }

    var logOut = function(){
        $("#logout").bind("click", function () {
            $("#logout_form").submit();
        });
    }

    return {
        init:function () {
            setInterval(function(){
                $("#currentTime").html(new Date().toLocaleString());
            },1000);
            menuSelect();
            validateForm();
            changePwd();
            logOut();
        }
    };
}();