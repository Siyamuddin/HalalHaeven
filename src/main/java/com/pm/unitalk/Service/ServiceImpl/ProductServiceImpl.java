package com.pm.unitalk.Service.ServiceImpl;
import com.pm.unitalk.DTOs.ProductDTO;
import com.pm.unitalk.Exceptions.ResourceNotFoundException;
import com.pm.unitalk.Model.Category;
import com.pm.unitalk.Model.LocalUser;
import com.pm.unitalk.Model.Product;
import com.pm.unitalk.Repository.CategoryRepo;
import com.pm.unitalk.Repository.LocalUserRepo;
import com.pm.unitalk.Repository.ProductRepository;
import com.pm.unitalk.Service.ProductService;
import com.pm.unitalk.Utility.APIResponse;
import com.pm.unitalk.Utility.PostResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalUserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private FileServiceImpl fileService;
    @Value("${project.image}")
    private String path;


    @Override
    public ProductDTO createPost(ProductDTO productDto, Long userId, Long categoryId) {
        LocalUser user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));
        Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category id",categoryId));
        Product product =this.modelMapper.map(productDto, Product.class);
        product.setImage("halalhaven.png");
        product.setCategory(category);
        product.setLocalUser(user);
        Product newProduct =this.postRepo.save(product);
        return this.modelMapper.map(newProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updatePost(ProductDTO productDto, Long postId) {
        Product product =this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Product","Product Id",postId));
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setProductPrice(productDto.getProductPrice());
        Product updatedProduct =this.postRepo.save(product);

        return this.modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public void deletePost(Long postId) {
        Product product =this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Product","product id",postId));
        this.postRepo.delete(product);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDirec) {
        Sort sort=null;
        if(sortDirec.equalsIgnoreCase("asc")){
            sort=Sort.by(sortBy).ascending();
        }
        else
        {
            sort=Sort.by(sortBy).descending();
        }
        Pageable p=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> pagePost=this.postRepo.findAll(p);
        List<Product> products =pagePost.getContent();
        List<ProductDTO> productDtos = products.stream().map((product)->this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(productDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public ProductDTO getPost(Long postId) {
        Product product =this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Product","Product Id",postId));

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public PostResponse getPostByCategory(Long categoryId, Integer pageNUmber, Integer pageSize,String sortBy,String sortDirec) {
        Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category Id",categoryId));
        Sort sort=null;
        if(sortDirec.equalsIgnoreCase("asc"))
        {
           sort=Sort.by(sortBy).ascending();
        }
        else
        {
            sort=Sort.by(sortBy).descending();
        }
        Pageable p=PageRequest.of(pageNUmber,pageSize,sort);
        Page<Product> postPage=this.postRepo.findByCategory(cat,p);
        List<Product> products =postPage.getContent();
        List<ProductDTO> productDtoList = products.stream().map((product)-> this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(productDtoList);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());

        return postResponse;
    }

    @Override
    public PostResponse getPostByUser(Long userId, Integer pageNumber, Integer pageSize,String sortBy, String sortDirec) {
        LocalUser user=this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user Id",userId));
        Sort sort=null;
        if(sortDirec.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else
        {
            sort=Sort.by(sortBy).descending();
        }
        Pageable p=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> postPage=this.postRepo.findByLocalUser(user,p);
        List<Product> products =postPage.getContent();
        List<ProductDTO> productDtos = products.stream().map((product)-> this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(productDtos);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());
        return postResponse;
    }

    @Override
    public List<ProductDTO> searchPost(String keyword) {
        List<Product> products =this.postRepo.findByProductNameContainingIgnoreCase(keyword);
        List<ProductDTO> productDtos = products.stream().map((product)-> this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public ProductDTO uploadImage(Long productId, MultipartFile file) {
        Product product=postRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ID",productId));
        fileService.deleteImage(path, product.getImage());
        String fileUrl=null;
        try {
            fileUrl=fileService.uploadImage(path,file);
        } catch (IOException e) {
            log.error("Found an Error while uploading image here is the description: "+e);
            throw new RuntimeException("Having problem while uploading image."+e);
        }
        product.setImage(fileUrl);
        Product savedProduct=postRepo.save(product);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO changeImage(Long productId,MultipartFile file) {
        Product product=postRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ID",productId));
        fileService.deleteImage(path, product.getImage());
        String fileUrl=null;
        try {
            fileUrl=fileService.uploadImage(path,file);
        } catch (IOException e) {
            log.error("Found an Error while uploading image here is the description: "+e);
            throw new RuntimeException("Having problem while uploading image."+e);
        }
        product.setImage(fileUrl);
        Product savedProduct=postRepo.save(product);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public InputStream getTheImage(Long productId) {
        Product product=postRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ID",productId));
        InputStream inputStream;
        try {
             inputStream=fileService.getSource(path,product.getImage());
        } catch (FileNotFoundException e) {
            log.error("Found an Error while loading image here is the description: "+e);
            throw new RuntimeException("Having problem while loading image."+e);
        }
        return inputStream;
    }
}
