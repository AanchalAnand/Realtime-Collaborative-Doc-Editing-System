package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.exceptions.documentException.PermissionNotGrantedException;
import com.project.realtime_collaborative_doc_editing.exceptions.documentException.UserNotFoundException;
import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import com.project.realtime_collaborative_doc_editing.model.HistoryDetails;
import com.project.realtime_collaborative_doc_editing.model.User;
import com.project.realtime_collaborative_doc_editing.repository.DocumentRepository;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.util.ObjectUtils;

@Data
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService
{

  private final JwtService jwtService;

  private final UserRepository userRepository;

  private final HttpServletRequest httpServletRequest;

  private final DocumentRepository documentRepository;

  @Override
  public BaseResponse createNewDocument(DocumentReqDto documentReqDto) {
    BaseResponse baseResponse = new BaseResponse();

    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);

    String userName = jwtService.extractUsername(tokenWithOutBearer);
    Optional<User> userOpt = userRepository.findByEmail(userName);
    if(Objects.isNull(userOpt)){
      throw new UserNotFoundException("User Not found.",HttpStatus.NOT_FOUND);
    }
    User user = userOpt.get();
    Set<String> userCanEdit = new HashSet<>();
    Set<String> userCanView = new HashSet<>();
    boolean isTokenValid = jwtService.isTokenValid(tokenWithOutBearer,user);
    if(isTokenValid){
      DocumentDetails documentDetails = documentDtoToDocument(documentReqDto,userName);
      userCanEdit.add(userName);
      userCanView.add(userName);
      documentDetails.setUsersCanEdit(userCanEdit);
      documentDetails.setUsersCanView(userCanView);

      HistoryDetails historyDetails = new HistoryDetails();
      historyDetails.setOperationType("CREATED");
      historyDetails.setUpdatedAt(new Date());
      historyDetails.setUpdatedBy(userName);

      List<HistoryDetails> history = new ArrayList<>();
      history.add(historyDetails);
      documentDetails.setHistoryDetails(history);

      documentRepository.save(documentDetails);
      baseResponse.setPayload(documentDetails);
      baseResponse.setSuccess(true);
      baseResponse.setMessage("New Document Created.");
      baseResponse.setStatusCode(HttpStatus.CREATED.toString());
      return baseResponse;
    }

    baseResponse.setSuccess(false);
    baseResponse.setMessage("Document Creation failed, It seems like you have permission for this operation");
    baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
    return baseResponse;
  }

  @Override
  public BaseResponse getDocumentById(String documentId) {
    BaseResponse baseResponse = new BaseResponse();
    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);

    String userName = jwtService.extractUsername(tokenWithOutBearer);
    Optional<User> userOpt = userRepository.findByEmail(userName);
    if(userOpt.isEmpty()){
      throw new UserNotFoundException("User not found with username : "+userName,HttpStatus.NOT_FOUND);
    }
    User user = userOpt.get();
    String emailId = user.getEmail();
    Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
    if(documentDetailsOpt.isEmpty()){
      throw new RuntimeException("Document not found.");
    }
    DocumentDetails documentDetails = documentDetailsOpt.get();
    if(!documentDetails.getUsersCanView().contains(emailId)){
      throw new PermissionNotGrantedException("You do not have the permission to view this document.",HttpStatus.FORBIDDEN);
    }
    baseResponse.setMessage("Document has been fetch.");
    baseResponse.setSuccess(true);
    baseResponse.setPayload(documentDetails);
    baseResponse.setStatusCode(HttpStatus.OK.toString());

    return baseResponse;
  }

  @Override
  public BaseResponse getDocumentByDocumentTitle(String documentName) {
    BaseResponse baseResponse = new BaseResponse();
    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);

    String userName = jwtService.extractUsername(tokenWithOutBearer);
    Optional<User> userOpt = userRepository.findByEmail(userName);
    if(userOpt.isEmpty()){
      throw new UserNotFoundException("User not found with username : "+userName,HttpStatus.NOT_FOUND);
    }
    User user = userOpt.get();
    String emailId = user.getEmail();
    Optional<DocumentDetails> documentDetailsOpt = documentRepository.findByDocumentTitle(documentName);
    if(documentDetailsOpt.isEmpty()){
      throw new RuntimeException("Document not found.");
    }
    DocumentDetails documentDetails = documentDetailsOpt.get();
    if(!documentDetails.getUsersCanView().contains(emailId)){
      throw new PermissionNotGrantedException("You do not have the permission to view this document.",HttpStatus.FORBIDDEN);
    }
    baseResponse.setMessage("Document has been fetch.");
    baseResponse.setSuccess(true);
    baseResponse.setPayload(documentDetails);
    baseResponse.setStatusCode(HttpStatus.OK.toString());

    return baseResponse;
  }

  @Override
  public BaseResponse editDocument(DocumentReqDto documentReqDto, String id) {
    System.out.print("Edit doc");
    String token = httpServletRequest.getHeader("Authorization");
    String accessToken = token.substring(7);

    String emailId = jwtService.extractUsername(accessToken);
    Optional<User> userOptional = userRepository.findByEmail(emailId);

    if(ObjectUtils.isEmpty(userOptional)){
      throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
    }

    Optional<DocumentDetails> documentOptional = documentRepository.findById(id);
    if(ObjectUtils.isEmpty(documentOptional)){
      throw new RuntimeException("Document not found");
    }

    if(!documentOptional.get().getUsersCanEdit().contains(emailId)){
      throw new PermissionNotGrantedException("You do not have permission to edit this document", HttpStatus.FORBIDDEN);
    }

    DocumentDetails document = documentOptional.get();
    document.setDocumentTitle(documentReqDto.getDocumentTitle());
    document.setDocumentDescription(documentReqDto.getDocumentDescription());
    document.setLastEditedAt(new Date());
    document.setLastEditedBy(emailId);

    HistoryDetails history = new HistoryDetails();
    history.setUpdatedAt(new Date());
    history.setUpdatedBy(emailId);
    history.setOperationType("UPDATED");

    List<HistoryDetails> historyList = document.getHistoryDetails();
    historyList.add(history);

    document.setHistoryDetails(historyList);
    documentRepository.save(document);

    BaseResponse baseResponse = new BaseResponse();
    baseResponse.setPayload(document);
    baseResponse.setSuccess(true);
    baseResponse.setMessage("Document updated successfully");
    baseResponse.setStatusCode(HttpStatus.OK.toString());
    return baseResponse;
  }

  @Override
  public BaseResponse deleteDocument(String documentId) {
    String token = httpServletRequest.getHeader("Authorization");
    String accessToken = token.substring(7);

    String email = jwtService.extractUsername(accessToken);
    Optional<User> userOptional = userRepository.findByEmail(email);
    if(ObjectUtils.isEmpty(userOptional)){
      throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
    }

    Optional<DocumentDetails> documentOptional = documentRepository.findById(documentId);
    if(ObjectUtils.isEmpty(documentOptional)){
      throw new RuntimeException("Document not found");
    }

    if(!documentOptional.get().getDocumentCreatedBy().contains(email)){
      throw new PermissionNotGrantedException("You do not have permission to delete this document", HttpStatus.FORBIDDEN);
    }

    BaseResponse baseResponse = new BaseResponse();
    baseResponse.setSuccess(true);
    baseResponse.setMessage("Document deleted successfully");
    baseResponse.setStatusCode(HttpStatus.OK.toString());
    baseResponse.setPayload(documentOptional.get().getDocumentTitle());

    documentRepository.delete(documentOptional.get());

    return baseResponse;
  }

  @Override
  public BaseResponse searchDocumentsByKeyword(String keyword) {
    BaseResponse baseResponse = new BaseResponse();
    String accessToken = httpServletRequest.getHeader("Authorization");
    String tokenWithOutBearer = accessToken.substring(7);

    String userName = jwtService.extractUsername(tokenWithOutBearer);
    Optional<User> userOpt = userRepository.findByEmail(userName);
    if(userOpt.isEmpty()){
      throw new UserNotFoundException("User not found with username : "+userName,HttpStatus.NOT_FOUND);
    }
    User user = userOpt.get();
    String emailId = user.getEmail();
    List<DocumentDetails> documentDetailsOpt = documentRepository.findByDocumentTitleContainingIgnoreCase(keyword);
    if(documentDetailsOpt.isEmpty()){
      throw new RuntimeException("Document not found.");
    }
//        DocumentDetails documentDetails = documentDetailsOpt;
//        if(!documentDetails.getUsersCanView().contains(emailId)){
//            throw new PermissionNotGrantedException("You do not have the permission to view this document.",HttpStatus.FORBIDDEN);
//        }
    baseResponse.setMessage("Document has been fetch.");
    baseResponse.setSuccess(true);
    baseResponse.setPayload(documentDetailsOpt);
    baseResponse.setStatusCode(HttpStatus.OK.toString());

    return baseResponse;
  }

  private DocumentDetails documentDtoToDocument(DocumentReqDto documentReqDto, String username){

    DocumentDetails documentDetails = new DocumentDetails();
    documentDetails.setDocumentCreatedAt(new Date());
    documentDetails.setDocumentDescription(documentReqDto.getDocumentDescription());
    documentDetails.setDocumentTitle(documentReqDto.getDocumentTitle());
    documentDetails.setDocumentCreatedBy(username);
    return documentDetails;
//    System

  }
}