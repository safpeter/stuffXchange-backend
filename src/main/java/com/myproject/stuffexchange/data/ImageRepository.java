package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.Image;
import com.myproject.stuffexchange.model.StuffProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {


  List<Image> getImagesByStuffProperty(StuffProperty stuffProperty);

}
