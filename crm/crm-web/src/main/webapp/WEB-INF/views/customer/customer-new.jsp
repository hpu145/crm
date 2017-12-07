<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-首页</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp" %>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp" %>
    <!-- 左侧菜单栏 -->
    <%@include file="../include/sidebar.jsp" %>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增客户</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-primary btn-sm" id="returnCustomerBtn"><i class="fa fa-arrow-left"></i> 返回列表</button>
                    </div>
                </div>
                <c:if test="${not empty message}">
                    <div class="alert alert-info">
                        <h5>${message}</h5>
                    </div>
                </c:if>
                <div class="box-body">
                    <form action="" method="post" id="addCustomerForm">
                        <%--<input type="hidden" name="accountId" value="${sessionScope.curr_account.id}">--%>

                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" class="form-control" name="custName">
                        </div>
                        <div class="form-group">
                            <label>性别</label>
                            <div>
                                <label class="radio-inline">
                                    <input type="radio" name="sex" value="男"> 男
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="sex" value="女"> 女
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>职位</label>
                            <input type="text" class="form-control" name="jobTitle">
                        </div>
                        <div class="form-group">
                            <label>联系方式</label>
                            <input type="text" class="form-control" name="mobile">
                        </div>
                        <div class="form-group">
                            <label>地址</label>
                            <input type="text" class="form-control" name="address">
                        </div>
                        <div class="form-group">
                            <label>所属行业</label>
                            <select name="trade" class="form-control">
                                <option value="">请选择所属行业</option>
                                <c:forEach items="${trades}" var="trade">
                                    <option value="${trade}">${trade}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>客户来源</label>
                            <select name="source" class="form-control">
                                <option value="">请选择客户来源</option>
                                <c:forEach items="${sources}" var="source">
                                    <option value="${source}">${source}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>级别</label>
                            <select name="level" class="form-control">
                                <option value="">请选择客户级别</option>
                                <option value="★">★</option>
                                <option value="★★">★★</option>
                                <option value="★★★">★★★</option>
                                <option value="★★★★">★★★★</option>
                                <option value="★★★★★">★★★★★</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>备注</label>
                            <input type="text" class="form-control" name="mark">
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <a href="/customer/customer-my" class="btn btn-default"> 取消</a>
                    <button class="btn btn-primary" id="addCustomerBtn">保存</button>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <!-- 底部 -->
    <%@include file="../include/footer.jsp" %>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp" %>
<script src="/static/dist/js/jquery.validate.min.js"></script>


<script>
    $(function () {
        $("#returnCustomerBtn").click(function () {
            window.location.href = "/customer/customer-my";
        });

        $("#addCustomerBtn").click(function () {
            $("#addCustomerForm").submit();
        });

        $("#addCustomerForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules : {
                custName : {
                    required : true
                },
                sex : {
                    required : true
                },
                jobTitle : {
                    required : true
                },
                mobile : {
                    required : true
                }

            },
            messages : {
                custName : {
                    required : "请输入姓名"
                },
                sex : {
                    required : "请选择性别"
                },
                jobTitle : {
                    required : "请输入职位"
                },
                mobile : {
                    required : "请输入联系方式"
                }
            }
        });







    });

</script>

</body>
</html>

