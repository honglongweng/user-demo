package com.ken.system.user.service;

import com.ken.system.user.common.Result;
import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;

public interface IUserService {
	Result createUser(User user);
	Result deleteUser(User user);
	Result createRole(Role role);
	Result deleteRole(Role role);
	Result addRole(User user, Role role);
	Result authenticate(User user);
	Result invalidate(String token);
	Result checkRole(String token, Role role);
	Result allRoles(String token);
}
