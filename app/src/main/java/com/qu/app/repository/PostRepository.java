package com.qu.app.repository;

import com.qu.app.dto.post.response.PostListWithUserDetails;
import com.qu.app.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query(value = "select * from post", nativeQuery = true)
    Page<Post> fetchAllPost(Pageable pageable);

    //  where description="something" and user mobile="mobile"
//    @Query(value = "select p.id as postId, p.description as postDescription , p.title as postTitle, u.id as authorId, u.name as authorName, u.image as authorImage from post p JOIN user u on p.fk_user = u.id where p.description LIKE :postDescription and u.mobile=:userMobile", nativeQuery = true)
    @Query("select new com.qu.app.dto.post.response.PostListWithUserDetails(p.id, p.description, p.title, u.id, u.name, u.profilePic) from Post p join p.user u where p.description LIKE CONCAT('%',:postDescription,'%') and u.mobile=:userMobile")
    List<PostListWithUserDetails> listOnPostDescriptionAndUserMobile(String postDescription, String userMobile);

    //  where description="something" and user email="email"
//    @Query(value = "select p.id as postId, p.description as postDescription , p.title as postTitle, u.id as authorId, u.name as authorName, u.image as authorImage from post p JOIN user u on p.fk_user = u.id where p.description LIKE :postDescription and u.email=:userEmail", nativeQuery = true)
    @Query("select new com.qu.app.dto.post.response.PostListWithUserDetails(p.id, p.description, p.title, u.id, u.name, u.profilePic) from Post p join p.user u where p.description LIKE CONCAT('%',:postDescription,'%') and u.email=:userEmail")
    List<PostListWithUserDetails> listOnPostDescriptionAndUserEmail(String postDescription, String userEmail);

    //  where description="something" and user dob=2000-03-11
//    @Query(value = "select p.id as postId, p.description as postDescription , p.title as postTitle, u.id as authorId, u.name as authorName, u.image as authorImage from post p JOIN user u on p.fk_user = u.id where p.description LIKE :postDescription and u.dob=:userDob", nativeQuery = true)
    @Query("select new com.qu.app.dto.post.response.PostListWithUserDetails(p.id, p.description, p.title, u.id, u.name, u.profilePic) from Post p join p.user u where p.description LIKE CONCAT('%',:postDescription,'%') and u.dob=:dob")
    List<PostListWithUserDetails> listOnPostDescriptionAndUserDob(String postDescription, LocalDate dob);

    @Query("select new com.qu.app.dto.post.response.PostListWithUserDetails(p.id, p.description, p.title, u.id, u.name, u.profilePic) from Post p join p.user u where p.title LIKE CONCAT('%',:postTitle,'%') and u.dob=:dob")
    List<PostListWithUserDetails> listOnPostTitleAndUserDob(String postTitle, LocalDate dob);
}