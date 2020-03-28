package com.ihrm.common.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected String companyId;

    protected String companyName;

    protected Claims claims;

    //加上@ModelAttribute在进入Controller之前执行
    @ModelAttribute
    public void setResAnReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        Object obj = request.getAttribute("user_claims");
        if (obj != null) {
            this.claims = (Claims) obj;
            this.companyId = (String) claims.get("companyId");
            this.companyName = (String) claims.get("companyName");
        }

    }
}
