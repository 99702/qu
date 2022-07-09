package com.qu.app.service;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.entity.Post;

import java.util.List;

public interface PostService {
    PostCreateDTO createPost(Post post, Long userId);

    List<GetAPostDTO> getAllPost();

    GetAPostDTO fetchPostTitle(String title);

    List<GetAPostDTO> fetchPostByDescription(String desc);

    List<GetAPostDTO> fetchCurrentUserPost(Long userId);

    Post updatePost(Post post, String postTitle, Long userId);

    String deletePost(String PostTitle, Long userId);

}
