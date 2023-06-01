<%@ include file="template.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>BROWSING ISSUES</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/browseIssuesStyle.css">

</head>
<body>
<h1>Browsing ISSUES Display</h1>

<s:form action="browseIssuesAction">
    <s:select label="Sort Issues"
              name="sort"
              list="{'issueID', 'title', 'category', 'status', 'description', 'resolutionDetails', 'dateTimeReported', 'dateTimeResolved'}"
              headerKey="" headerValue="Select"/>
    <s:textfield name="searchTerm" label="Search Term"/>
    <s:submit value="Sort"/>
</s:form>


<s:iterator value="issueList">
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


</body>
</html>
