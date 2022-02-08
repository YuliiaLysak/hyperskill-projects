package edu.lysak.platform.controllers;

import edu.lysak.platform.entities.CodeSnippet;
import edu.lysak.platform.services.CodeSnippetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
@Tag(name = "WEB Controller", description = "WEB Controller API")
public class CodeWebController {
    private final CodeSnippetService codeSnippetService;

    public CodeWebController(CodeSnippetService codeSnippetService) {
        this.codeSnippetService = codeSnippetService;
    }

    @GetMapping("/code/{uuid}")
    @Operation(summary = "Get code snippet by id")
    public String getWebCode(Model model, @PathVariable String uuid) {
        CodeSnippet codeSnippet = codeSnippetService.getCodeByUuid(uuid);
        if (codeSnippet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("code", codeSnippet.getCode());
        model.addAttribute("date", codeSnippet.getDate());
        if (codeSnippet.getTime() != null) {
            model.addAttribute("time", codeSnippet.getTime());
        }
        if (codeSnippet.getViews() != null) {
            model.addAttribute("views", codeSnippet.getViews());
        }
        return "code";
    }

    @GetMapping("/code/latest")
    @Operation(summary = "Get latest code snippet")
    public String getWebCode(Model model) {
        List<CodeSnippet> codeSnippets = codeSnippetService.findLastCodeSnippets(10);
        model.addAttribute("codeSnippets", codeSnippets);
        return "latest";
    }

    @GetMapping("/code/new")
    @Operation(summary = "Add new code snippet")
    public String getNewWebCode() {
        return "create";
    }
}
