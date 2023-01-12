package gr.hua.dit.distributed.project_46.payload.response;

import gr.hua.dit.distributed.project_46.entity.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserResponse {
    private Long id;
    private String username;
    private String tin;
    private List<String> roles;

    public UserResponse(Long id, String username, String tin, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.tin = tin;
        this.roles = roles.stream().map(item -> item.getName().name()).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
