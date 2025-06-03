package com.pm.unitalk.Repository;

import com.pm.unitalk.Model.Category;
import com.pm.unitalk.Model.LocalUser;
import com.pm.unitalk.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductNameContainingIgnoreCase(String title);
    Page<Product> findByCategory(Category Category, Pageable pageable);
    Page<Product> findByLocalUser(LocalUser user, Pageable pageable);


}
