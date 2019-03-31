package Blog.Dao;

import Blog.Model.Artical;
import Blog.Model.Catagory;
import Blog.Model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

public class ArticalDaoImpl extends HibernateDaoSupport implements ArticalDao{
    @Override
    public List<Catagory> FindAllCatagory(Integer parentid) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Catagory.class);

        if(parentid==null){
            detachedCriteria.add(Restrictions.eq("parentid",0));
        }else {
            detachedCriteria.add(Restrictions.eq("parentid",parentid));
        }

        List<Catagory> list = (List<Catagory>)this.getHibernateTemplate()
                .findByCriteria(detachedCriteria);

        return list;
    }

    @Override
    public void SaveArtical(Artical artical) {
        this.getHibernateTemplate().save(artical);
    }

    //指定用户名的文章数量
    @Override
    public Integer GetArtCountByUname(Integer uid) {
        DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Artical.class);
        detachedCriteria.add(Restrictions.eq("user.uid",uid));
        detachedCriteria.setProjection(Projections.rowCount());
        List<Long> list = (List<Long>)this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size()>0){
            return list.get(0).intValue();
        }
        return 0;
    }

    //分页查询数据
    @Override
    public List<Artical> GetArticalByUname(Integer uid, Integer index, Integer pageSize) {
        DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Artical.class);
        //根据id降序，实现从后往前查询
        detachedCriteria.addOrder(Order.desc("artical_id"));
        detachedCriteria.add(Restrictions.eq("user.uid",uid));
        List<Artical> list = (List<Artical>)this.getHibernateTemplate().findByCriteria(detachedCriteria, index, pageSize);
        return list;
    }

    //删除文章
    @Override
    public void deleteArtical(Artical artical) {
        this.getHibernateTemplate().delete(artical);
    }

    //根据id查询文章
    @Override
    public Artical getArticalById(Integer artical_id) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Artical.class);
        detachedCriteria.add(Restrictions.eq("artical_id",artical_id));
        List<Artical> list = (List<Artical>)this.getHibernateTemplate().
                findByCriteria(detachedCriteria);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateArtical(Artical artical) {
        this.getHibernateTemplate().update(artical);
    }

    @Override
    public Catagory GetCataById(Integer cid) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Catagory.class);
        detachedCriteria.add(Restrictions.eq("cid",cid));
        List<Catagory> list = (List<Catagory>)this.getHibernateTemplate().
                findByCriteria(detachedCriteria);

        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }

    //查询所有文章的数量
    @Override
    public Integer GetAllCount(DetachedCriteria detachedCriteria) {
        detachedCriteria.setProjection(Projections.rowCount());
        List<Long> list = (List<Long>)this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list.size()>0){
            return list.get(0).intValue();
        }
        return 0;
    }

    //查询所有文章
    @Override
    public List<Artical> GetAllArtical(Integer index, Integer pageSize,DetachedCriteria detachedCriteria) {
        //根据id降序，实现从后往前查询
        detachedCriteria.addOrder(Order.desc("artical_id"));
        //清空查询总记录数的条件
        detachedCriteria.setProjection(null);
        List<Artical> list = (List<Artical>)this.getHibernateTemplate().findByCriteria(detachedCriteria, index, pageSize);
        return list;
    }
}
