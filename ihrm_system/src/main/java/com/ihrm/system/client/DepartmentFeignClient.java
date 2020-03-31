package com.ihrm.system.client;

import com.ihrm.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ihrm-company")
public interface DepartmentFeignClient {
    @RequestMapping(value = "/company/department/{id}", method = RequestMethod.GET)
    Result findById(@PathVariable(value = "id") String id);
}
