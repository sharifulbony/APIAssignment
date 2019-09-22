package com.sharifulbony.api;

import com.sharifulbony.api.category.CategoryEntity;
import com.sharifulbony.api.category.CategoryRepository;

import com.sharifulbony.api.product.ProductEntity;
import com.sharifulbony.api.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public DatabaseSeeder(
            CategoryRepository categoryRepository,
            ProductRepository productRepository

    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;

    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<CategoryEntity> categoryEntities = new ArrayList<>();

        int dataSize =5;

        int maxCategoryForProduct=3;
        for (int i = 1; i <= dataSize; i++) {
            CategoryEntity categoryEntity = new CategoryEntity("Category" + i);
            categoryEntities.add(categoryEntity);
        }
        categoryRepository.save(categoryEntities);

        List<ProductEntity> productEntities = new ArrayList<>();
        for (int i = 1; i <= dataSize; i++) {
            ArrayList<CategoryEntity> randomCategoryEntities = new ArrayList<>();
            for (int j=0;j<=new Random().nextInt(maxCategoryForProduct);j++){
                CategoryEntity categoryEntity= categoryEntities.get(new Random().nextInt(dataSize));

                if(!randomCategoryEntities.contains(categoryEntity)){

                    randomCategoryEntities.add(categoryEntity);
                }

            }

            ProductEntity productEntity = new ProductEntity("Product" + i, randomCategoryEntities);
            productEntities.add(productEntity);
        }
        productRepository.save(productEntities);


    }

    protected String getSaltString(int size) {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


}
