package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.service.impl.HistoryTrackingImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document/history")
@RequiredArgsConstructor
public class HistoryController {

  private final HistoryTrackingImpl historyTracking;

  @GetMapping("/{documentId}")
  public ResponseEntity<BaseResponse> getHistory(@PathVariable("documentId") String documentId){
    BaseResponse baseResponse = historyTracking.checkHistory(documentId);
    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
  }
}