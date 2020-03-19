package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.Image;
import com.myproject.stuffexchange.model.StuffProperty;
import org.springframework.data.jpa.repository.JpaRepository;


import javax.transaction.Transactional;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {


  List<Image> getImagesByStuffProperty(StuffProperty stuffProperty);

  @Transactional
  void deleteImagesByStuffPropertyId(long id);

}
