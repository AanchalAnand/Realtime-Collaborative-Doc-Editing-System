package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.dto.UserResponseDto;
import com.project.realtime_collaborative_doc_editing.exceptions.documentException.InvalidCredentials;
import com.project.realtime_collaborative_doc_editing.exceptions.documentException.UserNotFoundException;
import com.project.realtime_collaborative_doc_editing.model.User;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService
{
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public BaseResponse registerUser(UserDto userDto)
  {
    BaseResponse baseResponse = new BaseResponse();
    String email = userDto.getEmail();
    String username = userDto.getUsername();
    Optional<User> userOpt = userRepository.findByEmail(email);
    if(Objects.isNull(userOpt)){
      throw new UserNotFoundException("User with email "+userDto.getEmail()+" is already exists",HttpStatus.NOT_FOUND);
    }
    Optional<User> userOpt1 = userRepository.findByUsername(username);

    if(userOpt1.isPresent())
    {
      throw new UserNotFoundException("User already exists with username : "+userDto.getUsername(),HttpStatus.BAD_REQUEST);
    }

    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setRole(userDto.getRole());
    userRepository.save(user);

    String jwtToken = jwtService.generateToken(user);
    //return AuthenticationResponse.builder().accessToken(jwtToken).build();

    UserResponseDto userResponseDto = userToUserResponseDto(userDto);
    baseResponse.setMessage("Registration Successful");
    baseResponse.setSuccess(true);
    baseResponse.setStatusCode(HttpStatus.CREATED.toString());
    baseResponse.setPayload(userResponseDto);

    return baseResponse;
  }


  @Override
  public ResponseEntity<String> loginUser(LoginDto loginDto) {
    Optional<User> userOpt = userRepository.findByEmail(loginDto.getEmail());
    if (Objects.isNull(userOpt)) {
      throw new RuntimeException("User not found");
    }
    User user = userOpt.get();
    if (!user.getPassword().equals(loginDto.getPassword())) {
      throw new InvalidCredentials("Invalid Credentials",HttpStatus.FORBIDDEN);
    }
    String jwtToken = jwtService.generateToken(user);
    return new ResponseEntity<>("accessToken : "+jwtToken, HttpStatus.OK);
  }

  private UserResponseDto userToUserResponseDto(UserDto userDto){
    UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setRole(userDto.getRole());
    userResponseDto.setEmail(userDto.getEmail());
    userResponseDto.setUsername(userDto.getUsername());
    userResponseDto.setFirstName(userDto.getFirstName());
    userResponseDto.setCreatedAt(new Date());
    return userResponseDto;
  }

}