package com.qu.app.service.impl;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.postVotes.response.GetAListOfUserWhoVotedThatPostDTO;
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
import com.qu.app.utils.AES;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    @Transactional
    public VoteAPostResponseDTO voteToAPost(String title, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String message = "";
        String votedBy = "";
        try {
            // get that current Post from post title
            Post currentPost = postRepository.fetchPostByTitle(title);

            // check if currentPost title post is not null
            if (currentPost == null) {
                throw new QuException("Post doesn't exists");
            }

            // get user from jwt token
            String jwt = aes.decryptText("AES", authorizationHeader.substring(7));
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

            Long checkLoggedInUserId = Long.valueOf((Integer) claims.get("userId"));

            // voting only works if the post author and voting user  is not same
            if (!currentPost.getUser().getId().equals(checkLoggedInUserId)) {
                // find logged in user
                User user = userRepository.fetchById(checkLoggedInUserId);
                votedBy = user.getName();

                // check if user have already voted that post
                // if he already voted that post,  delete that vote by user
                if (postVotesRepository.fetchIfUserHasVotedThatPost(title, checkLoggedInUserId) == 1) {
                    postVotesRepository.deletePostByThatUser(currentPost.getId(), checkLoggedInUserId);
                    message = "down";
                } else {
                    // else if user haven't voted that post save
                    PostVotesId postVotesId = new PostVotesId();
                    postVotesId.setPost(currentPost);
                    postVotesId.setUser(user);
                    PostVotes postVotes = new PostVotes();
                    postVotes.setPostVotesId(postVotesId);
                    message = "up";

                    // save
                    postVotesRepository.save(postVotes);
                }
            } else {
                throw new QuException("Cant vote own post");
            }
            return this.setterForVoteAPostResponseDTO(currentPost.getId(), currentPost.getTitle(), message, votedBy);
        } catch (Exception e) {
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public List<GetAListOfUserWhoVotedThatPostDTO> getAListOfUserWhoVotedThatPostService(String title) {
        List<GetAListOfUserWhoVotedThatPostDTO> listOfUserWhoVotedThatPostDTOS = new ArrayList<>();
        List<PostVotes> postVotesList = postVotesRepository.getListOfUserWhoVotedCurrentPost(title);
        for (PostVotes postVotes : postVotesList) {
            listOfUserWhoVotedThatPostDTOS.add(this.setterForGetAListOfUserWhoVotedThatPostDTO(postVotes));
        }
        return listOfUserWhoVotedThatPostDTOS;
    }

    @Override
    public List<GetAPostDTO> getCurrentUserVotedPostList(String token) {

        try {
            List<GetAPostDTO> getAPostDTOList = new ArrayList<>();

            //get userId from token
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(aes.decryptText("AES", token.substring(7)))
                    .getBody();
            Long checkUserIdFromToken = Long.valueOf((Integer) claims.get("userId"));
            List<PostVotes> postVotesList = postVotesRepository.getCurrentUserVotedPostList(checkUserIdFromToken);
            for (PostVotes postVotes : postVotesList) {
                getAPostDTOList.add(this.setterForGetAPostDTO(postVotes));
            }
            return getAPostDTOList;
        } catch (Exception e) {
            throw new QuException(e.getMessage());
        }
    }

    private VoteAPostResponseDTO setterForVoteAPostResponseDTO(Long postId, String postTitle, String message, String votedBy) {
        VoteAPostResponseDTO voteAPostResponseDTO = new VoteAPostResponseDTO();
        voteAPostResponseDTO.setPostTitle(postTitle);
        voteAPostResponseDTO.setVoteCount(postVotesRepository.fetchPostVoteCount(postId));
        voteAPostResponseDTO.setMessage(message);
        voteAPostResponseDTO.setVotedBy(votedBy);
        return voteAPostResponseDTO;
    }

    private GetAListOfUserWhoVotedThatPostDTO setterForGetAListOfUserWhoVotedThatPostDTO(PostVotes postVotes) {
        GetAListOfUserWhoVotedThatPostDTO getAListOfUserWhoVotedThatPostDTO = new GetAListOfUserWhoVotedThatPostDTO();
        getAListOfUserWhoVotedThatPostDTO.setUserId(postVotes.getPostVotesId().getUser().getId());
        getAListOfUserWhoVotedThatPostDTO.setName(postVotes.getPostVotesId().getUser().getName());
        getAListOfUserWhoVotedThatPostDTO.setProfilePic(postVotes.getPostVotesId().getUser().getProfilePic());
        return getAListOfUserWhoVotedThatPostDTO;
    }

    private GetAPostDTO setterForGetAPostDTO(PostVotes postVotes) {
        GetAPostDTO getAPostDTO = new GetAPostDTO();
        getAPostDTO.setDescription(postVotes.getPostVotesId().getPost().getDescription());
        getAPostDTO.setTitle(postVotes.getPostVotesId().getPost().getTitle());
        getAPostDTO.setImages(postVotes.getPostVotesId().getPost().getImages());
        getAPostDTO.setAuthorName(postVotes.getPostVotesId().getPost().getUser().getName());
        getAPostDTO.setTotalVotes(postVotesRepository.getTotalVoteOfPost(postVotes.getPostVotesId().getPost().getId()));
        return getAPostDTO;
    }

}