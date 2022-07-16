package com.qu.app.service;

import com.qu.app.dto.user.*;
import com.qu.app.dto.user.request.UpdateRequest;
import com.qu.app.dto.user.response.UpdateResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * deleteUser - Deletes that userId by admin
     * @param userId
     * @return String
     */
    String deleteUser(Long userId);

    /**
     * getSingleUser - Fetches single user given userId
     * @param userId
     * @return GetAUserDTO
     */
    GetAUserDTO getSingleUser(Long userId);

    /**
     * Get all users
     * @return List of GetAUserDTO
     */
    List<GetAUserDTO> getAllUser();

    /**
     * updateUser - Updates current user by that userId
     * @param userUpdateRequest
     * @param userId
     * @return UpdateResponse dto
     */
    UpdateResponse updateUser(UpdateRequest userUpdateRequest, Long userId);

    /**
     * fetchUserStatistics - Fetches Map of count of user statistics who are  enabled, admin, role....
     * @return Map<String, Long>
     */
    Map<String, Long> fetchUserStatistics();

    /**
     * Fetches user by user attributes as request params
     * @param allParams
     * @return List of GetAUserDTO
     */
    List<GetAUserDTO> fetchByAttrExact(Map<String, String> allParams);
}
