package Blog.Dao;

import Blog.Model.Artical;
import Blog.Model.Catagory;
import Blog.Model.User;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public interface ArticalDao {
    List<Catagory> FindAllCatagory(Integer parentid);
    void SaveArtical(Artical artical);

    Integer GetArtCountByUname(Integer uid);

    List<Artical> GetArticalByUname(Integer uid, Integer index, Integer pageSize);

    void deleteArtical(Artical artical);

    Artical getArticalById(Integer artical_id);

    void updateArtical(Artical artical);

    Catagory GetCataById(Integer cid);

    Integer GetAllCount(DetachedCriteria detachedCriteria);

    List<Artical> GetAllArtical(Integer index, Integer pageSize,DetachedCriteria detachedCriteria);
}
