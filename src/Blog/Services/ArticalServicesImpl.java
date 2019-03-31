package Blog.Services;

import Blog.Dao.ArticalDao;
import Blog.Dao.UserDao;
import Blog.Model.Artical;
import Blog.Model.Catagory;
import Blog.Model.PageBean;
import Blog.Model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class ArticalServicesImpl implements ArticalServices{

    @Resource(name = "pageBean")
    private PageBean<Artical> pageBean;

    @Resource(name = "articalDao")
    private ArticalDao articalDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public List<Catagory> FindAllCatagory(Integer parentid) {
        return articalDao.FindAllCatagory(parentid);
    }

    @Override
    public void SaveArtical(Artical artical) {
        articalDao.SaveArtical(artical);
    }

    @Override
    public PageBean<Artical> GetPageList(Integer currentPage, Integer uid) {
        //设置当前页(已经设置了如果是null，就是1)
        pageBean.setCurrentPage(currentPage);
        //一页显示几条数据
        pageBean.setPageSize(7);
        //从数据库查询的角标
        Integer index = pageBean.getIndex();
        //总记录数
        Integer count = articalDao.GetArtCountByUname(uid);
        pageBean.setTotalCount(count);
        //设置总页数
        pageBean.setTotalPage(pageBean.getTotalPage());
        //当前页数据
        List<Artical> articals = articalDao.GetArticalByUname(uid, index, pageBean.getPageSize());
        pageBean.setPageList(articals);
        return pageBean;
    }

    @Override
    public void deleteArtical(Artical artical) {
        articalDao.deleteArtical(artical);
    }

    @Override
    public Artical getArticalById(Integer artical_id) {
        return articalDao.getArticalById(artical_id);
    }

    @Override
    public void updateArtical(Artical artical) {
        articalDao.updateArtical(artical);
    }

    @Override
    public Catagory GetCataById(Integer parentid) {
        return articalDao.GetCataById(parentid);
    }

    @Override
    public PageBean GetAllPageList(Integer currentPage, DetachedCriteria detachedCriteria) {
        //设置当前页(已经设置了如果是null，就是1)
        pageBean.setCurrentPage(currentPage);
        //一页显示几条数据
        pageBean.setPageSize(7);
        //从数据库查询的角标
        Integer index = pageBean.getIndex();
        //总记录数
        Integer count = articalDao.GetAllCount(detachedCriteria);
        pageBean.setTotalCount(count);
        //设置总页数
        pageBean.setTotalPage(pageBean.getTotalPage());
        //当前页数据
        List<Artical> articals = articalDao.GetAllArtical(index, pageBean.getPageSize(),detachedCriteria);
        pageBean.setPageList(articals);
        return pageBean;
    }

    //查询个人文章总数
    @Override
    public Integer getArticalCount(Integer uid) {
        return articalDao.GetArtCountByUname(uid);
    }

    //收藏的文章分页展示
    @Override
    public PageBean PageBeanLike(Integer currentPage, Integer uid) {
        User userMessage = userDao.GetUserMessage(uid);
        //set集合转list
        List<Artical> articalList=new ArrayList<>(userMessage.getArticallike());
        //设置当前页(已经设置了如果是null，就是1)
        pageBean.setCurrentPage(currentPage);
        //一页显示几条数据
        pageBean.setPageSize(7);
        //从数据库查询的角标
        Integer index = pageBean.getIndex();
        //总记录数
        pageBean.setTotalCount(articalList.size());
        //设置总页数
        pageBean.setTotalPage(pageBean.getTotalPage());
        //当前页数据
        //如果当前页就是总页数，避免截取的时候报错(没有记录，页数为0)
        if (pageBean.getTotalPage()==0){
            pageBean.setPageList(null);
        }else if (currentPage==pageBean.getTotalPage()){
            pageBean.setPageList(articalList.subList(index,articalList.size()));
        }else{
            pageBean.setPageList(articalList.subList(index,index+7));
        }
        return pageBean;
    }
}
