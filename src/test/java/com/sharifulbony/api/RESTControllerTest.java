package com.sharifulbony.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sharifulbony.api.JWT.JwtRequestFilter;
import com.sharifulbony.api.JWT.JwtTokenUtil;
import com.sharifulbony.api.category.CategoryEntity;
import com.sharifulbony.api.category.CategoryRepository;
import com.sharifulbony.api.user.UserService;
import com.sharifulbony.api.user.UserDTO;
import com.sharifulbony.api.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@WebMvcTest(value = RESTController.class,secure = false)
@AutoConfigureMockMvc(secure = false)

//@SpringBootTest
public class RESTControllerTest {


    @MockBean
    private DatabaseInteractionService databaseInteractionService;


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
    private WebApplicationContext context;



    @Autowired
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    public void getAllCategoryTest() throws Exception {

        CategoryEntity categoryEntityTest = new CategoryEntity("sampleCategory");
        categoryEntityTest.setId(1);

        List<CategoryEntity> categoryEntityList = Arrays.asList(categoryEntityTest);

        categoryRepository.save(new CategoryEntity("test"));


        List<CategoryEntity> test = categoryRepository.findAll();


        given(categoryRepository.findAll()).willReturn(categoryEntityList);


        mvc.perform(
                get("/all-category/")
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

//        mvc.perform(
//                post("/create-category").
//                        requestAttr("name",categoryEntityTest.getName())
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());

        mvc.perform(post("/create-category").with(csrf())
                .requestAttr("name","ok")
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
