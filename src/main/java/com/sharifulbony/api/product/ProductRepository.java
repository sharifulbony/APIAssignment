package com.sharifulbony.api.product;

import com.sharifulbony.api.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {

//    @Query("select ID,name from PRODUCT inner join PRODUCT_CATEGORY PC on PRODUCT.ID = PC.PRODUCT_ID where PC.CATEGORY_ID = ?1")
//    List<ProductEntity> getByCategories(int category);
}
