<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 1/06/2023
  Time: 9:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="template.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
  <title>My Issues</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/browseKnowledgebaseStyle.css">

</head>
<body>
<h1>My Issues</h1>


<s:iterator value="issueList">
  <a href="<s:url action='viewIssueAction'><s:param name='issueID' value='%{issueID}' /></s:url>">
    <div class="individualKnowledgebase">
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

