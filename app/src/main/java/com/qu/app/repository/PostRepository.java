package com.qu.app.repository;

import com.qu.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.title=:title")
    Post fetchPostByTitle(String title);

    @Query(value = "SELECT * FROM post p WHERE p.title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    List<Post> fetchPostBySimilarTitle(String title);

//    @Query("SELECT p from Post p where p.description LIKE CONCAT('%',:desc,'%')")
    @Query(value = "SELECT * from post p where p.description LIKE CONCAT('%', :desc, '%')", nativeQuery = true)
    List<Post> fetchPostDescription(String desc);

//        @Query("SELECT p from Post p where p.user.id = :userId")
    @Query(value = "select * from post p INNER JOIN user u on p.fk_user = u.id where p.fk_user = :userId", nativeQuery = true)
    List<Post> fetchCurrentUserPost(Long userId);

    @Query(value="select * from post", nativeQuery = true)
    List<Post> fetchAllPost();
}
