package com.qu.app.repository;

import com.qu.app.entity.PostVotes;
import com.qu.app.entity.PostVotesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostVotesRepository extends JpaRepository<PostVotes, PostVotesId> {

    @Query(value = "select count(*) from post_votes pv where pv.fk_post=:postId", nativeQuery = true)
    Long fetchPostVoteCount(Long postId);
}
