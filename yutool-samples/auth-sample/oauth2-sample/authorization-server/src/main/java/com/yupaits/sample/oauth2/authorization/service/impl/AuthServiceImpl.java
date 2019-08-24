package com.yupaits.sample.oauth2.authorization.service.impl;

import com.yupaits.sample.oauth2.authorization.service.AuthService;
import com.yupaits.sample.user.service.UserService;
import com.yupaits.sample.user.vo.UserVo;
import com.yupaits.yutool.commons.exception.BusinessException;
import com.yupaits.yutool.commons.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yupaits
 * @date 2019/8/24
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Autowired
    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Result<UserVo> currentUser() throws BusinessException {

        return null;
    }
}
