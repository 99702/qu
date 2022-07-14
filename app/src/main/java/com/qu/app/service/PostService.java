package com.qu.app.service;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.entity.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
    /**
     * createPost - Creates a post
     * @param post
     * @return PostCreateDTO
     */
    PostCreateDTO createPost(Post post, HttpServletRequest request);

    /**
     * @return
     */
    List<GetAPostDTO> getAllPost();

    GetAPostDTO fetchPostTitle(String title);

    List<GetAPostDTO> fetchPostByDescription(String desc);

    List<GetAPostDTO> fetchCurrentUserPost(Long userId);

    Post updatePost(Post post, String postTitle, Long userId);

    String deletePost(String PostTitle, Long userId);

    List<GetAPostDTO> fetchSimilarPostTitle(String title);

    List<GetAPostDTO> fetchLoggedInUserPost();
}
