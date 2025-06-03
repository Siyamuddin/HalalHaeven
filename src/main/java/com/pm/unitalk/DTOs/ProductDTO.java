package com.pm.unitalk.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String productName;
    private String description;
    private Double productPrice;
    private String quantity;
    private String image;
    private Date createdAt;
    private Date updatedAt;
    private UserDTO localUser;
}
