package Blog.web.Filter;

import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//重新定义一个过滤器，对富文本编辑器生效
public class MyFilter extends StrutsPrepareAndExecuteFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        //获取当前请求
        HttpServletRequest request=(HttpServletRequest) req;
        //获取请求地址
        String requestURI = request.getRequestURI();
        //做判断
        if(requestURI.contains("umedit/jsp/controller.jsp")){
            //放行
            chain.doFilter(req,res);
        }else {
            //调用父类过滤器
            super.doFilter(req, res, chain);
        }
    }
}
