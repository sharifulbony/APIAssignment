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

import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "")
public class RESTController {

    @Autowired
    private DAO dao;

    @Autowired

    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userDetailsService;


    @RequestMapping(value = "/all-category", method = RequestMethod.GET)
    public List<CategoryEntity> getAllCategory() {

        List jsonArray = categoryRepository.findAll();
        return jsonArray;
    }

    @RequestMapping(value = "/all-product", method = RequestMethod.GET)
    public List<ProductEntity> getAllProduct() {
        return productRepository.findAll();
    }

    @RequestMapping(value = "/product-by-category", method = RequestMethod.POST)
    public List<Table> getProductByCategory(@RequestParam int category) {

        List<Table> productEntities = dao.getProductByCategory(category);
        return productEntities;
    }

    @RequestMapping(value = "/create-category", method = RequestMethod.POST)
    public String createCategory(@RequestParam String name) {
        long id = dao.createCategoryItem(name);
        return "Created Item with ID: " + id;
    }

    @RequestMapping(value = "/update-category", method = RequestMethod.POST)
    public String updateCategory(@RequestParam Integer id, @RequestParam String name) {

        dao.updateCategoryItem(id, name);
        return "updated item with new name " + name;

    }

    @RequestMapping(value = "/delete-category", method = RequestMethod.POST)
    public String deleteCategory(@RequestParam long id) {
        dao.deleteCategoryItem(id);
        return "Deleted Item with ID: " + id;
    }


    @RequestMapping(value = "/create-product", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createProduct(@RequestBody JSONObject data) {
        long id = dao.createProductItem(data);
        return "Created Item with ID: "+ id;
    }

    @RequestMapping(value = "/update-product", method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public String updateProduct(@RequestBody JSONObject data) {

        String name=dao.updateProductItem(data);
        return "updated item with new name " + name;

    }

    @RequestMapping(value = "/delete-product", method = RequestMethod.POST)
    public String deleteProduct(@RequestParam long id) {
        boolean success=dao.deleteProductItem(id);
        if(success){

            return "Deleted Item with ID: " + id;
        }else {
            return "Deletion failed . Try Again. ";
        }
    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
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


}
