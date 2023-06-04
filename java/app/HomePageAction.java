package app;

import com.opensymphony.xwork2.ActionContext;

public class HomePageAction {

    public String execute() {
        UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");

        // if user session is null, return error
        if (user == null) {
            return "error";
        }

        // if user role is student, return student, if staff return staff
        String userRole = user.getRole();
        if ("student".equals(userRole)) {
            return "student";
        } else {
            return "staff";
        }

    }
}
