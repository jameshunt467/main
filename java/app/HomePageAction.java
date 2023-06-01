package app;

import com.opensymphony.xwork2.ActionContext;

import java.util.Objects;

public class HomePageAction {

    public String execute() {
        UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");

        if (user == null) {
            return "error";
        }

        if (Objects.equals(user.getRole(), "student")) {
            return "student";
        } else if (Objects.equals(user.getRole(), "staff")) {
            return "staff";
        } else if (Objects.equals(user.getRole(), "manager")) {
            return "manager";
        }

        return "success";
    }
}
