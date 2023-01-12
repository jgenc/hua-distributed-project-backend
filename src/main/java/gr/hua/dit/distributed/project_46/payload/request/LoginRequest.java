package gr.hua.dit.distributed.project_46.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message="Please enter the username")
    private String username;

    @NotBlank(message="Please enter the password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}