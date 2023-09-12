package com.ken.system.user.model;

import com.ken.system.user.cache.UserCache;
import com.ken.system.user.common.TokenUtil;
import com.ken.system.user.entity.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenTest {

    private static UserCache userCache;

    String testToken;

    @BeforeClass
    public static void beforeClass() {
        userCache = UserCache.getInstance();
    }

    @Before
    public void beforeTest() {
        initToken();
    }

    @Test
    public void testTokenGenerate() {
        try {
            assertEquals("ken", TokenUtil.getUsername(testToken));
            // 此处测试的话把TokenUtil里过期时间变为1s
            Thread.sleep(1000);
//            assertEquals("ken", TokenUtil.getUsername(testToken));
        } catch (InterruptedException | RuntimeException e) {
            assertEquals("token 已过期", e.getMessage());
        }
    }

    @Test
    public void testAuthToken() {
        String username = TokenUtil.getUsername(testToken);
        assertTrue(userCache.authToken(testToken, username));
        try {
            // 此处测试的话把UserCache里过期时间变为1s
            Thread.sleep(1000);
//            assertFalse(userCache.authToken(testToken, username));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testInvalid() {
        String username = TokenUtil.getUsername(testToken);
        assertTrue(userCache.authToken(testToken, username));
        userCache.invalidToken(testToken);
        assertFalse(userCache.authToken(testToken, username));
    }

    private void initToken() {
        userCache.clear();
        User user = new User() {{
            setUsername("ken");
            setPassword("123");
        }};
        userCache.addUser(user);
        testToken = TokenUtil.token(user.getUsername(), user.getPassword());
        userCache.addToken(testToken, user.getUsername());
    }
}
