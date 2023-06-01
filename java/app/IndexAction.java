package app;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.interceptor.SessionAware; // retrieve session
import java.util.Map;                               // retrieve session

public class IndexAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;

    @Override
    public String execute() throws Exception {
        UserBean user = (UserBean) session.get("user");
        if (user != null) {
            if (user instanceof StudentBean) {
                return "STUDENT_HOME";
            } else if (user instanceof StaffBean) {
                return "STAFF_HOME";
            }
        } 
        return SUCCESS; // go to login page
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
