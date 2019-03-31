//加载头部文件
$("#header").load("Html/personal_head.html");

//加载分页数据方法
function GetPageList(currentPage) {
    //获取分页的PageBean
    $.ajax({
        url:"artical_PageList.action",
        type:"post",
        dataType:"json",
        data:{"currentPage":currentPage},
        success:function (data) {
            //利用JsTemplate模板取数据
            var html=template("mytemp1",{list:data.pageList});

            if(html==""){
                $("#artical").html("<p style='margin-left: 50px;margin-top: 20px'>你还没有写过文章！！</p>");
            }else {
                $("#artical").html(html);
                $("#number").html("共"+data.totalCount+"篇");
            }


            //有一页才显示分页
            if(data.totalPage>0) {
                //分页
                $(".page_div").paging({
                    pageNo: data.currentPage,
                    totalPage: data.totalPage,
                    totalSize: data.totalCount,
                    callback: function (num) {
                        //回调方法
                        GetPageList(num);
                    }
                });
            }
        }
    })
}

//获取第一页数据
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

    GetPageList(1);
})