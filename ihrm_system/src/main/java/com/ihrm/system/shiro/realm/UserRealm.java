package com.ihrm.system.shiro.realm;

import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class UserRealm extends IhrmRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //认证方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        User user = userService.findByMobile(mobile);
        if (user != null && user.getPassword().equals(password)) {
            ProfileResult result = null;
            if ("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            } else {
                HashMap<String, Object> map = new HashMap<>();
                if ("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible", "1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user, list);
            }

            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;
        }
        //返回Null会抛出异常
        return null;
    }
}
