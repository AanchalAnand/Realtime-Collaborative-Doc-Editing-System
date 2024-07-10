package com.project.realtime_collaborative_doc_editing.repository;

import com.project.realtime_collaborative_doc_editing.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>
{
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

}