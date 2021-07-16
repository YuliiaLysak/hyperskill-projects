package edu.lysak.platform.services;

import edu.lysak.platform.entities.CodeSnippet;
import edu.lysak.platform.repository.CodeSnippetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CodeSnippetService {
    private static final Logger LOG = Logger.getLogger(CodeSnippetService.class.getName());

    private final CodeSnippetRepository codeSnippetRepository;

    public CodeSnippetService(CodeSnippetRepository codeSnippetRepository) {
        this.codeSnippetRepository = codeSnippetRepository;
    }

    public CodeSnippet getCodeByUuid(String uuid) {
        LOG.warning("service.getCodeByUuid() with uuid " + uuid);
        CodeSnippet codeSnippet = codeSnippetRepository.findByUUID(uuid);

        if (codeSnippet == null) {
            return null;
        }

        // if there is no restriction at all
        if (codeSnippet.getTime() == null && codeSnippet.getViews() == null) {
            return codeSnippet;
        }

        // if there is only views restriction
        if (codeSnippet.getTime() == null) {
            LOG.warning("time == null, views BEFORE = " + codeSnippet.getViews());
            int newViews = codeSnippet.getViews() - 1;
            codeSnippet.setViews(newViews);
            codeSnippetRepository.save(codeSnippet);

            // view restriction check
            if (codeSnippet.getViews() < 0) {
                return null;
            }

            LOG.warning("time == null, views AFTER = " + codeSnippet.getViews());
            return codeSnippet;
        }

        // if there is only time restriction
        if (codeSnippet.getViews() == null) {
            LOG.warning("views == null, time BEFORE = " + codeSnippet.getTime());
            long duration = Duration.between(codeSnippet.getDate(), LocalDateTime.now()).getSeconds();
            long newTime = codeSnippet.getTime() - duration;
            codeSnippet.setTime(newTime);

            // time restriction check
            if (codeSnippet.getTime() <= 0) {
                return null;
            }
            LOG.warning("views == null, time AFTER = " + codeSnippet.getTime());
            return codeSnippet;
        }

        LOG.warning("views BEFORE = " + codeSnippet.getViews() +
                " time BEFORE = " + codeSnippet.getTime());

        int newViews = Math.max(codeSnippet.getViews() - 1, 0);
        codeSnippet.setViews(newViews);
        codeSnippetRepository.save(codeSnippet);

        long duration = Duration.between(codeSnippet.getDate(), LocalDateTime.now()).getSeconds();
        long newTime = Math.max(codeSnippet.getTime() - duration, 0);
        codeSnippet.setTime(newTime);

        LOG.warning("views AFTER = " + codeSnippet.getViews() +
                " time AFTER = " + codeSnippet.getTime());

        if (codeSnippet.getTime() > 0 && codeSnippet.getViews() <= 0 ||
                codeSnippet.getViews() > 0 && codeSnippet.getTime() <= 0
        ) {
            return null;
        }

        return codeSnippet;
    }

    public List<CodeSnippet> findLastCodeSnippets(int count) {
        List<CodeSnippet> lastSnippets = codeSnippetRepository.findLastSnippets(PageRequest.of(0, count));
        return lastSnippets.stream()
                .map(snippet -> {
                    snippet.setViews(0);
                    snippet.setTime(0L);
                    return snippet;
                })
                .collect(Collectors.toList());
    }

    public CodeSnippet addCodeSnippet(CodeSnippet codeSnippet) {
        Long time = codeSnippet.getTime() <= 0 ? null : codeSnippet.getTime();
        Integer views = codeSnippet.getViews() <= 0 ? null : codeSnippet.getViews();

        return codeSnippetRepository.save(
                new CodeSnippet(
                        codeSnippet.getCode(),
                        LocalDateTime.now(),
                        time,
                        views
                ));
    }
}
