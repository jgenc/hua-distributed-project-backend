package gr.hua.dit.distributed.project_46.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
    @NotBlank(message="Please enter the username")
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String username;

    @NotBlank(message="Please enter the Taxpayer Identification Number")
    @Size(min = 9, max = 9, message = "Taxpayer Identification Number should be 9 characters")
    private String tin;

    private String role;

    @NotBlank(message="Please enter the password")
    @Size(min = 6, max = 40, message = "Password should be between 6 and 40 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}