package com.ken.system.user.controller.dto;

import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;

public class UserRoleDTO {
    private User user;
    private Role role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
