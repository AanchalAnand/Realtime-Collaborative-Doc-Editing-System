package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import com.project.realtime_collaborative_doc_editing.model.HistoryDetails;
import com.project.realtime_collaborative_doc_editing.repository.DocumentRepository;
import com.project.realtime_collaborative_doc_editing.service.HistoryTracking;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoryTrackingImpl implements HistoryTracking {

  private final DocumentRepository documentRepository;

  private final HttpServletRequest httpServletRequest;

  private final JwtService jwtService;

  @Override
  public BaseResponse checkHistory(String documentId) {
    BaseResponse baseResponse = new BaseResponse();
    Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
    if(documentDetailsOpt.isEmpty()){
      throw new RuntimeException("Document Not found.");
    }
    String accessToken = httpServletRequest.getHeader("Authorization").substring(7);
    String username = jwtService.extractUsername(accessToken);
    DocumentDetails documentDetails = documentDetailsOpt.get();
    Set<String> allowedUsersToEdit = documentDetails.getUsersCanEdit();
    Set<String> allowedUsersToView = documentDetails.getUsersCanEdit();
    if(allowedUsersToEdit.contains(username) ||allowedUsersToView.contains(username)){
      List<HistoryDetails> history = documentDetails.getHistoryDetails();
      baseResponse.setPayload(history);
      baseResponse.setSuccess(true);
      baseResponse.setMessage("History has been fetched");
      if(Objects.isNull(history)){
        baseResponse.setMessage("No History found for : "+documentDetails.getDocumentTitle());
      }
      baseResponse.setStatusCode(HttpStatus.OK.toString());
      return baseResponse;
    }

    baseResponse.setMessage("History Can't be fetch, please try again.");
    baseResponse.setSuccess(false);
    baseResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
    return baseResponse;
  }
}