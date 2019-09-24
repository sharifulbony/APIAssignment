package com.sharifulbony.api;

import com.sharifulbony.api.JWT.JwtRequest;
import com.sharifulbony.api.JWT.JwtResponse;
import com.sharifulbony.api.JWT.JwtTokenUtil;
import com.sharifulbony.api.category.CategoryEntity;
import com.sharifulbony.api.category.CategoryRepository;

import com.sharifulbony.api.product.ProductEntity;
import com.sharifulbony.api.product.ProductRepository;
import com.sharifulbony.api.user.UserService;
import com.sharifulbony.api.user.UserDTO;
import com.sharifulbony.api.user.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *
 */
@RestController
@RequestMapping(value = "")
@Api(value = "Product Inventory System", description = "Operations related to Product Inventory Management System")

public class RESTController {

    @Autowired
    private DatabaseInteractionService databaseInteractionService;

    @Autowired

    private CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userDetailsService;


    @RequestMapping(value = "/all-category", method = RequestMethod.GET)
    @ApiOperation(value = DocumentationStaticContext.allCategoryDescription, response = Iterable.class)
    public List<CategoryEntity> getAllCategory() {

        return categoryRepository.findAll();
    }
//
//    @RequestMapping(value = "/all-product", method = RequestMethod.GET)
//    public List<ProductEntity> getAllProduct() {
//        return productRepository.findAll();
//    }

    @RequestMapping(value = "/product-by-category", method = RequestMethod.POST)
    @ApiOperation(value = DocumentationStaticContext.productByCategoryDescription, response = Iterable.class)
    public List<ProductEntity> getProductByCategory(@ApiParam(value = DocumentationStaticContext.productByCategoryParam, required = true) @RequestParam int category
    ) {

        if (category <= 0) {
            throw new RequestRejectedException("category should be positive");
        }
        List<ProductEntity> productEntities = databaseInteractionService.getProductByCategory(category);
        return productEntities;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = DocumentationStaticContext.userRegisterDescription, response = Iterable.class)
    public ResponseEntity<?> saveUser(@ApiParam(value = DocumentationStaticContext.userRegisterParam) @RequestBody UserDTO user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ApiOperation(value = DocumentationStaticContext.userAuthenticationDescription, response = Iterable.class)

    public ResponseEntity<?> createAuthenticationToken(@ApiParam(value = DocumentationStaticContext.userAuthenticationParam) @RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/create-category", method = RequestMethod.POST)
    @PreAuthorize("authenticated")
    @ApiOperation(value = DocumentationStaticContext.createCategoryDescription, response = Iterable.class)
    public String createCategory(@ApiParam(value = DocumentationStaticContext.createCategoryParam, required = true) @RequestParam String name) {
        long id = -1;
        id = databaseInteractionService.createCategoryItem(name);
        if (id < 0) {
            throw new RequestRejectedException("Category Creation Failed. Try Again!!");
        }
        return "Created Item with ID: " + id;
    }

    @RequestMapping(value = "/update-category", method = RequestMethod.POST)
    @ApiOperation(value = DocumentationStaticContext.updateCategoryDescription, response = Iterable.class)

    public String updateCategory(@ApiParam(value = DocumentationStaticContext.updateCategoryParam, required = true) @RequestParam Integer id, @RequestParam String name) {

        CategoryEntity categoryEntity= databaseInteractionService.updateCategoryItem(id, name);
        if(!categoryEntity.getName().equals(name)){
            throw new RequestRejectedException("Update Failed. Try Again!!");
        }
        return "updated item with new name: " + name;

    }

    @RequestMapping(value = "/delete-category", method = RequestMethod.POST)
    @ApiOperation(value = DocumentationStaticContext.deleteCategoryDescription, response = Iterable.class)
    public String deleteCategory(@ApiParam(value = DocumentationStaticContext.deleteCategoryParam) @RequestParam Integer id) {
        boolean result= databaseInteractionService.deleteCategoryItem(id);
        if(!result){
            throw new RequestRejectedException("No Data found With Id: "+ id);
        }
        return "Deleted Item with ID: " + id;
    }


    @RequestMapping(value = "/create-product", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = DocumentationStaticContext.createProductDescription, response = Iterable.class)
    @ResponseBody
    public String createProduct(@ApiParam(value = DocumentationStaticContext.createProductParam) @RequestBody JSONObject data) {
        ProductEntity productEntity = databaseInteractionService.createProductItem(data);
        return "Created Item with ID: " + productEntity.getId();
    }

    @RequestMapping(value = "/update-product", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = DocumentationStaticContext.updateProductDescription, response = Iterable.class)
    @ResponseBody
    public String updateProduct(@ApiParam(value = DocumentationStaticContext.updateProductParam) @RequestBody JSONObject data) {

        ProductEntity productEntity = databaseInteractionService.updateProductItem(data);
        return "updated item Successfully ";

    }

    @RequestMapping(value = "/delete-product", method = RequestMethod.POST)
    @ApiOperation(value = DocumentationStaticContext.deleteProductDescription, response = Iterable.class)
    public String deleteProduct(@ApiParam(value = DocumentationStaticContext.deleteProductParam) @RequestParam Integer id) {
        boolean success = databaseInteractionService.deleteProductItem(id);
        if (success) {

            return "Deleted Item with ID: " + id;
        } else {
            throw new RequestRejectedException( "Deletion failed. Check your data. ");
        }
    }


}
