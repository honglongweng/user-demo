package com.ken.system.user.model;

import com.ken.system.user.cache.UserCache;
import com.ken.system.user.entity.Role;
import com.ken.system.user.entity.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {
    private static UserCache userCache;

    @BeforeClass
    public static void beforeClass() {
        userCache = UserCache.getInstance();
    }

    @Before
    public void beforeTest() {
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
            assertEquals("已存在用户：ken", exception.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        try {
            userCache.deleteUser("ken");
            userCache.deleteUser("notExist");
        } catch (RuntimeException exception) {
            assertEquals("不存在用户：notExist", exception.getMessage());
        }
    }

    @Test
    public void testDeleteRole() {
        try {
            userCache.deleteRole("role1");
            userCache.deleteRole("role3");
        } catch (RuntimeException exception) {
            assertEquals("不存在角色：role3", exception.getMessage());
        }
    }

    @Test
    public void testAddMoreRelation() {
        try {
            userCache.addRoleUserRelation("other", "role2");
            userCache.addRoleUserRelation("ken", "role1");
            userCache.addRoleUserRelation("notExist", "notExist");
        } catch (RuntimeException exception) {
            assertEquals("不存在用户：notExist", exception.getMessage());
        }
    }

    @Test
    public void testGetAllRoles() {
        List<Role> roles = userCache.getAllRoles();
        assertEquals(2, roles.size());
        assertEquals("role1", roles.get(0).getRoleName());
        assertEquals("role2", roles.get(1).getRoleName());
    }

    @Test
    public void testCheckRole() {
        List<String> roles = userCache.getRoleByUser("ken");
        assertTrue(roles.contains("role1"));
        assertNull(userCache.getRoleByUser("notExist"));
    }

    private void initSomeUsersAndRoles() {
        userCache.clear();
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