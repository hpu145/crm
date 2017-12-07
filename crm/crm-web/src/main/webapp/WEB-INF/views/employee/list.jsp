<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-员工管理</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp" %>
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
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="employee"/>
    </jsp:include>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-md-2">
                    <div class="box">
                        <div class="box-body">
                            <button class="btn btn-default" id="addDept">添加部门</button>
                            <input type="hidden" id="deptId">
                            <ul id="ztree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-10">
                    <!-- Default box -->
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">员工管理</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool"  title="Collapse" id="addEmployeeBtn">
                                    <i class="fa fa-plus"></i> 添加员工</button>
                            </div>
                        </div>
                        <div class="box-body">
                            <table class="table" id="dataTable">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>姓名</th>
                                    <th>部门</th>
                                    <th>手机</th>
                                    <th>管理</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                    <!-- /.box -->
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%--添加员工的模态框--%>
    <!-- Modal -->
    <div class="modal fade" id="addEmployeeModel" >
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">添加账号</h4>
                </div>
                <div class="modal-body">
                    <form id="addEmployeeForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" class="form-control" name="employeeName">
                        </div>
                        <div class="form-group">
                            <label>手机号码</label>
                            <input type="text" class="form-control" name="mobile">
                        </div>
                        <div class="form-group">
                            <label>密码(默认000000)</label>
                            <input type="password" class="form-control" name="password" value="670b14728ad9902aecba32e22fa4f6bd">
                        </div>
                        <div class="form-group">
                            <label>所属部门</label>
                            <div id="checkboxList"></div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="addEmployeeFormBtn">保存</button>
                </div>
            </div>
        </div>
    </div>
    <%--添加员工的模态框--%>
    <!-- 底部 -->
    <%@include file="../include/footer.jsp" %>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script src="/static/plugins/datatables/jquery.dataTables.js"></script>
<script src="/static/dist/js/jquery.validate.min.js"></script>
<script>
    $(function () {

        //添加员工
        $("#addEmployeeBtn").click(function () {

            $.get("/employee/dept.json").done(function (data) {
                $("#checkboxList").html("");
                for (var i=0;i<data.length;i++) {
                    var obj = data[i];
                    if(obj.id != 1) {
                        var html = '<label class="checkbox-inline"><input type="checkbox" name="deptId" value="'+obj.id+'">'+obj.deptName+'</label>';
                        $("#checkboxList").append(html);
                    }
                }

                $("#addEmployeeModel").modal({
                    show : true,
                    backdrop : false
                });

            }).error(function () {
                layer.msg("获取部门失败，请稍后再试");
            });
        });


        $("#addEmployeeFormBtn").click(function () {
            $("#addEmployeeForm").submit();
        });

        $("#addEmployeeForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules:{
                employeeName:{
                    required:true
                },
                mobile:{
                    required:true
                },
                password:{
                    required:true
                },
                deptId:{
                    required:true
                }
            },
            messages:{
                employeeName:{
                    required:"请输入账号"
                },
                mobile:{
                    required:"请输入手机号码"
                },
                password:{
                    required:"请输入密码"
                },
                deptId:{
                    required:"请选择部门"
                }
            },

            submitHandler : function () {
                $.post("/employee/new",$("#addEmployeeForm").serialize()).done(function (data) {
                    if(data.state == "success") {
                        $("#addEmployeeModel").modal('hide');
                        dataTable.ajax.reload();
                    } else {
                        layer.msg(data.message);
                    }

                }).error(function () {
                    layer.msg("服务器繁忙，请稍后再试");

                });

            }

        });

        var dataTable = $("#dataTable").DataTable({
            "processing": true,
            "serverSide": true,
            "ajax": {
                "url" : "/employee/load.json",//发送ajax请求，查看浏览器端可以确认为get请求
                "data" : function(data){
                    data.deptId = $("#deptId").val();
                }
            },
            "lengthChange": false,
            "pageLength": 10,
            "columns":[
                {"data":"id"},
                {"data":"employeeName"},
                {"data":function(row){
                    var deptArray = row.deptList;
                    var str = "";
                    for(var i = 0;i < deptArray.length;i++) {
                        str += deptArray[i].deptName + " ";
                    }
                    return str;
                }},
                {"data":"mobile"},
                {"data":function(row){
                    return "<a href='javascript:;' rel='"+row.id+"' class='delEmployee'>删除</a>";
                }}
            ],
            "columnDefs": [
                {
                    "targets": [2,3,4],
                    "orderable": false
                },
                {
                    "targets": [0],
                    "visible": false
                }
            ],
            language:{
                "search":"用户名: ",
                "info": "显示从 _START_ 到 _END_ 条数据，共 _TOTAL_ 条",
                "infoEmpty":"没有任何数据",
                "emptyTable":"暂无数据",
                "processing":"加载中...",
                "paginate": {
                    "first":      "首页",
                    "last":       "末页",
                    "previous":       "上一页",
                    "next":   "下一页"
                }
            }
        });


        var setting = {
            data: {
                simpleData: {
                    enable: true
                },
                key:{
                    name:"deptName"
                }
            },
            async:{
                enable:true,
                url:"/employee/dept.json",
                type:"get",
                dataFilter:ajaxDataFilter
            },
            callback:{
                onClick:function(event,treeId,treeNode,clickFlag){
                    //alert(treeNode.id + treeNode.deptName + treeNode.pId);
                    $("#deptId").val(treeNode.id);
                    dataTable.ajax.reload();
                }
            }
        };

        function ajaxDataFilter(treeId, parentNode, responseData) {
            if (responseData) {
                for(var i =0; i < responseData.length; i++) {
                    if(responseData[i].id == 1) {
                        responseData[i].open = true;
                        break;
                    }
                }
            }
            return responseData;
        }
        $.fn.zTree.init($("#ztree"), setting);

        /**
         * 添加部门
         */
        $("#addDept").click(function () {
            layer.prompt({title: '请输入添加部门名称'}, function (text, index) {
                layer.close(index);
                //发送ajax请求
                $.post("/employee/dept/new",{"deptName" : text}).done(function (data) {
                    if(data.state == "success") {
                        layer.msg('添加部门成功');
                        //刷新树
                        var treeObj = $.fn.zTree.getZTreeObj("ztree");
                        treeObj.reAsyncChildNodes(null, "refresh");
                    } else {
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg('服务器繁忙，请稍后再试');
                });
            });
        });

        //删除员工(事件委托)
        $(document).delegate(".delEmployee","click",function () {
            var id = $(this).attr("rel");
            layer.confirm("确定要删除吗?",function(index){
                layer.close(index);

                $.get("/employee/delete/" + id,{"id" :id}).done(function (data) {
                    if(data.state == "success") {
                        dataTable.ajax.reload();
                    } else {
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("服务器异常，请稍后再试");
                });




            });
        });

        //该方法失效，异步请求进行删除，应使用事件委托
        /*$(".delEmployee").click(function () {
            var id = $(this).attr("rel");
            layer.msg(id);
        });*/




    });
</script>
</body>
</html>
