package org.backend.questionanswerplatform.repositories;

import org.backend.questionanswerplatform.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByNameContainingIgnoreCase(String query);
    Optional<Tag> findByNameIgnoreCase(String name);

}
