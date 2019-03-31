package Blog.Dao;

import Blog.Model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;


public class LoginDaoImpl extends HibernateDaoSupport implements LoginDao {


    @Override
    public User loginDaoByuname(String uname, String password)  {

        //使用QBC离线条件查询
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("uname",uname));
        detachedCriteria.add(Restrictions.eq("password",password));
        List<User> list = (List<User>)this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public User loginDaoByuemail(String uemail, String password) {
        //使用QBC离线条件查询
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("uemail",uemail));
        detachedCriteria.add(Restrictions.eq("password",password));
        List<User> list = (List<User>)this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void registDao(User user) {
        this.getHibernateTemplate().save(user);
    }
}
