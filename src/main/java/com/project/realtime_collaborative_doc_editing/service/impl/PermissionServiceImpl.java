package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import com.project.realtime_collaborative_doc_editing.repository.DocumentRepository;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.PermissionService;
import com.project.realtime_collaborative_doc_editing.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

  private final DocumentRepository documentRepository;

  private final UserRepository userRepository;

  private final HttpServletRequest httpServletRequest;

  private final JwtService jwtService;

  private final EmailUtils emailUtils;

  @Override
  public BaseResponse allowUserToView(String documentId, String username) {
    BaseResponse baseResponse = new BaseResponse();

    Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
    if (documentDetailsOpt.isEmpty()) {
      throw new RuntimeException("Required document not found.");
    }
    DocumentDetails documentDetails = documentDetailsOpt.get();

    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);
    String currentLoggedInUser = jwtService.extractUsername(tokenWithOutBearer);
    String documentOwner = documentDetails.getDocumentCreatedBy();
    if (Objects.equals(currentLoggedInUser, documentOwner)) {
      Set<String> allowedUsersToView = documentDetails.getUsersCanView();
      Set<String> approvingFor = documentDetails.getRequestForView();
      for(String allowing : approvingFor){
        allowedUsersToView.add(allowing);
        approvingFor.remove(allowing);
      }
      if(approvingFor.isEmpty()){
        documentDetails.setIsRequestPending(false);
      }
      allowedUsersToView.add(username);
      documentDetails.setUsersCanView(allowedUsersToView);
      documentRepository.save(documentDetails);

      baseResponse.setMessage("View Access Given Successfully");
      baseResponse.setStatusCode(HttpStatus.OK.toString());
      baseResponse.setSuccess(true);
      return baseResponse;
    }
    baseResponse.setMessage("You are not allowed to grant this permission");
    baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
    baseResponse.setSuccess(false);
    return baseResponse;
  }

  @Override
  public BaseResponse allowUserToEdit(String documentId, String username, DocumentReqDto documentReqDto) {
    BaseResponse baseResponse = new BaseResponse();

    Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
    if (documentDetailsOpt.isEmpty()) {
      throw new RuntimeException("Required document not found.");
    }
    DocumentDetails documentDetails = documentDetailsOpt.get();

    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);
    String currentLoggedInUser = jwtService.extractUsername(tokenWithOutBearer);
    String documentOwner = documentDetails.getDocumentCreatedBy();
    if (Objects.equals(currentLoggedInUser, documentOwner)) {
      Set<String> allowedUsersToView = documentDetails.getUsersCanView();
      Set<String> allowedUsersToEdit = documentDetails.getUsersCanView();
      Set<String> approvingFor = documentDetails.getRequestForView();
      for(String allowing : approvingFor){
        allowedUsersToView.add(allowing);
        allowedUsersToEdit.add(allowing);
        approvingFor.remove(allowing);
      }
      if(approvingFor.isEmpty()){
        documentDetails.setIsRequestPending(false);
      }
      allowedUsersToView.add(username);
      documentDetails.setUsersCanView(allowedUsersToView);
      documentDetails.setUsersCanEdit(allowedUsersToEdit);
      documentRepository.save(documentDetails);

      baseResponse.setMessage("View Access Given Successfully");
      baseResponse.setStatusCode(HttpStatus.OK.toString());
      baseResponse.setSuccess(true);
      return baseResponse;
    }
    baseResponse.setMessage("You are not allowed to grant this permission");
    baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
    baseResponse.setSuccess(false);
    return baseResponse;
  }

  @Override
  public BaseResponse withDrawViewPermission(String documentId) {
    return null;
  }

  @Override
  public BaseResponse withdrawEditPermission(String documentId) {
    return null;
  }


  @Override
  public BaseResponse requestForAccess(String documentId) {
    BaseResponse baseResponse = new BaseResponse();

    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);
    String requestedBy = jwtService.extractUsername(tokenWithOutBearer);
    Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
    if(documentDetailsOpt.isEmpty()){
      throw new RuntimeException("Document Not found.");
    }

    DocumentDetails documentDetails = documentDetailsOpt.get();
    Set<String> requestByUsers = new HashSet<>();
    requestByUsers.add(requestedBy);
    documentDetails.setRequestForView(requestByUsers);

    documentDetails.setIsRequestPending(true);
    documentRepository.save(documentDetails);
    String owner = documentDetails.getDocumentCreatedBy();
    String subject = "Requesting view of "+documentDetails.getDocumentTitle()+" document.";
    String body = "kuchh bhi body me daal do";
    emailUtils.sendMail(owner,subject,body);

    baseResponse.setSuccess(true);
    baseResponse.setMessage("Request Sent Successfully.");
    baseResponse.setStatusCode(HttpStatus.OK.toString());
    return baseResponse;
  }
}