<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-我的客户</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp" %>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        .name-avatar {
            display: inline-block;
            width: 50px;
            height: 50px;
            background-color: #ccc;
            border-radius: 50%;
            text-align: center;
            line-height: 50px;
            font-size: 24px;
            color: #FFF;
        }
        .table>tbody>tr:hover {
             cursor: pointer;
         }
        .table>tbody>tr>td {
            vertical-align: middle;
        }
        .star {
            font-size: 20px;
            color: #ff7400;
        }
        .pink {
            background-color: #ff00cc;
        }
    </style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp" %>
    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="customer-my"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的客户</h3>
                    <div class="box-tools pull-right">
                        <button id="newCustomerBtn" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增客户</button>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fa fa-file-excel-o"></i> 导出Excel <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="/customer/customer-my/export.xls">导出为xls文件</a></li>
                                <li><a href="/customer/customer-my/export.csv">导出为csv文件</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty message}">
                    <div class="alert alert-info">
                        <h5>${message}</h5>
                    </div>
                </c:if>
                <div class="box-body no-padding">
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th width="80"></th>
                            <th>姓名</th>
                            <th>职位</th>
                            <th>跟进时间</th>
                            <th>级别</th>
                            <th>联系方式</th>
                        </tr>
                        <c:if test="${empty page.list}">
                            <tr>
                                <td colspan="6" style="color:blue;text-align: center">暂无数据</td>
                            </tr>
                        </c:if>
                            <c:forEach items="${page.list}" var="customer">
                                <tr class="dataRow" rel="${customer.id}">
                                    <td><span class="name-avatar ${customer.sex == '女' ? "pink" : ''}">${fn:substring(customer.custName,0,1)}</span></td>
                                    <td>${customer.custName}</td>
                                    <td>${customer.jobTitle}</td>
                                    <td><fmt:formatDate value="${customer.lastContactTime}" type="both"/></td>
                                    <td class="star">${customer.level}</td>
                                    <td><i class="fa fa-phone"></i> ${customer.mobile} <br></td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                    <%--分页--%>
                    <span style="font-size:14px;"><div class="text-center">
                        <ul id="pagination-demo" class="pagination-sm"></ul>
                    </div></span>

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
<script src="/static/dist/js/jquery.twbsPagination.min.js"></script>
<script>
    $(function () {

        $("#newCustomerBtn").click(function () {
            window.location.href = "/customer/customer-my/new";//也可以直接在新增客户价格a标签
        });

        $(".dataRow").click(function () {
            var id = $(this).attr("rel");
            window.location.href = "/customer/customer-my/"+id;
        });

        //分页
        $('#pagination-demo').twbsPagination({
            totalPages: ${page.pages}, //total总记录数，就是多少条数据  pages总页数
            visiblePages: 5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href:"?p={{number}}" // 控制器部分加别名 name="p"   /customer/customer-my
        });




    });

</script>
</body>
</html>
