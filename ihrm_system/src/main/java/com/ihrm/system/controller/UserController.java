package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.User;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam() Map map) {
        map.put("companyId", companyId);
        Page pageUser = userService.findAll(map, page, size);
        PageResult<User> pageResult = new PageResult<User>(pageUser.getTotalElements(), pageUser.getContent());
        return new Result(ResultCode.SUCCESS, pageResult);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS, user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}