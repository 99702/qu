package com.qu.app.service.impl;

import com.qu.app.dto.postVotes.response.VoteAPostResponseDTO;
import com.qu.app.entity.Post;
import com.qu.app.entity.PostVotes;
import com.qu.app.entity.PostVotesId;
import com.qu.app.entity.User;
import com.qu.app.error.QuException;
import com.qu.app.repository.PostRepository;
import com.qu.app.repository.PostVotesRepository;
import com.qu.app.repository.UserRepository;
import com.qu.app.service.PostVotesService;
import com.qu.app.service.SessionService;
import com.qu.app.utils.AES;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

@Service
public class PostVotesServiceImpl implements PostVotesService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostVotesRepository postVotesRepository;

    @Autowired
    AES aes;

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    @Override
    public VoteAPostResponseDTO voteToAPost(String title, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        try{
            // get that current Post from post title
            Post currentPost = postRepository.fetchPostByTitle(title);

            // check if currentPost title post is not null
            if(currentPost == null){
                throw new QuException("Post doesn't exists");
            }

            // get user from jwt token
            String jwt = aes.decryptText("AES",authorizationHeader.substring(7) );
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

            Long checkLoggedInUserId = Long.valueOf((Integer) claims.get("userId"));

            // voting only works if the post author and voting user  is not same
            if(!currentPost.getUser().getId().equals(checkLoggedInUserId)){
                // find logged in user
                User user = userRepository.fetchById(checkLoggedInUserId);
                PostVotesId postVotesId = new PostVotesId();
                postVotesId.setPost(currentPost);
                postVotesId.setUser(user);
                PostVotes postVotes = new PostVotes();
                postVotes.setPostVotesId(postVotesId);
                postVotesRepository.save(postVotes);
            }
            return this.setterForVoteAPostResponseDTO(currentPost.getId(), currentPost.getTitle());
        }catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }


    private VoteAPostResponseDTO setterForVoteAPostResponseDTO(Long postId, String postTitle){
        VoteAPostResponseDTO voteAPostResponseDTO = new VoteAPostResponseDTO();
        voteAPostResponseDTO.setPostTitle(postTitle);
        voteAPostResponseDTO.setVoteCount(postVotesRepository.fetchPostVoteCount(postId));
        return voteAPostResponseDTO;
    }


}