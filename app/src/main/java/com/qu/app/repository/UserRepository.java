package com.qu.app.repository;

import com.qu.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value="SELECT * FROM user u where u.id = :userId", nativeQuery = true) // how to convert string to long, in query
//    @Query(value="SELECT u FROM User u where u.id = :userId")
    User fetchById(Long userId);

    @Query(value="SELECT * FROM user u where u.name = :name", nativeQuery = true)
//    @Query(value="SELECT u FROM User u where u.name = :name")
    List<User> fetchByNameExact(String name);

    @Query(value="SELECT u FROM User u where u.email = :email")
    User fetchByEmailExact(String email);

    @Query(value="SELECT u FROM User u where u.mobile = :mobile")
    User fetchByMobileExact(String mobile);

    @Query(value="SELECT u from User u where u.enabled = :enabled")
    List<User> fetchByEnabled(Boolean enabled);

    @Query(value="SELECT u FROM User u where u.dob= :dob")
    List<User> fetchBydobExact(LocalDate dob);

    @Query(value="SELECT u from User u where u.role = :role")
    List<User> fetchByRoleExact(String role);

    @Query("SELECT COUNT(u.id) from User u")
    Long fetchTotalUser();

    @Query("SELECT COUNT(u) from User u where u.enabled=True")
    Long fetchTotalEnabledUser();

    @Query("SELECT COUNT(u) from User u where u.enabled=False")
    Long fetchTotalNotEnabledUser();

    @Query("SELECT COUNT(u) from User u where u.role='ADMIN'")
    Long fetchTotalADMIN();

    @Query("SELECT COUNT(u) from User u where u.role='USER'")
    Long fetchTotalUSER();
}