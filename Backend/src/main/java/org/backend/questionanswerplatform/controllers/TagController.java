package org.backend.questionanswerplatform.controllers;

import jakarta.validation.Valid;
import org.backend.questionanswerplatform.dto.tag.CreateTagRequest;
import org.backend.questionanswerplatform.dto.tag.TagResponse;
import org.backend.questionanswerplatform.logic.TagService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagServices){
        this.tagService=tagServices;
    }

    @GetMapping("/suggest")
    public List<TagResponse> suggestTags(@RequestParam String query)
    {
        return tagService.getSuggestions(query);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public TagResponse createTag(@Valid @RequestBody CreateTagRequest request)
    {
     return tagService.createTag(request);
    }


}
