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
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void shouldCreateAnonimUserId() throws Exception {
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
    void shouldMigrateAnonimUserToGoogleTest() throws Exception {
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
    @Test
    void shouldCreateGoogleUser() throws Exception {
        String googleId = "TESTING";
        String nickName  = mockMvc.perform(post("/api/v1/google/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(nickName).isNotEmpty();
        System.out.println(nickName);
        assertThat(uNameRepository.findById(googleId).get().getName()).isEqualTo(nickName);
        assertThat(userRepository.findById(googleId)).isPresent();
    }
    @Test
    void shouldReturnConflictWhenCreatingGoogleUserThatAlreadyExists() throws Exception {
        String googleId = "TESTING";
        mockMvc.perform(post("/api/v1/google/" + googleId));

        assertThat(userRepository.findById(googleId)).isPresent();

        mockMvc.perform(post("/api/v1/google/" + googleId))
                .andExpect(status().isConflict());
    }
    @Test
    void shouldReturnConflictWhenMigratingToGoogleUserThatAlreadyExists() throws Exception {
        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);
        User anonimUser = userRepository.findById(userDTO.getUserId()).get();
        String anonimUserId = anonimUser.getUserId();
        String googleId = "TESTING";

        mockMvc.perform(put("/api/v1/google/" + anonimUserId + "/" + googleId));


        String responseContent2  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO2 = objectMapper.readValue(responseContent2, UserDTO.class);
        User anonimUser2 = userRepository.findById(userDTO2.getUserId()).get();
        String anonimUserId2 = anonimUser2.getUserId();


        mockMvc.perform(put("/api/v1/google/" + anonimUserId2 + "/" + googleId))
                .andExpect(status().isConflict());
    }
    @Test
    void shouldReturnNotFoundWhenMigratingToGoogleUserWithAnonimUserThatDoesntExist() throws Exception {
        String anonimUserId = "anonim_user_id_that_doesnt_exist";
        String googleId = "TESTING";

        Optional<User> anonimUser = userRepository.findById(anonimUserId);

        assertThat(anonimUser).isEmpty();

        mockMvc.perform(put("/api/v1/google/" + anonimUserId + "/" + googleId))
            .andExpect(status().isNotFound());
    }
    @Test
    void shouldSetUsernameForGoogleUser() throws Exception {
        String googleId = "TESTING";
        String username = "good_morning";

        mockMvc.perform(post("/api/v1/google/" + googleId));

        String prevUsername = uNameRepository.findById(googleId).get().getName();

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + username))
                .andExpect(status().isNoContent());

        String currentUsername = uNameRepository.findById(googleId).get().getName();

        assertThat(username).isEqualTo(currentUsername);
        assertThat(prevUsername).isNotEqualTo(currentUsername);
    }
    @Test
    void shouldReturnBadRequestWhenSettingUsernameWithRestrictedWordForGoogleUser() throws Exception {
        String googleId = "TESTING";
        String username = "AdMin123";

        mockMvc.perform(post("/api/v1/google/" + googleId));

        String prevUsername = uNameRepository.findById(googleId).get().getName();

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + username))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Contained restricted word."));

        String currentUsername = uNameRepository.findById(googleId).get().getName();

        assertThat(prevUsername).isEqualTo(currentUsername);
        assertThat(username).isNotEqualTo(currentUsername);
    }
    @Test
    void shouldReturnConflictWhenChangingUsernameTooSoonForGoogleUser() throws Exception {
        String googleId = "TESTING";
        String username = "good_morning";
        String secondUsername = "good_evening";

        mockMvc.perform(post("/api/v1/google/" + googleId));

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + username));

        LocalDate lastUpdate = uNameRepository.findById(googleId).get().getLastUpdate().toLocalDate();
        String newUpdateDate = LocalDateTime.of(lastUpdate.plusMonths(1),
                LocalTime.of(0,0,0,0)).toString();

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + secondUsername))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(newUpdateDate));

        String currentUsername = uNameRepository.findById(googleId).get().getName();

        assertThat(username).isEqualTo(currentUsername);
        assertThat(secondUsername).isNotEqualTo(currentUsername);
    }
    @Test
    void shouldReturnNotFoundWhenAnonimUserTriesToSetUsername() throws Exception {
        String username = "good_morning";

        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);
        User anonimUser = userRepository.findById(userDTO.getUserId()).get();

        String anonimUserId = anonimUser.getUserId();

        String anonimUsername = uNameRepository.findById(anonimUserId).get().getName();

        mockMvc.perform(put("/api/v1/set_name/" + anonimUserId + "/" + username))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Passed id belongs to guest."));

        String anonimUsernameAfterRequest = uNameRepository.findById(anonimUserId).get().getName();

        assertThat(anonimUsername).isEqualTo(anonimUsernameAfterRequest);
    }
}
