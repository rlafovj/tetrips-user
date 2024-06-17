package kr.co.tetrips.user.common.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.tetrips.user.common.security.domain.MessengerVO;
import kr.co.tetrips.user.common.security.service.CustomSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityController {
  private final CustomSecurityService securityService;

  @PostMapping("/api/security/renew")
  public ResponseEntity<MessengerVO> renew(@Nullable HttpServletRequest request) {
    return ResponseEntity.ok(securityService.renew(request));
  }
}
