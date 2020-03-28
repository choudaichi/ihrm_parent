package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PermissionService permissionService;


    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String, Object> map) {
        String userId = (String) map.get("id");
        List<String> roleIds = (List<String>) map.get("roleIds");
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }

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
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, name = "API-USER-DELETE")
    public Result delete(@PathVariable("id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, String> loginMap) {
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        User user = userService.findByMobile(mobile);
        if (user == null || !user.getPassword().equals(password)) {
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Role role : user.getRoles()) {
                for (Permission perm : role.getPermissions()) {
                    if (perm.getType() == PermissionConstants.PERMISSION_API) {
                        sb.append(perm.getCode()).append(",");
                    }
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("companyId", user.getCompanyId());
            map.put("companyName", user.getCompanyName());
            map.put("apis", sb.toString());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS, token);
        }
    }

    /**
     * 用户登录成功后，获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {

        String userId = claims.getId();
        User user = userService.findById(userId);

        ProfileResult result = null;
        //不同等级角色不同权限
        if ("user".equals(user.getLevel())) {
            result = new ProfileResult(user);
        } else {
            Map<String, Object> map = new HashMap<>();
            if ("coAdmin".equals(user.getLevel())) {
                map.put("enVisible", "1");
            }
            List<Permission> list = permissionService.findAll(map);
            result = new ProfileResult(user, list);
        }

        return new Result(ResultCode.SUCCESS, result);

    }

}