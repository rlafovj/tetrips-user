package kr.co.tetrips.user.common.security.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.tetrips.user.common.JwtProvider;
import kr.co.tetrips.user.common.security.domain.MessengerVO;
import kr.co.tetrips.user.token.TokenRepository;
import kr.co.tetrips.user.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomSecurityServiceImpl implements CustomSecurityService{
  private final JwtProvider jwtProvider;
  private final TokenRepository tokenRepository;
  private final UserRepository userRepository;
  @Override
  public MessengerVO renew(HttpServletRequest request) {
    String token = jwtProvider.extractTokenFromHeader(request);
    String subject = "refresh";
    if (token != null && jwtProvider.validateToken(token, subject)) {
      String email = jwtProvider.getPayload(token).get("userEmail", String.class);
      String refreshtoken = tokenRepository.getRefreshTokenByUserId_Id(userRepository.getUserIdByEmail(email)).getRefreshToken();
      if (jwtProvider.checkExpiration(refreshtoken)) {
        return MessengerVO.builder()
                .message("SUCCESS")
                .accessToken(jwtProvider.updateAccessToken(token))
                .status(200)
                .build();
      } else {
        //로그아웃 로직 실행
        return MessengerVO.builder()
                .message("Refresh token expired. Please log in again.")
                .status(401)
                .build();
      }
    } else {
      return MessengerVO.builder()
              .message("Token is invalid.")
              .status(401)
              .build();
    }
  }
}
