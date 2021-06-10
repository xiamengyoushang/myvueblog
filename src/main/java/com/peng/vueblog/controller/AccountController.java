package com.peng.vueblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.vueblog.common.dto.LoginDto;
import com.peng.vueblog.common.dto.RegisterDto;
import com.peng.vueblog.common.lang.Result;
import com.peng.vueblog.entity.User;
import com.peng.vueblog.service.UserService;
import com.peng.vueblog.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/*
* 使用账号功能，需要开启redis
* cd /usr/local/Cellar/redis/6.2.3/bin
* ./redis-server
 * */
@Api(tags = "账号管理接口")
@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        /*
            测试账号密码{
                "username":"markerhub",
                "password":"111111"
            }
        */
        User user =  userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));
        /*
        * 走断言的话是抛系统异常，
        * 前端前置响应拦截（interceptors）之前会直接被浏览器拦截掉导致前端收不到后端真实的响应信息
        * */
        if (user == null) {
            return Result.fail("账号不存在");
        }
        // Assert.notNull(user,"账号不存在");
        System.out.println("用户原密码："+user.getPassword());
        System.out.println("用户登录密码："+loginDto.getPassword());
        System.out.println("用户加密后密码："+SecureUtil.md5(loginDto.getPassword()));
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        // 此处可以回响应到hearder头里，一般不这么做
        // response.setHeader("Authorization",jwt);
        // response.setHeader("Access-control-Expose-Headers","Authorization");
        return Result.succ(MapUtil.builder()
                .put("id",user.getId())
                .put("username",user.getUsername())
                .put("token",jwt)
                .map()
        );
    }

    @ApiOperation("登出")
    @RequiresAuthentication
    @PostMapping("/logout")
    public Result logout() {
        System.out.println("logout--->"+SecurityUtils.getSubject().getPrincipal());
        System.out.println("logout--->"+SecurityUtils.getSubject().isAuthenticated());
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto registerDto){
        User user = userService.getOne(new QueryWrapper<User>().eq("username",registerDto.getUsername()));
        Assert.isNull(user,"账号已存在");
        user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(SecureUtil.md5(registerDto.getPassword())); // 密码入库做了一下MD5加密
        user.setEmail(registerDto.getEmail());
        user.setStatus(0);
        Assert.isTrue(userService.save(user),"账号注册失败");
        return Result.succ(null);
    }

}
