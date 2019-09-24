package com.sharifulbony.api;

public class DocumentationStaticContext {

    final static String allCategoryDescription="\"These Method Will return all the Category In the database\"";

    final static String productByCategoryDescription="These Method Will return all product of a specific Category";
    final static String productByCategoryParam="\"This method take\"category\" as a parameter\"";

    final static String createCategoryDescription="These Method Will create a category with specified data";
    final static String createCategoryParam="This method take \"name \" as a parameter for the category name";

    final static String updateCategoryDescription="\"These Method Will update the specified category\"";
    final static String updateCategoryParam="This method take \"name \" as a parameter for the new name of the category";

    final static String deleteCategoryDescription="These Method Will delete the specified category";
    final static String deleteCategoryParam="This method take \"id \" as a parameter for the category to delete it";

    final static  String createProductDescription="\"These Method Will create a product with specified data\"";
    final static String createProductParam="This Method will take JsonObject as Input ." +
            " The Json Object should consist of \"name\" as a mandatory Param, Also it can take optional  \"categories\" " +
            " array so that category can be defined while creating product";


    final static  String updateProductDescription="\"These Method Will update a product (like name, categories) with " +
            "specified data\"";
    final static String updateProductParam="This Method will take JsonObject as Input ." +
            " The Json Object should consists \"id\" as a mandatory param " +
            "also may consist of \"name\" as a Param to update product name," +
            " Also it can take optional  \"remove-categories\" " +
            " array so that relation to category can be removed while updating product"+
            "Also it can take optional  \"add-categories\"" +
            " array so that new relation with category can be added while updating product";


    final static String deleteProductDescription="These Method Will delete the specified product as well " +
            "as its any relation to category";
    final static String deleteProductParam="This method take \"id \" as a parameter for the product to delete it";

    final static String userRegisterDescription="These Method Will create a user in the database";
    final static String userRegisterParam="This method will take \"username\" and \"password\" as a parameter to register a" +
            " user in database ";

    final static String userAuthenticationDescription="These Method Will authenticate  a user and in response give a " +
            "token to successfully authenticated user. " +
            "This token should be used as authorization header for any create , update , delete operation";
    final static String userAuthenticationParam="This method will take \"username\" and \"password\" as a parameter " +
            "to authenticate a user ";



}
