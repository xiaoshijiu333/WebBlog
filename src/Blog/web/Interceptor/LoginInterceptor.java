package Blog.web.Interceptor;

import Blog.Model.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LoginInterceptor extends MethodFilterInterceptor {
    @Override
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
        User user = (User)actionInvocation.getInvocationContext().getSession().get("user");
        if(user==null){
            //拦截
            return "login";
        }else {
            //放行
            return actionInvocation.invoke();
        }
    }
}
