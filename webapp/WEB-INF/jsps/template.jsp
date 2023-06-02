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
<s:url var="displaySubmitIssueURL" action="displaySubmitIssue" namespace="/" />
<s:url var="statisticsURL" action="statisticsAction" namespace="/" />
<s:url var="logoutURL" action="logoutAction" namespace="/" />


<!-- Navbar -->
<div id="navbar">

  <!-- Add your navbar HTML code here -->
  <ul>
    <li><s:a href="%{homeURL}">Home</s:a></li>

    <s:if test="#session.user.role == 'staff'">
      <li><s:a href="%{browseIssueURL}">Browse Issues</s:a></li>
    </s:if>

    <li><s:a href="%{browseKnowledgebaseURL}">Browse Knowledgebase</s:a></li>

    <s:if test="#session.user.role == 'student'">
        <li><s:a href="%{displaySubmitIssueURL}">Submit Issue</s:a></li>
    </s:if>


    <s:if test="#session.user.role == 'staff'">
        <li><s:a href="%{statisticsURL}">View Statistics</s:a></li>
    </s:if>

    <li><s:a href="%{logoutURL}">Logout</s:a></li>
  </ul>
</div>

</body>
</html>
