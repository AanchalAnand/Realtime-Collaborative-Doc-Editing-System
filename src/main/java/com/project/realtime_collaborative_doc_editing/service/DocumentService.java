package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;

public interface DocumentService {

  BaseResponse createNewDocument(DocumentReqDto documentReqDto);

  BaseResponse getDocumentById(String documentId);

  BaseResponse getDocumentByDocumentTitle(String documentName);

  BaseResponse editDocument(DocumentReqDto documentReqDto, String documentId);

  BaseResponse deleteDocument(String documentId);

  BaseResponse searchDocumentsByKeyword(String keyword);

}