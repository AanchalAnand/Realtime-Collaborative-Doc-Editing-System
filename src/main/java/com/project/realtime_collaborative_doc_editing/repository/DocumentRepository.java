package com.project.realtime_collaborative_doc_editing.repository;

import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends MongoRepository<DocumentDetails,String> {


  Optional<DocumentDetails> findByDocumentId(String documentId);

  Optional<DocumentDetails> findByDocumentTitle(String documentTitle);

  //@Query("select * from documents where documents.documentsTitle = "+ keyword)
  List<DocumentDetails> findByDocumentTitleContainingIgnoreCase(String keyword);

}