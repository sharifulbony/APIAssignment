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
public class DAO {

    private SessionFactory sessionFactory;

    @Autowired
    CategoryRepository categoryRepository;


    @Autowired
    ProductRepository productRepository;


    @Autowired
    ProductCategoryRepository productCategoryRepository;
    public List<Object[]> searchByProgrammingLanguage(String name) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        String query2 =
                "SELECT per.id,\n" +
                        "  per.email,\n" +
                        "  conf.code,\n" +
                        "  pub.name\n" +
                        "FROM Developers AS per\n" +
                        "  LEFT JOIN developers_languages AS pconf\n" +
                        "    ON per.id = pconf.developersid\n" +
                        "  LEFT JOIN languages AS conf\n" +
                        "    ON pconf.languagesid = conf.id\n" +
                        "  LEFT JOIN programming_languages_developers AS ppub\n" +
                        "    ON per.id = ppub.developersid\n" +
                        "  LEFT JOIN programming_languages AS pub\n" +
                        "    ON ppub.programming_languagesid = pub.id\n";
        if (name.length() != 0) query2 += "  where pub.name = :name";
        List<Object[]> rows;
        if (name.length() != 0) {
            rows = s.createSQLQuery(query2).setString("name", name).list();
        } else rows = s.createSQLQuery(query2).list();
        tx.commit();
        s.close();
        return rows;
    }


    public List<Table> getProductByCategory(int category) {

        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();

        String query = "select id ,name from PRODUCT inner join PRODUCT_CATEGORY PC on PRODUCT.ID = PC.PRODUCT_ID where PC.CATEGORY_ID = :category";

        List<Table> productEntities = s.createSQLQuery(query)
                .setInteger("category", category)
                .setResultTransformer(Transformers.aliasToBean(Table.class))
                .list();
        tx.commit();
        s.close();


//        for (Object[]  o:productEntities
//             ) {
//            Table table=new Table();
//            table.setId();
//        }

        return productEntities;

    }

    public long createProductItem(JSONObject data) {

        String name="";
        if (data.containsKey("name")) {
            name = data.get("name").toString();
        }

        ArrayList<Integer> categories=new ArrayList<>();
        if (data.containsKey("categories")) {
            categories= (ArrayList<Integer>) data.get("categories");
        }

        ArrayList<CategoryEntity>categoryEntities=new ArrayList<>();
        for (Integer item:categories
             ) {
            CategoryEntity categoryEntity= categoryRepository.findOne( item);
            categoryEntities.add(categoryEntity);

        }

        ProductEntity productEntity = new ProductEntity(name, categoryEntities);
        productRepository.save(productEntity);
        return productEntity.getId();
    }

    public String updateProductItem(JSONObject data) {

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
            }
        }

        addAssociation(data,id);
        removeAssociation(data);
        productRepository.save(productEntity);
        return name;
    }


    public  void removeAssociation(JSONObject data){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<Integer> removedCategories=new ArrayList<>();
        if (data.containsKey("remove-categories")) {
            //todo get association and if no association throw error
            removedCategories= (ArrayList<Integer>) data.get("remove-categories");
            if(removedCategories!=null && removedCategories.size()>0) {
                for (Integer catId : removedCategories
                ) {
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
            productCategoryEntity.setCategory_id(item);
            productCategoryEntity.setProduct_id(id);
            productCategoryRepository.save(productCategoryEntity);
        }

    }

    public boolean deleteProductItem(long id) {

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
        CategoryEntity categoryEntity = new CategoryEntity(name);
        categoryRepository.save(categoryEntity);
        return categoryEntity.getId();
    }

    public boolean updateCategoryItem(Integer id, String name) {
        CategoryEntity categoryEntity = categoryRepository.findOne(id);
        categoryEntity.setName(name);
        categoryRepository.save(categoryEntity);
        return true;
    }

    public boolean deleteCategoryItem(long id) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

//        CategoryEntity categoryEntity = categoryRepository.findOne(id);
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
    public DAO(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

}
