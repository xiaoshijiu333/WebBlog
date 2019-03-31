package Blog.Services;

import Blog.Model.Artical;
import Blog.Model.Catagory;
import Blog.Model.PageBean;
import Blog.Model.User;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public interface ArticalServices {
    List<Catagory> FindAllCatagory(Integer parentid);

    void SaveArtical(Artical artical);

    PageBean GetPageList(Integer currentPage, Integer uid);

    void deleteArtical(Artical artical);

    Artical getArticalById(Integer artical_id);

    void updateArtical(Artical artical);

    Catagory GetCataById(Integer parentid);

    PageBean GetAllPageList(Integer currentPage, DetachedCriteria detachedCriteria);

    Integer getArticalCount(Integer uid);

    PageBean PageBeanLike(Integer currentPage, Integer uid);
}
