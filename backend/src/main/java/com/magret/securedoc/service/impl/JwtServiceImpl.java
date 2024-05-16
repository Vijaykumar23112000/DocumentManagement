package com.magret.securedoc.service.impl;

import com.magret.securedoc.Function.TriConsumer;
import com.magret.securedoc.domain.Token;
import com.magret.securedoc.domain.TokenData;
import com.magret.securedoc.dto.User;
import com.magret.securedoc.enumeration.TokenType;
import com.magret.securedoc.security.JwtConfiguration;
import com.magret.securedoc.service.JwtService;
import com.magret.securedoc.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.magret.securedoc.constant.Constants.*;
import static com.magret.securedoc.enumeration.TokenType.ACCESS;
import static com.magret.securedoc.enumeration.TokenType.REFRESH;
import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static org.apache.tomcat.util.http.SameSiteCookies.NONE;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfiguration implements JwtService {

    private final UserService userService;

    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {
        var token = Token
                        .builder()
                        .access(buildToken.apply(user , ACCESS))
                        .refresh(buildToken.apply(user , REFRESH))
                        .build();
        return tokenFunction.apply(token);
    }

    @Override
    public Optional<String> extractToken(HttpServletRequest request, String cookieName) {
        return extractToken.apply(request , cookieName);
    }

    @Override
    public void addCookie(HttpServletResponse response, User user, TokenType type) {
        addCookie.accept(response , user , type);
    }

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenFunction) {
        return null;
    }


    private final BiFunction<HttpServletRequest , String , Optional<Cookie>> extractCookie = (request , cookieName) ->
            Optional.of(
                    Arrays.stream(request.getCookies() == null ?
                                        new Cookie[]{new Cookie(EMPTY_VALUE , EMPTY_VALUE)} :
                                        request.getCookies())
                            .filter(cookie -> Objects.equals(cookieName , cookie.getName()))
                            .findAny()
                    ).orElse(Optional.empty());

    private final BiFunction<HttpServletRequest , String , Optional<String>> extractToken = (request , cookieName) ->
            Optional.of(
                            Arrays.stream(request.getCookies() == null ?
                                        new Cookie[]{new Cookie(EMPTY_VALUE , EMPTY_VALUE)} :
                                        request.getCookies())
                                .filter(cookie -> Objects.equals(cookieName , cookie.getName()))
                                .map(Cookie::getValue)
                                .findAny()
                    )
                    .orElse(Optional.empty());
    private final Supplier<SecretKey> key = () -> Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(getSecret()));

    private final Supplier<JwtBuilder> builder = () ->
            Jwts
                    .builder()
                    .header().add(Map.of(TYPE , JWT_TYPE))
                    .and()
                    .audience().add(DOC_MANAGEMENT_APPLICATION)
                    .and()
                    .id(UUID.randomUUID().toString())
                    .issuedAt(Date.from(Instant.now()))
                    .notBefore(new Date())
                    .signWith(key.get() , Jwts.SIG.HS512);


    private final BiFunction<User , TokenType , String> buildToken = (user , type) ->
            Objects.equals(type , ACCESS) ?
                    builder.get()
                        .subject(user.getUserId())
                        .claim(AUTHORITIES , user.getAuthorities())
                        .claim(ROLE,user.getRole())
                        .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
                        .compact() :
                    builder.get()
                            .subject(user.getUserId())
                            .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
                            .compact();



    private final Function<String , Claims> claimsFunction = token ->
                Jwts
                    .parser()
                    .verifyWith(key.get())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();


    private final TriConsumer<HttpServletResponse , User , TokenType> addCookie = (response , user , type) -> {
      switch (type){
          case ACCESS -> {
              var accessToken = createToken(user , Token::getAccess);
              var cookie = new Cookie(type.getValue() , accessToken);
              cookie.setHttpOnly(true);
              /*cookie.setSecure(true);*/
              cookie.setMaxAge(2*60);
              cookie.setPath("/");
              cookie.setAttribute("SameSite" , NONE.name());
              response.addCookie(cookie);
          }
          case REFRESH -> {
              var refreshToken = createToken(user , Token::getRefresh);
              var cookie = new Cookie(type.getValue() , refreshToken);
              cookie.setHttpOnly(true);
              /*cookie.setSecure(true);*/
              cookie.setMaxAge(2*60*60);
              cookie.setPath("/");
              cookie.setAttribute("SameSite" , NONE.name());
              response.addCookie(cookie);
          }
          default -> {}
      }
    };


    private final Function<String , String> subject = token -> getClaimsValue(token , Claims::getSubject);

    private <T> T getClaimsValue(String token , Function<Claims , T> claims){
        return claimsFunction.andThen(claims).apply(token);
    }

    public Function<String , List<GrantedAuthority>> authorities = token -> AuthorityUtils
                    .commaSeparatedStringToAuthorityList(new StringJoiner(AUTHORITY_DELIMITER)
                            .add(claimsFunction.apply(token).get(AUTHORITIES , String.class))
                            .add(ROLE_PREFIX + claimsFunction.apply(token).get(ROLE , String.class)).toString());

}
