//拿到地址栏的artical_id的值
function getParams(key) {
    var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
};
var artical_id = getParams("artical_id");

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

    //发送ajax请求获取数据
    $.ajax({
        url:"artical_getArticalById.action",
        type:"post",
        dataType:"json",
        data:{"artical_id":artical_id},
        success:function (artical) {

            //显示数据
            $(".atitle b").html(artical.artical_title);
            $(".atime").html(artical.artical_time);
            $(".auname a").html(artical.user.uname);
            $(".auname a").attr("href","person.html?uid="+artical.user.uid);
            $("#aperson").attr("href","person.html?uid="+artical.user.uid);
            $(".context").html(artical.artical_context);
            $("#articalid").val(artical.artical_id);

            $.ajax({
                url:"artical_GetCataById.action",
                type:"post",
                dataType:"json",
                data:{"parentid":artical.catagory.parentid},
                success:function (catagory) {
                    $(".acatagory").html(catagory.cname+"——"+artical.catagory.cname);
                }
            })
            
            //左边部分取数据
            if (artical.user.upicture==null){
                $("#aimage").attr("src","images/timg.jpg");
            } else{
                $("#aimage").attr("src","upload/"+artical.user.upicture+"");
            }
            $("#aname").append(artical.user.uname);
            $("#asex").append(artical.user.usex);
            $("#aarea").append(artical.user.uarea);
            $("#ajob").append(artical.user.ujob);
            $("#aid").val(artical.user.uid);

            //获取个人文章总数
            $.ajax({
                url:"artical_getArticalCount.action",
                type:"post",
                dataType:"text",
                data:{"uid":artical.user.uid},
                success:function (count) {
                    $("#artical_count").html(count);
                }
            })

            //用户粉丝数
            $.ajax({
                url: "user_fansNum2.action",
                dataType: "text",
                data:{"uid":artical.user.uid},
                type: "post",
                success: function (data) {
                    $("#fans").html(data);
                }
            })

            //是否显示关注的判断
            //显示收藏和已收藏的判断
            if ($(".a0").is(":visible")){
                $(".like").html("关注");
                $("#img").attr("src","images/like.png");
            }else if($(".aa").html().indexOf(artical.user.uname)>=0){
                $(".like").fadeOut();
                $("#likeartical").html("");
            }else{
                $.ajax({
                    url:"user_IsnotFans.action",
                    data:{"uid":artical.user.uid},
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
                $.ajax({
                    url:"artical_IsLikeArtical.action",
                    data:{"artical_id":artical.artical_id},
                    type:"post",
                    dataType:"text",
                    success:function (flag) {
                        if (flag==1){
                            $("#img").attr("src","images/like2.png");
                        } else{
                            $("#img").attr("src","images/like.png");
                        }
                    }
                })
            }
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
            var uid=$("#aid").val();
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

    //收藏文章或者取消收藏
    $("#likeartical").click(function () {
        //没有登录提醒
        if ($(".a0").is(":visible")){
            alert("你还没有登录，点击确定自动跳转登录页！");
            window.location.href="Html/login.html";
        }else {
            var imgsrc=$("#img").attr("src");
            var articalid = $("#articalid").val();
            //收藏
            if(imgsrc=="images/like.png"){
                $.ajax({
                    url: "artical_likeArtical.action",
                    data: {"artical_id": articalid},
                    type: "post",
                    success: function () {
                        alert("收藏成功！！");
                        window.location.reload();
                    }
                })
            }else {
                $.ajax({
                    url: "artical_notlikeArtical.action",
                    data: {"artical_id": articalid},
                    type: "post",
                    success: function () {
                        alert("取消收藏成功！！");
                        window.location.reload();
                    }
                })
            }
        }
        return false;
    })

})