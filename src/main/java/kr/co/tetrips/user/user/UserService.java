package com.tetrips.api.user;

import com.tetrips.api.common.security.domain.LoginDTO;
import com.tetrips.api.common.security.domain.MessengerVO;

public interface UserService {

  default UserDTO entityToDTO(User user) {
    return UserDTO.builder()
      .id(user.getId())
      .email(user.getEmail())
      .password(user.getPassword())
      .nickname(user.getNickname())
      .gender(user.isGender())
      .birthDate(user.getBirthDate())
      .token(user.getToken())
      .build();
  }
  default User dtoToEntity(UserDTO userDTO) {
    return User.builder()
            .id(userDTO.getId())
            .email(userDTO.getEmail())
            .password(userDTO.getPassword())
            .nickname(userDTO.getNickname())
            .gender(userDTO.isGender())
            .birthDate(userDTO.getBirthDate())
            .build();
  }
  MessengerVO signup(UserDTO param);
  MessengerVO login(LoginDTO param);
  MessengerVO logout(String token);
  User deleteToken(User user);


  MessengerVO existsEmail(String email);
}
