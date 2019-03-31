package Blog.Services;

import Blog.Dao.UserDao;
import Blog.Model.Favorite;
import Blog.Model.PageBean;
import Blog.Model.User;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
public class UserServicesImpl implements UserServices {

    @Resource(name = "pageBean")
    private PageBean<Favorite> pageBean;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public User GetUserMessage(Integer uid) {
        User user = userDao.GetUserMessage(uid);
        return user;
    }

    @Override
    public void UpdateUser(User up_user) {
        userDao.UpdateUser(up_user);
    }

    @Override
    public String OneUname(String uname) {
        User user = userDao.OneUname(uname);
        if(user==null){
            return null;
        }else{
            return user.getUname();
        }
    }

    @Override
    public String OneEmail(String uemail) {
        User user = userDao.OneEmail(uemail);
        if(user==null){
            return null;
        }else{
            return user.getUemail();
        }
    }

    //关注数和粉丝个数
    @Override
    public Integer GetfavoriteNum(Integer uid) {
        return userDao.GetfavoriteNum(uid).size();
    }

    @Override
    public Integer GetfansNum(Integer uid) {
        return userDao.GetfansNum(uid).size();
    }

    @Override
    public void favoritePeople(Favorite favorite) {
        userDao.favoritePeople(favorite);
    }

    //是否关注了的判断
    @Override
    public Integer IsnotFans(Integer uid, Integer uid1) {
        List<Favorite> favorites=userDao.IsnotFans(uid,uid1);
        if(favorites.size()==0){
            return 0;
        }else{
            return 1;
        }
    }

    //取消关注，删除记录
    @Override
    public void deleteFavorite(Favorite favorite) {
        userDao.deleteFavorite(favorite);
    }

    //查询粉丝记录
    @Override
    public Favorite QueryFans(Integer uid, Integer uid1) {
        List<Favorite> favorites = userDao.IsnotFans(uid, uid1);
        return favorites.get(0);
    }

    //查询所有关注
    @Override
    public PageBean<Favorite> AllFavorite(Integer currentPage,Integer uid) {
        //设置当前页(已经设置了如果是null，就是1)
        pageBean.setCurrentPage(currentPage);
        //一页显示几条数据
        pageBean.setPageSize(8);
        //从数据库查询的角标
        Integer index = pageBean.getIndex();
        //总记录数
        List<Favorite> favorites = userDao.GetfavoriteNum(uid);
        pageBean.setTotalCount(favorites.size());
        //设置总页数
        pageBean.setTotalPage(pageBean.getTotalPage());
        //当前页数据
        List<Favorite> favorite = userDao.AllFavorite(uid, index, pageBean.getPageSize());
        pageBean.setPageList(favorite);
        return pageBean;
    }

    //查询所有粉丝
    @Override
    public PageBean<Favorite> AllFans(Integer currentPage, Integer uid) {
        //设置当前页(已经设置了如果是null，就是1)
        pageBean.setCurrentPage(currentPage);
        //一页显示几条数据
        pageBean.setPageSize(8);
        //从数据库查询的角标
        Integer index = pageBean.getIndex();
        //总记录数
        List<Favorite> favorites = userDao.GetfansNum(uid);
        pageBean.setTotalCount(favorites.size());
        //设置总页数
        pageBean.setTotalPage(pageBean.getTotalPage());
        //当前页数据
        List<Favorite> favorite = userDao.AllFans(uid, index, pageBean.getPageSize());
        pageBean.setPageList(favorite);
        return pageBean;
    }

}
