package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.AllStuff;
import com.myproject.stuffexchange.model.StuffProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StuffPropertyRepository extends JpaRepository<StuffProperty,Long> {

    @Query("SELECT new com.myproject.stuffexchange.model.AllStuff(c.name, c.price) from StuffProperty c  ")
    Set<AllStuff> getAllStuff();
}
