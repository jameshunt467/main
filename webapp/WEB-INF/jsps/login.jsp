<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <link rel="stylesheet" href="styles/styles.css">
</head>
<body>
<h1>Login</h1>

<div class="flexContainer">
  <div class="flexItem" style="text-align:center; margin:10%;padding:2%;display:flex; align-content: center; justify-content: center">
<s:form action="login">
  <s:textfield name="username" label="Username" />
  <s:password name="password" label="Password" />
  <s:submit value="Login" />
  <s:reset/>
</s:form>
  </div>
</div>
<s:if test="hasActionErrors()">
  <s:actionerror />
</s:if>

</body>
</html>
