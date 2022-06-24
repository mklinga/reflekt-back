package com.mklinga.reflekt.journal.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.common.ApplicationTestConfiguration;
import com.mklinga.reflekt.common.TestAuthentication;
import com.mklinga.reflekt.journal.dtos.TagDataDto;
import com.mklinga.reflekt.journal.model.Tag;
import com.mklinga.reflekt.journal.services.TagService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TagController.class)
@Import(ApplicationTestConfiguration.class)
class TagControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TagService tagService;

  @Nested
  @DisplayName("getAllTags")
  public class GetAllTagsTest {

    @Test
    void getAllTagsReturnsUnauthenticatedWhenNotLoggedIn() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
          .andExpect(status().is4xxClientError());
    }

    List<Tag> getMockTagList() {
      Tag tag1 = new Tag();
      tag1.setId(UUID.fromString("e18154be-765f-4b27-a6dc-a8d90cf6b3df"));
      tag1.setOwner(TestAuthentication.testUser());
      tag1.setColor("#ff9900");
      tag1.setName("TAG 1");

      Tag tag2 = new Tag();
      tag2.setId(UUID.fromString("19ce638b-411e-4d3a-b2f9-0bdcd6f3d61c"));
      tag2.setOwner(TestAuthentication.testUser());
      tag2.setColor("#000001");
      tag2.setName("TAG 2");

      return List.of(tag1, tag2);
    }

    @Test
    @WithUserDetails(TestAuthentication.testUserName)
    public void getAllTagsReturnsCorrectlyFormattedList() throws Exception {
      List<Tag> mockList = getMockTagList();
      when(tagService.getAllTagsForOwner(any())).thenReturn(mockList);
      mockMvc.perform(MockMvcRequestBuilders.get("/tags/"))
          .andExpectAll(
              status().isOk(),
              content().json("""
                  [
                    {"id":"e18154be-765f-4b27-a6dc-a8d90cf6b3df","name":"TAG 1","color":"#ff9900"},
                    {"id":"19ce638b-411e-4d3a-b2f9-0bdcd6f3d61c","name":"TAG 2","color":"#000001"}
                  ]
                  """)
          );
    }
  }

  @Nested
  @DisplayName("addNewTag")
  public class AddNewTagTest {

    private static final String newTagName = "New Tag";
    private static final String newTagColor = "#123456";
    private static final UUID newTagId = UUID.fromString("8133ec81-ad0e-41b5-9bbd-1afeb5bd7ddf");

    private static final String postContentBody = "{\"name\":\"%s\",\"color\":\"%s\"}"
        .formatted(newTagName, newTagColor);

    private Tag createNewTag() {
      Tag newTag = new Tag();
      newTag.setId(newTagId);
      newTag.setName(newTagName);
      newTag.setColor(newTagColor);

      return newTag;
    }

    @BeforeEach
    public void setup() {
      Tag newTag = createNewTag();
      when(tagService.addNewTag(any(User.class), any(TagDataDto.class))).thenReturn(newTag);
    }

    @Test
    @WithUserDetails(TestAuthentication.testUserName)
    public void addNewTagReturnsCorrectJson() throws Exception {

      String expectedResponseBody = "{\"id\":\"%s\",\"name\":\"%s\",\"color\":\"%s\"}"
          .formatted(newTagId.toString(), newTagName, newTagColor);

      mockMvc
          .perform(post("/tags")
              .content(postContentBody)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().json(expectedResponseBody));
    }

    @Test
    @WithUserDetails(TestAuthentication.testUserName)
    public void addNewTagParsesUserAndTagFromRequest() throws Exception {
      mockMvc
          .perform(post("/tags")
              .content(postContentBody)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());

      ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
      ArgumentCaptor<TagDataDto> tagDataArgument = ArgumentCaptor.forClass(TagDataDto.class);
      verify(tagService).addNewTag(userArgument.capture(), tagDataArgument.capture());

      assertEquals(TestAuthentication.testUser(), userArgument.getValue());

      TagDataDto requestBody = tagDataArgument.getValue();
      assertEquals(newTagName, requestBody.getName());
      assertEquals(newTagColor, requestBody.getColor());
    }
  }
}