package com.qu.app.service.impl;

import com.qu.app.dto.CustomUserDetails;
import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.dto.post.response.PostListWithUserDetails;
import com.qu.app.dto.post.response.PostUpdateResponseDTO;
import com.qu.app.entity.Post;
import com.qu.app.entity.User;
import com.qu.app.error.QuException;
import com.qu.app.repository.PostRepository;
import com.qu.app.repository.PostVotesRepository;
import com.qu.app.repository.UserRepository;
import com.qu.app.service.PostService;
import com.qu.app.utils.AES;
import com.qu.app.utils.RSA;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostVotesRepository postVotesRepository;

    @Autowired
    private RSA rsa;
    @Autowired
    private AES aes;

    @Override
    public PostCreateDTO createPost(Post post, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        // get user from userId
        try{
            // get that user from jwt
            String jwt = aes.decryptText("AES",authorizationHeader.substring(7) );
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
            Boolean checkIfEnabled = (Boolean) claims.get("enabled");
            String checkUserRole = (String) claims.get("role");
            Long  checkUserIdFromToken = Long.valueOf((Integer)claims.get("userId"));

            User user = userRepository.fetchById(checkUserIdFromToken);
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
//
//            User user =  userRepository.fetchByEmailExact(customUserDetails.getUsername());

            // throw error if user doesn't exists
//            if(user == null){
//                throw new QuException("User doesn't exists");
//            }

            // throw error if description and title is none
            if(post.getDescription() == null){
                throw new QuException("Description can't be null");
            }
            if( post.getTitle() == null){
                throw  new QuException("Title can't be null");
            }

            // throw error if title already exists;
            if(postRepository.fetchPostByTitle(post.getTitle()) != null){
                throw new QuException("Post title already exists");
            }

            post.setUser(user);
            postRepository.save(post);
            return this.setterForPostCreateDTO(post);

        } catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public List<GetAPostDTO> getAllPost() {
        List<Post> postList = postRepository.fetchAllPost();
        List<GetAPostDTO> getAPostDTOList = new ArrayList<>();
        for(Post post: postList){
            getAPostDTOList.add(this.setterForGetAPostDTO(post));
        }
        return getAPostDTOList;

    }

    @Override
    public GetAPostDTO fetchPostTitle(String title) {
        try{
            Post post = postRepository.fetchPostByTitle(title);

            // throw error if post is null
            if(post == null){
                throw  new QuException("Post doesn't exists");
            }
            return this.setterForGetAPostDTO(post);
        }catch(Exception e){
            throw new QuException(e.getMessage());
        }

    }

    @Override
    public List<GetAPostDTO> fetchPostByDescription(String desc) {
        List<Post> postList = postRepository.fetchPostDescription(desc);
        List<GetAPostDTO> getAPostDTOList = new ArrayList<>();
        for(Post post: postList){
            GetAPostDTO getAPostDTO = this.setterForGetAPostDTO(post);
            getAPostDTOList.add(getAPostDTO);
        }
        return getAPostDTOList;

    }

    /* something went wrong here */
    @Override
    public List<GetAPostDTO> fetchCurrentUserPost(Long userId) {
        try{
            List<Post> postList = postRepository.fetchCurrentUserPost(userId);
            List<GetAPostDTO> getAPostDTOList = new ArrayList<>();
            for(Post post: postList){
                GetAPostDTO getAPostDTO = this.setterForGetAPostDTO(post);
                getAPostDTOList.add(getAPostDTO);
            }
            if(getAPostDTOList.size() == 0){
                throw new QuException("User doesn't exists");
            }
            return getAPostDTOList;

        }catch(Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public PostUpdateResponseDTO updatePost(HttpServletRequest request, Post post, String postTitle) {
        String authorizationHeader = request.getHeader("Authorization");
        try{
            // get that current Post from post title
            Post currentPost = postRepository.fetchPostByTitle(postTitle);

            // get loggedIn user from jwt
            String jwt = aes.decryptText("AES",authorizationHeader.substring(7) );
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

            Long checkLoggedInUserId = Long.valueOf((Integer) claims.get("userId"));
            String checkLoggedInUserName = (String) claims.get("name");
            String checkLoggedInUserRole = (String) claims.get("role");

            // if that user is not admin
            // then check if token userId and post userId matches
            // if loggedIn user id and post user id doesn't match throw error
            if(!Objects.equals(checkLoggedInUserRole, "ADMIN")){
                if(!checkLoggedInUserId.equals(currentPost.getId()))
                    throw new QuException("Current user `" + checkLoggedInUserName + "`" + " is unauthorized to update `" + currentPost.getUser().getName() + "` post.");
            }

            // get userId from jwt token
            User user = userRepository.fetchById(checkLoggedInUserId);

            // check if userId exists
            if(user == null){
                throw new QuException("User doesn't exists");
            }

            // check if currentPost title post is not null
            if(currentPost == null){
                throw new QuException("Post doesn't exists");
            }

            // check if given post title already exists in the database
            if(postRepository.fetchPostByTitle(post.getTitle()) != null){
                throw new QuException("Title already exists");
            }

            // check if given post fields are null  if null leave, else update the currentPost with the updated field
            if(Objects.nonNull(post.getTitle()) && !"".equalsIgnoreCase(post.getTitle())){
                currentPost.setTitle(post.getTitle());
            }
            if(Objects.nonNull(post.getDescription()) && !"".equalsIgnoreCase(post.getDescription())){
                currentPost.setDescription(post.getDescription());
            }
            Post updatedPost = postRepository.save(currentPost);
            return this.setterForPostUpdateResponseDTO(updatedPost);

        }catch (Exception e){
            throw  new QuException(e.getMessage());
        }
    }

    @Override
    public String deletePost(String postTitle, Long userId){
        try{
            Post getCurrentPost = postRepository.fetchPostByTitle(postTitle);
            User getCurrentUser = userRepository.fetchById(userId);

            // check if that postTitle exists
            if(getCurrentPost == null){
                throw new QuException("Post doesn't exists");
            }
            if(getCurrentUser == null){
                throw new QuException("User doesn't exists");
            }

            // if given userId , and posttitle userId doesnt match throw error
            if(!Objects.equals(getCurrentPost.getUser().getId(), getCurrentUser.getId())){
                throw new QuException("Unauthorized, user dont match with post author");
            }

            // now delete post
            postRepository.delete(getCurrentPost);
            return "Post deleted successfully";
        }catch(Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public List<GetAPostDTO> fetchSimilarPostTitle(String title) {
        List<Post> postList = postRepository.fetchPostBySimilarTitle(title);
        List<GetAPostDTO> getAPostDTOList = new ArrayList<>();
        for(Post post: postList){
            GetAPostDTO getAPostDTO = this.setterForGetAPostDTO(post);
            getAPostDTOList.add(getAPostDTO);
        }
        return getAPostDTOList;

    }

    @Override
    public List<GetAPostDTO> fetchLoggedInUserPost() {
        try{
            // get userid from jwt token
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

            List<Post> postList = postRepository.fetchCurrentUserPost(customUserDetails.getId());
            List<GetAPostDTO> getAPostDTOList = new ArrayList<>();
            for(Post post: postList){
                GetAPostDTO getAPostDTO = this.setterForGetAPostDTO(post);
                getAPostDTOList.add(getAPostDTO);
            }
            if(getAPostDTOList.size() == 0){
                throw new QuException("You haven't created any post. ");
            }
            return getAPostDTOList;
        } catch(Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public List<PostListWithUserDetails> fetchListOfPostsBasedOn(Map<String, String> allParams) {
        String email = allParams.get("userEmail");
        String mobile = allParams.get("userMobile");
        String dobStr = allParams.get("userDob");
        String role = allParams.get("userRole");
        String title = allParams.get("postTitle");
        String description = allParams.get("postDescription");

        try{
            if(description != null && mobile != null){
                return postRepository.listOnPostDescriptionAndUserMobile(description, aes.encryptText("AES",mobile));
            }
            if(description != null && email != null){
                return postRepository.listOnPostDescriptionAndUserEmail(description, aes.encryptText("AES",email));
            }
            if(description != null && dobStr != null){
                return postRepository.listOnPostDescriptionAndUserDob(description, LocalDate.parse(dobStr));
            }
            if(title != null && dobStr != null){
                return postRepository.listOnPostTitleAndUserDob(title, LocalDate.parse(dobStr));
            }


            return null;
        } catch(Exception e){
            throw new QuException(e.getMessage());
        }

    }

    private PostCreateDTO setterForPostCreateDTO(Post post){
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setDescription(post.getDescription());
        postCreateDTO.setImages(post.getImages());
        postCreateDTO.setTitle(post.getTitle());

        return postCreateDTO;
    }
    private PostUpdateResponseDTO setterForPostUpdateResponseDTO(Post post){
        return PostUpdateResponseDTO.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .authorName(post.getUser().getName())
                .build();
    }
    private GetAPostDTO setterForGetAPostDTO(Post post){
        GetAPostDTO getAPostDTO = new GetAPostDTO();
        getAPostDTO.setDescription(post.getDescription());
        getAPostDTO.setTitle(post.getTitle());
        getAPostDTO.setImages(post.getImages());
        getAPostDTO.setAuthorName(post.getUser().getName());
        getAPostDTO.setTotalVotes(postVotesRepository.getTotalVoteOfPost(post.getId()));
        return getAPostDTO;
    }
//    private List<PostListWithUserDetails> setterForPostListWithUserDetails(PostListWithUserDetails postListWithUserDetails){
//        List<PostListWithUserDetails> postListWithUserDetailsList = new ArrayList<>();
////        for(PostListWithUserDetails postListWithUserDetails1: postListWithUserDetailsList)
//
//    }
}
