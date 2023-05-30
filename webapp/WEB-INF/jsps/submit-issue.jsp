<!-- When a users fills out the form and submits it, Struts will instantiate a 
'SubmitIssueAction' object, set the 'issueDescription' property to the submitted
value, and call the 'execute' method.  Then 'execute()' will save the data to 
the database and return a String indicating the result (typically 'SUCCESS' or 
'ERROR').  Struts then uses this result to determine what page to show next, in
this case the submit-issue-success.jsp page -->

<!-- HTML form that the user fills out to submit issue -->
<!DOCTYPE html>
<html>
<head>
    <title>Submit Issue</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/submitIssueStyle.css" />
</head>
<body>
    <h1>Submit Issue</h1>
    <form action="submit-issue" method="post">
        <label for="issueDescription">Issue Description:</label><br>
        <textarea id="issueDescription" name="issueDescription"></textarea><br>
        <input type="submit" value="Submit Issue">
    </form>
</body>
</html>

