package com.pm.unitalk.Controller;
import com.pm.unitalk.Configurations.AppConstants;
import com.pm.unitalk.DTOs.ProductDTO;
import com.pm.unitalk.Service.ProductService;
import com.pm.unitalk.Utility.APIResponse;
import com.pm.unitalk.Utility.PostResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/user/{userId}/category/{categoryId}/create")
    public ResponseEntity<ProductDTO> createPost(@RequestBody ProductDTO productDto,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long categoryId)
    {
        ProductDTO createProductDto =this.productService.createPost(productDto,userId,categoryId);
        return new ResponseEntity<ProductDTO>(createProductDto, HttpStatus.CREATED);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PostResponse> getPostByCategory(@PathVariable Long categoryId,
                                                          @RequestParam(value="pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                                          @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize,
                                                          @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                          @RequestParam(value = "sortDirec",defaultValue = "dsc",required = false) String sortDirec)
    {
        PostResponse postResponse=this.productService.getPostByCategory(categoryId,pageNumber,pageSize,sortBy,sortDirec);
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
    }
    @GetMapping("/get/{productId}")
    public ResponseEntity<ProductDTO> getPost(@PathVariable Long productId)
    {
        ProductDTO productDto =this.productService.getPost(productId);
        return new ResponseEntity<ProductDTO>(productDto,HttpStatus.OK);
    }
    @GetMapping("/get/all")
    public ResponseEntity<PostResponse> getAllPost(@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id",required = false) String sortBy,
                                                   @RequestParam(value = "sortDirec",defaultValue = "dsc",required = false) String sortDirec)
    {
        PostResponse postResponse=this.productService.getAllPost(pageNumber,pageSize,sortBy,sortDirec);
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{productId}")
    public APIResponse deletePost(@PathVariable Long productId)
    {
        this.productService.deletePost(productId);
        return new APIResponse("This post has been deleted.",true);
    }
    @PutMapping("/update/{postId}")
    public ResponseEntity<ProductDTO> updatePost(@RequestBody ProductDTO productDto, @PathVariable Long postId)
    {
        ProductDTO postDots=this.productService.updatePost(productDto,postId);
        return new ResponseEntity<ProductDTO>(postDots,HttpStatus.OK);
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ProductDTO>> searchPostByTitle(@PathVariable("keyword") String keyword)
    {
        List<ProductDTO> dtos=this.productService.searchPost(keyword);
        return new ResponseEntity<List<ProductDTO>>(dtos,HttpStatus.OK);
    }








    @GetMapping("/user/{userId}")
    public ResponseEntity<PostResponse> getPostByUser(@PathVariable Long userId,
                                                      @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                      @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = "id",required = false) String sortBy,
                                                      @RequestParam(value = "sortDirec", defaultValue ="asc",required = false) String sortDirec)
    {
        PostResponse postResponse=this.productService.getPostByUser(userId,pageNumber,pageSize,sortBy,sortDirec);
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
    }

    @PutMapping("/upload/{productId}")
    public ResponseEntity<ProductDTO> uploadImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image){
        ProductDTO productDTO=productService.uploadImage(productId,image);

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @PutMapping("/change/{productId}")
    public ResponseEntity<ProductDTO> changeImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image){
        ProductDTO productDTO=productService.changeImage(productId,image);

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/image/{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveImage(@PathVariable Long productId, HttpServletResponse response) throws IOException {
        InputStream inputStream = productService.getTheImage(productId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }


}
