<!-- When a users fills out the form and submits it, Struts will instantiate a
'SubmitIssueAction' object, set the 'issueDescription' property to the submitted
value, and call the 'execute' method.  Then 'execute()' will save the data to
the database and return a String indicating the result (typically 'SUCCESS' or
'ERROR').  Struts then uses this result to determine what page to show next, in
this case the submit-issue-success.jsp page -->

<!-- HTML form that the user fills out to submit issue -->
<%@ include file="template.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Submit Issue</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css" />
</head>
<body>
    <h1>Submit Issue</h1>
    <div class="flexContainer">
        <div class="flexItem">
    <s:form action="processIssue">
        <s:textfield name="issueTitle" label="Issue Title" required="true" />
        <s:select name="issueCategory" label="Issue Category" list="{'Network', 'Software', 'Hardware', 'Email', 'Account'}" required="true" />
        <s:textarea name="issueDescription" label="Issue Description" required="true" />
        <s:submit value="Submit Issue" />
    </s:form>
        </div>
    </div>
</body>
</html>

