package com.sharifulbony.api;

import com.sharifulbony.api.category.CategoryEntity;
import com.sharifulbony.api.category.CategoryRepository;
import com.sharifulbony.api.product.ProductEntity;
import com.sharifulbony.api.product.ProductRepository;
import com.sharifulbony.api.productCategory.ProductCategoryEntity;
import com.sharifulbony.api.productCategory.ProductCategoryRepository;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DatabaseInteractionService {

    private SessionFactory sessionFactory;

    @Autowired
    CategoryRepository categoryRepository;


    @Autowired
    ProductRepository productRepository;


    @Autowired
    ProductCategoryRepository productCategoryRepository;

    public List<ProductEntity> getProductByCategory(int category_id) {

        List<ProductCategoryEntity> productCategoryEntities=productCategoryRepository.findByCategory(category_id);
        if(productCategoryEntities==null || productCategoryEntities.size()==0){
            throw new RequestRejectedException("No data found" );
        }
        List<Integer> productIds=new ArrayList<>();
        for (ProductCategoryEntity productCategoryEntity : productCategoryEntities
        ){
            productIds.add(productCategoryEntity.getProduct_id());
        }
        List<ProductEntity> productEntities=productRepository.findBYIdList(productIds);
        if(productEntities==null||productEntities.size()==0){
            throw new RequestRejectedException("No data found" );
        }

        return productEntities;

    }

    public ProductEntity createProductItem(JSONObject data) {

        String name="";
        if (data.containsKey("name")) {
            name = data.get("name").toString();
            if(name.length()<1){
                throw new RequestRejectedException("Product Name should Contain atLeast one letter");
            }
        }
        else{
            throw new RequestRejectedException("A product should have a name");
        }

        ArrayList<Integer> categories=new ArrayList<>();
        if (data.containsKey("categories")) {
            categories= (ArrayList<Integer>) data.get("categories");
        }

        ArrayList<CategoryEntity>categoryEntities=new ArrayList<>();
        for (Integer item:categories
             ) {
            CategoryEntity categoryEntity= categoryRepository.findOne( item);
            if(categoryEntity!=null){

                categoryEntities.add(categoryEntity);
            }

        }

        ProductEntity productEntity = new ProductEntity(name, categoryEntities);
        productRepository.save(productEntity);
        return productEntity;
    }

    public ProductEntity  updateProductItem(JSONObject data) {

        Integer id = null;
        if (data.containsKey("id")) {
            id = (Integer) data.get("id");
            if(id==null||id<=0){
                throw new RequestRejectedException("Id is Invalid :");
            }
        }else{
            throw new RequestRejectedException("Id is not specified for update :");
        }
        ProductEntity productEntity =productRepository.findOne( id);

        if(productEntity==null){
            throw new RequestRejectedException("Can not find any product with specified ID :" +id);
        }
        String name="";
        if (data.containsKey("name")) {
            name = data.get("name").toString();
            if(name.length()>0){
                productEntity.setName(name);
            }else {
                throw new RequestRejectedException("Product name should contain at least one character");
            }
        }

        addAssociation(data,id);
        removeAssociation(data);
        productRepository.save(productEntity);
        return productEntity;
    }

    public  void removeAssociation(JSONObject data){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<Integer> removedCategories=new ArrayList<>();
        if (data.containsKey("remove-categories")) {
            removedCategories= (ArrayList<Integer>) data.get("remove-categories");
            if(removedCategories!=null && removedCategories.size()>0) {
                for (Integer catId : removedCategories
                ) {

                    CategoryEntity categoryEntity=categoryRepository.findOne(catId);
                    if(categoryEntity==null){
                        throw new RequestRejectedException("Unable to find specified category to delete  relation");
                    }
                    String query = "delete  from PRODUCT_CATEGORY WHERE CATEGORY_ID = :association";
                    session.createSQLQuery(query)
                            .setLong("association", catId)
                            .executeUpdate();
                }
            }
        }

        transaction.commit();
        session.close();

    }

    public  void addAssociation(JSONObject data,Integer id){

        ArrayList<Integer> addCategories=new ArrayList<>();
        if (data.containsKey("add-categories")) {
            addCategories= (ArrayList<Integer>) data.get("add-categories");
        }

        for (Integer item:addCategories
        ) {
            ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
            CategoryEntity categoryEntity=categoryRepository.findOne(item);
            if(categoryRepository==null){
                throw new RequestRejectedException("Specified Category is not present in the database");
            }
            productCategoryEntity.setCategory_id(item);
            productCategoryEntity.setProduct_id(id);
            productCategoryRepository.save(productCategoryEntity);
        }

    }

    public boolean deleteProductItem(Integer id) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ProductEntity productEntity = session.get(ProductEntity.class,id);
        if (productEntity != null) {


            String query = "delete  from PRODUCT_CATEGORY WHERE PRODUCT_ID = :association";
            session.createSQLQuery(query)
                    .setLong("association", id)
                    .executeUpdate();

            session.delete(productEntity);
            transaction.commit();
            session.close();
            return true;
        } else {
            return false;

        }

    }


    public long createCategoryItem(String name) {
        if(name.length()<1){
            throw new RequestRejectedException("category name should contain at least one character");
        }
        CategoryEntity categoryEntity = new CategoryEntity(name);
        categoryRepository.save(categoryEntity);
        return categoryEntity.getId();
    }

    public CategoryEntity updateCategoryItem(Integer id, String name) {

        if(name.length()<1){
            throw new RequestRejectedException("name should contain at least one character ");
        }
        CategoryEntity categoryEntity = categoryRepository.findOne(id);
        if(categoryEntity==null){
            throw new RequestRejectedException("No data found with Id: "+id);
        }

        categoryEntity.setName(name);
        categoryRepository.save(categoryEntity);
        return categoryEntity;
    }

    public boolean deleteCategoryItem(Integer id) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CategoryEntity categoryEntity = session.get(CategoryEntity.class,id);

        if (categoryEntity != null) {
            String query = "delete  from PRODUCT_CATEGORY WHERE CATEGORY_ID = :association";
            session.createSQLQuery(query)
                    .setLong("association", id)
                    .executeUpdate();

            session.delete(categoryEntity);
            transaction.commit();
            session.close();
            return true;
        } else {
            return false;

        }

    }

    @Autowired
    public DatabaseInteractionService(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

}
