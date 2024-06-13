package com.tetrips.api.common.security.domain;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import java.io.Serializable;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleModel implements Serializable  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id ;
  @Enumerated(EnumType.STRING)
  RoleName roleName ;
  public RoleModel (RoleName roleName) {this.roleName = roleName;}
  public String getRoleName() {
    return roleName.toString();
  }
}