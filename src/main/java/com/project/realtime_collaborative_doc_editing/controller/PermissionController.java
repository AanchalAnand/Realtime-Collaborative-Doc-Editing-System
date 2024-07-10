package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.service.impl.PermissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/permission")
public class PermissionController {

  private final PermissionServiceImpl permissionService;

  @PutMapping("allow-to-view/{id}/{username}")
  public ResponseEntity<BaseResponse> allowUserToView(@PathVariable("id") String documentId, @PathVariable("username") String username){
    BaseResponse baseResponse = permissionService.allowUserToView(documentId,username);
    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
  }

  @PutMapping("allow-to-edit/{id}/{username}")
  public ResponseEntity<BaseResponse> allowUserToEdit(@PathVariable("id") String documentId, @PathVariable("username") String username,
      @RequestBody DocumentReqDto documentReqDto){
    BaseResponse baseResponse = permissionService.allowUserToEdit(documentId,username,documentReqDto);
    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
  }

  @PutMapping("/withdraw-view-access/{documentId}")
  public ResponseEntity<BaseResponse> withDrawViewPermission(String documentId){
    BaseResponse baseResponse = permissionService.withDrawViewPermission(documentId);
    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
  }

  @PutMapping("/withdraw-edit-access/{documentId}")
  public ResponseEntity<BaseResponse> withdrawEditPermission(String documentId){
    BaseResponse baseResponse = permissionService.withdrawEditPermission(documentId);
    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
  }

  @PutMapping("access-request/{documentId}")
  public ResponseEntity<BaseResponse> requestForAccess(@PathVariable("documentId") String documentId){
    BaseResponse baseResponse = permissionService.requestForAccess(documentId);
    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
  }

}