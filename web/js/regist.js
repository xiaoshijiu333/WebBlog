//点击图片刷新验证码
$("#reg_img").click(function () {
    this.src="CheckCode.action?time="+new Date().getTime();
});

$("#reg_button").click(function () {
    if($("#uemail").val()==""){
        alert("邮箱不能为空");
    }
    else if($("#uname").val()==""){
        alert("用户名不能为空");
    }
    else if($("#password").val()==""){
        alert("密码不能为空");
    }
    else if($("#checkpassword").val()==""){
        alert("确定密码不能为空");
    }
    else if($("#checkcode").val()==""){
        alert("验证码不能为空");
    }
    else{
        $("#regedit").submit();
    }
})

//验证两个密码是否一样
$("#checkpassword").blur(function () {
    if($("#checkpassword").val()!=$("#password").val()){
        $(".p3").html("两次密码不一样！！");
        $("#reg_button").attr("disabled",true);
    }else {
        $(".p3").html("");
        $("#reg_button").attr("disabled",false);
    }
})

//验证邮箱是否唯一
$("#uemail").blur(function () {
    var uemail=$("#uemail").val();

    $.ajax({
        url:"user_OneEmail.action",
        data:{uemail:uemail},
        datatype:"text",
        success:function (data) {
            if(data.toString()=="oneEmail"){
                $(".p1").html("该邮箱已经注册！请重新输入");
                $("#reg_button").attr("disabled",true);
            }else {
                $(".p1").html("");
                $("#reg_button").attr("disabled",false);
            }
        }
    })
})

//验证用户名是否唯一
$("#uname").blur(function () {
    var uname=$("#uname").val();

    $.ajax({
        url:"user_OneUname.action",
        data:{uname:uname},
        datatype:"text",
        success:function (data) {
            if(data.toString()=="oneUname"){
                $(".p2").html("用户名已经注册！请重新输入");
                $("#reg_button").attr("disabled",true);
            }else {
                $(".p2").html("");
                $("#reg_button").attr("disabled",false);
            }
        }
    })
})

//判断验证码
$("#checkcode").blur(function () {
    var checkcode=$("#checkcode").val();

    $.ajax({
        url:"blog_CheckCode.action",
        data:{CheckCode:checkcode},
        dataType:"text",
        success:function (data) {
            if(data.toString()=="CheckErr"){
                $(".p5").html("验证码不匹配！！");
                $("#reg_button").attr("disabled",true);
            }else {
                $(".p5").html("");
                $("#reg_button").attr("disabled",false);
            }
        }
    })
})