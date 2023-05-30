<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 29/05/2023
  Time: 7:12 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><s:property value="issue.title"/></title>
    <link rel="stylesheet" href="styles/viewIssueDetails.css">
</head>
<body>
<div class="header">
    <a href="browseIssuesAction.action">Back to Issues</a>
    <h1><s:property value="issue.title"/></h1>
</div>

<div class="description">
    <s:property value="issue.description"/>
</div>

<div class="issueDetails">
    Issue ID: <s:property value="issue.issueID"/><br>
    Category: <s:property value="issue.category"/><br>
    Status: <s:property value="issue.status"/><br>
    Resolution Details: <s:property value="issue.resolutionDetails"/><br>
    Reported Date: <s:property value="issue.dateTimeReported"/><br>
    Resolved Date: <s:property value="issue.dateTimeResolved"/><br>
</div>

<div class="comments">
    <s:if test="issue.dateTimeResolved == null">
        <div class="addCommentContainer">
            <s:textarea class="addComment" name="addCommentAction" placeholder="Add a Comment"/>
            <s:submit value="Submit" action="addCommentAction" align="center" class="submitCommentButton"/>
        </div>
    </s:if>
    <s:iterator value="issue.comments">
        <div class="comment">
            <s:property/>
        </div>
    </s:iterator>
</div>

</body>
</html>
