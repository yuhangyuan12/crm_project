layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    $("#closeBtn").click(function (){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })



    $.post(ctx+"/user/queryAllSales",function (result) {
        for (var i = 0; i < result.length; i++) {
            if($("input[name='man']").val() == result[i].id ){
                $("#assignMan").append("<option value=\"" + result[i].id + "\" selected='selected' >" + result[i].name + "</option>");
            }else {
                $("#assignMan").append("<option value=\"" + result[i].id + "\">" + result[i].name + "</option>");
            }
        }
        //重新渲染
        layui.form.render("select");
    });


    form.on("submit(addOrUpdateSaleChance)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        var url=ctx + "/sale_chance/add";
        if($("input[name='id']").val()){
            url=ctx + "/sale_chance/update";
        }
        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(
                    result.msg, {
                        icon: 5
                    }
                );
            }
        });
        return false;
    });
});