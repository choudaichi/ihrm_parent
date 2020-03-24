package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private PermissionApiDao permissionApiDao;


    public void save(Map<String, Object> map) throws Exception {
        String id = String.valueOf(idWorker.nextId());
        Permission perm = BeanMapUtils.mapToBean(map, Permission.class);
        perm.setId(id);
        int type = perm.getType();
        switch (type) {
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(id);
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(id);
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(id);
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.save(perm);
    }

    public void update(Map<String, Object> map) throws Exception {
        Permission perm = BeanMapUtils.mapToBean(map, Permission.class);

        Permission permission = permissionDao.findById(perm.getId()).get();
        permission.setName(perm.getName());
        permission.setCode(perm.getCode());
        permission.setDescription(perm.getDescription());
        permission.setEnVisible(perm.getEnVisible());

        int type = perm.getType();
        switch (type) {
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(perm.getId());
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(perm.getId());
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(perm.getId());
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }

        permissionDao.save(permission);
    }

    public Map<String, Object> findById(String id) throws Exception {
        Permission perm = permissionDao.findById(id).get();
        int type = perm.getType();

        Object object = null;

        if (type == PermissionConstants.PERMISSION_MENU) {
            object = permissionMenuDao.findById(id).get();
        } else if (type == PermissionConstants.PERMISSION_POINT) {
            object = permissionPointDao.findById(id).get();
        } else if (type == PermissionConstants.PERMISSION_API) {
            object = permissionApiDao.findById(id).get();
        } else {
            throw new CommonException(ResultCode.FAIL);
        }

        Map<String, Object> map = BeanMapUtils.beanToMap(object);

        map.put("name", perm.getName());
        map.put("type", perm.getType());
        map.put("code", perm.getCode());
        map.put("description", perm.getDescription());
        map.put("pid", perm.getPid());
        map.put("enVisible", perm.getEnVisible());

        return map;

    }

    public List<Permission> findAll(Map<String, Object> map) {
        //查询条件
        Specification<Permission> spec = new Specification<Permission>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("pid"))) {
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class), map.get("pid")));
                }
                if (!StringUtils.isEmpty(map.get("enVisible"))) {
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class), map.get("enVisible")));
                }
                if (!StringUtils.isEmpty(map.get("type"))) {
                    String ty = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if ("0".equals(ty)) {
                        in.value(1).value(2);
                    } else {
                        in.value(Integer.parseInt(ty));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(spec);
    }

    public void deleteById(String id) throws Exception {
        //1.通过传递的权限id查询权限
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //2.根据类型构造不同的资源
        int type = permission.getType();
        switch (type) {
            case PermissionConstants.PERMISSION_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PERMISSION_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PERMISSION_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }
}
