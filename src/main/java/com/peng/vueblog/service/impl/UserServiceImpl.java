package com.peng.vueblog.service.impl;

import com.peng.vueblog.entity.User;
import com.peng.vueblog.mapper.UserMapper;
import com.peng.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peng.lei
 * @since 2021-05-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
