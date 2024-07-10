package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;

public interface HistoryTracking {

  BaseResponse checkHistory(String documentId);

}