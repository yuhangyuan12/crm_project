package com.hhy.crm.service;

import com.hhy.crm.base.BaseService;
import com.hhy.crm.dao.ModuleMapper;
import com.hhy.crm.dao.PermissionMapper;
import com.hhy.crm.dao.RoleMapper;
import com.hhy.crm.utils.AssertUtil;
import com.hhy.crm.vo.Permission;
import com.hhy.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    //查询所有的角色
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    //添加角色
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        //通过角色名查询记录
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp,"角色名称已经存在！");

        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"角色添加失败！");
    }

    //更新角色
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
       AssertUtil.isTrue(null == role.getId(),"待更新记录不存在！");
       Role temp = roleMapper.selectByPrimaryKey(role.getId());
       AssertUtil.isTrue(null == temp,"待更新记录不存在!");

       AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空！");
       temp = roleMapper.selectByRoleName(role.getRoleName());
       AssertUtil.isTrue(null != temp&&(!temp.getId().equals(role.getId())),"角色名称已存在！");

       role.setUpdateDate(new Date());
       AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"更新失败！");
    }

    //删除角色
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer id){
        AssertUtil.isTrue(null == id,"待删除记录不存在！");
        Role role = roleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == role,"待删除记录不存在！");

        role.setIsValid(0);
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除失败！");
    }


    //角色授权操作
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mids) {
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        //判断记录是否存在 删除对应的角色拥有的权限记录
        if(count>0){
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        //批量添加
        if(mids != null&&mids.length>0){
            List<Permission> permissionList = new ArrayList<>();
            for (Integer mid :mids) {
                Permission permission = new Permission();
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)!=permissionList.size(),"角色授权失败！");
        }
    }
}
