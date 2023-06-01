<!DOCTYPE html>
<html>
<head>
    <title>Issue Submission Failed</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/submitIssueStyle.css" />
</head>
<body>
    <h1>Issue Submission Failed</h1>
    <p>We're sorry, but there was an error submitting your issue. Please try again later.</p>
    <s:if test="hasActionErrors()">
        <p><s:actionerror /></p>
    </s:if>
    <a href="submit-issue.jsp">Try Again</a>
</body>
</html>
