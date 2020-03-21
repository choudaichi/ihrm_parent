package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService extends BaseService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Department department) {
        String id = String.valueOf(idWorker.nextId());
        department.setId(id);
        departmentDao.save(department);
    }

    public void update(Department department) {
        Department dept = departmentDao.findById(department.getId()).get();
        dept.setCode(department.getCode());
        dept.setIntroduce(department.getIntroduce());
        dept.setName(department.getName());
        departmentDao.save(dept);
    }

    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    public List<Department> findAll(String companyId) {

        return departmentDao.findAll(getSpec(companyId));
    }

    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }
}
