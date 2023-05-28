<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
</head>
<body>
<h1>Login</h1>

<s:form action="login">
  <s:textfield name="username" label="Username" />
  <s:password name="password" label="Password" />
  <s:submit value="Login" />
  <s:reset/>
</s:form>

<s:if test="hasActionErrors()">
  <s:actionerror />
</s:if>

</body>
</html>
