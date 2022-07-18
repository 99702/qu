package com.qu.app.controller;

import com.qu.app.dto.user.GetAUserDTO;
import com.qu.app.dto.user.request.UpdateRequest;
import com.qu.app.dto.user.response.UpdateResponse;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Delete that user given userId
     *
     * @param userId
     * @return - ResponseEntity<String>
     */
    @DeleteMapping(PathConstant.DELETE_USER)
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }

    /**
     * Update that user given userId
     *
     * @param userUpdateRequest
     * @param userId
     * @return - UpdateResponse
     */
    @PutMapping(PathConstant.UPDATE_USER)
    public UpdateResponse updateUser(@RequestBody UpdateRequest userUpdateRequest, @PathVariable("userId") Long userId) {
        return userService.updateUser(userUpdateRequest, userId);
    }

    /**
     * Get single user , given userId
     *
     * @param userId
     * @return - GetAUserDTO
     */
    @PostMapping(PathConstant.SINGLE_USER)
    public GetAUserDTO getSingleUser(@PathVariable("userId") Long userId) {
        return userService.getSingleUser(userId);
    }

    /**
     * Get all user
     *
     * @return - List<GetAUserDTO>
     */
    @PostMapping(PathConstant.ALL_USER)
    public List<GetAUserDTO> getAllUser() {
        return userService.getAllUser();
    }

    /**
     * Gives stats of user , their role, enabled, admins etc.
     *
     * @return - Map<String, Long>
     */
    @PostMapping(PathConstant.USER_STATS)
    public Map<String, Long> fetchStatistics() {
        return userService.fetchUserStatistics();
    }


    /**
     * find/exact?  on (name , role, dob=yyyy-MM-dd , enabled, email )
     *
     * @param allParams
     * @return - return list of GetAUserDTO relative to params
     */
    @PostMapping(PathConstant.GET_USER_ATTR_EXACT)
    public List<GetAUserDTO> fetchByAttrExact(@RequestParam Map<String, String> allParams) {
        return userService.fetchByAttrExact(allParams);
    }
}