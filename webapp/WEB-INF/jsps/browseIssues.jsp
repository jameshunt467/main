<%@ include file="template.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>BROWSING ISSUES</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css">

</head>
<body>
<h1>Browsing ISSUES Display</h1>

<div class="flexContainer">
    <div class="flexItem">
        <s:form action="browseIssuesAction">
            <s:select label="Sort Issues"
                      name="sort"
                      list="{'Category', 'Status', 'Reporting Time', 'Time In Progress'}"
                      headerKey="" headerValue="Select"/>
            <s:textfield name="searchTerm" label="Search Term"/>
            <s:submit value="Sort"/>
        </s:form>
    </div>
</div>

<div class="flexContainer2">

<s:iterator value="issueList">
    <div class="flexItem">
    <a href="<s:url action='viewIssueAction'><s:param name='issueID' value='%{issueID}' /></s:url>">

            <p>
<%--                Issue ID: <s:property value="issueID"/><br>--%>
                <h3>Title: <s:property value="title"/><br></h3>
                Category: <s:property value="category"/><br>
                Status: <s:property value="status"/><br>
                Description: <s:property value="description"/><br>
                Resolution Details: <s:property value="resolutionDetails"/><br>
                Reported Date: <s:property value="dateTimeReported"/><br>
                Resolved Date: <s:property value="dateTimeResolved"/><br>
                Keywords:
                <s:iterator value="keywords">
                    <s:property value="keyword"/>,
                </s:iterator>
            </p>
        </div>
    </a>
</s:iterator>
<s:if test="issueList.size() == 0">
    <p>No issues found</p>
</s:if>
</div>

</body>
</html>
