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
</head>
<body>

<p>No. of issues in each category <s:property value="totalCategoryIssues" /> </p>
<p>No. of issues in each status <s:property value="totalStatusIssues" /></p>
<p>No. of issues assigned to each staff <s:property value="StaffAssignedIssues" /></p>
<p>Average time to resolution last 30 days <s:property value="averageTimeToResolve" /></p>
<p>5 Longest Unresolved Issues</p>
<s:iterator value="longestIssues">
    <a href="<s:url action='viewIssueAction'><s:param name='issueID' value='%{issueID}' /></s:url>">
        <div class="individualIssue">
            <p>
                Issue ID: <s:property value="issueID"/><br>
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

<p>Knowledgebase </p>
<p></p>

</body>
</html>
