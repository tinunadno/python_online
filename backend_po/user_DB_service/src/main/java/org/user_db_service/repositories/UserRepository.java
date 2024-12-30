package org.user_db_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.user_db_service.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findUserByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.username = :username AND u.password = :password")
    int deleteUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}