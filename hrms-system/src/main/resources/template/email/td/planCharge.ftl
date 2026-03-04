<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style>
        @page {
            margin: 0;
        }
    </style>
</head>
<body style="margin: 0px;
            padding: 0px;
			font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;
            color: #000;">
<div style="height: auto;
			width: 820px;
			min-width: 820px;
			margin: 0 auto;
			margin-top: 20px;
            border: 1px solid #eee;">
    <div style="padding: 10px;padding-bottom: 0px;">
        <p style="margin-bottom: 10px;padding-bottom: 0px;">尊敬的用户，您好：</p>
        <#if mailType == 'dept'>
            <p style="text-indent: 2em; margin-bottom: 10px;">您有培训计划《${planName}》需要指定计划负责人，请使用链接进行跳转</p>
        <#else>
            <p style="text-indent: 2em; margin-bottom: 10px;">您已被指派为《${planName}》的培训计划负责人，请使用链接进行跳转</p>
        </#if>
        <p style="text-align: center;
			font-family: Times New Roman;
			font-size: small;
			color: #C60024;
			padding: 20px 0px;
			margin-bottom: 10px;
			font-weight: bold;
			background: #ebebeb;">
            <a href='http://172.18.1.159:8016/td/TdPlan' target='_blank'>http://172.18.1.159:8016/td/TdPlan</a>
        </p>
        <div class="foot-hr hr" style="margin: 0 auto;
			z-index: 111;
			width: 800px;
			margin-top: 30px;
			border-top: 1px solid #DA251D;">
        </div>
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
