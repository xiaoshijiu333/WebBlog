package Blog.web;

import Blog.Model.Artical;
import Blog.Model.Catagory;
import Blog.Model.PageBean;
import Blog.Model.User;
import Blog.Services.ArticalServices;
import Blog.Services.UserServices;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lombok.Setter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArticalAction extends ActionSupport implements ModelDriven<Artical> {

    @Resource(name = "articalservices")
    private ArticalServices articalServices;

    @Resource(name = "userservices")
    private UserServices userServices;

    @Resource(name = "artical")
    private Artical artical;

    @Override
    public Artical getModel() {
        return artical;
    }

    //转发到写博客界面
    public String WriteArtical(){
        return "write";
    }

    //转发到个人文章中心
    public String myartical(){
        return "myartical";
    }

    //转发到文章详情页
    public String detail(){
        return "detail";
    }

    //转发到个人收藏页
    public String like(){
        return "like";
    }

    //接受parentid参数
    @Setter
    private Integer parentid;
    //查询所有分类(子类)
    public String AllCatagory() throws IOException {
        List<Catagory> catagories = articalServices.FindAllCatagory(parentid);

        //转换成json格式
        JSONArray jsonArray = JSONArray.fromObject(catagories, new JsonConfig());

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonArray.toString());
        return null;
    }

    //添加文章
    public String AddArtical(){

        //获取当前时间
        artical.setArtical_time(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        //拿到session中的user，设置文章的用户
        User user = (User) ActionContext.getContext().getSession().get("user");
        artical.setUser(user);

        //保存到数据库
        articalServices.SaveArtical(artical);
        return "add";
    }

    //获取分页数据
    @Setter
    private Integer currentPage;
    public String PageList() throws IOException {
        //拿到session中的user，设置文章的用户名
        User user = (User) ActionContext.getContext().getSession().get("user");
        PageBean pageBean;
        if (uid==null){
            pageBean= articalServices.GetPageList(currentPage, user.getUid());
        }else {
            pageBean = articalServices.GetPageList(currentPage, uid);
        }

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错，以及过滤user里面的artical
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer","articallike","password"});
        JSONObject jsonObject = JSONObject.fromObject(pageBean,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }

    //关键词搜索的关键词
    @Setter
    private String value;
    //加载二级子类文章列表
    @Setter
    private Integer cid;
    //所有文章的分页
    public String AllPageList() throws IOException {

        //定义离线查询语句
        DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Artical.class);

        //查询分类数据
        if(parentid!=null){
            List<Catagory> catagories = articalServices.FindAllCatagory(parentid);
            Object[] objects = new Object[catagories.size()];
            for(int i=0;i<objects.length;i++){
                objects[i]=catagories.get(i).getCid();
            }
            //in语句查询
            detachedCriteria.add(Restrictions.in("catagory.cid",objects));
        }

        if(value!=null){
            System.out.println(value);
            //添加模糊查询条件
            detachedCriteria.add(Restrictions.like("artical_title","%"+value+"%"));
        }

        if(cid!=null){
            detachedCriteria.add(Restrictions.eq("catagory.cid",cid));
        }

        PageBean pageBean = articalServices.GetAllPageList(currentPage,detachedCriteria);

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer","articallike","password"});
        JSONObject jsonObject = JSONObject.fromObject(pageBean,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }

    //删除文章
    public String deleteArtical(){

        articalServices.deleteArtical(artical);

        return "delete";
    }

    //根据id获取文章
    public String getArticalById() throws IOException {

        Artical articalById = articalServices.getArticalById(artical.getArtical_id());

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer","articallike","password"});
        JSONObject jsonObject = JSONObject.fromObject(articalById,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());

        return null;
    }

    //修改文章
    public String updateArtical(){

        //获取当前时间
        artical.setArtical_time(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        //拿到session中的user，设置文章的用户名，不然更新会报错
        User user = (User) ActionContext.getContext().getSession().get("user");
        artical.setUser(user);

        articalServices.updateArtical(artical);

        return "update";
    }

    //根据id查询分类
    public String GetCataById() throws IOException {

        Catagory catagory = articalServices.GetCataById(parentid);

        //转换成json格式
        JSONObject jsonObject = JSONObject.fromObject(catagory,new JsonConfig());

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());

        return null;
    }

    //查询个人文章总数
    @Setter
    private Integer uid;
    public String getArticalCount() throws IOException {
        Integer count=articalServices.getArticalCount(uid);

        //数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(count.toString());

        return null;
    }

    //收藏文章
    public String likeArtical(){
        User user = (User) ActionContext.getContext().getSession().get("user");
        //给文章设置用户，级联保存更新
        Artical articalById = articalServices.getArticalById(artical.getArtical_id());
        articalById.getUserlike().add(user);
        //保存更新到数据库
        articalServices.updateArtical(articalById);
        return null;
    }

    //取消收藏
    public String notlikeArtical(){
        User user = (User) ActionContext.getContext().getSession().get("user");
        //给文章设置用户，级联保存更新
        Artical articalById = articalServices.getArticalById(artical.getArtical_id());
        for (User user1 : articalById.getUserlike()) {
            if (user1.getUid()==user.getUid()){
                articalById.getUserlike().remove(user1);
                break;
            }
        }
        //保存更新到数据库
        articalServices.updateArtical(articalById);
        return null;
    }

    //查询是否收藏了该文章
    public String IsLikeArtical() throws IOException {
        Integer flag=0;
        User user = (User) ActionContext.getContext().getSession().get("user");
        User userMessage = userServices.GetUserMessage(user.getUid());

        for (Artical artical1 : userMessage.getArticallike()) {
            if (artical1.getArtical_id()==artical.getArtical_id()){
                flag=1;
                break;
            }
        }
        //数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(flag.toString());
        return null;
    }

    //查询所有收藏的文章
    public String AllLikeArtical() throws IOException {
        User user = (User) ActionContext.getContext().getSession().get("user");

        //封装成PageBean
        PageBean pageBean=articalServices.PageBeanLike(currentPage,user.getUid());

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer","password","articallike"});
        JSONObject jsonObject = JSONObject.fromObject(pageBean,jsonConfig);

        //json数据写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }
}
