// 监听修改个人资料的点击事件，发送ajax请求以及显示数据
$(".update").click(function () {

    //发送ajax请求
    $.ajax({
        url: "user_getUserJson.action",
        type:"post",
        dataType: "json",
        success: function (data) {
            $("#uname").val(data.uname);
            $("#uemail").val(data.uemail);
            $("#usex").val(data.usex);
            $("#ujob").val(data.ujob);
            $("#udesc").val(data.udesc);
            $("#uarea").val(data.uarea);
        }
    })

    $("#update_context").fadeIn();
    $("#update_background").fadeIn();

    //阻止a标签的href请求发送
    return false;
})

//    退出按钮，退出修改窗口
$("#exit").click(function () {
    $("#update_context").fadeOut();
    $("#update_background").fadeOut();
    return false;
})

//监听输入框失去焦点就判断用户名是否重复
$("#uname").blur(function () {
    var uname = $("#uname").val();

    $.ajax({
        url: "user_OneUname.action",
        data: {uname: uname},
        datatype: "text",
        success: function (data) {
            if (data.toString() == "oneUname") {
                $("#OneUname").html("用户名重复！请重新输入");
                $("#update_button").attr("disabled", true);
            } else {
                $("#OneUname").html("");
                $("#update_button").attr("disabled", false);
            }
        }
    })
})

//监听输入框失去焦点就判断邮箱是否重复
$("#uemail").blur(function () {
    var uemail = $("#uemail").val();

    $.ajax({
        url: "user_OneEmail.action",
        data: {uemail: uemail},
        datatype: "text",
        success: function (data) {
            if (data.toString() == "oneEmail") {
                $("#OneEmail").html("该邮箱已经注册！请重新输入");
                $("#update_button").attr("disabled", true);
            } else {
                $("#OneEmail").html("");
                $("#update_button").attr("disabled", false);
            }
        }
    })
})

//进入页面就发送ajax请求，获取session中user数据
$(function () {

    //引入头部文件
    $("#header").load("Html/personal_head.html");

    $.ajax({
        url: "user_getUserJson.action",
        dataType: "json",
        type:"post",
        success: function (data) {

            $("#l1").html("昵称：&nbsp" + data.uname);
            $("#l2").html("邮箱：&nbsp" + data.uemail);
            $("#l3").html("性别：&nbsp" + data.usex);
            $("#l4").html("地区：&nbsp" + data.uarea);
            $("#l5").html("职位：&nbsp" + data.ujob);
            $("#l6").html("简介：&nbsp" + data.udesc);

            $(".a1").html("个人中心(" + data.uname + ")");
            $("#aperson").attr("href","person.html?uid="+data.uid);

            //头像判断
            if (data.upicture == "") {
                $("#imageview").attr("src", "images/timg.jpg");
            } else {
                $("#imageview").attr("src", "upload/" + data.upicture);
            }
        }
    })

    //关注数
    $.ajax({
        url: "user_favoriteNum.action",
        dataType: "text",
        type: "post",
        success: function (data) {
            $(".pp1").append(data);
        }
    })
    //粉丝数
    $.ajax({
        url: "user_fansNum.action",
        dataType: "text",
        type: "post",
        success: function (data) {
            $(".pp2").append(data);
        }
    })


})

//上传头像
$("#fileupload").change(function () {
    //注意这里不能写错。。。
    var file = $(this)[0].files[0];
    var formData = new FormData();
    formData.append("upload", file);
    //对文件类型进行判断
    var index = file.name.lastIndexOf(".");
    var type = file.name.substring(index);
    if (type != ".jpg" && type != ".png") {
        alert("只能上传jpg和png格式的图片！！");
        return;
    }
    //对图片大小验证
    if((file.size / 1024)>2048){
        alert("上传图片的最大尺寸为2M！！");
        return;
    }
    $.ajax({
        url: "user_UserPicture.action",
        data: formData,
        dataType: "text",
        type: "post",
        contentType: false,
        processData: false,
        success: function (data) {
            $("#imageview").attr("src", "upload/" + data);
        }
    })
});

