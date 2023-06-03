<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 29/05/2023
  Time: 7:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="template.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><s:property value="issue.title"/></title>
    <link rel="stylesheet" href="styles/viewIssueDetails.css">
    <script>
        function checkBeforeSubmit() {
            const select = document.getElementById("staffSelect");
            if (select.value == "") {
                alert("Please select a staff member");
                return false;
            }
            return true;
        }

        function confirmAddToKnowledgebase() {
            const confirmation = confirm("Are you sure you want to add this issue to the Knowledgebase?");
            if (confirmation) {
                document.getElementById("addToKnowledgebaseForm").submit();
            }
        }

    </script>
</head>
<body>
<div class="header">
    <s:if test="#session.user.role == 'staff'">
        <a href="browseIssuesAction.action">Back to Issues</a>
    </s:if>
    <s:if test="issue.dateTimeResolved == null">
        <h1>Issue - <s:property value="issue.title"/></h1>
    </s:if>
    <s:if test="issue.dateTimeResolved != null">
        <h1>Knowledgebase - <s:property value="issue.title"/></h1>
    </s:if>
</div>

<!-- display existing keywords -->
<s:if test="#session.user.role == 'staff'">
    <s:iterator value="issue.keywords">
        <div class="keyword">
            <s:form action="editKeywordAction">
                <s:hidden name="keywordID" value="%{#attr.keywordID}"/>
                <s:hidden name="issueID" value="%{issue.issueID}"/> <!-- new hidden input -->
                <s:textfield name="keyword" value="%{#attr.keyword}"/>
            </s:form>
            <s:form action="deleteKeywordAction">
                <s:hidden name="keywordID" value="%{#attr.keywordID}"/>
                <s:hidden name="issueID" value="%{issue.issueID}"/>
                <s:submit value="Delete" align="center" class="deleteKeywordButton"/>
            </s:form>
        </div>
    </s:iterator>
</s:if>

<s:if test="#session.user.role == 'student'">
    <s:iterator value="issue.keywords">
        <div class="keyword">
            <s:property value="keyword"/>
        </div>
    </s:iterator>
</s:if>

<!-- form for adding a new keyword -->
<div class="addKeywordContainer">
    <s:form action="addKeywordAction">
        <s:hidden name="issueID" value="%{issue.issueID}"/>
        <s:textfield class="addKeyword" name="keyword" placeholder="Add a Keyword"/>
        <s:fielderror fieldName="keyword"/>
        <s:submit value="Submit" align="center" class="submitKeywordButton"/>
    </s:form>
</div>

<s:if test="#session.user.role == 'staff'">
    <s:if test="issue.dateTimeResolved == null">
        <s:form action="addToKnowledgebase" id="addToKnowledgebaseForm">
            <s:hidden name="issueID" value="%{issue.issueID}"/>
            <input type="button" value="Add To Knowledgebase" class="submitKeywordButton" onclick="confirmAddToKnowledgebase()"/>
        </s:form>
    </s:if>
</s:if>


<div class="updateStatusContainer">
    <s:form action="updateStatusAction">
        <s:hidden name="issueID" value="%{issue.issueID}"/>
        <s:if test="#session.user.role == 'staff'">
        <s:select name="newStatus"
                  list="{'In Progress','Waiting on Third Party', 'Waiting on Reporter','Completed', 'Resolved'}"
                  value="%{issue.status}"
                  headerKey="0"
                  headerValue="%{issue.status}"
                  id="issueStatusStaff"
                  cssClass="form-control"/>
        </s:if>

        <s:if test="#session.user.role == 'student'">
            <s:select name="newStatus"
                      list="{'Not Accepted', 'Resolved'}"
                      value="%{issue.status}"
                      headerKey="0"
                      headerValue="%{issue.status}"
                      id="issueStatusUser"
                      cssClass="form-control"/>
        </s:if>
        <s:submit value="Update Status" align="center" class="submitStatusButton"/>
    </s:form>
</div>


<div class="description">
    <s:property value="issue.description"/>
</div>

<div class="issueDetails">
    Issue ID: <s:property value="issue.issueID"/><br>
    Category: <s:property value="issue.category"/><br>
    Status: <s:property value="issue.status"/><br>
    Resolution Details: <s:property value="issue.resolutionDetails"/><br>
    Reported Date: <s:property value="issue.dateTimeReported"/><br>
    Resolved Date: <s:property value="issue.dateTimeResolved"/><br>
</div>

<div class="comments">
    <s:if test="issue.dateTimeResolved == null">
        <div class="addCommentContainer">
            <s:form action="addCommentAction">
                <s:hidden name="issueID" value="%{issue.issueID}"/>
                <s:textarea class="addComment" name="comment" placeholder="Add a Comment"/>
                <!-- illegal characters -->
                <s:fielderror fieldName="comment"/>
                <s:submit value="Submit" align="center" class="submitCommentButton"/>
            </s:form>
        </div>
    </s:if>

    <s:iterator value="issue.comments">
        <div class="comment">
            <div class="commentText">
                <s:property value="comment"/>
            </div>
            <div class="commentDetails">
                Posted by: <s:property value="username"/> on <s:property value="dateTimePosted"/>
            </div>
        </div>
    </s:iterator>

    <!-- IF JAVASCRIPT IS DISABLED AND SUBMIT IS CLICKED WITHOUT SELECTING, BAD -->
    <s:if test="#session.user.manager">
      <div class="assignIssueContainer">
          <s:form action="assignIssueAction" onsubmit="return checkBeforeSubmit()">
              <s:hidden name="issueID" value="%{issue.issueID}"/>
              <s:if test="not empty staffMembers">
                <s:select id="staffSelect" name="staffUsername" list="staffMembers" headerKey="" headerValue="Select Staff" />
                <s:if test="currentlyAssigned != null">
                    <p>Currently assigned to: <s:property value="currentlyAssigned" /></p>
                    <s:submit value="Reassign Issue" align="center" class="submitAssignButton"/>
                </s:if>
                <s:else>
                    <s:submit value="Assign Issue" align="center" class="submitAssignButton"/>
                </s:else>
              </s:if>
              <s:else>
                <p>No staff available to assign</p>
              </s:else>
          </s:form>
      </div>
    </s:if>
    <s:elseif test="#session.user.staff">
    <!-- Content for staff users -->
        <s:if test="currentlyAssigned == null">
            <!-- if no one is assigned, this staff member can accept it -->
            <div class="assignIssueContainer">
                <s:form action="assignIssueAction" onsubmit="return checkBeforeSubmit()">
                    <s:hidden name="issueID" value="%{issue.issueID}"/>
                    <s:select id="staffSelect" name="staffUsername" list="staffMembers" headerKey="" headerValue="Select Staff" />
                    <s:submit value="Accept Issue" align="center" class="submitAssignButton"/>
                </s:form>
            </div>
        </s:if>
        <s:else>
            <!-- if someone is assigned, just display their username -->
            <div class="currentlyAssignedDisplay">
                <p>%{currentlyAssigned} is assigned to this issue</p>
            </div>
        </s:else>
    </s:elseif>
    <s:else>
      <div>
          Debug: You do not have required permissions: Username - <s:property value="#session.user.username" />, Role - <s:property value="#session.user.role" />
      </div>
    </s:else>

</div>


</body>
</html>
