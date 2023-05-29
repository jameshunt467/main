package app;

public class IssueBean {

    private String issueID;
    private String title;
    private String category;
    private String status;
    private String description;
    private String resolutionDetails;
    private String dateTimeReported;
    private String dateTimeResolved;

    public IssueBean() {
    }

    public IssueBean(String issueID, String title, String category, String status, String description, String resolutionDetails, String dateTimeReported, String dateTimeResolved) {
        this.issueID = issueID;
        this.title = title;
        this.category = category;
        this.status = status;
        this.description = description;
        this.resolutionDetails = resolutionDetails;
        this.dateTimeReported = dateTimeReported;
        this.dateTimeResolved = dateTimeResolved;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResolutionDetails() {
        return resolutionDetails;
    }

    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }

    public String getDateTimeReported() {
        return dateTimeReported;
    }

    public void setDateTimeReported(String dateTimeReported) {
        this.dateTimeReported = dateTimeReported;
    }

    public String getDateTimeResolved() {
        return dateTimeResolved;
    }

    public void setDateTimeResolved(String dateTimeResolved) {
        this.dateTimeResolved = dateTimeResolved;
    }
}
