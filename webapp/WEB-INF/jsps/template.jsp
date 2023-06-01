<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>Test</title>
  <!-- Add your common CSS and JS files here -->
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/template.css">
</head>
<body>

<s:url var="homeURL" action="homePage" namespace="/" />
<s:url var="browseIssueURL" action="browseIssuesAction" namespace="/" />
<s:url var="browseKnowledgebaseURL" action="browseKnowledgebaseAction" namespace="/" />


<!-- Navbar -->
<div id="navbar">

  <!-- Add your navbar HTML code here -->
  <ul>
    <li><s:a href="%{homeURL}">Home</s:a></li>
<%--    TODO IF USER IS A MANAGER OR STAFF --%>
    <li><s:a href="%{browseIssueURL}">Browse Issues</s:a></li>
    <li><s:a href="%{browseKnowledgebaseURL}">Browse Knowledgebase</s:a></li>

    <li><a href="#">Submit Issue</a></li>
<%--    TODO IF USER IS A MANAGER--%>
    <s:if test="">
      <li><a href="#">View Statistics</a></li>
    </s:if>

    <li><a href="#">Logout</a></li>
  </ul>
</div>

</body>
</html>
