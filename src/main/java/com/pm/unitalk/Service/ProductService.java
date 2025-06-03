package com.pm.unitalk.Service;


import com.pm.unitalk.DTOs.ProductDTO;
import com.pm.unitalk.Utility.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ProductService {
    ProductDTO createPost(ProductDTO productDto, Long userId, Long categoryId);
    ProductDTO updatePost(ProductDTO productDto, Long postId);
    void deletePost(Long postId);
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDirec);
    ProductDTO getPost(Long postId);
    PostResponse getPostByCategory(Long categoryId, Integer pageNumber, Integer pageSize,String sortBy,String sortDirec);
    PostResponse getPostByUser(Long userId, Integer pageNumber, Integer pageSize,String sortBy, String sortDirec);
    List<ProductDTO> searchPost(String keyword);
    ProductDTO uploadImage(Long productId, MultipartFile file);
    ProductDTO changeImage(Long productId, MultipartFile file);
    InputStream getTheImage(Long productId);


}
