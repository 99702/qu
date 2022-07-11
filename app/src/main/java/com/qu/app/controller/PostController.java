package com.qu.app.controller;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.entity.Post;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     * getAllPost - Get a list of GetAPostDTO
     * @return List<GetAPostDTO>
     */
    @PostMapping(PathConstant.ALL_POST)
    public List<GetAPostDTO> getAllPost(){
        return postService.getAllPost();
    }

    /**
     * createPost - Creates a post
     * @param post
     * @return PostCreateDTO
     */
    @PostMapping(PathConstant.CREATE_POST)
    public PostCreateDTO createPost(@RequestBody Post post){
        return postService.createPost(post);
    }

    /**
     * updatePost - Update a post
     * @param post
     * @param postTitle
     * @param userId
     * @return Post
     */
    @PutMapping(PathConstant.UPDATE_POST)
    public Post updatePost(@RequestBody Post post, @PathVariable("postTitle") String postTitle, @PathVariable("userId") Long userId){
        return postService.updatePost(post, postTitle, userId);
    }

    /**
     * deletePost - Delete a post
     * @param postTitle
     * @param userId
     * @return ResponseEntity<String>
     */
    @DeleteMapping(PathConstant.DELETE_POST)
    public ResponseEntity<String> deletePost(@PathVariable("postTitle") String postTitle, @PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postTitle, userId));
    }

    /**
     * GetPostByTitle - get a post by title(unique)
     * @param title
     * @return GetAPostDTO
     */
    @PostMapping(PathConstant.SEARCH_POST_BY+"/t")
    @PreAuthorize("ADMIN")
    public GetAPostDTO getByPostTitle(@RequestParam("title") String title) {
        return postService.fetchPostTitle(title);}

    /**
     * Gives array of posts matching that word in the title
     * @param title
     * @return List<GetAPostDTO>
     */
    @PostMapping(PathConstant.SEARCH_POST_BY+"/similar")
    public List<GetAPostDTO> getByPostSimilarTitle(@RequestParam("title") String title) {
        return postService.fetchSimilarPostTitle(title);}



    /**
     * Search post by description
     * @param desc
     * @return List<GetAPostDTO>
     */
    @PostMapping(PathConstant.SEARCH_POST_BY+"/d")
    public List<GetAPostDTO> getByPostDescription(@RequestParam("description") String desc) {return postService.fetchPostByDescription(desc);}

    /**
     * Search post by that author(userId)
     * @param userId
     * @return List<GetAPostDTO>
     */
    @PostMapping(PathConstant.SEARCH_POST_BY+"/u")
    public List<GetAPostDTO> getCurrentUserPost(@RequestParam("user") Long userId ) {return postService.fetchCurrentUserPost(userId);}


    @PostMapping(PathConstant.CURRENT_LOGGED_USER)
    public List<GetAPostDTO> getLoggedinUserPost(){
        return postService.fetchLoggedInUserPost();
    }
}