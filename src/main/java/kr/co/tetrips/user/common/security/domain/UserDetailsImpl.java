package kr.co.tetrips.user.common.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.tetrips.user.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private final Long id;
  private final String email;
  @JsonIgnore private final String password;
  private final String nickname;
  private final boolean gender;
  private final LocalDate birthDate;
  private final Collection<? extends GrantedAuthority> getAuthorities;

  public static UserDetailsImpl build(User user){
    return null;
//    return new UserDetailsImpl(
//      user.getId(),
//      user.getEmail(),
//      user.getPassword(),
//      user.getNickname(),
//      user.isGender(),
//      user.getBirthDate(),
//      //getAuthorities
//    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }
}
