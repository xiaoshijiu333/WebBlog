//拿到地址栏的uid的值
function getParams(key) {
    var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
};

//加载分页数据方法
function GetPageList(currentPage,uid) {
    //获取分页的PageBean
    $.ajax({
        url:"artical_PageList.action",
        type:"post",
        dataType:"json",
        data:{"currentPage":currentPage,"uid":uid},
        success:function (data) {
            //利用JsTemplate模板取数据
            var html=template("mytemp1",{list:data.pageList});

            if(html==""){
                $("#artical").html("<p style='margin-left: 50px;margin-top: 20px'>还没有写过文章！！</p>");
            }else {
                $("#artical").html(html);
            }

            //只在第一页时加载判断
            if(data.currentPage==1){
                //个人文章总数
                $("#artical_count").html(data.totalCount);
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
                        GetPageList(num,uid);
                    }
                });
            }
        }
    })
}

$(function () {

    $.ajax({
        type: "post",
        url: "blog_HaveSession.action",
        dataType: "json",
        success: function (data) {
            if (data != null) {
                $(".aa").html("个人中心(" + data.uname + ")");
                $(".aa").fadeIn();
            } else {
                $(".a0").fadeIn();
                $(".a1").fadeIn();
            }
        }
    })

    var uid = getParams("uid");
    //获取第一页数据
    GetPageList(1,uid);

    $("#aperson").attr("href","person.html?uid="+uid);

    //加载个人数据（避免没有文章，显示不出个人数据的问题）
    $.ajax({
        url: "user_GetUser.action",
        dataType: "json",
        type:"post",
        data:{"uid":uid},
        success: function (person) {
            //左边部分取数据
            if (person.upicture==null){
                $("#aimage").attr("src","images/timg.jpg");
            } else{
                $("#aimage").attr("src","upload/"+person.upicture);
            }
            $("#aname").html(person.uname);
            $("#asex").html("性别:&nbsp&nbsp&nbsp"+person.usex);
            $("#aarea").html("地区:&nbsp&nbsp&nbsp"+person.uarea);
            $("#ajob").html("工作:&nbsp&nbsp&nbsp"+person.ujob);

            //是否显示关注的判断
            if ($(".a0").is(":visible")){
                $(".like").html("关注");
            }else if($(".aa").html().indexOf(person.uname)>=0){
                $(".like").fadeOut();
            }else{
                $.ajax({
                    url:"user_IsnotFans.action",
                    data:{"uid":uid},
                    type:"post",
                    dataType:"text",
                    success:function (flag) {
                        if (flag==1){
                            $(".like").html("取消关注");
                        } else{
                            $(".like").html("关注");
                        }
                    }
                })
            }
        }
    })

    //用户粉丝数
    $.ajax({
        url: "user_fansNum2.action",
        dataType: "text",
        data:{"uid":uid},
        type: "post",
        success: function (data) {
            $("#fans").html(data);
        }
    })

    //关注和取消关注
    $(".like").click(function () {

        //没有登录提醒
        if ($(".a0").is(":visible")){
            alert("你还没有登录，点击确定自动跳转登录页！");
            window.location.href="Html/login.html";
        }else{
            var fan=$(".like").html();
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
        }
        return false;
    })
})