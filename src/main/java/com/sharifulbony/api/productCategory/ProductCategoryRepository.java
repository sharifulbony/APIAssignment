package com.sharifulbony.api.productCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity,Integer> {


    ProductCategoryRepository findById( Integer id);

    @Query(value = "SELECT u FROM ProductCategoryEntity u WHERE u.category_id = ?1 ")
     List<ProductCategoryEntity> findByCategory(Integer category_id);
}
