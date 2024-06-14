package kr.co.tetrips.user.common;


import kr.co.tetrips.user.user.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

@Slf4j
@Component
public class JwtProvider {
  private final String issuer;
  private final Long accessExpiredDate;
  private final Long refreshExpiredDate;
  private final SecretKey secretKey;

  public JwtProvider(
    @Value("${jwt.iss}") String issuer,
    @Value("${jwt.acc-exp}") Long accessExpiration,
    @Value("${jwt.ref-exp}") Long refreshExpiration,
    @Value("${jwt.secret}") String secret) {
    this.issuer = issuer;
    this.accessExpiredDate = accessExpiration;
    this.refreshExpiredDate = refreshExpiration;
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
  }

  public String createAccessToken(UserDTO user) {
    String token = Jwts.builder()
            .issuer(issuer)
            .signWith(secretKey)
            .expiration(Date.from(Instant.now().plus(accessExpiredDate, ChronoUnit.MILLIS)))
            .subject("access")
            .claim("userEmail", user.getEmail())
            .claim("userId", user.getId())
            .compact();
    log.info("발급된 엑세스토큰 : " + token);
    return token;
  }
  public String createRefreshToken(UserDTO user) {
    String token = Jwts.builder()
            .issuer(issuer)
            .signWith(secretKey)
            .expiration(Date.from(Instant.now().plus(refreshExpiredDate, ChronoUnit.MILLIS)))
            .subject("refresh")
            .claim("userEmail", user.getEmail())
            .claim("userId", user.getId())
            .compact();
    log.info("발급된 리프레쉬토큰 : " + token);
    return token;
  }

  public String extractTokenFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }else {return "undefined token";}
  }

  public void printPayload(String Token) {
    Base64.Decoder decoder = Base64.getDecoder();

    String[] chunk = Token.split("\\.");
    String payload = new String(decoder.decode(chunk[1]));
    String header = new String(decoder.decode(chunk[0]));

    log.info("Token Header : "+header);
    log.info("Token Payload : "+payload);

    //return payload;
  }

  public Claims getPayload(String token) {
//    Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
//            .parseSignedClaims(accessToken);
//    String IDstr = claimsJws.getPayload().getId();
//    log.info("Jwt 프로바이더 Access Token ID : "+IDstr);
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }

  public Long getRefreshExpired() {
    return Instant.now().plus(refreshExpiredDate, ChronoUnit.MILLIS).toEpochMilli();
  }

  public Boolean checkExpiration(String token){
    return Stream.of(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token))
            .filter(i -> i.getPayload().getExpiration().after(Date.from(Instant.now())))
            .map(i -> true)
            .findAny()
            .orElseGet(() -> false);
  }

  public String updateExpiration(String token){
    token = Stream.of(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token))
            .map(i -> Jwts.builder()
                    .expiration(Date.from(Instant.now().plus(accessExpiredDate, ChronoUnit.MILLIS)))
                    .compact())
                    .toString();
    return token;
  }


  public Boolean validateToken(String token, String subject) {
    try {
      return Stream.of(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token))
              .filter(i -> i.getPayload().getSubject().equals(subject))
              .filter(i -> i.getPayload().getIssuer().equals(issuer))
              .map(i -> true)
              .findAny()
              .orElseGet(() -> false);
//    }catch (SignatureException e) {
//      log.info("Invalid JWT signature.");
//      log.trace("Invalid JWT signature trace: {}", e);
//    } catch (MalformedJwtException e) {
//      log.info("Invalid JWT token.");
//      log.trace("Invalid JWT token trace: {}", e);
//    } catch (ExpiredJwtException e) {
//      log.info("Expired JWT token.");
//      log.trace("Expired JWT token trace: {}", e);
//    } catch (UnsupportedJwtException e) {
//      log.info("Unsupported JWT token.");
//      log.trace("Unsupported JWT token trace: {}", e);
//    } catch (IllegalArgumentException e) {
//      log.info("JWT token compact of handler are invalid.");
//      log.trace("JWT token compact of handler are invalid trace: {}", e);
    } catch (Exception e) {
      return false;
    }
  }
}
