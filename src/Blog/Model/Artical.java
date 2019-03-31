package Blog.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter@Getter
public class Artical {
    private Integer artical_id;
    private String artical_title;
    private User user;
    private String artical_time;
    private String artical_desc;
    private String artical_context;
    private Catagory catagory;

    //与用户表进行多对多的配置关联，收藏文章
    private Set<User> userlike=new HashSet<>();

}
