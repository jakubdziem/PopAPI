package com.dziem.popapi.controller;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UserDTO;
import com.dziem.popapi.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
public class UserControllerITTest {
    @Autowired
    UserController userController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UNameRepository uNameRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    @Transactional
    void createAnonimUserIdTest() throws Exception {
        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);


        assertThat(userRepository.findById(userDTO.getUserId())).isPresent();
    }
    @Test
    void anonimUserMigrationToGoogleTest() throws Exception {
        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);
        User anonimUser = userRepository.findById(userDTO.getUserId()).get();
        String googleId = "TESTING";
        String anonimUserId = anonimUser.getUserId();
        String nickName = mockMvc.perform(put("/api/v1/google/" + anonimUserId + "/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(nickName).isNotEmpty();
        System.out.println(nickName);
        assertThat(uNameRepository.findById(googleId).get().getName()).isEqualTo(nickName);
        assertThat(userRepository.findById(anonimUserId)).isNotPresent();
        assertThat(userRepository.findById(googleId)).isPresent();
    }
}
