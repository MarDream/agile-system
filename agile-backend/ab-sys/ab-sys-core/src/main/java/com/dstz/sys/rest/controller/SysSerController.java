package com.dstz.sys.rest.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.utils.IdGeneratorUtils;
import com.dstz.sys.core.entity.SysDataDict;

/**
 * <p>
 * 系统附件 前端控制器
 * </p>
 *
 * @since 2022-02-17
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/serialNo")
public class SysSerController {

   
 
    @GetMapping(value = "/getConfigByCode/{code}")
    public ApiResponse<SysDataDict> getConfigByCode(@PathVariable("code") String code, HttpServletResponse response) {
    	return ApiResponse.success(new SysDataDict());
    }
    
    @RequestMapping(value = "getNextNo", method = RequestMethod.POST)
    public ApiResponse<String> getNextNo( ) {
    	return ApiResponse.success(IdGeneratorUtils.nextId());
    }

}
