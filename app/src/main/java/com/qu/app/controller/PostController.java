package com.qu.app.controller;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.entity.Post;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param userId
     * @return PostCreateDTO
     */
    @PostMapping(PathConstant.CREATE_POST)
    public PostCreateDTO createPost(@RequestBody Post post, @PathVariable("userId") Long userId){
        return postService.createPost(post, userId);
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
    public GetAPostDTO getByPostTitle(@RequestParam("title") String title) {
        return postService.fetchPostTitle(title);}

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
}