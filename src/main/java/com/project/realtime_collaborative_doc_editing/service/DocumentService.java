package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;

public interface DocumentService {

  BaseResponse createNewDocument(DocumentReqDto documentReqDto);

  BaseResponse getDocumentById(String documentId);

  BaseResponse getDocumentByDocumentTitle(String documentName);

  BaseResponse editDocument(String documentId, DocumentReqDto documentReqDto);

  BaseResponse deleteDocument(String documentId);

  BaseResponse searchDocumentsByKeyword(String keyword);

}