<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix='security' uri='http://www.springframework.org/security/tags'%>
<%
	String basePath = request.getContextPath();
%>
<html>
<head>
<base href="${basePath}">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>登录</title>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="static/metronic/assets/global/plugins/select2/select2.css" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/admin/pages/css/login-soft.css" rel="stylesheet" type="text/css"/>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME STYLES -->
<link href="static/metronic/assets/global/css/components-md.css" id="style_components" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/global/css/plugins-md.css" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
<link id="style_color" href="static/metronic/assets/admin/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css"/>
<link href="static/metronic/assets/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico"/>

</head>

<body class="page-md login">
<!-- BEGIN LOGO -->
<div class="logo">
    <img src="static/metronic/assets/admin/layout/img/logo-big.png" alt=""/>
</div>
<!-- END LOGO -->
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGIN -->
<div class="content">
	<!-- BEGIN LOGIN FORM -->
    <input type="hidden" value="${SPRING_SECURITY_LAST_EXCEPTION.message}" id="errormsg">

	<form class="login-form" action="login.do" method="post">
		<h3 class="form-title">系统登录</h3>
		<div class="alert alert-danger display-hide">
			<button class="close" data-close="alert"></button>
			<span id="error">
			请输入用户名和密码. </span>
		</div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<div class="form-group">
			<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
			<label class="control-label visible-ie8 visible-ie9">用户名</label>
			<div class="input-icon">
				<i class="fa fa-user"></i>
				<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="Username" name="username"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">密码</label>
			<div class="input-icon">
				<i class="fa fa-lock"></i>
				<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Password" name="password"/>
			</div>
		</div>
		<div class="form-actions">

			<button type="submit" class="btn blue pull-right">
				登录 <i class="m-icon-swapright m-icon-white"></i>
			</button>
            <br/>
		</div>

	</form>
	<!-- END LOGIN FORM -->

</div>
<!-- BEGIN COPYRIGHT -->
<div class="copyright">
    2014 &copy; Metronic - Admin Dashboard Template.
</div>
<!-- END COPYRIGHT -->
	<script src="static/metronic/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
	<script src="static/metronic/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
	<script src="static/metronic/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="static/metronic/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="static/metronic/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
	<script src="static/metronic/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="static/metronic/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
	<script src="static/metronic/assets/global/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script src="static/js/jquery.form.js"></script>
	<script src="static/metronic/assets/global/scripts/metronic.js" type="text/javascript"></script>
	<script src="static/metronic/assets/admin/layout/scripts/layout.js" type="text/javascript"></script>
	<script src="static/metronic/assets/admin/layout/scripts/demo.js" type="text/javascript"></script>
	<script src="static/metronic/assets/admin/pages/scripts/login-soft.js" type="text/javascript"></script>

	<!-- END PAGE LEVEL SCRIPTS -->
	<script>

        jQuery(document).ready(function() {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            Login.init();
            Demo.init();
            // init background slide images
            $.backstretch([
                    "static/metronic/assets/admin/pages/media/bg/1.jpg",
                    "static/metronic/assets/admin/pages/media/bg/2.jpg",
                    "static/metronic/assets/admin/pages/media/bg/3.jpg",
                    "static/metronic/assets/admin/pages/media/bg/4.jpg"
                ], {
                    fade: 1000,
                    duration: 8000
                }
            );
        });

		$(function() {
            $('.alert-danger', $('.login-form')).hide();
			if (self != top) {
				top.location = self.location.href;
				return;
			}
			var errormsg = $("#errormsg");
			if ($.trim(errormsg.val()) != "") {
                $("#error").text(errormsg.val());
                $('.alert-danger', $('.login-form')).show();
			}
		});


	</script>
</body>
</html>
