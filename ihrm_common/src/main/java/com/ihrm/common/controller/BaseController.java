package com.ihrm.common.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected String companyId;

    protected String companyName;

    //加上@ModelAttribute在进入Controller之前执行
    @ModelAttribute
    public void setResAnReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.companyId = "1";
        this.companyName = "companyName";
    }
}
