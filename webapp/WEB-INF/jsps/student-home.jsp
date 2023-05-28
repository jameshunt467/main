<%--
  Created by IntelliJ IDEA.
  User: benvi
  Date: 27/05/2023
  Time: 8:32 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Display Test</title>
</head>
<body>
<h1>Student</h1>
<p>Fuck me it's <s:property value="student.username" />!</p>
<ul>
    <li><s:property value="student.firstName"></s:property></li>
    <li><s:property value="student.lastName"></s:property></li>
    <li><s:property value="student.email"></s:property></li>
    <li><s:property value="student.contactNumber"></s:property></li>
    <li><s:property value="student.role"></s:property></li>
    <li><s:property value="student.studentNumber"></s:property></li>
</ul>
</body>
</html>
