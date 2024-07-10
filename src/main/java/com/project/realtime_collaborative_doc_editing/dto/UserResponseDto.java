package com.project.realtime_collaborative_doc_editing.dto;

import com.project.realtime_collaborative_doc_editing.model.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDto {

  private String username;
  private String firstName;
  private Date createdAt;
  private String email;
  private Role role;

}