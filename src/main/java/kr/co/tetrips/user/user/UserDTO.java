package kr.co.tetrips.user.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import kr.co.tetrips.user.token.Token;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
@Data
@Builder
@Log4j2
public class UserDTO {
  private Long id;
  private String email;
  private String password;
  private String nickname;
  private boolean gender;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate birthDate;
  private Token token;

  @QueryProjection
  public UserDTO(Long id, String email, String password, String nickname, boolean gender, LocalDate birthDate, Token token) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.gender = gender;
    this.birthDate = birthDate;
    this.token = token;
  }
}
