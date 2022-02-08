package edu.lysak.platform.repository;

import edu.lysak.platform.entities.CodeSnippet;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Tag(name = "Code Snippets repository", description = "Code Snippets repository API")
public interface CodeSnippetRepository extends CrudRepository<CodeSnippet, Long> {

    @Query("SELECT snippet FROM CodeSnippet snippet " +
            "WHERE snippet.time IS NULL AND snippet.views IS NULL " +
            "ORDER BY snippet.date DESC")
    List<CodeSnippet> findLastSnippets(Pageable pageable);

    @Query("SELECT snippet FROM CodeSnippet snippet WHERE snippet.uuid = :uuid")
    CodeSnippet findByUUID(@Param("uuid") String uuid);
}
