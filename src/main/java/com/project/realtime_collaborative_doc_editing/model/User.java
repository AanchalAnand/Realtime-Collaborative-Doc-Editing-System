package com.project.realtime_collaborative_doc_editing.model;

import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User implements UserDetails
{

  @Id
  private String id;

  private String firstName;

  @Column(unique = true)
  @NotNull(message = "Username cannot be null")
  private String username;

  @NotNull(message = "Password cannot be null")
  @Size(min = 6,max = 10)
  private String password;

  @Column(unique = true)
  @NotNull(message = "Email cannot be null")
  //@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[a-z]{2,3}$", message = "Email should have a valid domain (.com, .in, etc.)")
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername()
  {
    return email;
  }


  @Override
  public boolean isAccountNonExpired()
  {
    return true;
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean isEnabled()
  {
    return true;
  }

}