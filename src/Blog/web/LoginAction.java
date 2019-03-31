package Blog.web;

import Blog.Model.User;
import Blog.Services.LoginServices;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lombok.Setter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import java.io.IOException;

public class LoginAction extends ActionSupport implements ModelDriven<User> {

    @Resource(name = "user")
    private User user;

    @Resource(name = "loginservices")
    private LoginServices loginServices;

    //接受验证码参数
    @Setter
    private String CheckCode;

    @Override
    public User getModel() {
        return user;
    }

    public void unamelogin() throws IOException {
        User user = loginServices.loginServicesByuname(this.user);
        if (user == null) {
            //因为是Ajax请求，所以要设置响应内容
            ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
            ServletActionContext.getResponse().getWriter().print("err");
        } else {
            //保存到Session域中，用Ajax进行跳转
            ActionContext.getContext().getSession().put("user", user);
        }
    }

    public void emaillogin() throws IOException {
        User user = loginServices.loginServicesByuemail(this.user);
        if (user == null) {
            //因为是Ajax请求，所以要设置响应内容
            ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
            ServletActionContext.getResponse().getWriter().print("err");
        } else {
            //保存到Session域中，用Ajax进行跳转
            ActionContext.getContext().getSession().put("user", user);
        }
    }

    //判断验证码
    public String CheckCode() throws IOException {
        //拿到域中的验证码
        String checkcodeSession = (String) ActionContext.getContext().getSession()
                .get("checkcode_session");

        if (!checkcodeSession.equals(CheckCode) && !CheckCode.equals("")) {
            ServletActionContext.getResponse().getWriter().print("CheckErr");
        }
        return null;
    }

    //注册
    public String regist() {
        loginServices.registServices(user);
        ActionContext.getContext().getSession().put("user", user);
        return SUCCESS;
    }

    //首页显示的确定
    public String HaveSession() throws IOException {
        User user = (User) ActionContext.getContext().getSession().get("user");

        //返回Json数据
        //转换成json格式
        JsonConfig jsonConfig = new JsonConfig();
        //设置Hibernate懒加载转换Json时出错，以及过滤user里面的artical
        jsonConfig.setExcludes(new String[]{"handler", "hibernateLazyInitializer", "articallike", "password"});
        JSONObject jsonObject = JSONObject.fromObject(user, jsonConfig);
        //写给浏览器
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().print(jsonObject.toString());
        return null;
    }

    //退出登录，清除用户session中的user
    public String ExitLogin() {
        ActionContext.getContext().getSession().remove("user");
        return "exit";
    }
}
