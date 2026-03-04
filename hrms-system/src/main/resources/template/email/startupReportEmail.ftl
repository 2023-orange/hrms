<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body style="margin: 0px; padding: 0px;">
<div style="height: auto;
			width: 98%;
			min-width: 820px;
			margin: 0 auto;
			margin-top: 20px;
            border: 1px solid #eee;">
    <div style="padding: 10px;padding-bottom: 0px;">
        <P style="text-align: center;">HRMS系统启动报告</P>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">后端服务启动时间：${startupTime}</p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">    后端服务地址：${serverUrl}</p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">一、数据库列表</p>
        <table >
            <tr>
                <th>序号</th>
                <th>数据库</th>
                <th>url</th>
                <th>用户</th>
                <th>密码</th>
            </tr>
            <#list dbInfoList as dbInfo>
                <tr>
                    <td>${dbInfo_index + 1}</td>
                    <td><#if dbInfo.resourceName??>${dbInfo.resourceName}<#else>&nbsp;</#if></td>
                    <td><#if dbInfo.url??>${dbInfo.url}<#else>&nbsp;</#if></td>
                    <td><#if dbInfo.user??>${dbInfo.user}<#else>&nbsp;</#if></td>
                    <td><#if dbInfo.password??>${dbInfo.password}<#else>&nbsp;</#if></td>
                </tr>
            </#list>
        </table>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">二、oauth列表</p>
        <table >
            <tr>
                <th>序号</th>
                <th>系统</th>
                <th>url</th>
            </tr>
            <#list oauthInfoList as oauthInfo>
                <tr>
                    <td>${oauthInfo_index + 1}</td>
                    <td><#if oauthInfo.oauthName??>${oauthInfo.oauthName}<#else>&nbsp;</#if></td>
                    <td><#if oauthInfo.url??>${oauthInfo.url}<#else>&nbsp;</#if></td>
                </tr>
            </#list>
        </table>
        <br><br>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">三、hrms服务启动情况列表</p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">1、redis <a class="light <#if redisStatus = 'pass'>green_light<#else>red_light</#if>">${redisStatus}</a></p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">${redisMessage}</p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">2、邮件<a class="light <#if mailStatus = 'pass'>green_light<#else>red_light</#if>">${mailStatus}</a></p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">${mailMessage}</p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">3、定时任务<a class="light <#if jobStatus = 'started'>green_light<#else><#if jobStatus = 'standbyMode'>yellow_light<#else>red_light</#if></#if>">${jobStatus}</a></p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">      数据库关联服务检查结果： <a class="light <#if serverCheckStatus = 'pass'>green_light<#else>red_light</#if>">${serverCheckStatus}</a></p>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">                       ${serverCheckMessage}</p>
        <#if jobStatus = 'started' && serverCheckStatus = 'pass'>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">生效定时任务列表</p>
        <table>
            <tr>
                <th>序号</th>
                <th>任务名称</th>
                <th>Bean名称</th>
                <th>方法名称</th>
                <th>cron 表达式</th>
                <th>参数</th>
                <th>备注</th>
            </tr>
            <#list enableJobList as enableJob>
                <tr>
                    <td>${enableJob_index + 1}</td>
                    <td><#if enableJob.jobName??>${enableJob.jobName}<#else>&nbsp;</#if></td>
                    <td><#if enableJob.beanName??>${enableJob.beanName}<#else>&nbsp;</#if></td>
                    <td><#if enableJob.methodName??>${enableJob.methodName}<#else>&nbsp;</#if></td>
                    <td><#if enableJob.cronExpression??>${enableJob.cronExpression}<#else>&nbsp;</#if></td>
                    <td><#if enableJob.params??>${enableJob.params}<#else>&nbsp;</#if></td>
                    <td><#if enableJob.remark??>${enableJob.remark}<#else>&nbsp;</#if></td>
                </tr>
            </#list>
        </table>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">未生效定时任务列表</p>
        <table>
            <tr>
                <th>序号</th>
                <th>任务名称</th>
                <th>Bean名称</th>
                <th>方法名称</th>
                <th>cron 表达式</th>
                <th>参数</th>
                <th>备注</th>
            </tr>
            <#list pauseJobList as pauseJob>
                <tr>
                    <td>${pauseJob_index + 1}</td>
                    <td><#if pauseJob.jobName??>${pauseJob.jobName}<#else>&nbsp;</#if></td>
                    <td><#if pauseJob.beanName??>${pauseJob.beanName}<#else>&nbsp;</#if></td>
                    <td><#if pauseJob.methodName??>${pauseJob.methodName}<#else>&nbsp;</#if></td>
                    <td><#if pauseJob.cronExpression??>${pauseJob.cronExpression}<#else>&nbsp;</#if></td>
                    <td><#if pauseJob.params??>${pauseJob.params}<#else>&nbsp;</#if></td>
                    <td><#if pauseJob.remark??>${pauseJob.remark}<#else>&nbsp;</#if></td>
                </tr>
            </#list>
        </table>
        <#else>
        <p style="margin-bottom: 10px;padding-bottom: 0px;">${jobMessage}</p>
        </#if>
        <div class="foot-hr hr" style="margin: 0 auto;
			z-index: 111;
			width: 800px;
			margin-top: 30px;
			border-top: 1px solid #DA251D;">
        <div style="text-align: center;
			font-size: 12px;
			padding: 20px 0px;
			font-family: Microsoft YaHei;">
                Copyright &copy;${.now?string("yyyy")} SUNTEN 人力资源管理系统 All Rights Reserved.
        </div>

    </div>
</div>
</body>
</html>
<style type="text/css">
    .light {
        left: 50px;
        position: relative;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        margin-left: 10px;
        display: inline-block;
        line-height: 30px;
        text-align: center;
    }
    .red_light {
        background-color: red;
    }
    .yellow_light {
        background-color: yellow;
    }
    .green_light {
        background-color: green;
    }
    table, table tr td, table tr th {
        border: 1px solid #000;
        border-collapse: collapse;
        border-spacing: 0px;
    }
</style>
