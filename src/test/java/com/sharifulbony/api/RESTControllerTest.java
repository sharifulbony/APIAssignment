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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
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



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
