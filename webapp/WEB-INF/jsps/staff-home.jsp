<%--
  Created by IntelliJ IDEA.
  User: benvi
  Date: 27/05/2023
  Time: 8:35 pm
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
<h1>Staff</h1>
<p>Fuck me it's <s:property value="staff.username" />!</p>
<ul>
  <li><s:property value="staff.firstName"></s:property></li>
  <li><s:property value="staff.lastName"></s:property></li>
  <li><s:property value="staff.email"></s:property></li>
  <li><s:property value="staff.contactNumber"></s:property></li>
  <li><s:property value="staff.role"></s:property></li>
  <li><s:property value="staff.staffNumber"></s:property></li>
  <li><s:property value="staff.manager"></s:property></li>
</ul>
</body>
</html>
