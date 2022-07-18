package com.qu.app.service.impl;

import com.qu.app.dto.user.request.LoginRequest;
import com.qu.app.dto.user.response.LoginResponse;
import com.qu.app.dto.user.response.RegisterResponse;
import com.qu.app.entity.User;
import com.qu.app.error.QuException;
import com.qu.app.repository.PostRepository;
import com.qu.app.repository.UserRepository;
import com.qu.app.service.AuthService;
import com.qu.app.service.KeysService;
import com.qu.app.utils.AES;
import com.qu.app.utils.JwtUtil;
import com.qu.app.utils.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    // set photo upload directory
    public static String uploadDirectory = System.getProperty("user.dir") + "/app/src/main/resources/static/images/profilePictures/";
    @Autowired
    KeysService keysService;
    @Autowired
    RSA rsa;
    @Autowired
    SessionServiceImpl sessionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AES aes;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public RegisterResponse registerUser(User user, MultipartFile profilePic) {//, MultipartFile photoFile){
        try {
            User checkUserMobile = userRepository.fetchByMobileExact(aes.encryptText("AES", user.getMobile()));
            User checkUserEmail = userRepository.fetchByEmailExact(aes.encryptText("AES", user.getEmail()));

            // check if any required fields are not null
            if (user.getPassword() == null) throw new QuException("Password can't be null");
            else if (user.getEmail() == null) throw new QuException("Email can't be null");
            else if (user.getMobile() == null) throw new QuException("Name can't be null");
            else if (user.getDob() == null) throw new QuException("Date of birth can't be null");

            // check if email already exists
            if (checkUserEmail != null) {
                throw new QuException("Email already exists");
            }

            // check if mobile already exists
            if (checkUserMobile != null) {
                throw new QuException("Mobile already exists");
            }

            // check if user password is equal to email
            if (user.getEmail().equals(user.getPassword())) {
                throw new QuException("Password can't be same as email");
            }

            // check if mobile is less than 10
            if (user.getMobile().length() < 10) {
                throw new QuException("Mobile number invalid");
            }
            // throw error if password is less than 8
            if (user.getPassword().length() < 8) {
                throw new QuException("Password must be longer than 8 characters");
            }

            // throw error if age less than 16 and greater than 120
            int age = Period.between(user.getDob(), LocalDate.now()).getYears();
            if (age < 16 || age > 120) {
                throw new QuException("Please provide valid date of birth");
            }

            // If user uploads profile pic then
            if (profilePic != null) {

                // check upload type
                if (!profilePic.getContentType().contains("image")) {
                    throw new QuException("Please upload image type only");
                }

                // check size
                if (profilePic.getSize() >= 6291456) {
                    throw new QuException("Please upload less than 6mb profile pic");
                }

                // generate random string , attach current file extension
                String filename = UUID.randomUUID() + profilePic.getOriginalFilename().substring(profilePic.getOriginalFilename().lastIndexOf("."));

                // create profilePictures folder if not exists
                File file = new File(uploadDirectory);
                if (!file.exists()) {
                    file.mkdir();
                }

                // file copy
                Files.copy(profilePic.getInputStream(), Paths.get(filename));
                user.setProfilePic(filename);
            }

            /******************* Get or generate *****************/

//            Map<String, String> publicPrivateKeys = keysService.SaveGetRSAKeys();
//            PublicKey publicKey  = keysService.decodePublicKey(publicPrivateKeys.get("PUBLIC"));
//            PrivateKey privateKey  = keysService.decodePrivateKey(publicPrivateKeys.get("PRIVATE"));
//            user.setName(rsa.encryptText(user.getName(), publicKey));

            // save an encrypted email , encrypted password, encrypted mobile
            user.setRole("USER");
            user.setEmail(aes.encryptText("AES", user.getEmail()));
            user.setPassword(aes.encryptText("AES", passwordEncoder.encode(user.getPassword())));
            user.setMobile(aes.encryptText("AES", user.getMobile()));
            User RegisteredUser = userRepository.save(user);
            return this.setterForRegisterUserDTO(RegisteredUser);
        } catch (Exception e) {
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public LoginResponse loginUser(HttpServletRequest request, LoginRequest loginRequest) {
        try {
            // first check session exists or not
            // If not available create new session
            HttpSession sessionObj = request.getSession(false);

            if (sessionObj == null) {
                sessionObj = request.getSession(true);
            }

            // check if email or phone, password is not null
            if (loginRequest.getEmail() == null && loginRequest.getMobile() == null) {
                throw new QuException("Email or mobile can't be blank");
            }
            // check if user exists on given email or given mobile
            User user = (loginRequest.getEmail() != null) ?
                    userRepository.fetchByEmailExact(aes.encryptText("AES", loginRequest.getEmail())) :
                    userRepository.fetchByMobileExact(aes.encryptText("AES", loginRequest.getMobile())
                    );
            if (user == null) {
                throw new QuException("User doesn't exists");
            }

            // check given user password and database password matched
            String encodedPassword = aes.decryptText("AES", userRepository.fetchById(user.getId()).getPassword());
            boolean result = passwordEncoder.matches(loginRequest.getPassword(), encodedPassword);
            if (!result) {
                throw new QuException("Password doesn't matches");
            }

            //generate token for this user
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//            String jwtToken = sessionObj.getAttribute("jwtToken") == null ?
//                    aes.encryptText("AES", jwtUtil.generateToken(userDetails))
//                    :(String) sessionObj.getAttribute("jwtToken");
            String jwtToken = aes.encryptText("AES", jwtUtil.generateToken(userDetails));

            // save that token and other logged-in user details in session
            sessionObj.setAttribute("jwtToken", jwtToken);
            sessionObj.setAttribute("userId", user.getId());
            sessionObj.setAttribute("name", user.getName());
            sessionObj.setAttribute("email", user.getEmail());
            sessionObj.setAttribute("enabled", user.getEnabled());
            sessionObj.setAttribute("mobile", user.getMobile());
            sessionObj.setAttribute("role", user.getRole());

            // send that sessionobj to LoggedInUserDTO :)
            sessionService.saveForSession(request);

            return this.setterForLoginResponse(
                    user.getName(),
                    user.getEmail(),
                    jwtToken,
                    (String) sessionObj.getAttribute("name"),
                    Instant.now()
//                    Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())
            );

//            // first check session exists or not
//            // If not available create new session
////            HttpSession sessionObj = request.getSession(false);
//
//            if(sessionObj == null){
//                sessionObj = request.getSession(true);
//            }
//
//            // check if email or phone, password is not null
//            if(loginRequest.getEmail() == null && loginRequest.getMobile() == null){
//                throw new QuException("Email or mobile can't be blank");
//            }
//            // check if user exists on given email or given mobile
//            User user = (loginRequest.getEmail() != null) ?
//                    userRepository.fetchByEmailExact(aes.encryptText("AES", loginRequest.getEmail())):
//                    userRepository.fetchByMobileExact(aes.encryptText("AES",loginRequest.getMobile())
//            );
//            if(user == null){
//                throw new QuException("User doesn't exists");
//            }
//
//            // check given user password and database password matched
//            String encodedPassword = aes.decryptText("AES", userRepository.fetchById(user.getId()).getPassword());
//            boolean result = passwordEncoder.matches(loginRequest.getPassword(), encodedPassword);
//            if(!result){
//                throw new QuException("Password doesn't matches");
//            }
//
//            //generate token for this user
//            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//            String jwtToken = sessionObj.getAttribute("jwtToken") == null ?
//                    aes.encryptText("AES", jwtUtil.generateToken(userDetails))
//                    :(String) sessionObj.getAttribute("jwtToken");
//
//            // save that token and other logged-in user details in session
//            sessionObj.setAttribute("jwtToken", jwtToken);
//            sessionObj.setAttribute("userId", user.getId());
//            sessionObj.setAttribute("name", user.getName());
//            sessionObj.setAttribute("email", user.getEmail());
//            sessionObj.setAttribute("enabled", user.getEnabled());
//            sessionObj.setAttribute("mobile", user.getMobile());
//            sessionObj.setAttribute("role", user.getRole());
//
//            return this.setterForLoginResponse(
//                    user.getName(),
//                    user.getEmail(),
//                    jwtToken,
//                    (String) sessionObj.getAttribute("name"),
//                    Instant.now()
////                    Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())
//            );

        } catch (Exception e) {
            throw new QuException(e.getMessage());
        }
    }

    private RegisterResponse setterForRegisterUserDTO(User user) {
        RegisterResponse registerUserDTO = new RegisterResponse();
        registerUserDTO.setMessage(user.getName() + " registered successful.");
        return registerUserDTO;
    }

    private LoginResponse setterForLoginResponse(String name, String email, String token, String refreshToken, Instant expiresAt) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(name);
        loginResponse.setEmail(aes.decryptText("AES", email));
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresAt(expiresAt);
        return loginResponse;
    }

}