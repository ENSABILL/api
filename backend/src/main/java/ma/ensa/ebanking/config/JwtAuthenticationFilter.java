package ma.ensa.ebanking.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensa.ebanking.services.utils.JwtService;
import ma.ensa.ebanking.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException
    {

        final String header, jwt, username;

        header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header == null){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = header.substring(7);

        username = jwtService.extractUsername(jwt);

        UserDetails userDetails = userService.loadUserByUsername(username);

        if(jwtService.isTokenValid(jwt, userDetails)){
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            // set the authentication into the `SecurityContextHolder`
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);

    }

}