package com.ihrm.common.controller;

import com.ihrm.domain.system.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected String companyId;

    protected String companyName;

    protected String userId;

    protected Claims claims;

    //加上@ModelAttribute在进入Controller之前执行
    //jwt方式
//    @ModelAttribute
//    public void setResAnReq(HttpServletRequest request, HttpServletResponse response) {
//        this.request = request;
//        this.response = response;
//
//        Object obj = request.getAttribute("user_claims");
//        if (obj != null) {
//            this.claims = (Claims) obj;
//            this.companyId = (String) claims.get("companyId");
//            this.companyName = (String) claims.get("companyName");
//        }
//
//    }

    //shiro方式
    @ModelAttribute
    public void setResAnReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        if (principals != null && !principals.isEmpty()) {
            ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
            this.companyId = result.getCompanyId();
            this.companyName = result.getCompany();
            this.userId = result.getUserId();
        }

    }
}
