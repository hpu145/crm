<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-个人设置</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="include/css.jsp" %>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="include/header.jsp" %>
    <!-- 左侧菜单栏 -->
    <%@include file="include/sidebar.jsp" %>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">个人设置</h3>
                </div>
                <div class="box-body">
                    <form action="" method="post" id="saveForm" enctype="multipart/form-data">
                        <input type="hidden" name="employeeId" value="<shiro:principal property="id"/>">

                        <div class="form-group">
                            <label>更改头像</label>
                            <input type="file"  name="imige">
                        </div>

                    </form>
                </div>
                <div class="box-footer">
                    <a href="/home" class="btn btn-default"> 取消</a>
                    <button class="btn btn-primary" id="saveBtn">保存</button>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <!-- 底部 -->
    <%@include file="include/footer.jsp" %>
</div>
<!-- ./wrapper -->
<%@include file="include/js.jsp" %>
<script src="/static/dist/js/jquery.validate.min.js"></script>


<script>
    $(function () {
        $("#saveBtn").click(function () {
            $("#saveForm").submit();
        });

    });

</script>

</body>
</html>

