<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-我的机会</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp"%>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        .table>tbody>tr:hover {
            cursor: pointer;
        }
        .table>tbody>tr>td {
            vertical-align: middle;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>
    <!-- 左侧菜单栏 -->
    <%@include file="../include/sidebar.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="chance-my"/>
    </jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的销售机会</h3>
                    <div class="box-tools pull-right">
                        <a href="/sale/chance-my/new" type="button" class="btn btn-success btn-sm">
                            <i class="fa fa-plus"></i> 添加机会
                        </a>
                    </div>
                </div>
                <c:if test="${not empty message}">
                    <div class="alert alert-info">
                        <h5>${message}</h5>
                    </div>
                </c:if>
                <div class="box-body no-padding">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th width="40"></th>
                                <th>机会名称</th>
                                <th>关联客户</th>
                                <th>机会价值(元)</th>
                                <th>当前进度</th>
                                <th>最后跟进时间</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:if test="${empty page.list}">
                                <tr>
                                    <td colspan="6" style="color:blue;text-align: center">暂无数据</td>
                                </tr>
                            </c:if>
                            <c:forEach items="${page.list}" var="chance">
                                <tr class="dataRow" rel="${chance.id}">
                                    <td width="40"></td>
                                    <td>${chance.name}</td>
                                    <td>${chance.customer.custName}</td>
                                    <td><fmt:formatNumber value="${chance.price}"/> </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${chance.process == '成交'}">
                                                <span class="label label-success">${chance.process}</span>
                                            </c:when>
                                            <c:when test="${chance.process == '暂时搁置'}">
                                                <span class="label label-danger">${chance.process}</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${chance.process}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${chance.lastTime}" type="both"/></td>
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
    <%@include file="../include/footer.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script src="/static/dist/js/jquery.twbsPagination.min.js"></script>
<script>
    $(function () {
        //分页
        $('#pagination-demo').twbsPagination({
            totalPages: ${page.pages},
            visiblePages: 5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href:"?p={{number}}"
        });

        $(".dataRow").click(function () {
            var id = $(this).attr("rel");
            window.location.href = "/sale/chance-my/detail/"+id;
        });







    });
</script>
</body>
</html>


