<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>
</head>
<body>
    <h1>Your Notifications</h1>

    <s:if test="notifications.size() > 0">
        <table>
            <tr>
                <th>Notification ID</th>
                <th>Message</th>
                <th>DateTime Sent</th>
                <th>Issue ID</th>
            </tr>
            <s:iterator value="notifications">
                <tr>
                    <td><s:property value="notificationID" /></td>
                    <td><s:property value="message" /></td>
                    <td><s:property value="dateTimeSent" /></td>
                    <td><s:property value="issueID" /></td>
                </tr>
            </s:iterator>
        </table>
    </s:if>

    <s:else>
        <p>You have no notifications.</p>
    </s:else>
</body>
</html>
