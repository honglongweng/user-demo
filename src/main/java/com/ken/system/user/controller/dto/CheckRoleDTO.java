package com.ken.system.user.controller.dto;

import com.ken.system.user.entity.Role;

public class CheckRoleDTO extends TokenDTO{
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
