package com.sharifulbony.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sharifulbony.api.JWT.JwtRequestFilter;

import com.sharifulbony.api.JWT.JwtTokenUtil;
import com.sharifulbony.api.category.CategoryEntity;
import com.sharifulbony.api.category.CategoryRepository;
import com.sharifulbony.api.product.ProductRepository;

import com.sharifulbony.api.user.UserService;
import com.sharifulbony.api.user.UserDTO;
import com.sharifulbony.api.user.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(RESTController.class)
@AutoConfigureMockMvc(secure = false)
//@SpringBootTest
public class RESTControllerTest {

//    @Autowired
//    private DAO dao;
//
//    @Autowired
//
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;

//

    @MockBean
    private DatabaseInteractionService databaseInteractionService;


    @MockBean
    private ProductRepository productRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserService userDetailsService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;


    @Autowired
    private MockMvc mvc;
    ;


    @Test
    public void getAllCategoryTest() throws Exception {

        CategoryEntity categoryEntityTest = new CategoryEntity("sampleCategory");
        categoryEntityTest.setId(1);

        List<CategoryEntity> categoryEntityList = Arrays.asList(categoryEntityTest);

        categoryRepository.save(new CategoryEntity("test"));


        List<CategoryEntity> test = categoryRepository.findAll();


        given(categoryRepository.findAll()).willReturn(categoryEntityList);


        mvc.perform(
                get("/all-category")
//                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect((ResultMatcher) jsonPath("$[0].name", is(categoryEntityTest.getName())
//                        )
//
//                );

    }

    //
//    @RequestMapping(value = "/all-product", method = RequestMethod.GET)
//    public List<ProductEntity> getAllProduct() {
//        return productRepository.findAll();
//    }
//
//    @RequestMapping(value = "/product-by-category", method = RequestMethod.POST)
//    public List<ProductEntity> getProductByCategory(@RequestParam int category) {
//
//        List<ProductEntity> productEntities = dao.getProductByCategory(category);
//        return productEntities;
//    }
//
    @Test
    public void createCategoryTest() throws Exception {

        CategoryEntity categoryEntityTest = new CategoryEntity("sampleCategory");
        categoryEntityTest.setId(10);
//        categoryRepository.save(categoryEntityTest);

        mvc.perform(
                post("/create-category").
                        requestAttr("name",categoryEntityTest.getName())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
//
//    @RequestMapping(value = "/update-category", method = RequestMethod.POST)
//    public String updateCategory(@RequestParam Integer id, @RequestParam String name) {
//
//        dao.updateCategoryItem(id, name);
//        return "updated item with new name " + name;
//
//    }
//
//    @RequestMapping(value = "/delete-category", method = RequestMethod.POST)
//    public String deleteCategory(@RequestParam long id) {
//        dao.deleteCategoryItem(id);
//        return "Deleted Item with ID: " + id;
//    }
//
//
//    @RequestMapping(value = "/create-product", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public String createProduct(@RequestBody JSONObject data) {
//        long id = dao.createProductItem(data);
//        return "Created Item with ID: "+ id;
//    }
//
//    @RequestMapping(value = "/update-product", method = RequestMethod.POST,produces = "application/json")
//    @ResponseBody
//    public String updateProduct(@RequestBody JSONObject data) {
//
//        String name=dao.updateProductItem(data);
//        return "updated item with new name " + name;
//
//    }
//
//    @RequestMapping(value = "/delete-product", method = RequestMethod.POST)
//    public String deleteProduct(@RequestParam long id) {
//        boolean success=dao.deleteProductItem(id);
//        if(success){
//
//            return "Deleted Item with ID: " + id;
//        }else {
//            return "Deletion failed . Try Again. ";
//        }
//    }
//
//
//    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getUsername());
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

    @Test
    public void saveUser() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test_user");
        userDTO.setPassword("12345");

        mvc.perform(
                post("/register").
                        content(asJsonString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
