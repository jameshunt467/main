package app;

public class NotificationBean {
    private int notificationID;
    private String message;
    private String dateTimeSent;
    private String username;
    private int issueID;
    private boolean hasSeen;

    // Constructors, getters, and setters

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTimeSent() {
        return dateTimeSent;
    }

    public void setDateTimeSent(String dateTimeSent) {
        this.dateTimeSent = dateTimeSent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public boolean getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        this.hasSeen = hasSeen;
    }
}
