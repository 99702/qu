package com.qu.app.controller;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.dto.post.response.PostListWithUserDetails;
import com.qu.app.dto.post.response.PostUpdateResponseDTO;
import com.qu.app.entity.Post;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostUpdate;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping(PathConstant.ALL_POST)
    public List<GetAPostDTO> getAllPost(){
        return postService.getAllPost();
    }


    @PostMapping(PathConstant.CREATE_POST)
    //@PreAuthorize("hasAuthority('ADMIN'")
    public PostCreateDTO createPost(@RequestBody Post post, HttpServletRequest request){
        return postService.createPost(post, request);
    }

    @PutMapping(PathConstant.UPDATE_POST)
    public PostUpdateResponseDTO updatePost(@RequestBody Post post, @PathVariable("postTitle") String postTitle, HttpServletRequest request){
        return postService.updatePost(request, post, postTitle);
    }

    @DeleteMapping(PathConstant.DELETE_POST)
    public ResponseEntity<String> deletePost(@PathVariable("postTitle") String postTitle, @PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postTitle, userId));
    }

    @PostMapping(PathConstant.SEARCH_POST_BY+"/t")
    @PreAuthorize("ADMIN")
    public GetAPostDTO getByPostTitle(@RequestParam("title") String title) {
        return postService.fetchPostTitle(title);}

    @PostMapping(PathConstant.SEARCH_POST_BY+"/similar")
    public List<GetAPostDTO> getByPostSimilarTitle(@RequestParam("title") String title) {
        return postService.fetchSimilarPostTitle(title);}

    @PostMapping(PathConstant.SEARCH_POST_BY+"/d")
    public List<GetAPostDTO> getByPostDescription(@RequestParam("description") String desc) {return postService.fetchPostByDescription(desc);}

    @PostMapping(PathConstant.SEARCH_POST_BY+"/u")
    public List<GetAPostDTO> getCurrentUserPost(@RequestParam("user") Long userId ) {return postService.fetchCurrentUserPost(userId);}

    @PostMapping(PathConstant.CURRENT_LOGGED_USER)
    public List<GetAPostDTO> getLoggedinUserPost(){
        return postService.fetchLoggedInUserPost();
    }

    @PostMapping(PathConstant.LIST_POST)
    public List<PostListWithUserDetails> getListOfPostsBasedOn(@RequestParam Map<String, String> allParams ) {
        return postService.fetchListOfPostsBasedOn(allParams);}
}