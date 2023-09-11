package com.ken.system.user.model;

import com.ken.system.user.cache.UserCache;
import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {
    private final UserCache userCache;

    public CacheTest() {
        userCache = UserCache.getInstance();
        initSomeUsersAndRoles();
    }

    @Test
    public void testAdd() {
        try {
            userCache.addUser(new User() {{
                setUsername("ken");
                setPassword("234");
            }});
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        try {
            userCache.deleteUser("ken");
            userCache.deleteUser("notExist");
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void testDeleteRole() {
        try {
            userCache.deleteRole("role1");
            userCache.deleteRole("role3");
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void testAddMoreRelation() {
        try {
            userCache.addRoleUserRelation("other", "role2");
            userCache.addRoleUserRelation("ken", "role1");
            userCache.addRoleUserRelation("notExist", "notExist");
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void testGetAllRoles() {
        List<Role> roles = userCache.getAllRoles();
        roles.stream().map(Role::getRoleName).forEach(System.out::println);
    }

    @Test
    public void testCheckRole() {
        List<String> roles = userCache.getRoleByUser("ken");
        System.out.println(roles.contains("role1"));
        System.out.println(userCache.getRoleByUser("notExist"));
    }

    private void initSomeUsersAndRoles() {
        userCache.addUser(new User() {{
            setUsername("ken");
            setPassword("123");
        }});
        userCache.addUser(new User() {{
            setUsername("other");
            setPassword("234");
        }});
        userCache.addRole(new Role() {{
            setRoleName("role1");
        }});
        userCache.addRole(new Role() {{
            setRoleName("role2");
        }});
        userCache.addRoleUserRelation("ken", "role1");
        userCache.addRoleUserRelation("ken", "role2");
        userCache.addRoleUserRelation("other", "role1");
    }

}