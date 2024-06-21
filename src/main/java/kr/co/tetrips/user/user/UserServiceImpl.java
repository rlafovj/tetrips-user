package kr.co.tetrips.user.user;

import kr.co.tetrips.user.common.JwtProvider;
import kr.co.tetrips.user.common.config.SecurityConfig;
import kr.co.tetrips.user.common.security.domain.LoginDTO;
import kr.co.tetrips.user.common.security.domain.MessengerVO;
import kr.co.tetrips.user.token.TokenRepository;
import kr.co.tetrips.user.token.Token;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final SecurityConfig securityConfig;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public MessengerVO signup(UserDTO param) {
        return Stream.of(param)
                .filter(i -> !userRepository.existsByEmail(i.getEmail()))
                .map(i -> userRepository.save(User.builder()
                        .email(i.getEmail())
                        .password(securityConfig.passwordEncoder().encode(i.getPassword()))
                        .nickname(i.getNickname())
                        .gender(i.isGender())
                        .birthDate(i.getBirthDate())
                        .build()))
                .map(i -> MessengerVO.builder()
                        .message("SUCCESS")
                        .status(200)
                        .build())
                .findAny()
                .orElseGet(() -> MessengerVO.builder()
                        .message("FAIL")
                        .status(409)//duplicate email
                        .build());
    }

    @Override
    public MessengerVO login(LoginDTO param) {
        try {
            User user = userRepository.findUserByEmail(param.getEmail()).orElseGet(() -> User.builder().build());
            boolean flag = securityConfig.passwordEncoder().matches(param.getPassword(), user.getPassword());
            String accessToken = null;
            String refreshToken = null;
            if (flag) {
                accessToken = jwtProvider.createAccessToken(entityToDTO(user));
                refreshToken = jwtProvider.createRefreshToken(entityToDTO(user));

                jwtProvider.printPayload(accessToken);
                jwtProvider.printPayload(refreshToken);

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, param.getPassword(), userDetails.getAuthorities());
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                securityContext.setAuthentication(authentication);
                if(authentication.isAuthenticated()){
                    log.info("@@@@@@@@@@@@인증된유저임@@@@@@@@@@@@User is authenticated@@@@@@@@@@@@@@");
                }
                SecurityContextHolder.setContext(securityContext);
                Token token = Token.builder()
                        .userId(user)
                        .expDate(jwtProvider.getRefreshExpired())
                        .refreshToken(refreshToken)
                        .build();
                tokenRepository.save(token);
            }

            return MessengerVO.builder()
                    .message(flag ? "SUCCESS" : "FAIL")
                    .accessToken(flag ? accessToken : null)
                    .refreshToken(flag ? refreshToken : null)
                    .status(flag ? 200 : 401)
                    .build();
        }catch (Exception e) {
            log.info("로그인 실패 : " + e.getMessage());
            return MessengerVO.builder()
                    .message("FAIL")
                    .status(500)
                    .build();
        }
    }

    @Override
    public MessengerVO logout(String token) {
        try {
            return Stream.of(token)
                    .map(i -> i.substring(7))
                    .map(i -> jwtProvider.getPayload(i).get("userId", Long.class))
                    .map(i -> userRepository.findById(i).orElseGet(User::new).getToken().getId())
                    .filter(i -> tokenRepository.findById(i).isPresent())
                    .map(i -> tokenRepository.findById(i))
                    .peek(i -> tokenRepository.deleteById(i.get().getId()))
                    .map(i -> MessengerVO.builder()
                            .message("SUCCESS")
                            .status(200)
                            .build())
                    .findAny()
                    .orElseGet(() -> MessengerVO.builder()
                            .message("FAIL")
                            .status(401)
                            .build());
        }catch (Exception e) {
            log.info("로그아웃 실패 : " + e.getMessage());
            return MessengerVO.builder()
                    .message("FAIL")
                    .status(500)//invalid token
                    .build();
        }
    }

    @Override
    @Transactional
    public User deleteToken(User user) {
        return Stream.of(user)
                .filter(i -> i.getToken() != null)
                .peek(i -> tokenRepository.deleteById(i.getToken().getId()))
                .peek(i -> i.setToken(null))
                .map(i -> userRepository.save(i))
                .findFirst()
                .get();
    }

    @Override
    public MessengerVO existsEmail(String email) {
        return userRepository.existsByEmail(email) ?
                MessengerVO.builder()
                        .message("SUCCESS")
                        .status(200)
                        .build() :
                MessengerVO.builder()
                        .message("FAIL")
                        .status(409)//duplicate email
                        .build();
    }
}
