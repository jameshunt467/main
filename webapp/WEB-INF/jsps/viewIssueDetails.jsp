<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="template.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- This page is for managing issues and their details -->

<html>
<head>
    <!-- Set the title of the page to the issue's title -->
    <title><s:property value="issue.title"/></title>
    <!-- Add the styles.css stylesheet -->
    <link rel="stylesheet" href="styles/styles.css">

    <script>
        // This function checks if a staff member has been selected before form submission
        function checkBeforeSubmit() {
            const select = document.getElementById("staffSelect");
            if (select.value == "") {
                alert("Please select a staff member");
                return false;
            }
            return true;
        }

        // This function confirms before adding an issue to the Knowledgebase
        function confirmAddToKnowledgebase() {
            const resolutionDetails = document.getElementById('addToKnowledgebaseForm').resolutionDetails.value;

            if (resolutionDetails.trim() == "") {
                alert("Please enter resolution details.");
                return false;
            }
            else {
                const confirmation = confirm("Are you sure you want to add this issue to the Knowledgebase?");
                if (confirmation) {
                    document.getElementById("addToKnowledgebaseForm").submit();
                }
            }
        }

    </script>
</head>

<body>

<div class="header">
    <!-- Display the title based on the status of the issue -->
    <s:if test="!issue.inKnowledgebase">
        <h1>Issue - <s:property value="issue.title"/></h1>
    </s:if>
    <s:else>
        <h1>Knowledge Base - <s:property value="issue.title"/></h1>
    </s:else>
</div>

<!-- Flex container for displaying keywords -->
<div class="flexContainer2">
    <!-- Display keywords for editing and deleting if user is staff -->
    <s:if test="#session.user.role == 'staff'">
        <s:iterator value="issue.keywords">
            <div class="flexItem">
                <!-- Form for editing a keyword -->
                <s:form action="editKeywordAction">
                    <s:hidden name="keywordID" value="%{#attr.keywordID}"/>
                    <s:hidden name="issueID" value="%{issue.issueID}"/>
                    <s:textfield name="keyword" value="%{#attr.keyword}"/>
                </s:form>
                <!-- Form for deleting a keyword -->
                <s:form action="deleteKeywordAction">
                    <s:hidden name="keywordID" value="%{#attr.keywordID}"/>
                    <s:hidden name="issueID" value="%{issue.issueID}"/>
                    <s:submit value="Delete" align="center" class="deleteKeywordButton"/>
                </s:form>
            </div>
        </s:iterator>
    </s:if>

    <!-- If user is a student, only display keywords -->
    <s:if test="#session.user.role == 'student'">
        <div class="flexItem">
            <s:iterator value="issue.keywords">
                <s:property value="keyword"/>
            </s:iterator>
        </div>
    </s:if>

    <!-- Form for adding a new keyword, visible only to staff or for unresolved issues -->
    <s:if test="#session.user.role == 'staff' || !issue.inKnowledgebase">
        <div class="flexItem">
            <s:form action="addKeywordAction">
                <s:hidden name="issueID" value="%{issue.issueID}"/>
                <s:textfield class="addKeyword" name="keyword" placeholder="Add a Keyword"/>
                <s:fielderror fieldName="keyword"/>
                <s:submit value="Submit" align="center" class="submitKeywordButton"/>
                <s:actionerror/>
            </s:form>
        </div>
    </s:if>

    <!-- Form for adding issue to knowledgebase, visible only to staff for resolved issues -->
    <s:if test="#session.user.role == 'staff' && (issue.status == 'Resolved' || issue.status == 'Completed') && !issue.inKnowledgebase">
        <div class="flexItem">
            <s:form action="addToKnowledgebase" id="addToKnowledgebaseForm">
                <s:hidden name="issueID" value="%{issue.issueID}"/>
                <s:textarea name="resolutionDetails" label="Resolution Details" cssClass="form-control"/>
                <input type="button" value="Add To Knowledgebase" class="submitKeywordButton" onclick="confirmAddToKnowledgebase()"/>
            </s:form>
        </div>
    </s:if>

    <!-- The following block is to manage issue status -->
    <s:if test="!issue.inKnowledgebase">
        <div class="flexItem">
            <s:form action="updateStatusAction">
                <s:hidden name="issueID" value="%{issue.issueID}"/>
                <!-- If user is staff, allow selection of new status -->
                <s:if test="#session.user.role == 'staff'">
                    <s:select name="newStatus"
                              list="{'In Progress','Waiting on Third Party', 'Waiting on Reporter','Completed', 'Resolved'}"
                              value="%{issue.status}"
                              headerKey="0"
                              headerValue="%{issue.status}"
                              id="issueStatusStaff"
                              cssClass="form-control"/>
                    <s:submit value="Update Status" align="center" class="submitStatusButton"/>
                </s:if>
                <!-- If user is student, allow status to be set to Resolved if not already completed -->
                <s:if test="#session.user.role == 'student' && issue.status != 'Completed'">
                    <s:submit name="newStatus" value="Resolved" id="issueStatusUser" cssClass="form-control"/>
                </s:if>
                <!-- If user is student and issue is completed, allow status to be Not Accepted or Resolved -->
                <s:else>
                    <s:select name="newStatus"
                              list="{'Not Accepted', 'Resolved'}"
                              value="%{issue.status}"
                              headerKey="0"
                              headerValue="%{issue.status}"
                              id="issueStatusUserRes"
                              cssClass="form-control"/>
                    <s:submit value="Update Status" align="center" class="submitStatusButton"/>
                </s:else>
            </s:form>
        </div>
    </s:if>

    <!-- Display issue details -->
    <div class="flexItem" class="issueDetails">
        Issue ID: <s:property value="issue.issueID"/><br>
        Category: <s:property value="issue.category"/><br>
        Status: <s:property value="issue.status"/><br>
        Resolution Details: <s:property value="issue.resolutionDetails"/><br>
        Reported Date: <s:property value="issue.dateTimeReported"/><br>
        Resolved Date: <s:property value="issue.dateTimeResolved"/><br>
    </div>

    <!-- Section for managing comments -->
    <div class="comments">
        <!-- Form for adding a comment, visible if issue is not resolved -->
        <s:if test="!issue.inKnowledgebase">
            <div class="flexItem">
                <s:form action="addCommentAction">
                    <s:hidden name="issueID" value="%{issue.issueID}"/>
                    <s:textarea class="addComment" name="comment" placeholder="Add a Comment"/>
                    <!-- illegal characters -->
                    <s:fielderror fieldName="comment"/>
                    <s:submit value="Submit" align="center" class="submitCommentButton"/>
                </s:form>
            </div>
        </s:if>

        <!-- Display comments, visible to staff or if issue is not resolved -->
        <s:if test="#session.user.role == 'staff' || !issue.inKnowledgebase">
            <div class="flexItem">
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
            </div>
        </s:if>

        <!-- Section for assigning or reassigning issues -->
        <s:if test="!issue.inKnowledgebase">
            <s:if test="#session.user.manager">
                <div class="flexItem">
                    <!-- Form for deallocating issue, visible if issue is currently assigned -->
                    <s:if test="currentlyAssigned != null">
                        <s:form action="deallocateIssueAction">
                            <s:hidden name="issueID" value="%{issue.issueID}"/>
                            <s:hidden name="username" value="%{currentlyAssigned}"/>  <!-- This line was added -->
                            <p>Currently assigned to: <s:property value="currentlyAssigned" /></p>
                            <s:submit value="Deallocate Issue" align="center" class="submitDeallocateButton"/>
                        </s:form>
                    </s:if>
                    <!-- Form for assigning or reassigning issue -->
                    <s:form action="assignIssueAction" onsubmit="return checkBeforeSubmit()">
                        <s:hidden name="issueID" value="%{issue.issueID}"/>
                        <!-- If issue is currently assigned, allow reassignment -->
                        <s:if test="currentlyAssigned != null">
                            <s:select id="staffSelect" name="staffUsername" list="staffMembers" headerKey="" headerValue="Select Staff" />
                            <s:submit value="Reassign Issue" align="center" class="submitAssignButton"/>
                        </s:if>
                        <!-- If issue is not currently assigned, allow assignment -->
                        <s:else>
                            <s:select id="staffSelect" name="staffUsername" list="staffMembers" headerKey="" headerValue="Select Staff" />
                            <s:submit value="Assign Issue" align="center" class="submitAssignButton"/>
                        </s:else>
                    </s:form>
                </div>
            </s:if>
            <s:else>
                <div class="flexItem">
                    <s:if test="currentlyAssigned == #session.user.username">
                        <s:form action="deallocateIssueAction">
                            <s:hidden name="issueID" value="%{issue.issueID}"/>
                            <s:hidden name="username" value="%{#session.user.username}"/>
                            <p>Currently assigned to you.</p>
                            <s:submit value="Deallocate Issue" align="center" class="submitDeallocateButton"/>
                        </s:form>
                    </s:if>
                    <s:elseif test="currentlyAssigned == null">
                        <s:form action="assignIssueAction">
                            <s:hidden name="issueID" value="%{issue.issueID}"/>
                            <s:hidden name="staffUsername" value="%{#session.user.username}"/>
                            <s:submit value="Assign Issue to Me" align="center" class="submitAssignButton"/>
                        </s:form>
                    </s:elseif>
                    <s:else>
                        <p>Issue is currently assigned to someone else.</p>
                    </s:else>
                </div>
            </s:else>
        </s:if>
    </div>
</div>

</body>
</html>
