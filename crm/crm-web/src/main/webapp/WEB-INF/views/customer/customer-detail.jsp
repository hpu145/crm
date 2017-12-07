<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-我的客户资料</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp"%>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        .td_title {
            font-weight: bold;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
   <%@include file="../include/header.jsp"%>
   <%@include file="../include/sidebar.jsp"%>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的客户资料</h3>
                    <div class="box-tools">
                        <a href="/customer/customer-my" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                        <button id="editCustomerBtn" rel="${customer.id}"class="btn bg-purple btn-sm"><i class="fa fa-pencil"></i> 编辑</button>
                        <button id="transCustomerBtn" rel="${customer.id}" class="btn bg-orange btn-sm"><i class="fa fa-exchange"></i> 转交他人</button>
                        <button id="publicCustomerBtn" rel="${customer.id}" class="btn bg-maroon btn-sm"><i class="fa fa-recycle"></i> 放入公海</button>
                        <button id="delCustomerBtn" rel="${customer.id}" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除</button>
                    </div>
                </div>
                <c:if test="${not empty message}">
                    <div class="alert alert-info">
                        <h5>${message}</h5>
                    </div>
                </c:if>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">姓名</td>
                            <td>${customer.custName}</td>
                            <td class="td_title">职位</td>
                            <td>${customer.jobTitle}</td>
                            <td class="td_title">联系电话</td>
                            <td>${customer.mobile}</td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>${customer.trade}</td>
                            <td class="td_title">客户来源</td>
                            <td>${customer.source}</td>
                            <td class="td_title">级别</td>
                            <td style="color: #ff7400">${customer.level}</td>
                        </tr>
                        <c:if test="${not empty customer.address}">
                            <tr>
                                <td class="td_title">地址</td>
                                <td colspan="5">${customer.address}</td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty customer.mark}">
                            <tr>
                                <td class="td_title">备注</td>
                                <td colspan="5">${customer.mark}</td>
                            </tr>
                        </c:if>
                    </table>
                </div>
                <div class="box-footer">
                    <span style="color: #ccc" class="pull-right">创建日期：<span title="<fmt:formatDate value="${customer.createTime}"/>"><fmt:formatDate value="${customer.createTime}" pattern="MM月dd日"/></span> &nbsp;&nbsp;&nbsp;&nbsp;
                        最后修改日期：<span title="<fmt:formatDate value="${customer.updateTime}"/>"><fmt:formatDate value="${customer.updateTime}" pattern="MM月dd日"/></span></span>
                </div>
            </div>

            <div class="row">
                <div class="col-md-8">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">跟进记录</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">日程安排</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">相关资料</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>

    <%--用户选择对话框（转交他人）--%>
    <div class="modal fade" id="chooseUserModel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">请选择转入账号</h4>
                </div>
                <div class="modal-body">
                    <select id="userSelect" class="form-control">
                        <option value="">--请选择--</option>
                        <c:forEach items="${employeeList}" var="employee">
                            <c:if test="${employee.id != customer.employeeId}">
                                <option value="${employee.id}">${employee.employeeName} (${employee.mobile})</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="saveTransBtn">确定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script>
    $(function () {
        //删除客户
        var customerId = ${customer.id};
        $("#delCustomerBtn").click(function () {
            var id = $(this).attr("rel");
            layer.confirm("您确定要删除该客户吗?",function (index) {
                layer.close(index);
                window.location.href = "/customer/customer-my/delete/"+id;
            });
        });
        //将客户放入公海
        $("#publicCustomerBtn").click(function () {
            var id = $(this).attr("rel");
            layer.confirm("您确定要将该客户放入公海吗?",function (index) {
                layer.close(index);
                window.location.href = "/customer/customer-my/public/"+id;
            });
        });
        //编辑客户
        $("#editCustomerBtn").click(function () {
            var id = $(this).attr("rel");
            window.location.href = "/customer/customer-my/edit/"+id;
        });
        //转交他人
        $("#transCustomerBtn").click(function () {
            $("#chooseUserModel").modal({
                show : true,
                backdrop : 'static'
            });
        });

        $("#saveTransBtn").click(function () {
            var toEmployeeId = $("#userSelect").val();
            var toAccountName = $("#userSelect option:selected").text();
            layer.confirm("您确定要将该客户转交给"+toAccountName+"吗?",function (index) {
                layer.close(index);
                window.location.href = "/customer/customer-my/"+customerId+"/trans/"+toEmployeeId;
            });
        });










    });

</script>
</body>
</html>
