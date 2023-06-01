<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 29/05/2023
  Time: 7:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="template.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><s:property value="issue.title"/></title>
    <link rel="stylesheet" href="styles/viewIssueDetails.css">
</head>
<body>
<%--TODO CHECK WHETHER ISSUE IS SOLVED OR NOT // MAKE SURE IT GOES BACK TO THE PROPER PAGE--%>
<div class="header">
    <a href="browseIssuesAction.action">Back to Issues</a>
    <h1><s:property value="issue.title"/></h1>
</div>

<!-- display existing keywords -->
<s:iterator value="issue.keywords">
    <div class="keyword">
        <s:property/>
    </div>
</s:iterator>

<!-- form for adding a new keyword -->
<div class="addKeywordContainer">
    <s:form action="addKeywordAction">
        <s:hidden name="issueID" value="%{issue.issueID}"/>
        <s:textfield class="addKeyword" name="keyword" placeholder="Add a Keyword"/>
        <s:submit value="Submit" align="center" class="submitKeywordButton"/>
    </s:form>
</div>

<s:if test="issue.dateTimeResolved == null">
    <s:form action="addToKnowledgebase">
        <s:hidden name="issueID" value="%{issue.issueID}"/>
        <s:submit value="Add To Knowledgebase" align="center" class="submitKeywordButton"/>
    </s:form>
</s:if>


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
            <s:form action="addCommentAction">
                <s:hidden name="issueID" value="%{issue.issueID}"/>
                <s:textarea class="addComment" name="comment" placeholder="Add a Comment"/>
                <s:submit value="Submit" align="center" class="submitCommentButton"/>
            </s:form>
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
