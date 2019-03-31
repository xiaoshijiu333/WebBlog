package Blog.Dao;

import Blog.Model.Favorite;
import Blog.Model.User;

import java.util.List;

public interface UserDao {
    User GetUserMessage(Integer uid);

    void UpdateUser(User up_user);

    User OneUname(String uname);

    User OneEmail(String uemail);

    List<Favorite> GetfavoriteNum(Integer uid);

    List<Favorite> GetfansNum(Integer uid);

    void favoritePeople(Favorite favorite);

    List<Favorite> IsnotFans(Integer uid, Integer uid1);

    void deleteFavorite(Favorite favorite);

    List<Favorite> AllFavorite(Integer uid, Integer index, Integer pageSize);

    List<Favorite> AllFans(Integer uid, Integer index, Integer pageSize);
}
