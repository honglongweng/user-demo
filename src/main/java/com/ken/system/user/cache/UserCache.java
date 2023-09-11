package com.ken.system.user.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class UserCache {
    private Cache<String, User> userCache;
    private Cache<String, Role> roleCache;
    private Cache<String, Set<String>> userRoleRelation;
    private Cache<String, Set<String>> roleUserRelation;
    private Cache<String, String> tokenCache;

    private static class CacheHolder {
        private final static UserCache instance = new UserCache();
    }

    public UserCache() {
        initCache();
    }

    private void initCache() {
        userCache = CacheBuilder.newBuilder().build();
        roleCache = CacheBuilder.newBuilder().build();
        userRoleRelation = CacheBuilder.newBuilder().build();
        roleUserRelation = CacheBuilder.newBuilder().build();
        tokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build();
    }

    public static UserCache getInstance() {
        return CacheHolder.instance;
    }

    public User getUser(String username) {
        return userCache.getIfPresent(username);
    }

    public void addUser(User user) {
        if (userCache.asMap().putIfAbsent(user.getUsername(), user) != null ) {
            throw new RuntimeException("已存在用户：" + user.getUsername());
        }
    }

    public void deleteUser(String username) {
        if (userCache.asMap().remove(username) == null) {
            throw new RuntimeException("不存在用户：" + username);
        } else {
            // 成功删除用户，删去用户相关
            Set<String> roles = userRoleRelation.asMap().remove(username);
            if (roles != null && !roles.isEmpty()) {
                roles.forEach(role -> Objects.requireNonNull(roleUserRelation.getIfPresent(role)).removeIf((user) -> user.equals(username)));
            }
        }
    }

    public List<String> getRoleByUser(String username) {
        Set<String> roles = userRoleRelation.getIfPresent(username);
        return roles == null ? null : roles.stream().toList();
    }

    public List<Role> getAllRoles() {
        return roleCache.asMap().values().stream().toList();
    }

    public void addRole(Role role) {
        if (roleCache.asMap().putIfAbsent(role.getRoleName(), role) != null ) {
            throw new RuntimeException("已存在角色：" + role.getRoleName());
        }
    }

    public void deleteRole(String roleName) {
        if (roleCache.asMap().remove(roleName) == null) {
            throw new RuntimeException("不存在角色：" + roleName);
        } else {
            // 成功删除角色，删去角色相关
            Set<String> users = roleUserRelation.asMap().remove(roleName);
            if (users != null && !users.isEmpty()) {
                users.forEach(user -> Objects.requireNonNull(userRoleRelation.getIfPresent(user)).removeIf((role) -> role.equals(roleName)));
            }
        }
    }

    public void addRoleUserRelation(String username, String roleName) {
        if (userCache.getIfPresent(username) == null) {
            throw new RuntimeException("不存在用户：" + username);
        }
        if (roleCache.getIfPresent(roleName) == null) {
            throw new RuntimeException("不存在角色：" + roleName);
        }
        userRoleRelation.asMap().computeIfAbsent(username, k -> new HashSet<>()).add(roleName);
        roleUserRelation.asMap().computeIfAbsent(roleName, k -> new HashSet<>()).add(username);
    }

    public boolean authToken(String token, String username) {
        return username.equals(tokenCache.getIfPresent(token));
    }

    public void addToken(String token, String username) {
        tokenCache.asMap().putIfAbsent(token, username);
    }

    public void invalidToken(String token) {
        tokenCache.invalidate(token);
    }

}
