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
        <#if type = 'DOC'>
                <p style="text-indent: 2em; margin-bottom: 10px;">您负责的人员范围内，有以下人员的异常被打回：</p>
                <table style="border: 1px solid #2a2a2d; margin-left: 10px ">
                    <tr>
                        <th>序号</th>
                        <th>日期</th>
                        <th>姓名</th>
                    </tr>
                        <#list acList as acItem>
                            <tr>
                                <td>
                                    ${acItem_index + 1}
                                </td>
                                <td>
                                    ${acItem.dateStr}
                                </td>
                                <td>
                                    ${acItem.fromName}
                                </td>
                            </tr>
                        </#list>
                </table>
            <#else>
                <p style="text-indent: 2em; margin-bottom: 10px;">您存在以下日期的考勤异常被打回：</p>
                <table style="border: 1px solid #2a2a2d; margin-left: 10px">
                    <tr>
                        <th>序号</th>
                        <th>日期</th>
                    </tr>
                    <#list acList as acItem>
                        <tr>
                            <td>
                                ${acItem_index + 1}
                            </td>
                            <td>
                                ${acItem.dateStr}
                            </td>
                        </tr>
                    </#list>
                </table>
        </#if>
        <div style="text-align: center;
			font-size: 12px;
			padding: 20px 0px;
			font-family: Microsoft YaHei;">
            访问链接：<a href='http://172.18.1.159:8016/ac/DisposeList' target='_blank'>http://172.18.1.159:8016/ac/DisposeList</a>
        </div>
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
