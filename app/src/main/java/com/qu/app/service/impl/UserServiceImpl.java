package com.qu.app.service.impl;

import com.qu.app.dto.user.*;
import com.qu.app.entity.Post;
import com.qu.app.entity.User;
import com.qu.app.error.QuException;
import com.qu.app.repository.PostRepository;
import com.qu.app.repository.UserRepository;
import com.qu.app.service.KeysService;
import com.qu.app.service.UserService;
import com.qu.app.utils.AES;

import com.qu.app.utils.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.*;
import java.util.*;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private KeysService keysService;

    @Autowired
    RSA rsa;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AES aes;
    @Override
    public String deleteUser(Long userId) {
        try{
            User user = userRepository.fetchById(userId);
            // throws error if user is null
            if(user == null){
                throw new QuException("User doesn't exists");
            }

            // if user role is admin dont delete it
            if(user.getRole().equals("ADMIN")){
                throw new QuException("Admin can't be deleted");
            }
            // begin first by delete the post by that user in Post table
            List<Post> postByThatUser = postRepository.fetchCurrentUserPost(userId);
            for(Post post: postByThatUser){
                postRepository.delete(post);
            }
            // now delete the user
            userRepository.delete(user);
            return "User deleted successfully.";
        }catch (Exception e){
            System.out.println(e);
            throw  new QuException(e.getMessage());
        }
    }

    @Override
    public GetAUserDTO getSingleUser(Long userId){
        try{
            User user = userRepository.fetchById(userId);
            if(user == null){
                throw new QuException("User doesn't exists");
            }
            return this.setterForGetAUserDTO(user);
        }catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public List<GetAUserDTO> getAllUser(){
        List<User> userList = userRepository.findAll();
        List<GetAUserDTO> getAUserDTOList = new ArrayList<>();
        for (User user: userList) {
            GetAUserDTO getAUserDTO = this.setterForGetAUserDTO(user);
            getAUserDTOList.add(getAUserDTO);
        }
        return getAUserDTOList;
    }

    @Override
    public UpdateResponse updateUser(UpdateRequest userUpdateRequest, Long userId) {
        try{
            User getCurrentUser = userRepository.fetchById(userId);

            // throw error if user of userId doesnt exists
            if(getCurrentUser == null){
                throw new QuException("User doesn't exists");
            }

            // check if request password is equal to email
//            if(userUpdateRequest.getEmail().equals(userUpdateRequest.getPassword())){
//                throw new QuException("Password can't be same as email");
//            }


            // check if given user fields are null  if null leave,
            // else update the currentPost with the updated field
            if(Objects.nonNull(userUpdateRequest.getName()) && !"".equalsIgnoreCase(userUpdateRequest.getName())){
                getCurrentUser.setName(userUpdateRequest.getName());
            }
            if(Objects.nonNull(userUpdateRequest.getDob())){
                // throw error if age less than 16 and greater than 120
                int age = Period.between(userUpdateRequest.getDob(), LocalDate.now()).getYears();
                if(age < 16 || age > 120){
                    throw new QuException("Please provide valid date of birth");
                }

                getCurrentUser.setDob(userUpdateRequest.getDob());
            }
            if(Objects.nonNull(userUpdateRequest.getEmail()) && !"".equalsIgnoreCase(userUpdateRequest.getEmail())){
                // check if email already exists
                if(userRepository.fetchByEmailExact(userUpdateRequest.getEmail()) != null){
                    throw new QuException("Email is already exists");
                }
                getCurrentUser.setEmail(userUpdateRequest.getEmail());
            }
            if(Objects.nonNull(userUpdateRequest.getMobile())){
                // check if mobile is less than 10
                if(userUpdateRequest.getMobile().length() < 10){
                    throw new QuException("Mobile number invalid");
                }
                // Encrypt mobile with AES algorithm , throw if encrypted mobile string already exists
                String encryptedMobile = aes.encryptText("AES", userUpdateRequest.getMobile());
                if(userRepository.fetchByMobileExact(encryptedMobile) != null){
                    throw new QuException("Mobile number already exists");
                }
                getCurrentUser.setMobile(encryptedMobile);
            }
            if(Objects.nonNull(userUpdateRequest.getProfilePic())){
                getCurrentUser.setProfilePic(userUpdateRequest.getProfilePic());
            }
             if(Objects.nonNull(userUpdateRequest.getPassword())){

                 // throw error if password is less than 8
                 if(userUpdateRequest.getPassword().length() < 8){
                     throw new QuException("Password must be longer than 8 characters");
                 }
                 // hash password
                 getCurrentUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
             }

            userRepository.save(getCurrentUser);
            return this.setterForUpdateResponse(getCurrentUser);
        }catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public Map<String, Long> fetchUserStatistics() {
        Map<String, Long> statsDict = new HashMap<>();
        Long totalUser = userRepository.fetchTotalUser();
        Long totalEnabledUser = userRepository.fetchTotalEnabledUser();
        Long totalNotEnabledUser = userRepository.fetchTotalNotEnabledUser();
        Long totalUSER = userRepository.fetchTotalUSER();
        Long totalADMIN = userRepository.fetchTotalADMIN();
        statsDict.put("total_user", totalUser);
        statsDict.put("total_enabled_user", totalEnabledUser);
        statsDict.put("total_not_enabled_user", totalNotEnabledUser);
        statsDict.put("total_ADMIN", totalADMIN);
        statsDict.put("total_USER", totalUSER);
        return statsDict;
    }

    @Override
    public List<GetAUserDTO> fetchByAttrExact(Map<String, String> allParams) {
        String name = allParams.get("name");
        String email = allParams.get("email");
        Boolean enabled = Boolean.parseBoolean(allParams.get("enabled"));
        String mobileStr = allParams.get("mobile");
        String role = allParams.get("role");
        String dobStr = allParams.get("dob");

//        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(allParams.get("dob"));
        List<GetAUserDTO> results = new ArrayList<>();

        if(name != null){
            List<User> userList = userRepository.fetchByNameExact(name);
            List<GetAUserDTO> getAUserDTOList = new ArrayList<>();
            for(User user: userList){
                GetAUserDTO getAUserDTO = this.setterForGetAUserDTO(user);
                getAUserDTOList.add(getAUserDTO);
            }
            results.addAll(getAUserDTOList);
        }
        if(email != null){
            User user = userRepository.fetchByEmailExact(email);
            GetAUserDTO getAUserDTO = this.setterForGetAUserDTO(user);
            results.add(getAUserDTO);
        }

        if(mobileStr != null){
            User user = userRepository.fetchByMobileExact(mobileStr);
            GetAUserDTO getAUserDTO = this.setterForGetAUserDTO(user);
            results.add(getAUserDTO);
        }
        if(enabled){
            List<User> userList = userRepository.fetchByEnabled(enabled);
            List<GetAUserDTO> getAUserDTOList = new ArrayList<>();
            for(User user: userList){
                GetAUserDTO getAUserDTO = this.setterForGetAUserDTO(user);
                getAUserDTOList.add(getAUserDTO);
            }
            results.addAll(getAUserDTOList);
        }
        if(role != null){
            List<User> userList = userRepository.fetchByRoleExact(role);
            List<GetAUserDTO> getAUserDTOList = new ArrayList<>();
            for(User user: userList){
                GetAUserDTO getAUserDTO = this.setterForGetAUserDTO(user);
                getAUserDTOList.add(getAUserDTO);
            }
            results.addAll(getAUserDTOList);
        }

        if(dobStr != null){
            LocalDate dob = LocalDate.parse(dobStr);
            List<User> userList = userRepository.fetchBydobExact(dob);
            List<GetAUserDTO> getAUserDTOList = new ArrayList<>();
            if(dob!=null){
                for(User user: userList){
                    getAUserDTOList.add(this.setterForGetAUserDTO(user));
                }
                results.addAll(getAUserDTOList);
            }

        }

        return results;
    }

    private GetAUserDTO setterForGetAUserDTO(User user){
        /** decrypt user name **/
        Map<String, String> pubPrivateKeys = keysService.SaveGetRSAKeys();
        PrivateKey privateKey = keysService.decodePrivateKey(pubPrivateKeys.get("PRIVATE"));

        GetAUserDTO getAUserDTO = new GetAUserDTO();
        getAUserDTO.setEmail(user.getEmail());
        getAUserDTO.setMobile(aes.decryptText("AES", user.getMobile()));
//        getAUserDTO.setName(user.getName());
        getAUserDTO.setName(rsa.decryptText(user.getName(), privateKey));
        return getAUserDTO;
    }

    private LoginResponse setterForLoginResponse(User user){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(user.getEmail());
        loginResponse.setName(user.getName());
        loginResponse.setToken("0-32i23892uihskjbsdfiq2379ry233iorfbkvgasigb");
        return loginResponse;
    }

    private  UpdateResponse setterForUpdateResponse(User user){
        UpdateResponse updateResponse = new UpdateResponse();
        updateResponse.setName(user.getName());
        updateResponse.setDob(user.getDob());
        updateResponse.setEmail(user.getEmail());
        updateResponse.setMobile(aes.decryptText("AES", user.getMobile()));
        updateResponse.setProfilePic(user.getProfilePic());
        return updateResponse;
    }
}
