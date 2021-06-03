package me.hwanse.jwtdemo.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hwanse.jwtdemo.controller.dto.JoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("API 회원 가입 테스트 - success")
  public void join() throws Exception {
    // given
    JoinRequest joinRequest = new JoinRequest("test@gmail.com", "test",
      "test", "test");

    // when
    mockMvc.perform(post("/api/join")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(objectMapper.writeValueAsString(joinRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success", is(true)))
      .andExpect(jsonPath("$.response.email", is(joinRequest.getEmail())))
      .andExpect(jsonPath("$.response.name", is(joinRequest.getUsername())))
      .andExpect(jsonPath("$.response.nickname", is(joinRequest.getNickname())))
      .andExpect(jsonPath("$.response.authorities.[0].name", is("ROLE_USER")))
      .andExpect(jsonPath("$.response.createAt").exists())
      .andExpect(jsonPath("$.response.modifiedAt").exists())
      .andExpect(jsonPath("$.response.deletedAt").isEmpty())
      .andExpect(jsonPath("$.response.beDeleted", is(false)))
      .andExpect(jsonPath("$.error").doesNotExist());

  }

  @Test
  @DisplayName("API 회원 가입 테스트 - failure")
  public void join_fail() throws Exception {
    // given
    JoinRequest joinRequest = new JoinRequest("testgmail.com", "test",
      "test", null);

    // when
    mockMvc.perform(post("/api/join")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(joinRequest)))
      .andExpect(status().isBadRequest());
  }

}