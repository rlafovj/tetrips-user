package com.tetrips.api.user;

import com.tetrips.api.common.security.domain.LoginDTO;
import com.tetrips.api.common.security.domain.MessengerVO;
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
//logout은 내부 상태가 변하므로 GetMapping이 아닌 PostMapping으로 변경 필요
    @GetMapping("/logout")
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
