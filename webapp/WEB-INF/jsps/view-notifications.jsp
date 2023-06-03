<%@ include file="template.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 0;
            margin: 0;
            background-color: #f4f4f4;
        }
        /* h1 {
            text-align: center;
            padding: 20px;
            background-color: #333;
            color: #fff;
        } */
        .notification-box {
            max-width: 600px;
            margin: 20px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 15px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            background-color: #fff;
        }
        .notification-property {
            font-weight: bold;
        }
        .notification-value {
            margin-left: 10px;
        }
        .mark-as-seen-button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 10px 24px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 10px 2px;
            cursor: pointer;
            border-radius: 12px;
        }
    </style>
</head>
<body>
    <h1>Your Notifications</h1>

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger">
            <s:actionerror />
        </div>
    </s:if>

    <s:if test="notifications.size() > 0">
        <s:iterator value="notifications">
            <div class="notification-box">
                <p><span class="notification-property">Notification ID:</span> <span class="notification-value"><s:property value="notificationID" /></span></p>
                <p><span class="notification-property">Message:</span> <span class="notification-value"><s:property value="message" /></span></p>
                <p><span class="notification-property">DateTime Sent:</span> <span class="notification-value"><s:property value="dateTimeSent" /></span></p>
                <p><span class="notification-property">Issue ID:</span> <span class="notification-value"><s:property value="issueID" /></span></p>
                <s:form action="markNotificationAsSeen">
                    <s:hidden name="notificationID" value="%{notificationID}" />
                    <s:submit value="Mark as Seen" cssClass="mark-as-seen-button" />
                </s:form>
                <s:form action="viewIssueAction">
                    <s:hidden name="issueID" value="%{issueID}" />
                    <s:submit value="Show me" cssClass="mark-as-seen-button" />
                </s:form>
            </div>
        </s:iterator>
    </s:if>

    <s:else>
        <div style="text-align: center; margin-top: 20px;">
            <p>You have no notifications.</p>
        </div>
    </s:else>
</body>
</html>
