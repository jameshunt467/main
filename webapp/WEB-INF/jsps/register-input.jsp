<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: haydencheers
  Date: 26/11/20
  Time: 2:55 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration Form</title>
</head>
<body>
<h1>Register</h1>
<s:form action="register">
    <s:textfield name="name" label="Your Name" />
    <s:textfield name="email" type="email" label="Your eMail" />
    <s:textfield name="age" type="number" label="Your Age" />
    <s:textfield name="password" label="Password" />
    <s:textfield name="passwordConfirm" label="Confirm Password" />

    <s:reset />
    <s:submit />
</s:form>
</body>
</html>
