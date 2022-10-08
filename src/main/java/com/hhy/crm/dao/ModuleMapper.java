package com.hhy.crm.dao;

import com.hhy.crm.base.BaseMapper;
import com.hhy.crm.model.TreeModel;
import com.hhy.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    public List<TreeModel> queryAllModules();

    //查询所有的资源数据
    public List<Module> queryModuleList();

    //通过层级关系查询对象
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade,@Param("moduleName") String moduleName);

    //通过url查询对象
    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade,@Param("url") String url);

    //通过权限码查询对象
    Module queryModuleByOptValue(String optValue);

    Integer countSubModuleByParentId(Integer id);
}