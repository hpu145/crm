<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-找回密码</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp"%>

    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>

    <![endif]-->
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="../../index2.html"><b>找回密码</b></a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <p class="login-box-msg">
            <c:if test="${not empty message}">
        <div class="alert alert-info">
            <h5>${message}</h5>
        </div>
        </c:if>
        </p>

        <form action="" method="post" id="loginForm">
            <div>
                <label style="color: #0c0c0c">请输入账户名</label>
            </div>
            <div class="form-group has-feedback">
                <input type="text" name="mobile" class="form-control" placeholder="请输入账户">
                <span class="glyphicon glyphicon-phone form-control-feedback"></span>
            </div>

            <div>
                <button id="loginBtn" class="btn btn-primary btn-block btn-flat">下一步</button>
            </div>
        </form>

    </div>
    <!-- /.login-box-body -->
</div>
<!-- /.login-box -->
<%@include file="../include/js.jsp"%>
<script src="/static/dist/js/jquery.validate.min.js"></script>
<script>
    $(function () {

        $("#loginForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules : {
                mobile : {
                    required : true
                }
            },
            messages : {
                mobile : {
                    required : "请输入账户",
                }
            }
        });

    });

</script>
</body>
</html>

