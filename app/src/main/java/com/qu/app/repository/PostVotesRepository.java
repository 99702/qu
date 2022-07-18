package com.qu.app.repository;

import com.qu.app.entity.PostVotes;
import com.qu.app.entity.PostVotesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostVotesRepository extends JpaRepository<PostVotes, PostVotesId> {
    @Query(value = "select count(*) from post_votes pv where pv.fk_post=:postId", nativeQuery = true)
    Long fetchPostVoteCount(Long postId);

    // Return 1 if that `userId` have voted that `postId` else 0
    @Query(value = "select count(u.name) as voted from user u inner join post_votes pv on u.id=pv.fk_user inner join post p on pv.fk_post = p.id where p.title=:postTitle and u.id=:userId", nativeQuery = true)
    Long fetchIfUserHasVotedThatPost(String postTitle, Long userId);

    @Modifying
    @Query(value = "DELETE from post_votes pv where pv.fk_user=:userId and pv.fk_post=:postId", nativeQuery = true)
    void deletePostByThatUser(Long postId, Long userId);

    // count total vote of that postId
    @Query(value = "SELECT COUNT(*) as total_vote from post_votes pv  where pv.fk_post=:postId", nativeQuery = true)
    Long getTotalVoteOfPost(Long postId);


    @Query("SELECT p FROM PostVotes p WHERE p.postVotesId.post.title= :title")
//    @Query(value = "SELECT u.* from post_votes pv inner join `user` u on pv.fk_user =u.id INNER JOIN post p on pv.fk_post=p.id where p.title=:title", nativeQuery = true)
    List<PostVotes> getListOfUserWhoVotedCurrentPost(String title);

    @Query("SELECT p FROM PostVotes p where p.postVotesId.user.id=:userId")
    List<PostVotes> getCurrentUserVotedPostList(Long userId);
}
