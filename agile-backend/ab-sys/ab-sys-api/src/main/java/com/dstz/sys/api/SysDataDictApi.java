package com.dstz.sys.api;

import com.dstz.sys.api.vo.SysDataDictVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Name SysDataDictApi
 * @description: 字典
 * @date 2022/2/2317:13
 */
public interface SysDataDictApi {
    /**
     * 获取字典列表
     *
     * @param dictKey
     * @param hasRoot
     * @return
     */
    List<SysDataDictVO> getDictNodeList(String dictKey, Boolean hasRoot);

    /**
     * 批量新增或者保存
     * @param dtoList
     */
    void batchSaveOrUpdate(List<SysDataDictVO> dtoList) ;

    /**
     * 导出分类
     * @param dictKey 分类的字典key
     * @param typeCodes 要导出的分类字典项的codes 集合
     * @return
     */
    List<SysDataDictVO> exportTypeData(String dictKey, Set<String> typeCodes);

    /**
     * 通过code删除
     * @param codes
     */
    void deleteByCodes(Collection<? extends Serializable> codes);
}
