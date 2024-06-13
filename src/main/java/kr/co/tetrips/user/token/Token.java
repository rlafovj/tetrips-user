package com.tetrips.api.token;

import com.tetrips.api.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "TOKENS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString(exclude = {"id"})
@Builder
public class Token {
  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "REFRESH_TOKEN", nullable = false)
  private String refreshToken;

  @Column(name = "EXP_DATE", nullable = false)
  private Long expDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User userId;
}
