package com.project.realtime_collaborative_doc_editing.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Document(collection = "history_details")
public class HistoryDetails {

  private String documentId;

  private String updatedBy;

  private Date updatedAt;

  private String operationType;

}