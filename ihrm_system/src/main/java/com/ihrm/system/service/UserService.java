package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    public void save(User user) {
        String id = String.valueOf(idWorker.nextId());
        user.setId(id);
        user.setPassword("123456");
        user.setEnableState(1);
        userDao.save(user);
    }

    public void update(User user) {
        User target = userDao.findById(user.getId()).get();
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        userDao.save(target);
    }

    public User findById(String id) {
        return userDao.findById(id).get();
    }

    public Page<User> findAll(Map<String, Object> map, int page, int size) {
        //查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if ("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return userDao.findAll(spec, new PageRequest(page - 1, size));
    }

    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    public void assignRoles(String userId, List<String> roleIds) {
        User user = userDao.findById(userId).get();
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        user.setRoles(roles);
        userDao.save(user);
    }

    public User findByMobile(String mobile) {
        User user = userDao.findByMobile(mobile);
        return user;
    }
}
