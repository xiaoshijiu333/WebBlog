//如果是从注册来的，用户名写给浏览器
$(function () {
    $.ajax({
        type:"post",
        url:"blog_HaveSession.action",
        dataType:"json",
        success:function (data) {
            if (data!=null){
                $("#uname").val(data.uname);
            }
        }
    })
})

//切换用户名或者邮箱登录
$("#email_login").click(function () {
    $("#uname_email").html("邮箱");
    $("#uname").attr("placeholder","请输入邮箱");
    $("#uname").attr("type","email");
    $("#uname").attr("name","uemail");
    return false;
})
$("#uname_login").click(function () {
    $("#uname_email").html("用户名");
    $("#uname").attr("placeholder","请输入用户名");
    $("#uname").attr("type","text");
    $("#uname").attr("name","uname");
    return false;
})

//登录提交ajax请求
$("#Button").click(function () {
    if($("#uname").val()==""){
        alert("用户名/邮箱为空");
    }
    else if($("#password").val()==""){
        alert("密码为空");
    }
    else{
        $("#errMessage").html("");
        if($("#uname_email").html()=="用户名"){
            $.ajax({
                url:"blog_unamelogin.action",
                data:$("#user_form").serialize(),
                type:"POST",
                dataType:"text",
                success:function (data) {
                    if(data.toString()=="err"){
                        $("#errMessage").html("用户名或密码错误");
                    }
                    else {
                        window.location.href="../index.html";
                    }
                }
            })
        }
        else {
            $.ajax({
                url:"blog_emaillogin.action",
                data:$("#user_form").serialize(),
                type:"POST",
                dataType:"text",
                success:function (data) {
                    if(data.toString()=="err"){
                        $("#errMessage").html("邮箱或密码错误");
                    }
                    else {
                        window.location.href="../index.html";
                    }
                }
            })
        }
    }
})