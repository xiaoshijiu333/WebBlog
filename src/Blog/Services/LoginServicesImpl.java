package Blog.Services;

import Blog.Dao.LoginDao;
import Blog.Model.User;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
public class LoginServicesImpl implements LoginServices {

    @Resource(name = "loginDao")
    private LoginDao loginDao;

    @Override
    public User loginServicesByuname(User user) {
        User user1 = loginDao.loginDaoByuname(user.getUname(), user.getPassword());
        return user1;
    }
    public User loginServicesByuemail(User user) {
        User user1 = loginDao.loginDaoByuemail(user.getUemail(), user.getPassword());
        return user1;
    }

    @Override
    public void registServices(User user) {
        loginDao.registDao(user);
    }
}
