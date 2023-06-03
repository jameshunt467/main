package app;

public class CommentBean {
    private String comment;
    private String dateTimePosted;
    private String username;

    public CommentBean() {

    }

    public CommentBean(String comment, String dateTimePosted, String username) {
        this.comment = comment;
        this.dateTimePosted = dateTimePosted;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public String getDateTimePosted() {
        return dateTimePosted;
    }

    public String getUsername() {
        return username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDateTimePosted(String dateTimePosted) {
        this.dateTimePosted = dateTimePosted;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
