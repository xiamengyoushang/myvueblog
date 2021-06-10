package com.peng.vueblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.peng.vueblog.entity.User;
import com.peng.vueblog.service.UserService;
import com.peng.vueblog.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        // 判断是否是我们支持的Token
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户信息
        JwtToken jwtToken = (JwtToken)token;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账号不存在");
        }
        if (user.getStatus() == -1) {
            throw new LockedAccountException("账号已被锁定");
        }
        System.out.println("token: "+jwtToken);
        System.out.println("userId: "+userId);
        System.out.println("user: "+user);
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);
        // 将用户信息返回给Shiro
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }

}
