<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>凯盛软件CRM-销售机会详情</title>
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
        .star {
            font-size: 20px;
            color: #ff7400;
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

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">销售机会基本资料</h3>
                    <div class="box-tools">
                        <a href="/sale/chance-my" class="btn btn-primary btn-sm">
                            <i class="fa fa-arrow-left"></i> 返回列表
                        </a>
                        <button id="delBtn" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除</button>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">机会名称</td>
                            <td>${saleChance.name}</td>
                            <td class="td_title">价值(元)</td>
                            <td><fmt:formatNumber value="${saleChance.price}"/> </td>
                            <td class="td_title">当前进度</td>
                            <td>
                                ${saleChance.process}
                                <button class="btn btn-xs btn-success" id="showChangeProcessModalBtn"><i class="fa fa-pencil"></i></button>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="box-footer">
                    <span style="color: #ccc" class="pull-right">
                        创建日期： <fmt:formatDate value="${saleChance.createTime}"/>
                    </span>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">关联客户资料</h3>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">姓名</td>
                            <td>${saleChance.customer.custName}</td>
                            <td class="td_title">职位</td>
                            <td>${saleChance.customer.jobTitle}</td>
                            <td class="td_title">联系电话</td>
                            <td>${saleChance.customer.mobile}</td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>${saleChance.customer.trade}</td>
                            <td class="td_title">客户来源</td>
                            <td>${saleChance.customer.source}</td>
                            <td class="td_title">级别</td>
                            <td class="star">${saleChance.customer.level}</td>
                        </tr>
                        <tr>
                            <td class="td_title">地址</td>
                            <td colspan="5">${saleChance.customer.address}</td>
                        </tr>
                        <tr>
                            <td class="td_title">备注</td>
                            <td colspan="5">${saleChance.customer.mark}</td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="row">
                <div class="col-md-8">
                    <h4>跟进记录
                        <small><button id="showRecordModalBtn" class="btn btn-success btn-xs"><i class="fa fa-plus"></i></button></small>
                    </h4>
                    <ul class="timeline">
                        <c:if test="${empty recordList}">
                            <li>
                                <!-- timeline icon -->
                                <i class="fa fa-circle-o bg-red"></i>
                                <div class="timeline-item">
                                    <div class="timeline-body">
                                        暂无跟进记录
                                    </div>
                                </div>
                            </li>
                        </c:if>
                        <c:forEach items="${recordList}" var="record">
                            <c:choose>
                                <c:when test="${record.content == '将当前进度修改为：[ 成交 ]'}">
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-check bg-green"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${record.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:when>
                                <c:when test="${record.content == '将当前进度修改为：[ 暂时搁置 ]'}">
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-close bg-red"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${record.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-info bg-blue"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${record.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
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
            <%--跟进记录模态框--%>
            <div class="modal fade" id="recordModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">添加跟进记录</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/sale/chance-my/new/record" id="recordForm" method="post">
                                <input type="hidden" name="saleId" value="${saleChance.id}">
                                <textarea id="recordContent" style="resize: none" class="form-control" name="content"></textarea>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveRecordBtn">保存</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            <%--跟进记录模态框--%>

            <%--销售机会进度模态框--%>
            <div class="modal fade" id="changeProcessModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">更新当前进度</h4>
                        </div>
                        <div class="modal-body">
                            <form method="post" action="/sale/chance-my/${saleChance.id}/process/update" id="updateProcessForm">
                                <input type="hidden" name="id" value="${saleChance.id}">
                                <select name="process" class="form-control">
                                    <c:forEach items="${processList}" var="process">
                                        <option value="${process}">${process}</option>
                                    </c:forEach>
                                </select>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveProcessBtn">更新</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            <%--销售机会进度模态框--%>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%@include file="../include/js.jsp"%>
</div>
<!-- ./wrapper -->
<script>
    $(function () {
        var saleId = ${saleChance.id};
        //删除
        $("#delBtn").click(function () {
            layer.confirm('您确定要删除吗?',function () {
                window.location.href = "/sale/chance-my/delete/"+saleId;
            })
        });

        //修改当前进度
        $("#showChangeProcessModalBtn").click(function () {
            $("#changeProcessModal").modal({
                show : true,
                backdrop : 'static'
            });
        });

        $("#saveProcessBtn").click(function () {
            $("#updateProcessForm").submit();
        });

        //添加跟进记录
        $("#showRecordModalBtn").click(function () {
            $("#recordModal").modal({
                show : true,
                dropback : 'static'
            });
        });

        $("#saveRecordBtn").click(function () {
            if(!$("#recordContent").val()) {
                layer.msg("请输入跟进记录内容");
                return;
            }
            $("#recordForm").submit();
        });


    });
</script>
</body>
</html>
