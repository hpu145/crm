<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- 左侧菜单栏 -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- 菜单 -->
        <ul class="sidebar-menu">
            <li class="${param.menu == 'home' ? 'active' : ''}"><a href="/home">
                <i class="fa fa-home"></i> <span>首页</span></a></li>
            <li class="header">系统功能</li>
            <!-- 客户管理 -->
            <li class="treeview ${fn:startsWith(param.menu,"customer-") ? "active" : ""}">
                <a href="#">
                    <i class="fa fa-address-book-o"></i> <span>客户管理</span>
                    <span class="pull-right-container">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu">
                    <li class="${param.menu == 'customer-my' ? 'active' : ''}"><a href="/customer/customer-my"><i class="fa fa-circle-o"></i> <span>我的客户</span></a></li>
                    <li class="${param.menu == 'customer-public' ? 'active' : ''}"><a href="/customer/customer-public"><i class="fa fa-circle-o"></i><span>公海客户</span></a></li>
                </ul>
            </li>
            <!-- 销售机会 -->
            <li class="treeview ${fn:startsWith(param.menu,"chance-") ? "active" : ""}">
                <a href="#">
                    <i class="fa fa-bars"></i> <span>销售机会</span>
                    <span class="pull-right-container">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu">
                    <li class="${param.menu == 'chance-my' ? 'active' : ''} || ${param.menu == 'chance-new' ? 'active' : ''}">
                        <a href="/sale/chance-my"><i class="fa fa-circle-o"></i> <span>我的机会</span>
                    </a></li>
                </ul>
            </li>
            <!-- 待办事项 -->
            <li class="treeview ${fn:startsWith(param.menu,"task-" )?'active':''}">
                <a href="#">
                    <i class="fa fa-calendar"></i> <span>待办事项</span>
                    <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
                </a>
                <ul class="treeview-menu">
                    <li class="${param.menu == 'task-todo' ? 'active':''}"><a href="/task/todo"><i class="fa fa-circle-o"></i> 待办列表</a></li>
                    <li class="${param.menu == 'task-overdue'?'active':''}"><a href="/task/overdue"><i class="fa fa-circle-o"></i> 逾期事项</a></li>
                </ul>
            </li>
            <!-- 统计报表 -->
            <%--<li class="treeview ${param.menu == 'charts' ? 'active':''}">
                <a href="/charts">
                    <i class="fa fa-pie-chart"></i> <span>统计报表</span>
                    <span class="pull-right-container">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu">
                    <li><a href="#"><i class="fa fa-circle-o"></i> 待办列表</a></li>
                    <li><a href="#"><i class="fa fa-circle-o"></i> 逾期事项</a></li>
                </ul>
            </li>--%>
            <li class="${param.menu == 'charts' ? 'active':''}">
                <a href="/charts">
                    <i class="fa fa-pie-chart"></i>
                    <span>统计报表</span>
                </a>
            </li>

            <%--公司网盘--%>
            <li class="${param.menu == 'disk' ? 'active':''}">
                <a href="/disk">
                    <i class="fa fa-share-alt"></i>
                    <span>公司网盘</span>
                </a>
            </li>

            <shiro:hasRole name="系统管理部">
                <!-- 部门员工管理 -->
                <li class="header">系统管理</li>
                <li class="${param.menu == 'employee' ? 'active' : ''}">
                    <a href="/employee"><i class="fa fa-users"></i>
                        <span>员工管理</span>
                    </a>
                </li>
            </shiro:hasRole>

        </ul>
    </section>
    <!-- /.sidebar -->
</aside>