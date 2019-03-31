package Blog.Dao;

import Blog.Model.User;

public interface LoginDao {
    User loginDaoByuname(String uname,String password);
    User loginDaoByuemail(String uemail,String password);

    void registDao(User user);
}
