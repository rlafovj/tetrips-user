package kr.co.tetrips.user.user;

import kr.co.tetrips.user.common.security.domain.LoginDTO;
import kr.co.tetrips.user.common.security.domain.MessengerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessengerVO> signup(@RequestBody UserDTO param) {
        log.info("signup: {}", param);
        return ResponseEntity.ok(userService.signup(param));
    }

    @PostMapping("/login")
    public ResponseEntity<MessengerVO> login(@RequestBody LoginDTO param) {
        log.info("login: {}", param);
        return ResponseEntity.ok(userService.login(param));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessengerVO> logout(@RequestHeader("Authorization") String token) {
        log.info("logout: {}", token);
        return ResponseEntity.ok(userService.logout(token));
    }
    @GetMapping("/exists-email")
    public ResponseEntity<MessengerVO> existsEmail(@RequestParam String email) {
        log.info("existsEmail: {}", email);
        return ResponseEntity.ok(userService.existsEmail(email));
    }

}
