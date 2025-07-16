package com.studentscoringapp.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Paginated response wrapper")
public class PagedResponseDTO<T> {

    @Schema(description = "List of items")
    private List<T> content;

    @Schema(description = "Current page number (0-based)")
    private int pageNumber;

    @Schema(description = "Page size")
    private int pageSize;

    @Schema(description = "Total number of elements")
    private long totalElements;

    @Schema(description = "Total number of pages")
    private int totalPages;

    @Schema(description = "Is first page")
    private boolean first;

    @Schema(description = "Is last page")
    private boolean last;
}