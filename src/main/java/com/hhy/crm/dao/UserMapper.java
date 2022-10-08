package com.hhy.crm.dao;

import com.hhy.crm.base.BaseMapper;
import com.hhy.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    public User queryByUserName(String userName);

    public List<Map<String,Object>> queryAllSales();

}