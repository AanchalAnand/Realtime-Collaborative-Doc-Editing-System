package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;


public interface PermissionService {

  BaseResponse allowUserToView(String documentId, String username);

  BaseResponse allowUserToEdit(String documentId, String username, DocumentReqDto documentReqDto);

  BaseResponse withDrawViewPermission(String documentId);

  BaseResponse withdrawEditPermission(String documentId);

  BaseResponse requestForAccess(String documentId);

}