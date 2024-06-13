package com.tetrips.api.user;

import com.tetrips.api.common.security.domain.RoleModel;
import com.tetrips.api.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"id"})
public class User implements Serializable, UserDetails {
  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "NICKNAME")
  private String nickname;

  @Column(name = "GENDER", nullable = true)
  private boolean gender;

  @Column(name = "BIRTH_DATE", nullable = true)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @Setter
  @OneToOne(mappedBy = "userId", fetch = FetchType.LAZY)
  private Token token;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  List<RoleModel> roles;
  public User(String email, String password, List<RoleModel> roles){
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

  @Builder(builderMethodName = "builder")
  public User(Long id, String email, String password, String nickname, boolean gender, LocalDate birthDate){
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.gender = gender;
    this.birthDate = birthDate;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
    return authorities;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
