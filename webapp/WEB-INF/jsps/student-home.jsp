<%--
  Created by IntelliJ IDEA.
  User: benvi
  Date: 27/05/2023
  Time: 8:32 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="template.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Home</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css">
</head>
<body>
<h1>Home - Student</h1>

<div class="flexContainer">
    <div class="flexItem">
<h2>My Issues</h2>
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
    </div>

    <div class="flexItem">
        <h2>Your Notifications</h2>

        <s:if test="hasActionErrors()">
            <div class="alert alert-danger">
                <s:actionerror />
            </div>
        </s:if>

        <s:if test="notifications.size() > 0">
            <s:iterator value="notifications">
                <s:if test="hasSeen">
                    <div class="notification-box-seen">
                </s:if>
                <s:else>
                    <div class="notification-box">
                </s:else>
                    <p><span class="notification-property">Notification ID:</span> <span class="notification-value"><s:property value="notificationID" /></span></p>
                    <p><span class="notification-property">Message:</span> <span class="notification-value"><s:property value="message" /></span></p>
                    <p><span class="notification-property">DateTime Sent:</span> <span class="notification-value"><s:property value="dateTimeSent" /></span></p>
                    <s:if test="!hasSeen">
                        <s:form action="markNotificationAsSeen">
                            <s:hidden name="notificationID" value="%{notificationID}" />
                            <s:submit value="Mark as Seen" cssClass="mark-as-seen-button" />
                        </s:form>
                    </s:if>
                    <s:form action="viewIssueAction">
                        <s:hidden name="issueID" value="%{issueID}" />
                        <s:submit value="Go to issue" cssClass="mark-as-seen-button" />
                    </s:form>
                </div>
            </s:iterator>
        </s:if>

        <s:else>
            <div style="text-align: center; margin-top: 20px;">
                <p>You have no notifications.</p>
            </div>
        </s:else>
    </div>
</div>

</body>
</html>

