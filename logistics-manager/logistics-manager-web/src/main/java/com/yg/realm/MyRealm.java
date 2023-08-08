package com.yg.realm;

import com.yg.pojo.Role;
import com.yg.pojo.User;
import com.yg.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 自定义的Realm 完成认证和授权操作
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    /**
     * 认证的方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        System.out.println("---->"+username);
        // 完成账号的验证
        User user = userService.login(username);
        if(user == null){
            return null;
        }
        String salt = user.getU1();
        return new SimpleAuthenticationInfo(user,user.getPassword(),new SimpleByteSource(salt),"myRealm");
    }

    /**
     * 授权的方法
     * 获取当前登录的账号具有的权限信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前登录用户的信息
        User user = (User)principalCollection.getPrimaryPrincipal();
        // 查询出当前用户具有的角色信息
        List<Role> roles = userService.queryUserHaveRole(user);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(roles != null && roles.size() > 0){
            for (Role role : roles) {
                info.addRole(role.getRoleName());
            }
            return info;
        }
        return null;
    }

    //这是一个main方法,是程序的入口:
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123","123",1024);
        System.out.println(md5Hash.toString());
    }
}
