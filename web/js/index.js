//拿到地址栏的parentid的值
function getParams(key) {
    var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
};
var parentid = getParams("parentid");

$(function () {
    $.ajax({
        type:"post",
        url:"blog_HaveSession.action",
        dataType:"json",
        success:function (data) {
            if(data!=null){
                $(".aa").html("个人中心("+data.uname+")");
                $(".aa").fadeIn();
            }else{
                $(".a0").fadeIn();
                $(".a1").fadeIn();
            }
        }
    })

    //获取分类
    $.ajax({
        type:"post",
        url:"artical_AllCatagory.action",
        dataType:"json",
        success:function (data) {
            //遍历data数组(json数组)
            $(data).each(function (i,obj){
                //加循环判断就是因为让点击的那个li具有class属性
                if(obj.cid==parentid){
                    $("#ul_catagory").append("<a href='index.html?parentid="+obj.cid+"'>" +
                        "<li class='index'>"+obj.cname+"</li></a>");
                }else{
                    $("#ul_catagory").append("<a href='index.html?parentid="+obj.cid+"'>" +
                        "<li>"+obj.cname+"</li></a>");
                }
            })
        }
    })

    //获取子分类
    if(parentid!=null){
        $.ajax({
            type:"post",
            data:{"parentid":parentid},
            url:"artical_AllCatagory.action",
            dataType:"json",
            success:function (data) {
                //遍历data数组(json数组)
                $(data).each(function (i,obj){
                    $("#childCata").append("<a href='' id='"+obj.cid+"'>" + "<li>"+obj.cname+"</li></a>");
                })
            }
        })
        $(".index").attr("class","null");
    }

    //加载第一页的数据
    GetPageList(1,parentid);

})

//加载分页数据方法
function GetPageList(currentPage,parentid) {
    //获取分页的PageBean
    $.ajax({
        url:"artical_AllPageList.action",
        type:"post",
        dataType:"json",
        data:{"currentPage":currentPage,"parentid":parentid},
        success:function (articals) {
            //利用JsTemplate模板取数据
            var html=template("mytemp2",{list:articals.pageList});
            $(".centext").html(html);

            //分页
            $(".page_div").paging({
                pageNo:articals.currentPage,
                totalPage: articals.totalPage,
                totalSize: articals.totalCount,
                callback: function(num) {
                    //回调方法
                    GetPageList(num,parentid);
                }
            });
        }
    })
}

//关键词搜索文章
function KeySearch(currentPage) {
    //拿到文本框的值
    var value=$("#search_artical").val();
    if (value==""){
        alert("请输入关键字在搜索！")
    } else{
        $.ajax({
            url:"artical_AllPageList.action",
            type:"post",
            dataType:"json",
            data:{"currentPage":currentPage,"value":value},
            success:function (articals) {
                //利用JsTemplate模板取数据
                var html=template("mytemp2",{list:articals.pageList});
                $(".centext").html(html);

                //分页
                $(".page_div").paging({
                    pageNo:articals.currentPage,
                    totalPage: articals.totalPage,
                    totalSize: articals.totalCount,
                    callback: function(num) {
                        KeySearch(num);
                    }
                });
            }
        })
    }
    return false;
}

//加载二级子类文章列表
function ChildArtical(currentPage,cid) {
    $.ajax({
        url:"artical_AllPageList.action",
        type:"post",
        dataType:"json",
        data:{"currentPage":currentPage,"cid":cid},
        success:function (articals) {
            //利用JsTemplate模板取数据
            var html=template("mytemp2",{list:articals.pageList});
            $(".centext").html(html);

            //分页
            $(".page_div").paging({
                pageNo:articals.currentPage,
                totalPage: articals.totalPage,
                totalSize: articals.totalCount,
                callback: function(num) {
                    ChildArtical(num,cid);
                }
            });
        }
    })
    return false;
}

//代理注册点击事件
$("#childCata").on("click","a",function () {
    //获取属性id值，也就是cid的值
    var cid=$(this).attr("id");
    ChildArtical(1,cid);
    return false;
})