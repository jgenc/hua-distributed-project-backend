package gr.hua.dit.distributed.project_46.config;

import gr.hua.dit.distributed.project_46.entity.User;
import gr.hua.dit.distributed.project_46.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtUtils {
    @Autowired
    UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserTinFromJwtToken(HttpServletRequest request)
            throws ServletException, IOException {
        String tin="";
        try {
            String jwt = parseJwt(request);
            if (jwt != null && validateJwtToken(jwt)) {
                String userName=getUserNameFromJwtToken(jwt);
                User user = userRepository.findByUsername(userName)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userName));
                tin=user.getTin();
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        return tin;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}