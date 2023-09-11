package com.ken.system.user.controller;

import com.ken.system.user.common.Result;
import com.ken.system.user.controller.dto.CheckRoleDTO;
import com.ken.system.user.controller.dto.TokenDTO;
import com.ken.system.user.controller.dto.UserRoleDTO;
import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;
import com.ken.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

	@Autowired
	private IUserService userService;

	@RequestMapping("/user/create")
	Result createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@RequestMapping("/user/delete")
	Result deleteUser(@RequestBody User user) {
		return userService.deleteUser(user);
	}

	@RequestMapping("/role/create")
	Result createRole(@RequestBody Role role) {
		return userService.createRole(role);
	}

	@RequestMapping("/role/delete")
	Result deleteRole(@RequestBody Role role) {
		return userService.deleteRole(role);
	}

	@RequestMapping("/user/add_role")
	Result addRole(@RequestBody UserRoleDTO userRoleDTO) {
		return userService.addRole(userRoleDTO.getUser(), userRoleDTO.getRole());
	}

	@RequestMapping("/user/auth")
	Result authenticate(@RequestBody User user) {
		return userService.authenticate(user);
	}
	
	@RequestMapping("/user/invalid_auth")
	Result invalidate(@RequestBody TokenDTO token) {
		return userService.invalidate(token.getToken());
	}
	
	@RequestMapping("/user/check_role")
	Result checkRole(@RequestBody CheckRoleDTO dto) {
		return userService.checkRole(dto.getToken(), dto.getRole());
	}
	
	@RequestMapping("/role/list")
	Result allRoles(@RequestBody TokenDTO token) {
		return userService.allRoles(token.getToken());
	}
}
