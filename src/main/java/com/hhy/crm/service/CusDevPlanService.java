package com.hhy.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhy.crm.base.BaseService;
import com.hhy.crm.dao.CusDevPlanMapper;
import com.hhy.crm.query.CusDevPlanQuery;

import com.hhy.crm.utils.AssertUtil;
import com.hhy.crm.vo.CusDevPlan;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;


    //多条件分页查询  客户开发计划
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());

        //分完页的列表
        map.put("data",pageInfo.getList());
        return map;
    }


    //计划项添加
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        checkCusDevPlanParams(cusDevPlan);
        //设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());

        //判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"计划项数据添加失败！");
    }


    //计划项更新
    @Transactional(propagation = Propagation.REQUIRED)
    public void updataCusDevPlan(CusDevPlan cusDevPlan){
        //判断id是否存在
        AssertUtil.isTrue(null == cusDevPlan.getId()||null == selectByPrimaryKey(cusDevPlan.getId()),"待更新记录不存在！");
        //参数校验
        checkCusDevPlanParams(cusDevPlan);

        //设置默认值
        cusDevPlan.setUpdateDate(new Date());
        //判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"更新失败！");
    }

    //计划项删除
    public void deleteCusDevPlan(Integer id) {
        AssertUtil.isTrue(null == id,"待删除记录不存在!");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"删除失败！");

    }

    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //参数判断
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(null == sId||cusDevPlanMapper.selectByPrimaryKey(sId)==null,"数据异常，请重试!");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划项内容不能为空!");
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(),"计划项时间不能为空!");
    }


}
