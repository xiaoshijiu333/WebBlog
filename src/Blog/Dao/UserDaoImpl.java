package Blog.Dao;

import Blog.Model.Favorite;
import Blog.Model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {
    @Override
    public User GetUserMessage(Integer uid) {

        //使用QBC离线条件查询
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("uid", uid));
        List<User> list = (List<User>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void UpdateUser(User up_user) {
        this.getHibernateTemplate().update(up_user);
    }

    @Override
    public User OneUname(String uname) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("uname", uname));
        List<User> list = (List<User>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public User OneEmail(String uemail) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("uemail", uemail));
        List<User> list = (List<User>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    //查询关注数和粉丝数
    @Override
    public List<Favorite> GetfavoriteNum(Integer uid) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Favorite.class);
        detachedCriteria.add(Restrictions.eq("user.uid", uid));
        List<Favorite> list = (List<Favorite>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }

    @Override
    public List<Favorite> GetfansNum(Integer uid) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Favorite.class);
        detachedCriteria.add(Restrictions.eq("favoriteuser.uid", uid));
        List<Favorite> list = (List<Favorite>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }

    //关注别人
    @Override
    public void favoritePeople(Favorite favorite) {
        this.getHibernateTemplate().save(favorite);
    }

    //是否关注了
    @Override
    public List<Favorite> IsnotFans(Integer uid, Integer uid1) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Favorite.class);
        detachedCriteria.add(Restrictions.eq("user.uid", uid));
        detachedCriteria.add(Restrictions.eq("favoriteuser.uid", uid1));
        List<Favorite> list = (List<Favorite>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }

    //取消关注，删除记录
    @Override
    public void deleteFavorite(Favorite favorite) {

        this.getHibernateTemplate().delete(favorite);
    }

    //查询指定用户所有关注
    @Override
    public List<Favorite> AllFavorite(Integer uid, Integer index, Integer pageSize) {
        DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Favorite.class);
        //根据id降序，实现从后往前查询
        detachedCriteria.addOrder(Order.desc("fid"));
        detachedCriteria.add(Restrictions.eq("user.uid",uid));
        List<Favorite> list = (List<Favorite>)this.getHibernateTemplate().findByCriteria(detachedCriteria, index, pageSize);
        return list;
    }

    //查询指定用户所有粉丝
    @Override
    public List<Favorite> AllFans(Integer uid, Integer index, Integer pageSize) {
        DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Favorite.class);
        //根据id降序，实现从后往前查询
        detachedCriteria.addOrder(Order.desc("fid"));
        detachedCriteria.add(Restrictions.eq("favoriteuser.uid",uid));
        List<Favorite> list = (List<Favorite>)this.getHibernateTemplate().findByCriteria(detachedCriteria, index, pageSize);
        return list;
    }

}
