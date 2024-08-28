package com.yolo.customer.idea;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/users/ideas")
public class IdeaController {

    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @PatchMapping("/{ideaId}")
    public ResponseEntity<Map<String, String>> submitIdeaToVendor(@PathVariable("ideaId") Integer ideaId, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        return ideaService.submitIdeaToVendor(ideaId, status);
    }

    @PostMapping("/draft")
    public ResponseEntity<Idea> createDraftIdea(@RequestBody DraftIdeaRequest request) {
        Idea idea = ideaService.createDraftIdea(request);
        return ResponseEntity.ok(idea);
    }

}