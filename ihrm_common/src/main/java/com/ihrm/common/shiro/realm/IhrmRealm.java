package com.ihrm.common.shiro.realm;

import com.ihrm.domain.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

public class IhrmRealm extends AuthorizingRealm {

    public void setName(String name) {
        super.setName("IhrmRealm");
    }

    //授权方法
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        Set<String> apisPerms = (Set<String>) result.getRoles().get("apis");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(apisPerms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }
}
