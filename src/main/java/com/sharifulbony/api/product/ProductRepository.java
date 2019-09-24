package com.sharifulbony.api.product;

import com.sharifulbony.api.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {

    @Query(value = "SELECT u FROM ProductEntity u WHERE u.id in  :ids ")
    List<ProductEntity> findBYIdList(@Param("ids")List<Integer> ids);

    ProductEntity findByName(String name);
}
