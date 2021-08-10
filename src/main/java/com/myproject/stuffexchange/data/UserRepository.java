package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Modifying
    @Transactional
    void deleteById(long id);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    boolean existsById(long id);

    Optional<AppUser> findByName(String name);

    AppUser getAppUserByName(String name);

    AppUser getAppUserById(long id);

}
