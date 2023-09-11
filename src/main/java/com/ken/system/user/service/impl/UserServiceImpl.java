package com.ken.system.user.service.impl;

import com.ken.system.user.cache.UserCache;
import com.ken.system.user.common.Result;
import com.ken.system.user.common.TokenUtil;
import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;
import com.ken.system.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

	public UserServiceImpl() {
		this.userCache = UserCache.getInstance();
	}

	private final UserCache userCache;

	@Override
	public Result createUser(User user) {
		try {
			userCache.addUser(user);
			return Result.success("创建用户成功！");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result deleteUser(User user) {

		try {
			userCache.deleteUser(user.getUsername());
			return Result.success("删除用户成功！");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result createRole(Role role) {
		try {
			userCache.addRole(role);
			return Result.success("创建角色成功！");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result deleteRole(Role role) {
		try {
			userCache.deleteRole(role.getRoleName());
			return Result.success("删除角色成功！");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result addRole(User user, Role role) {
		try {
			userCache.addRoleUserRelation(user.getUsername(), role.getRoleName());
			return Result.success("用户添加角色成功");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result authenticate(User user) {
		try {
			User existUser = userCache.getUser(user.getUsername());
			if (!existUser.getPassword().equals(user.getPassword())) {
				return Result.fail("用户名或密码错误！");
			}
			String token = TokenUtil.token(user.getUsername(), user.getPassword());
			userCache.addToken(token, user.getUsername());
			return Result.data(token, "登陆成功！");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result invalidate(String token) {
		try {
			String username = TokenUtil.getUsername(token);
			if (!userCache.authToken(token, username)) {
				return Result.fail("token失效，请重新登陆！");
			}
			userCache.invalidToken(token);
			return Result.success("token成功失效");
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result checkRole(String token, Role role) {
		try {
			String username = TokenUtil.getUsername(token);
			if (!userCache.authToken(token, username)) {
				return Result.fail("token失效，请重新登陆！");
			}
			List<String> roles = userCache.getRoleByUser(username);
			if (roles == null || roles.isEmpty()) {
				return Result.fail("当前用户没有绑定任何角色！");
			}
			return Result.data(roles.contains(role.getRoleName()));
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public Result allRoles(String token) {
		try {
			String username = TokenUtil.getUsername(token);
			if (!userCache.authToken(token, username)) {
				return Result.fail("token失效，请重新登陆！");
			}
			return Result.data(userCache.getAllRoles());
		} catch (RuntimeException e) {
			return Result.fail(e.getMessage());
		}
	}
}
