<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>凯盛软件CRM-统计报表</title>
    <%@ include file="../include/css.jsp"%>
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
        <jsp:param name="menu" value="charts"/>
    </jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="box">
                <%--客户级别统计--%>
                <div class="box-header with-border">
                    <h3 class="box-title">客户级别数量统计</h3>
                </div>
                <div class="box-body">
                    <div id="bar" style="height: 300px;width: 100%"></div>
                </div>
                <%--每月新增客户的数量 折线图--%>
                <div class="box-header with-border">
                    <h3 class="box-title">每月新增客户数量统计</h3>
                </div>
                <div class="box-body">
                    <div id="line" style="height: 300px;width: 100%"></div>
                </div>
                <%--销售机会  漏斗图--%>
                <div class="box-header with-border">
                    <h3 class="box-title">员工销售机会数量统计</h3>
                </div>
                <div class="box-body">
                    <div id="funnel" style="height: 300px;width: 100%"></div>
                </div>


            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <!-- 底部 -->
    <%@ include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/echarts/echarts.min.js"></script>

<script>
    $(function () {

        //客户级别统计
        var bar = echarts.init(document.getElementById("bar"));
        var option = {
            title: {
                text: "客户级别数量",
                left: 'center'
            },
            tooltip: {},
            legend: {
                data: ['人数'],
                left: 'right'
            },
            xAxis: {
                type: 'category',
                data: []
            },
            yAxis: {},
            series: {
                name: "人数",
                type: 'bar',
                data:[]
            }
        }
        bar.setOption(option);

        $.get("/charts/customer/level").done(function (resp) {
            if(resp.state == "success") {
                var nameArray = [];
                var valueArray = [];
                var dataArray = resp.data;

                for(var i = 0;i < dataArray.length;i++) {
                    var obj = dataArray[i];
                    nameArray.push(obj.level);
                    valueArray.push(obj.count);
                }

                bar.setOption({
                    xAxis:{
                        data:nameArray,
                    },
                    series:{
                        data:valueArray,
                        itemStyle: {
                            normal : {
                                color: '#172edd'
                            }
                        },
                        barWidth: 60,

                    }
                });
            } else {
                layer.msg(resp.message);
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });

        //每月新增客户的数量 折线图
        //客户级别统计
        var line = echarts.init(document.getElementById("line"));
        var lineOption = {
            title: {
                text: "每月新增客户数量",
                left: 'center'
            },
            tooltip: {},
            legend: {
                data: ['新增人数'],
                left: 'right'
            },
            xAxis: {
                type: 'category',
                data: []
            },
            yAxis: {},
            series: {
                name: "新增人数",
                type: 'line',
                data:[]
            }
        }
        line.setOption(lineOption);

        $.get("/charts/customer/increase").done(function (resp) {
            if (resp.state == "success") {
                var nameArray = [];
                var valueArray = [];
                var dataArray = resp.data;
                for (var i=0;i<dataArray.length;i++) {
                    var obj = dataArray[i];
                    nameArray.push(obj.months);
                    valueArray.push(obj.increase);
                }
                line.setOption({
                    xAxis:{
                        data:nameArray
                    },
                    series:{
                        data:valueArray
                    }
                });
            } else {
                layer.msg(resp.message)
            }
        }).error(function () {
            layer.msg("加载数据异常")
        });

        //销售机会数量统计图 漏斗
        var funnel = echarts.init(document.getElementById("funnel"));
        var funnelOption = {
            title: {
                text: '销售机会数量统计图'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}  {c}"
            },
            toolbox: {
                feature: {
                    dataView: {readOnly: false},
                    restore: {},
                    saveAsImage: {}
                }
            },
            legend: {
                data: []
            },
            calculable: true,
            series: [
                {
                    name:'漏斗图',
                    type:'funnel',
                    left: '10%',
                    top: 60,
                    bottom: 0,

                    min: 0,
                    max: 100,
                    minSize: '0%',
                    maxSize: '100%',
                    sort: 'descending',
                    gap: 2,
                    label: {
                        normal: {
                            show: true,
                            position: 'inside'
                        },
                        emphasis: {
                            textStyle: {
                                fontSize: 20
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            length: 10,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        }
                    },
                    itemStyle: {
                        normal: {
                            borderColor: '#fff',
                            borderWidth: 1
                        }
                    },
                    data: []
                }
            ]
        };
        funnel.setOption(funnelOption);

        $.get("/charts/employee/salechance").done(function (resp) {
            if (resp.state == "success") {
                var nameArray = [];
                var valueArray = [];
                var dataArray = resp.data;
                for (var i=0;i<dataArray.length;i++) {
                    var obj = dataArray[i];
                    valueArray.push(obj.value);
                    nameArray.push(obj.name);
                }

                funnel.setOption({
                    legend:{
                        data:nameArray
                    },
                    series:{
                        data: dataArray
                    }
                });

            } else {
                layer.msg(resp.message)
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });


    });
</script>

</body>
</html>
