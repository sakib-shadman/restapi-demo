package com.example.restapi_service.authentication;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ApiAuthorizationFilter extends BasicAuthenticationFilter {


    private static String TOKEN_HEADER  = "Authorization";
    private static String TOKEN_PREFIX = "Bearer ";
    private static String SECRET = "javarestapi";

    public ApiAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
   /* public ApiAuthorizationFilter(AuthenticationManager authManager, CachedRoleInfoRepository roleInfoRepository) {
        super(authManager);
        this.roleInfoRepository = roleInfoRepository;
    }*/

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(TOKEN_HEADER);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null) {

            String user;
            List<String> role;
            try {
                user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();

                Jws<Claims> claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
                } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException | UnsupportedJwtException e) {
                return null;
            }

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, null);
            }
            return null;
        }
        return null;
    }

   /* private List<GrantedAuthority> getGrantedAuthorities(final List<String> roles) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            for (String role : roles){
                authorities.add(new SimpleGrantedAuthority(role));
                CachedRoleInfo roleInfo = roleInfoRepository.findOne(role);
                List<String> privileges = roleInfo.getPrivileges();
                for (String privilege : privileges) {
                    authorities.add(new SimpleGrantedAuthority(privilege));
                }
            }
        }

        return authorities;
    }*/

}