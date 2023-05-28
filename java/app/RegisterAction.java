package app;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import java.util.Objects;

public class RegisterAction extends ActionSupport implements ModelDriven<RegisterBean> {

    private RegisterBean model = new RegisterBean();

    @Override
    @Validations(
            requiredStrings = {
                    @RequiredStringValidator(fieldName = "name", message = "name is required"),
                    @RequiredStringValidator(fieldName = "age", message = "age is required"),
                    @RequiredStringValidator(fieldName = "email", message = "email is required"),
                    @RequiredStringValidator(fieldName = "password", message = "password is required"),
                    @RequiredStringValidator(fieldName = "passwordConfirm", message = "confirmPassword is required")
            },
            intRangeFields = {
                    @IntRangeFieldValidator(fieldName = "age", min = "18", max = "100", message = "age must be between 18 and 100")
            },
            emails = {
                    @EmailValidator(fieldName = "email", message = "email is not a valid email")
            }
    )
    public String execute() throws Exception {
        return SUCCESS;
    }

    @Override
    public void validate() {
        if (!Objects.equals(this.model.getPassword(), this.model.getPasswordConfirm())) {
            this.addFieldError("passwordConfirm", "Password and confirmation do not match");
        }
    }

    @Override
    public RegisterBean getModel() {
        return model;
    }
}
