package Blog.Model;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Catagory {
    private Integer cid;
    private String cname;
    private Integer parentid;

}
