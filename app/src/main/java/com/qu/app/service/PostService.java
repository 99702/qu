package com.qu.app.service;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.dto.post.response.PostListWithUserDetails;
import com.qu.app.dto.post.response.PostUpdateResponseDTO;
import com.qu.app.entity.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PostService {
    /**
     * createPost - Creates a post for authenticated user
     *
     * @param post
     * @return PostCreateDTO
     */
    PostCreateDTO createPost(Post post, HttpServletRequest request);

    /**
     * getAllPost() - Get all the post
     *
     * @return List of GetAPostDTO
     */
//    List<GetAPostDTO> getAllPost(int offset, int pageSize);
    List<GetAPostDTO> getAllPost(Integer pageNo);

    /**
     * fetchPostTitle - Given Exact title of post, returns exact title related post
     *
     * @param title
     * @return GetAPostDTO
     */
    GetAPostDTO fetchPostTitle(String title);

    /**
     * fetchPostTitle - Given keyword , it returns all post that matches given keyword
     *
     * @param desc
     * @return List<GetAPostDTO>
     */
    List<GetAPostDTO> fetchPostByDescription(String desc);

    /**
     * fetchCurrentUserPost - Automatically fetches current loggedIn user post
     *
     * @param userId
     * @return
     */
    List<GetAPostDTO> fetchCurrentUserPost(Long userId);

    /**
     * updatePost - updates a current post
     *
     * @param post
     * @param postTitle
     * @return PostUpdateResponseDTO
     */
    PostUpdateResponseDTO updatePost(HttpServletRequest request, Post post, String postTitle);

    /**
     * deletePost - deletes a post
     *
     * @param PostTitle
     * @param userId
     * @return String
     */
    String deletePost(String PostTitle, Long userId);

    /**
     * fetchSimilarPostTitle - find posts that search matching word from title
     *
     * @param title
     * @return List of GetAPostDTO
     */
    List<GetAPostDTO> fetchSimilarPostTitle(String title);

    /**
     * fetchLoggedInUserPost - Fetches a list of post by loggedin user
     *
     * @return List of GetAPostDTO
     */
    List<GetAPostDTO> fetchLoggedInUserPost();


    /**
     * Lists custom post  and user fields , tangled
     *
     * @param allParams
     * @return
     */
    List<PostListWithUserDetails> fetchListOfPostsBasedOn(Map<String, String> allParams);
}