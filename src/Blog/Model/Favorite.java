package Blog.Model;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class Favorite {
    private Integer fid;
    private User user;
    private User favoriteuser;
    private String favoritetime;

}
