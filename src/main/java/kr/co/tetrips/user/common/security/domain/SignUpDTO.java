package com.tetrips.api.common.security.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpDTO implements Serializable {
  String email;
  String password;
  String nickname;
  boolean gender;
  LocalDate birthDate;
}

