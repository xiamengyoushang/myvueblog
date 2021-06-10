package com.peng.vueblog.controller;


import com.peng.vueblog.common.lang.Result;
import com.peng.vueblog.entity.User;
import com.peng.vueblog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author peng.lei
 * @since 2021-05-30
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("获取用户信息")
    // http://localhost:8081/user/index
    @RequiresAuthentication  // 需要进行认证的注解
    @GetMapping("/index")
    public Result index(){
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public Result list(){
        List<User> userList = userService.getBaseMapper().selectList(null);
        return Result.succ(userList);
    }

    @ApiOperation("编辑用户")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){
        return Result.succ(user);
    }

}
