//加载主分类
$(function () {
    $.ajax({
        url:"artical_AllCatagory.action",
        type:"post",
        dataType:"json",
        success:function (data) {
            $(data).each(function (i,obj) {
                $("#Catagory").append("<option value="+obj.cid+">"+obj.cname+"</option>");
            })
            //立刻触发change事件，一进来就有数据
            $("#Catagory").trigger("change");
        }
    })
})

//加载子分类
$("#Catagory").change(function () {
    var parentid=$("#Catagory").val();
    $.ajax({
        url:"artical_AllCatagory.action",
        type:"post",
        dataType:"json",
        data:{"parentid":parentid},
        success:function (data) {
            //清空之前的option
            $("#ChildCata").empty();
            $(data).each(function (i,obj) {
                $("#ChildCata").append("<option value="+obj.cid+">"+obj.cname+"</option>");
            })
        }
    })
})


//初始化富文本编程器
var ue = UE.getEditor("editor",{scaleEnabled:true});

//提交表单，上传文章
$("#send_form").click(function () {

    //生成文章摘要
    //拿到富文本正文，截取前20个字符，赋值给摘要描述
    var text=ue.getContentTxt();
    var text2=text.substring(0,100)+"...";
    $("#artical_desc").val(text2);
    //提交表单
    if($("#artical_title").val()==""){
        alert("文章标题不能为空！！");
        return;
    }else if(text==""){
        alert("文章内容不能为空！！");
        return;
    } else {
        $("#form_artical").submit();
    }
})