package com.pm.unitalk.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Long categoryId;
    @NotEmpty
    @Size(min=2)
    private String categoryTitle;
}
