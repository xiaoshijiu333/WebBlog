//加载头部文件
$("#header").load("Html/personal_head.html");

//加载分页数据方法
function GetPageList(currentPage) {
    //获取分页的PageBean
    $.ajax({
        url:"user_AllFans.action",
        type:"post",
        dataType:"json",
        data:{"currentPage":currentPage},
        success:function (data) {
            //利用JsTemplate模板取数据
            var html=template("mytemp1",{list:data.pageList});
            if(html==""){
                $("#fans").html("<p style='margin-left: 50px;margin-top: 20px'>你还没有任何粉丝！！</p>");
            }else {
                $("#fans").html(html);
                //一共几人的显示
                $("#number").html("共"+data.totalCount+"人");
            }

            //是否显示关注的判断
            $(data.pageList).each(function (i,obj) {
                $.ajax({
                    url:"user_IsnotFans.action",
                    data:{"uid":obj.user.uid},
                    type:"post",
                    async:false,  //设置为同步请求，不然多次的ajax接受的结果会混乱
                    dataType:"text",
                    success:function (flag) {
                        if (flag==1){
                            $("#"+obj.user.uid+"").html("取消关注");
                        } else{
                            $("#"+obj.user.uid+"").html("关注");
                        }
                    }
                })
            })

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

    //关注或取消关注
    $("#fans").on("click",".unfollow",function () {
        var fan=$(this).html();
        var uid=$(this).attr("id");
        //关注逻辑
        if (fan=="关注"){
            $.ajax({
                url:"user_favoritePeople.action",
                data:{"uid":uid},
                type:"post",
                success:function () {
                    alert("关注成功！");
                    //重新加载页面
                    window.location.reload();
                }
            })
        }
        //取消关注
        else{
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
        }
        return false;
    })
})