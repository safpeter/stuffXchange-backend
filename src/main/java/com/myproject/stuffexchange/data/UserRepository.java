package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<AppUser> findByName(String name);

}
