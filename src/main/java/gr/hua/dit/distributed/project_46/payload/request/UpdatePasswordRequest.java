package gr.hua.dit.distributed.project_46.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatePasswordRequest {
    @NotBlank(message="Please enter the password")
    @Size(min = 6, max = 40, message = "Password should be between 6 and 40 characters")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
