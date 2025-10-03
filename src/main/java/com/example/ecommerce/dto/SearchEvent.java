package com.example.ecommerce.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SearchEvent {
    private String query;
    private long timestamp;
    private int resultsCount;
}
