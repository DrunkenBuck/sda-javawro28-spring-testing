package pl.sda.springboottraining.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserAccount userAccount = new ObjectMapper().readValue(request.getInputStream(), UserAccount.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userAccount.getUsername(),
                            userAccount.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        List<String> authorities = new ArrayList<>();
        user.getAuthorities().forEach(authority -> {
            authorities.add(authority.getAuthority());
        });
//        ((User) authResult.getPrincipal()).getAuthorities().stream().map(GrantedAuthority::getAuthority).forEach(authorities::add);

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("Role", authorities.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 86_400_000)) // token is valid for 24h
                .sign(Algorithm.HMAC512("XYZ"));
            response.addHeader("Authorization", "Bearer " + token);

    }
}
