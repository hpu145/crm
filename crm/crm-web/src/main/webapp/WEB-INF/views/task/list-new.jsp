<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-新增任务</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp"%>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>
    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="task-todo"/>
    </jsp:include>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增待办任务</h3>

                    <div class="box-tools pull-right">
                        <a href="/task/todo" class="btn btn-primary btn-sm">
                            <i class="fa fa-arrow-left"></i> 返回列表
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <form action="/task/new" method="post" id="saveTaskForm">
                        <div class="form-group">
                            <label>任务名称</label>
                            <input type="hidden" name="employeeId" value="<shiro:principal property="id"/>">
                            <input type="text" name="title" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>完成日期</label>
                            <input type="text" name="finishTime" class="form-control" id="datepicker">
                        </div>
                        <div class="form-group">
                            <label>提醒时间</label>
                            <input type="text" name="remindTime" class="form-control" id="datepicker2">
                        </div>

                        <%--<div class="form-group">
                            <label>关联客户</label>
                            <select name="custId" id="" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option  value="${customer.id}">${customer.custName}(联系电话: ${customer.mobile})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>关联销售机会</label>
                            <select name="saleId" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${saleChanceList}" var="sale">
                                    <option  value="${sale.id}">${sale.name}</option>
                                </c:forEach>
                            </select>
                        </div>--%>

                    </form>
                </div>
                <!-- /.box-body -->
                <div class="box-footer">
                    <button id="saveTaskBtn" class="btn btn-primary">保存</button>
                </div>
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script src="/static/dist/js/jquery.validate.min.js"></script>
<script>
    $(function () {

        $("#saveTaskBtn").click(function () {
            $("#saveTaskForm").submit();
        });

        $("#saveTaskForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules : {
                title : {
                    required : true
                },
                finishTime : {
                    required : true
                },
                remindTime : {
                    required : true
                }
            },
            messages : {
                title : {
                    required : "请输入任务名称"
                },
                finishTime : {
                    required : "请选择完成日期"
                },
                remindTime : {
                    required : "请选择提醒时间"
                }
            }

        });



        var picker = $('#datepicker').datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true,
            startDate:moment().format("yyyy-MM-dd")
        });
        picker.on("changeDate",function (e) {
            var today = moment().format("YYYY-MM-DD");
            $('#datepicker2').datetimepicker('setStartDate',today);
            $('#datepicker2').datetimepicker('setEndDate', e.format('yyyy-mm-dd'));
        });
        var timepicker = $('#datepicker2').datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true
        });


    });
</script>
</body>
</html>
