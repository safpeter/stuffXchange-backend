package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.AllStuff;
import com.myproject.stuffexchange.model.StuffDetail;
import com.myproject.stuffexchange.model.StuffProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface StuffPropertyRepository extends JpaRepository<StuffProperty,Long> {

    @Query("select new com.myproject.stuffexchange.model.AllStuff(c.id, c.name,c.price,c.currency,c.user,c.mainPicture,i.image) " +
            "from StuffProperty c left JOIN Image i ON c.mainPicture=i.id where c.user.name = :username")
    List<AllStuff> getAllUserStuff(@Param("username") String username);


    @Query("select  new  com.myproject.stuffexchange.model.StuffDetail(d.id,d.name,d.price,d.currency,d.date,d.description,d.user)" +
            "from StuffProperty d where d.id = :id ")
    StuffDetail getStuffDetails(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("Delete from StuffProperty c where c.id = :id")
    void deleteStuffPropertyById(@Param("id") long id);

    StuffProperty getById(long id);

    @Query("select new com.myproject.stuffexchange.model.AllStuff(c.id, c.name,c.price,c.currency,c.user,c.mainPicture,i.image) " +
            "from StuffProperty c left JOIN Image i ON c.mainPicture=i.id where lower(c.name) like lower(concat('%',:search,'%'))")
    List<AllStuff> searchStuff(@Param("search") String search);


    @Query("select new com.myproject.stuffexchange.model.AllStuff(c.id, c.name,c.price,c.currency,c.user,c.mainPicture,i.image) " +
            "from StuffProperty c left JOIN Image i ON c.mainPicture=i.id where c.id in :stuffs")
    List<AllStuff> getFavouriteStuff(@Param("stuffs") List<Long> stuffs);

    @Query("select new com.myproject.stuffexchange.model.AllStuff(c.id, c.name,c.price,c.currency,c.user,c.mainPicture,i.image) " +
            "from StuffProperty c left JOIN Image i ON c.mainPicture=i.id where c.user.name <> :username")
    List<AllStuff> getPopularStuff(@Param("username") String username);

}
