package com.qu.app.service;

import com.qu.app.dto.user.*;
import java.util.List;
import java.util.Map;

public interface UserService {
    String deleteUser(Long userId);
    GetAUserDTO getSingleUser(Long userId);
    List<GetAUserDTO> getAllUser();
    UpdateResponse updateUser(UpdateRequest userUpdateRequest, Long userId);
    Map<String, Long> fetchUserStatistics();
    List<GetAUserDTO> fetchByAttrExact(Map<String, String> allParams);
}
