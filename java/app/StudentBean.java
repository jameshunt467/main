package app;

public class StudentBean extends UserBean {
    private String studentNumber;

    public StudentBean() {}

    public String getStudentNumber() {
        return studentNumber;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
