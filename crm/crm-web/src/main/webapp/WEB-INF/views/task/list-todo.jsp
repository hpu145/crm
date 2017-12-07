<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-待办列表</title>
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
                    <h3 class="box-title">计划任务</h3>

                    <div class="box-tools pull-right">
                        <a href="/task/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增任务</a>
                        <button id="showAllBtn" class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示所有任务</button>
                    </div>
                </div>
                <%--添加或删除成功后消息提示--%>
                <c:if test="${not empty message}">
                    <div class="alert alert-info">
                        <h5>${message}</h5>
                    </div>
                </c:if>

                <div class="box-body">

                    <ul class="todo-list">
                        <c:forEach items="${taskList}" var="task">
                            <li class="${task.done==1 ? 'done' : ''}" style="display: ${task.done==1 ? 'none':'block'}">
                                <input type="checkbox"  class="task_checkbox" ${task.done==1 ? 'checked' : ''} value="${task.id}">
                                <span  class="text">${task.title}</span>

                                    <%--<c:choose>
                                         <c:when test="${not empty task.customer and not empty task.customer.id}">
                                             <a href="/customer/customer-my/${task.customer.id}"><i class="fa fa-user-o"></i> ${task.customer.custName}</a>
                                         </c:when>
                                         <c:when test="${not empty task.saleChance and not empty task.saleChance.id}">
                                             <a href="/sale/chance-my/${task.saleChance.id}"><i class="fa fa-money"></i> ${task.saleChance.name}</a>
                                         </c:when>
                                     </c:choose>--%>

                                <small  class="label ${task.overTime ? 'label-danger' : 'label-success'}"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${task.finishTime}" type="both"/></small>
                                <div class="tools">
                                    <i class="fa fa-trash-o del_task" rel="${task.id}"></i>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <!-- /.box-body -->
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
<script>
    $(function () {
        //删除任务
        $(".del_task").click(function () {
            layer.confirm("您确定要删除该任务吗?",function () {
                var id = $(".del_task").attr("rel");
                window.location.href = "/task/delete/"+id;
            });
        });

        //修改任务的状态
        $(".task_checkbox").click(function () {
            var id = $(this).val();
            var checked = $(this)[0].checked;
            if(checked) {
                window.location.href = "/task/state/done/"+id;
            } else {
                window.location.href = "/task/state/undone/"+id;
            }
        });

        //显示所有任务
        $("#showAllBtn").click(function () {
            var elements = $(".done");
            for (var i = 0; i <elements.length; i++) {
                $(elements[i]).css("display","block");
            }
        });


    });

</script>

</body>
</html>
