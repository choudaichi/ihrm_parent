package com.ihrm.system.client;

import com.ihrm.common.entity.Result;
import com.ihrm.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("ihrm-company")
public interface DepartmentFeignClient {
    @RequestMapping(value = "/company/department/{id}", method = RequestMethod.GET)
    Result findById(@PathVariable(value = "id") String id);

    @RequestMapping(value = "/company/department/search", method = RequestMethod.POST)
    Department findByCode(@RequestParam("code") String code, @RequestParam("companyId") String companyId);
}
