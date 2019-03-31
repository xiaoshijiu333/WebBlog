package Blog.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter@Getter
public class PageBean<T> {
    //当前页
    private Integer currentPage;
    //总页数
    private Integer totalPage;
    //总记录数
    private Integer totalCount;
    //一页显示几条数据
    private Integer pageSize;
    //当前数据库查询的角标
    private Integer index;
    //当前页数据
    private List<T> pageList;


    //如果没有设置当前页，默认的为第一页
    public void setCurrentPage(Integer currentPage) {
        if (currentPage == null) {
            currentPage = 1;
        }
        this.currentPage = currentPage;
    }

    //计算从数据库查询的角标
    public Integer getIndex() {
        return (currentPage-1)*pageSize;
    }

    //默认一页显示7条数据
    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            pageSize = 7;
        }
        this.pageSize = pageSize;
    }

    //计算总页数
    public Integer getTotalPage() {
        double ceil = Math.ceil(totalCount * 1.0 / pageSize);
        return (int)ceil;
    }

}
