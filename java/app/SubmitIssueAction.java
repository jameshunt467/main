package app;

import com.opensymphony.xwork2.ActionSupport;
// import javax.servlet.http.HttpSession;
// import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class SubmitIssueAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;

    public String execute() {
        String username = (String) session.get("username");
        
        if (username != null) {
            // Use username here...
            return SUCCESS;
        } else {
            // Handle case where username is not in session or not a string...
            addActionError("User is not logged in.");
            return LOGIN;
        }
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    
}
