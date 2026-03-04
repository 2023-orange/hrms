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
        <#if nameList?size != 0 >
            <p style="text-indent: 2em; margin-bottom: 10px;">有培训需要您代通知，请通知到位：</p>
            <#else>
            <p style="text-indent: 2em; margin-bottom: 10px;">您有培训通知如下：</p>
        </#if>
        <p style="text-indent: 3em; margin-bottom: 5px;">培训名称：《${trainingName}》</p>
        <p style="text-indent: 3em; margin-bottom: 5px;">讲师：${teacher}</p>
        <p style="text-indent: 3em; margin-bottom: 5px;">培训日期：${trainingDate}</p>
        <p style="text-indent: 3em; margin-bottom: 5px;">培训地址：${trainingAddress}</p>
        <p style="text-indent: 3em; margin-bottom: 5px;">培训内容：${trainingContent}</p>

<#if nameList?size != 0 >
    <p style="text-indent: 3em; margin-bottom: 5px;">以下为没有邮箱的人员名单：</p>
    <table style="margin-left: 10px ">
        <#list nameList as row>
            <tr>
                <#list row as col>
                    <td>
                        ${col}
                    </td>
                </#list>
            </tr>
        </#list>
    </table>
</#if>
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
