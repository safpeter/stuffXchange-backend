package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.AllStuff;
import com.myproject.stuffexchange.model.StuffDetail;
import com.myproject.stuffexchange.model.StuffProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface StuffPropertyRepository extends JpaRepository<StuffProperty,Long> {

    @Query("select new com.myproject.stuffexchange.model.AllStuff(c.id, c.name,c.price,c.mainPicture,i.image) from StuffProperty c left JOIN Image i ON c.mainPicture=i.id")
    List<AllStuff> getAllStuff();

    @Query("select  new  com.myproject.stuffexchange.model.StuffDetail(d.id,d.name,d.price,d.date,d.description)  from StuffProperty d where d.id = :id ")
    StuffDetail getStuffDetails(@Param("id") long id);

    StuffProperty getById(long id);
}
