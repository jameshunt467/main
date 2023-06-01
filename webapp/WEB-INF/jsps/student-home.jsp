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
<h1>Pushing works!</h1>
<p>Fuck me it's <s:property value="student.username" />!</p>
<ul>
  <li><s:property value="#session['StudentBean'].firstName" /></li>
  <li><s:property value="#session['StudentBean'].lastName" /></li>
  <li><s:property value="#session['StudentBean'].email" /></li>
  <li><s:property value="#session['StudentBean'].contactNumber" /></li>
  <li><s:property value="#session['StudentBean'].role" /></li>
  <li><s:property value="#session['StudentBean'].studentNumber" /></li>
</ul>

<!-- TODO THE BELOW ISNT WORKING? -->
<s:url var="browseIssueURL" action="browseIssuesAction" namespace="/" />
<s:a href="%{browseIssueURL}">Go to Browse Issues JSP</s:a>

<s:url var="browseKnowledgebaseURL" action="browseKnowledgebaseAction" namespace="/" />
<s:a href="%{browseKnowledgebaseURL}">Go to Browse Knowledgebase JSP</s:a>

<s:url var="displaySubmitIssueURL" action="displaySubmitIssue" namespace="/" />
<s:a href="%{displaySubmitIssueURL}">Go to Submit Issue JSP</s:a>

</body>
</html>
