package app;

public class StaffBean extends UserBean {
    private String staffNumber;
    private boolean manager;

    public StaffBean(){}

    public String getStaffNumber() {
        return staffNumber;
    }
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }
    public boolean isManager() {
        return manager;
    }
    public void setManager(boolean manager) {
        this.manager = manager;
    }
}
