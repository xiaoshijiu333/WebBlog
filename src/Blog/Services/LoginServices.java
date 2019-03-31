package Blog.Services;

import Blog.Model.User;

public interface LoginServices {
    User loginServicesByuname(User user);
    User loginServicesByuemail(User user);
    void registServices(User user);
}
