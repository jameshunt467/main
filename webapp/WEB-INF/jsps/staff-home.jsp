<%--
  Created by IntelliJ IDEA.
  User: benvi
  Date: 27/05/2023
  Time: 8:35 pm
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
<h1>Staff</h1>
<p>Fuck me it's <s:property value="staff.username" />!</p>
<ul>
  <li><s:property value="#session.user.firstName"></s:property></li>
  <li><s:property value="#session.user.lastName"></s:property></li>
  <li><s:property value="#session.user.email"></s:property></li>
  <li><s:property value="#session.user.contactNumber"></s:property></li>
  <li><s:property value="#session.user.role"></s:property></li>
  <li><s:property value="#session.user.staffNumber"></s:property></li>
  <li><s:property value="#session.user.manager"></s:property></li>
</ul>
</body>
</html>
