package com.project.realtime_collaborative_doc_editing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "documents")
public class DocumentDetails {

  @Id
  private String documentId;

  private String documentTitle;

  private String documentDescription;

  private Date documentCreatedAt;

  private String documentCreatedBy;

  private Date lastEditedAt;

  private String lastEditedBy;

  private Set<String> usersCanEdit;

  private Set<String> usersCanView;

  private Set<String> requestForView;

  private Boolean isRequestPending;

  private Set<String> requestForEdit;

  private List<HistoryDetails> historyDetails;

}