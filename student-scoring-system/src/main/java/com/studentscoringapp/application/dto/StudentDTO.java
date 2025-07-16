package com.studentscoringapp.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Student Data Transfer Object")
public class StudentDTO {

    @Schema(description = "Database ID", example = "1")
    private Long id;

    @NotBlank(message = "Student ID is required")
    @Size(min = 3, max = 20, message = "Student ID must be between 3 and 20 characters")
    @Schema(description = "Unique student identifier", example = "STU001", required = true)
    private String studentId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "Student's first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "Student's last name", example = "Doe", required = true)
    private String lastName;

    @Email(message = "Email should be valid")
    @Schema(description = "Student's email address", example = "john.doe@example.com")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}