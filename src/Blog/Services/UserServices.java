package Blog.Services;

import Blog.Model.Favorite;
import Blog.Model.PageBean;
import Blog.Model.User;


public interface UserServices {
    User GetUserMessage(Integer uid);

    void UpdateUser(User up_user);

    String OneUname(String uname);

    String OneEmail(String uemail);

    Integer GetfavoriteNum(Integer uid);

    Integer GetfansNum(Integer uid);

    void favoritePeople(Favorite favorite);

    Integer IsnotFans(Integer uid, Integer uid1);

    void deleteFavorite(Favorite favorite);

    Favorite QueryFans(Integer uid, Integer uid1);

    PageBean<Favorite> AllFavorite(Integer currentPage,Integer uid);

    PageBean<Favorite> AllFans(Integer currentPage, Integer uid);
}
