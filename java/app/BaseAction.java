package app;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import app.UserBean;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public abstract class BaseAction extends ActionSupport implements SessionAware {
    protected Map<String, Object> session;

    public UserBean getLoggedInUser() {
        return (UserBean) session.get("user");
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
