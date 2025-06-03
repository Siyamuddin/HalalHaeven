package com.pm.unitalk.Utility;

import com.pm.unitalk.DTOs.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private List<ProductDTO> content;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
