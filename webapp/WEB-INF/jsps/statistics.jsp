<%@ include file="template.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 1/06/2023
  Time: 11:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="styles/styles.css">
</head>
<body>
<div class="flexContainer2">
    <div class="flexItem">
<p>No. of issues in each category: <s:property value="totalCategoryIssues" /> </p>
    </div>

        <div class="flexItem">
<p>No. of issues in each status: <s:property value="totalStatusIssues" /></p>
        </div>
            <div class="flexItem">
<p>No. of issues assigned to each staff: <s:property value="StaffAssignedIssues" /></p>
            </div>
                <div class="flexItem">
<p>Average time to resolution last 30 days: <s:property value="formattedTimeToResolve" /></p>
                </div>
                    <div class="flexItem">
<p>5 Longest Unresolved Issues: </p>
<s:iterator value="longestIssues">
    <a href="<s:url action='viewIssueAction'><s:param name='issueID' value='%{issueID}' /></s:url>">
        <div class="individualIssue">
            <p>
                Title: <s:property value="title"/><br>
                Category: <s:property value="category"/><br>
                Status: <s:property value="status"/><br>
                Description: <s:property value="description"/><br>
                Resolution Details: <s:property value="resolutionDetails"/><br>
                Reported Date: <s:property value="dateTimeReported"/><br>
                Resolved Date: <s:property value="dateTimeResolved"/><br>
            </p>
        </div>
    </a>
</s:iterator>
                    </div>
    <div class="flexItem">
        <p>Knowledgebase Counts:</p>
        <s:iterator value="knowledgeBaseArticleCounts" status="stat">
            <p>
                Issue: <s:property value="key" />
                Views: <s:property value="value" />
            </p>
        </s:iterator>
    </div>

</div>
</body>
</html>
