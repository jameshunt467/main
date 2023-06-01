<%--
  Created by IntelliJ IDEA.
  User: benvi
  Date: 27/05/2023
  Time: 8:32 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="template.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Display Test</title>
</head>
<body>
<h1>Pushing works!</h1>
<p>Fuck me it's <s:property value="#session.user.username" />!</p>
<ul>
    <li><s:property value="#session.user.firstName" /></li>
    <li><s:property value="#session.user.lastName" /></li>
    <li><s:property value="#session.user.email" /></li>
    <li><s:property value="#session.user.contactNumber" /></li>
    <li><s:property value="#session.user.role" /></li>
    <li><s:property value="#session.user.studentNumber" /></li>
</ul>

<s:url var="myIssuesURL" action="myIssuesAction" namespace="/" />
<s:a href="%{myIssuesURL}">My Issues</s:a>

<s:url var="notificationURL" action="notificationAction" namespace="/" />
<s:a href="%{notificationURL}">My Notifications</s:a>

</body>
</html>

