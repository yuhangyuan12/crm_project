layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //计划项数据展示
    var  tableIns = table.render({
        elem: '#cusDevPlanList',
        url : ctx+'/cus_dev_plan/list?saleChanceId='+$("input[name='id']").val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    });


    //头工具栏事件
    table.on('toolbar(cusDevPlans)', function(obj){
        switch(obj.event){
            case "add":
                openAddOrUpdateCusDevPlanDialog();
                break;
            case "success":
                updateSaleChanceDevResult(2);
                break;
            case "failed":
                updateSaleChanceDevResult(3);
                break;
        };
    });



    /**
     * 行监听
     */
    table.on("tool(cusDevPlans)", function(obj){
        var layEvent = obj.event;
        if(layEvent === "edit") {
            openAddOrUpdateCusDevPlanDialog(obj.data.id);
        }else if(layEvent === "del") {
            layer.confirm('确定删除当前数据？', {icon: 3, title: "开发计划管理"}, function (index) {
                $.post(ctx+"/cus_dev_plan/delete",{id:obj.data.id},function (data) {
                    if(data.code==200){
                        layer.msg("操作成功！");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg, {icon: 5});
                    }
                });
            })
        }

    });


    // 打开添加计划项数据页面
    function openAddOrUpdateCusDevPlanDialog(id){
        var url  =  ctx+"/cus_dev_plan/addOrUpdateCusDevPlanPage?sid="+$("input[name='id']").val();
        var title="计划项管理-添加计划项";
        if(id){
            url = url+"&id="+id;
            title="计划项管理-更新计划项";
        }
        layui.layer.open({
            title : title,
            type : 2,
            area:["700px","400px"],
            maxmin:true,
            content : url
        });
    }


    function updateSaleChanceDevResult(devResult) {
        layer.confirm('确定执行当前操作？', {icon: 3, title: "营销机会管理"}, function (index) {
            var sid = $("input[name='id']").val()
            $.post(ctx+"/sale_chance/updateSaleChanceDevResult",
                {
                    id:sid,
                    devResult:devResult
                },function (data) {
                if(data.code==200){
                    layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }else{
                    layer.msg(data.msg, {icon: 5});
                }
            });
        })
    }
});
