package Blog.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter@Getter
public class User {
    private Integer uid;
    private String uname;
    private String password;
    private String uemail;
    private String usex;
    private String uarea;
    private String ujob;
    private String udesc;
    private String upicture;

    //用户收藏多个文章，多对多关系映射
    private Set<Artical> articallike=new HashSet<>();

}
