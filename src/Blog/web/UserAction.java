package Blog.web;

import Blog.Model.Favorite;
import Blog.Model.PageBean;
import Blog.Model.User;
import Blog.Services.UserServices;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lombok.Setter;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UserAction extends ActionSupport implements ModelDriven<User> {

    @Resource(name = "userservices")
    private UserServices userServices;

    @Resource(name = "user")
    private User up_user;

    @Resource(name = "favorite")
    private Favorite favorite;

    @Override
    public User getModel() {
        return up_user;
    }

    //跳转个人主页
    public String personal(){
        return "person";
    }

    //跳转个人关注
    public String favorite(){
        return "favorite";
    }

    //跳转个人粉丝
    public String fans(){
        return "fans";
    }

    //根据id获取个人信息
    public String GetUser() throws IOException {
        User user = userServices.GetUserMessage(up_user.getUid());

        JsonConfig jsonConfig = new JsonConfig();
        //过滤user中的artical
        jsonConfig.setExcludes(new String[]{"articallike","password"});
        //返回Json数据
        //转换成json格式
        JSONObject jsonObject = JSONObject.fromObject(user,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }

    //获取更新后的个人信息
    public String getUser(){
        User user = (User) ActionContext.getContext().getSession().get("user");
        Integer uid = user.getUid();
        User user1 = userServices.GetUserMessage(uid);
        //重新定义session中的user，更新数据用的
        ActionContext.getContext().getSession().put("user",user1);
        return SUCCESS;
    }


    //ajax请求，获取个人信息，返回json数据
    public String getUserJson() throws IOException {

        User user =(User) ActionContext.getContext().getSession().get("user");

        JsonConfig jsonConfig = new JsonConfig();
        //过滤user中的artical
        jsonConfig.setExcludes(new String[]{"articallike","password"});
        //返回Json数据
        //转换成json格式
        JSONObject jsonObject = JSONObject.fromObject(user,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());

        return null;
    }

    //根据id更新用户数据
    public String UpdateUser(){
        User user =(User) ActionContext.getContext().getSession().get("user");
        Integer uid = user.getUid();
        up_user.setUid(uid);
        //需要设置一下，不然会被置为空
        up_user.setUpicture(user.getUpicture());

        userServices.UpdateUser(up_user);
        return "update";
    }

    //判断用户名是否重复
    public String OneUname() throws IOException {
        User user =(User) ActionContext.getContext().getSession().get("user");

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");

        String uname = up_user.getUname();

        //区别登录和修改的业务逻辑
        if (user==null){
            String s = userServices.OneUname(uname);
            if(s!=null){
                response.getWriter().print("oneUname");
            }
        }else{
            if(!uname.equals(user.getUname())){
                String s = userServices.OneUname(uname);
                if(s!=null){
                    response.getWriter().print("oneUname");
                }
            }
        }
        return null;
    }

    //判断邮箱是否重复
    public String OneEmail() throws IOException {
        User user =(User) ActionContext.getContext().getSession().get("user");

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");

        String uemail = up_user.getUemail();

        if(user==null){
            String s = userServices.OneEmail(uemail);
            if(s!=null){
                response.getWriter().print("oneEmail");
            }
        }else {
            if(!uemail.equals(user.getUemail())){
                String s = userServices.OneEmail(uemail);
                if(s!=null){
                    response.getWriter().print("oneEmail");
                }
            }
        }
        return null;
    }

    //上传头像处理
    /*
     * 接受文件，利用Struts2提供的方法
     * 需要接受三个参数
     * uploadFileName 文件名称
     * upload 上传（临时）文件（本地上传文件到服务器，都会在c盘生成一个临时文件，然后需要自己写代码将文件从C盘copy过来）
     * uploadContentType 文件类型
     */
    @Setter
    private String uploadFileName; // 文件名称
    @Setter
    private File upload; // 上传文件，与form表单input发送的name名一样
    @Setter
    private String uploadContentType; // 文件类型

    public String UserPicture() throws IOException {
        User user =(User) ActionContext.getContext().getSession().get("user");

        //用户修改头像时，删除原来存在服务器的头像！节约内存！
        String upicture = user.getUpicture();
        if(upicture!=null){
            String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
            //拿到文件夹
            File file = new File(realPath);
            //遍历文件夹下所有文件
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                if (upicture.equals(listFile.getName())){
                    listFile.delete();
                }
            }
        }

        //处理上传的文件
        if(upload!=null){
            //获取文件扩展名
            int index = uploadFileName.lastIndexOf(".");
            String exName = uploadFileName.substring(index);
            //随机生成文件名
            String uuid = UUID.randomUUID().toString();
            //将生成的uuid中的"-"去掉，并拼接扩展名
            String fileName = uuid.replace("-", "") + exName;

            //新建服务器接受文件的目录
            String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
            //路径转文件
            File file = new File(realPath);
            //如果文件不存在，新建文件夹
            if(!file.exists()){
                file.mkdirs();
            }
            //拼接新文件路径
            File newFile = new File(realPath + "/" + fileName);
            //把临时文件copy过来
            FileUtil.copyFile(upload,newFile);

            user.setUpicture(fileName);

            //用户信息更新到数据库
            userServices.UpdateUser(user);

            //数据返回给前端
            ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
            ServletActionContext.getResponse().getWriter().print(fileName);
        }

        return null;
    }

    //关注数
    public String favoriteNum() throws IOException {

        User user =(User) ActionContext.getContext().getSession().get("user");
        Integer getfavoriteNum=userServices.GetfavoriteNum(user.getUid());

        //数据返回给前端
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(getfavoriteNum);

        return null;
    }

    //个人粉丝数
    public String fansNum() throws IOException {

        User user =(User) ActionContext.getContext().getSession().get("user");
        Integer getfansNum=userServices.GetfansNum(user.getUid());

        //数据返回给前端
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(getfansNum);

        return null;
    }
    //别人粉丝数
    public String fansNum2() throws IOException {

        Integer getfansNum=userServices.GetfansNum(up_user.getUid());

        //数据返回给前端
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(getfansNum);

        return null;
    }
    //关注别人
    public String favoritePeople(){
        User user =(User) ActionContext.getContext().getSession().get("user");
        //设置favorite参数
        favorite.setUser(user);
        favorite.setFavoriteuser(up_user);
        favorite.setFavoritetime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        userServices.favoritePeople(favorite);

        return null;
    }
    //取消关注，删除记录，但是必须要有fid
    public String notfavorite(){
        User user =(User) ActionContext.getContext().getSession().get("user");

        Favorite favorite=userServices.QueryFans(user.getUid(),up_user.getUid());

        userServices.deleteFavorite(favorite);

        return null;
    }

    //查询是否是粉丝
    public String IsnotFans() throws IOException {
        User user =(User) ActionContext.getContext().getSession().get("user");
        Integer flag=userServices.IsnotFans(user.getUid(),up_user.getUid());

        //数据返回给前端
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(flag);

        return null;
    }

    //分页用的页数
    @Setter
    private Integer currentPage;
    //查询所有关注
    public String AllFavorite() throws IOException {
        User user =(User) ActionContext.getContext().getSession().get("user");
        PageBean<Favorite> favoritePageBean=userServices.AllFavorite(currentPage,user.getUid());

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer","articallike","password"});
        JSONObject jsonObject = JSONObject.fromObject(favoritePageBean,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }

    //查询所有粉丝
    public String AllFans() throws IOException {
        User user =(User) ActionContext.getContext().getSession().get("user");
        PageBean<Favorite> favoritePageBean=userServices.AllFans(currentPage,user.getUid());

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer","articallike","password"});
        JSONObject jsonObject = JSONObject.fromObject(favoritePageBean,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }
}
