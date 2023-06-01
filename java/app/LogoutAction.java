package app;

import com.opensymphony.xwork2.ActionContext;

public class LogoutAction {

    public String execute() {
        ActionContext.getContext().getSession().remove("user");
        return "success";
    }
}
