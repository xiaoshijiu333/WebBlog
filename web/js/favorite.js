//加载头部文件
$("#header").load("Html/personal_head.html");

//加载分页数据方法
function GetPageList(currentPage) {
    //获取分页的PageBean
    $.ajax({
        url:"user_AllFavorite.action",
        type:"post",
        dataType:"json",
        data:{"currentPage":currentPage},
        success:function (data) {
            //利用JsTemplate模板取数据
            var html=template("mytemp1",{list:data.pageList});
            if(html==""){
                $("#favorite").html("<p style='margin-left: 50px;margin-top: 20px'>你还没有关注任何人！！</p>");
            }else {
                $("#favorite").html(html);
                //一共几人的显示
                $("#number").html("共"+data.totalCount+"人");
            }
            //有两页才显示分页
            if(data.totalPage>1){
                //分页
                $(".page_div").paging({
                    pageNo:data.currentPage,
                    totalPage: data.totalPage,
                    totalSize: data.totalCount,
                    callback: function(num) {
                        //回调方法
                        GetPageList(num);
                    }
                });
            }

        }
    })
}

$(function () {

    //个人中心显示
    $.ajax({
        type:"post",
        url:"blog_HaveSession.action",
        dataType:"json",
        success:function (data) {
            $(".a1").html("个人中心("+data.uname+")");
            $("#aperson").attr("href","person.html?uid="+data.uid);
        }
    })

    //加载第一页数据
    GetPageList(1);

    //取消关注
    $("#favorite").on("click",".unfollow",function () {
        var uid=$(this).attr("id");
        $.ajax({
            url:"user_notfavorite.action",
            data:{"uid":uid},
            type:"post",
            success:function () {
                alert("取消关注成功！");
                //重新加载页面
                window.location.reload();
            }
        })
        return false;
    })
})