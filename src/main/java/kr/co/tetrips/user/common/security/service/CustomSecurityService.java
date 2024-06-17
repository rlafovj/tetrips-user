package kr.co.tetrips.user.common.security.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.tetrips.user.common.security.domain.MessengerVO;
import org.springframework.lang.Nullable;

public interface CustomSecurityService {
  MessengerVO renew(HttpServletRequest request);
}
