package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {


}
