package com.ken.system.user.model;

import com.ken.system.user.cache.UserCache;
import com.ken.system.user.common.TokenUtil;
import com.ken.system.user.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenTest {

    private final UserCache userCache;

    public TokenTest() {
        userCache = UserCache.getInstance();
    }

    @Test
    public void testTokenGenerate() {
        String token = initToken();
        try {
            // 此处测试的话把TokenUtil里过期时间变为1s
            Thread.sleep(1000);
            TokenUtil.getUsername(token);
        } catch (InterruptedException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testAuthToken() {
        String token = initToken();
        String username = TokenUtil.getUsername(token);
        System.out.println(userCache.authToken(token, username));
        try {
            Thread.sleep(1000);
            System.out.println(userCache.authToken(token, username));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testInvalid() {
        String token = initToken();
        String username = TokenUtil.getUsername(token);
        System.out.println(userCache.authToken(token, username));
        userCache.invalidToken(token);
        System.out.println(userCache.authToken(token, username));
    }

    private String initToken() {
        User user = new User() {{
            setUsername("ken");
            setPassword("123");
        }};
        userCache.addUser(user);
        String token = TokenUtil.token(user.getUsername(), user.getPassword());
        userCache.addToken(token, user.getUsername());
        return token;
    }
}
