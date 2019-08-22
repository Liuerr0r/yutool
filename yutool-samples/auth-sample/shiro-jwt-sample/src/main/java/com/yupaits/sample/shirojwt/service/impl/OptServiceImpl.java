package com.yupaits.sample.shirojwt.service.impl;

import com.yupaits.sample.user.model.User;
import com.yupaits.sample.user.service.UserService;
import com.yupaits.yutool.commons.service.OptService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yupaits
 * @date 2019/8/22
 */
@Service
public class OptServiceImpl implements OptService {
    private final UserService userService;

    @Autowired
    public OptServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getOperatorId() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.getByUsername(username);
        if (user == null) {
            return null;
        }
        return String.valueOf(user.getId());
    }

    @Override
    public String getOptName(String operatorId) {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }
}
