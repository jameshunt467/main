<%@ include file="template.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>BROWSING ISSUES</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css">

</head>
<body>
<h1>Browsing Knowledgebase</h1>

<div class="flexContainer">
    <div class="flexItem">
<s:form action="browseKnowledgebaseAction">
    <s:select label="Sort Issues"
              name="sort"
              list="{'issueID', 'title', 'category', 'status', 'description', 'resolutionDetails', 'dateTimeReported', 'dateTimeResolved'}"
              headerKey="" headerValue="Select"/>
    <s:textfield name="searchTerm" label="Search Term"/>
    <s:submit value="Sort"/>
</s:form>
    </div>
</div>

<!-- TODO CHECK THE value BELOW -->
<div class="flexContainer2">

<s:iterator value="issueList">
    <div class="flexItem">
    <a href="<s:url action='viewIssueAction'><s:param name='issueID' value='%{issueID}' /></s:url>">
            <p>
                <h3>Title: <s:property value="title"/><br></h3>
                Category: <s:property value="category"/><br>
                Description: <s:property value="description"/><br>
                Keywords:
                <s:iterator value="keywords">
                    <s:property value="keyword"/>,
                </s:iterator>
            </p>
        </a>
    </div>
</s:iterator>
<s:if test="issueList.size() == 0">
    <p>No Articles found</p>
</s:if>
</div>
</body>
</html>
