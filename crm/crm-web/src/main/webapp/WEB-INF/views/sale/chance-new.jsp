<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-添加机会</title>
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
        <jsp:param name="menu" value="chance-new"/>
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
                        <a href="/sale/chance-my" class="btn btn-primary btn-sm">
                            <i class="fa fa-arrow-left"></i> 返回列表
                        </a>
                        <a href="/sale/chance-my/new" type="button" class="btn btn-success btn-sm">
                            <i class="fa fa-plus"></i> 添加机会
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <form action="" method="post" id="addChanceForm">
                        <div class="form-group">
                            <label>机会名称</label>
                            <input type="text" class="form-control" name="name">
                        </div>
                        <div class="form-group">
                            <label>关联客户</label>
                            <select name="custId" id="" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option  value="${customer.id}">${customer.custName}(联系电话: ${customer.mobile})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>机会价值(元)</label>
                            <input type="text" class="form-control" name="price">
                        </div>
                        <div class="form-group">
                            <label>当前进度</label>
                            <select name="process" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${saleProcess}" var="process">
                                    <option value="${process}">${process}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>详细内容</label>
                            <textarea name="content" style="resize: none" class="form-control"></textarea>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->
                <div class="box-footer">
                    <button id="addChanceBtn" class="btn btn-primary">保存</button>
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

        $("#addChanceBtn").click(function () {
            $("#addChanceForm").submit();
        });

        $("#addChanceForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules : {
                name : {
                    required : true
                },
                custId : {
                    required : true
                },
                price : {
                    required : true,
                    number : true,
                    min : 1
                },
                process : {
                    required : true
                }
            },
            messages : {
                name : {
                    required : "请输入销售机会名称"
                },
                custId : {
                    required : "请选择关联客户"
                },
                price : {
                    required : "请输入机会价值",
                    number : "请输入有效数字",
                    min : "请输入有效数字"
                },
                process : {
                    required : "请选择当前进度"
                }

            }


        });





    });

</script>
</body>
</html>
