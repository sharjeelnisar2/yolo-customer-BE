package com.yolo.customer.idea.dto;

import lombok.Data;

import java.util.List;

@Data
public class DraftIdeaRequest {
    private String title;
    private String description;
    private List<String> interests;
    private List<String> dietaryRestrictions;
}