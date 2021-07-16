package edu.lysak.platform.controllers;

import edu.lysak.platform.entities.CodeSnippet;
import edu.lysak.platform.services.CodeSnippetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CodeAPIController {
    private final CodeSnippetService codeSnippetService;

    public CodeAPIController(CodeSnippetService codeSnippetService) {
        this.codeSnippetService = codeSnippetService;
    }


    @GetMapping("/api/code/{uuid}")
    public ResponseEntity<CodeSnippet> getApiCode(@PathVariable String uuid) {
        CodeSnippet codeSnippet = codeSnippetService.getCodeByUuid(uuid);
        if (codeSnippet == null) {
            return ResponseEntity.notFound().build();
        }
        if (codeSnippet.getTime() == null) {
            codeSnippet.setTime(0L);
        }
        if (codeSnippet.getViews() == null) {
            codeSnippet.setViews(0);
        }

        return ResponseEntity.ok(codeSnippet);
    }

    @GetMapping("/api/code/latest")
    public List<CodeSnippet> findLastCodeSnippets() {
        return codeSnippetService.findLastCodeSnippets(10);
    }

    @PostMapping("/api/code/new")
    public Map<String, String> addCodeSnippet(@RequestBody CodeSnippet codeSnippet) {
        CodeSnippet savedCodeSnippet = codeSnippetService.addCodeSnippet(codeSnippet);
        return Map.of("id", savedCodeSnippet.getUuid());
    }
}
