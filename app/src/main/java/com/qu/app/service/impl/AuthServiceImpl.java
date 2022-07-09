package com.qu.app.service.impl;
import com.qu.app.dto.user.*;

import com.qu.app.entity.Keys;
import com.qu.app.entity.User;
import com.qu.app.error.QuException;
import com.qu.app.repository.KeysRepository;
import com.qu.app.repository.PostRepository;
import com.qu.app.repository.UserRepository;

import com.qu.app.service.KeysService;
import com.qu.app.utils.AES;

import com.qu.app.utils.JwtProvider;
import com.qu.app.utils.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.*;
import java.util.*;


import com.qu.app.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AES aes;


    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    KeysService keysService;

    @Autowired
    RSA rsa;


//    /******************* Get or generate *****************/
//    List<String> publicPrivateKeys = keysService.SaveGetRSAKeys();
//    PublicKey publicKey  = keysService.decodePublicKey(publicPrivateKeys.get(0));
//    PrivateKey privateKey  = keysService.decodePrivateKey(publicPrivateKeys.get(1));
//

    @Override
    public RegisterUserDTO registerUser(User user){
        try{
            User checkUserMobile = userRepository.fetchByMobileExact(aes.encryptText("AES", user.getMobile()));
            User checkUserEmail = userRepository.fetchByEmailExact(user.getEmail());

            // check if any required fields are not null
            if( user.getPassword() == null) throw new QuException("Password can't be null");
            else if(user.getEmail() == null) throw new QuException("Email can't be null");
            else if(user.getMobile() == null) throw new QuException("Name can't be null");
            else if(user.getDob() == null) throw new QuException("Date of birth can't be null");

            // check if email already exists
            if(checkUserEmail != null){
                throw new QuException("Email already exists");
            }

            // check if mobile already exists
            if(checkUserMobile != null){
                throw new QuException("Mobile already exists");
            }

            // check if user password is equal to email
            if(user.getEmail().equals(user.getPassword())){
                throw new QuException("Password can't be same as email");
            }

            // check if mobile is less than 10
            if(user.getMobile().length() < 10){
                throw new QuException("Mobile number invalid");
            }
            // throw error if password is less than 8
            if(user.getPassword().length() < 8){
                throw new QuException("Password must be longer than 8 characters");
            }

            // throw error if age less than 16 and greater than 120
            int age = Period.between(user.getDob(), LocalDate.now()).getYears();
            if(age < 16 || age > 120){
                throw new QuException("Please provide valid date of birth");
            }

            /******************* Get or generate *****************/
            Map<String, String> publicPrivateKeys = keysService.SaveGetRSAKeys();
            PublicKey publicKey  = keysService.decodePublicKey(publicPrivateKeys.get("PUBLIC"));
            PrivateKey privateKey  = keysService.decodePrivateKey(publicPrivateKeys.get("PRIVATE"));


            user.setName(rsa.encryptText(user.getName(), publicKey));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setMobile(aes.encryptText("AES", user.getMobile()));
            User RegisteredUser = userRepository.save(user);
            return this.setterForRegisterUserDTO(RegisteredUser);
        }catch(Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        try {
            // check if email or phone, password is not null
            if(loginRequest.getEmail() == null && loginRequest.getMobile() == null){
                throw new QuException("Email or mobile can't be blank");
            }
            // check if user exists on given email or given mobile
            User user = (loginRequest.getEmail() != null) ? userRepository.fetchByEmailExact(loginRequest.getEmail()) : userRepository.fetchByMobileExact(aes.encryptText("AES",loginRequest.getMobile())
            );
            if(user == null){
                throw new QuException("User doesn't exists");
            }

            // check given user password and database password matched
            String encodedPassword = userRepository.fetchById(user.getId()).getPassword();
            boolean result = passwordEncoder.matches(loginRequest.getPassword(), encodedPassword);
            if(!result){
                throw new QuException("Password doesn't matches");
            }

            return this.setterForLoginResponse(
                    user.getName(),
                    user.getEmail(),
                    user.getId().toString()+"_"+randomString,
                    "SASAFSADF",
                    Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())
            );

        } catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }
    private  RegisterUserDTO setterForRegisterUserDTO(User user){
        /******************* Get private key *****************/
        Map<String, String> publicPrivateKeys = keysService.SaveGetRSAKeys();
        PrivateKey privateKey  = keysService.decodePrivateKey(publicPrivateKeys.get("PRIVATE"));

        String decryptedMobile = aes.encryptText("AES",user.getMobile());
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setDob(user.getDob());
        registerUserDTO.setEmail(user.getEmail());
        registerUserDTO.setMobile(decryptedMobile);
//        registerUserDTO.setName(user.getName());
        registerUserDTO.setName(rsa.decryptText(user.getName(), privateKey));
        return registerUserDTO;
    }

    private LoginResponse setterForLoginResponse(String name, String email, String token, String refreshToken, Instant expiresAt){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(name);

        loginResponse.setEmail(email);
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresAt(expiresAt);
        return loginResponse;
    }

    private String shuffler(Long num){
        String str="QWERTYusdfghjkgywehbdsxhjnx!@#$%^&*()mzbcvcbnfn";
        List<String> letters = Array.asList(str.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for(String l:letters){
            shuffled += l;
        }
        return shuffled;
    }
}
