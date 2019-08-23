package com.yupaits.sample.springsecurityjwt;

import com.yupaits.sample.user.model.User;
import com.yupaits.sample.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yupaits
 * @date 2019/8/23
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test1_addUser() {
        String password = passwordEncoder.encode("password");
        User user = User.builder()
                .username("user")
                .password(password)
                .build();
        Assert.assertTrue(userService.save(user));
    }
}
