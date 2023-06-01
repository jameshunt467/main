package app;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware; // retrieve session
import java.util.Map;                               // retrieve session

public class IndexAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;

    @Override
    public String execute() throws Exception {
        if (session.containsKey("isStudent")) {
            boolean isStudent = (boolean) session.get("isStudent");
            if(isStudent) {
                return "STUDENT_HOME";    // go straight to student home
            } else {
                return "STAFF_HOME";      // go straight to staff home
            }
        } else {
            return SUCCESS; // go to login page
        }
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
