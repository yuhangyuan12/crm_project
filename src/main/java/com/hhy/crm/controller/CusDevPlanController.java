package com.hhy.crm.controller;


import com.hhy.crm.base.BaseController;
import com.hhy.crm.base.ResultInfo;
import com.hhy.crm.enums.StateStatus;
import com.hhy.crm.query.CusDevPlanQuery;
import com.hhy.crm.query.SaleChanceQuery;
import com.hhy.crm.service.CusDevPlanService;
import com.hhy.crm.service.SaleChanceService;
import com.hhy.crm.utils.LoginUserUtil;
import com.hhy.crm.vo.CusDevPlan;
import com.hhy.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;


    //进入客户开发计划页面
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }


    //打开计划项开发页面
    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    //开发计划分页多条件查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }


    //开发项数据添加
    @ResponseBody
    @RequestMapping("add")
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功!");
    }

    //进入添加或修改页面
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer sid,HttpServletRequest request,Integer id){
        //将id设置到作用域中
        request.setAttribute("sid",sid);

        //通过计划项id查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }

    //开发项数据添加
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updataCusDevPlan(cusDevPlan);
        return success("计划项更新成功!");
    }

    //开发项数据删除
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项删除成功!");
    }
}
