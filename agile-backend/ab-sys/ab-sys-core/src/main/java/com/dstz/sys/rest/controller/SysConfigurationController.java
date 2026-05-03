package com.dstz.sys.rest.controller;


import cn.hutool.core.util.ObjectUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.core.entity.SysConfiguration;
import com.dstz.sys.core.manager.SysConfigurationManager;
import com.dstz.sys.rest.model.vo.SysConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.dstz.base.api.vo.ApiResponse.success;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @since 2023-05-11
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/sysConfiguration")
public class SysConfigurationController extends AbCrudController<SysConfiguration> {

    @Override
    protected String getEntityDesc() {
        return "";
    }

    @Autowired
    SysConfigurationManager sysConfigurationManager;

    /**
     * 根据编码获取系统配置
     * @param code 配置编码
     * @return 配置信息
     */
    @RequestMapping({"/getConfByCode/{code}"})
    public ApiResponse<String> getConfByCode(@PathVariable String code) {
        return ApiResponse.success(ObjectUtil.isNotNull(sysConfigurationManager.getConfByCode(code))?sysConfigurationManager.getConfByCode(code).getJson():"");
    }

    /**
     * 根据编码获取系配置对象
     * @param code 配置编码
     * @return 配置信息
     */
    @GetMapping({"/getConfObjByCode/{code}"})
    public ApiResponse<SysConfiguration> getConfObjByCode(@PathVariable String code) {
        return ApiResponse.success(sysConfigurationManager.getConfByCode(code));
    }

    /**
     * 保存实体数据
     *
     * @param sysConfiguration 实体
     * @return 接口响应-实体ID
     */
    @Override
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResponse<String> save(@Valid @RequestBody SysConfiguration sysConfiguration) {
        invokeBeforeCheck("saveCheck", sysConfiguration);
        sysConfigurationManager.save(sysConfiguration);
        return ApiResponse.success(sysConfiguration.getId());
    }

    /**
     * 获取系统属性集合
     */
    @RequestMapping(value = "/getSysProperties")
    public ApiResponse<List<SysConfigurationVO>> getSysProperties(@Valid @RequestParam  String codes) {
        return ApiResponse.success(sysConfigurationManager.getSysProperties(codes));
    }

    /**
     * 根据编码获取系统配置
     * @param code 配置编码
     * @return 配置信息
     */
    @RequestMapping(value = "/changeEnableByCode")
    public ApiResponse<String> changeEnableByCode(@Valid @RequestParam  String code) {
      sysConfigurationManager.changeEnableByCode(code);
      return success();
    }
}
