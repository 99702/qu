package com.qu.app.filter;

import com.qu.app.error.QuException;
import com.qu.app.utils.AES;
import com.qu.app.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AES aes;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // get jwt(encrypted)  from header and decrypt
                jwt = aes.decryptText("AES", authorizationHeader.substring(7));
                username = jwtUtil.extractUsername(jwt);
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(jwt)
                        .getBody();

//                // if user is  not enabled throw exception
//                if(!(Boolean) claims.get("enabled")){
//                    throw new QuException("User not enabled");
//                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // get userDetails from loadUserByUsername given Username as email
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // validate if token is valid or else throw an error
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new QuException("Token is invalid.");
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new QuException(e.getMessage());
        }
    }


//    private JwtData generateJwtData(LinkedHashMap<String, Object> jwtHashMapper) {
//        JwtData jwtData = new JwtData();
//        jwtData.setName((String) jwtHashMapper.get("name"));
//        jwtData.setId((Long) jwtHashMapper.get("id"));
//        jwtData.setEnabled((Boolean) jwtHashMapper.get("enabled"));
//        return jwtData;
//    }
}
