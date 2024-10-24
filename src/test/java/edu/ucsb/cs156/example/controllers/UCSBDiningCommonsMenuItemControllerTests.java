package edu.ucsb.cs156.example.controllers;
import java.util.Optional;
import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBDate;
import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItem;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemRepository;
//import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.x509;

import java.time.LocalDateTime;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBDiningCommonsMenuItemController.class)
@Import(TestConfig.class)


public class UCSBDiningCommonsMenuItemControllerTests extends ControllerTestCase{
    @MockBean
    UCSBDiningCommonsMenuItemRepository repo;

    @MockBean
    UserRepository userRepository;
    
    @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_UCSBDiningCommonsMenuItems() throws Exception {

                

            UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
                                .name("Taco")
                                .station("East")
                                .diningCommonsCode("DLG")
                                .build();

                
            UCSBDiningCommonsMenuItem Item2 = UCSBDiningCommonsMenuItem.builder()
                                .name("Rice")
                                .station("West")
                                .diningCommonsCode("Ortega")
                                .build();

                ArrayList<UCSBDiningCommonsMenuItem> expectedItems = new ArrayList<>();
                expectedItems.addAll(Arrays.asList(Item1, Item2));

                when(repo.findAll()).thenReturn(expectedItems);

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(repo, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedItems);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_UCSBDiningCommonsMenuItem() throws Exception {
                

            UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("DLG")
                                .name("Taco")
                                .station("East")
                                .build();

                when(repo.save(eq(Item1))).thenReturn(Item1);

                
                MvcResult response = mockMvc.perform(
                                post("/api/ucsbdiningcommonsmenuitem/post?name=Taco&station=East&diningCommonsCode=DLG")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(repo, times(1)).save(Item1);
                String expectedJson = mapper.writeValueAsString(Item1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("DLG")
                                .name("Taco")
                                .station("East")
                                .build();

                when(repo.findById(Item1.getId())).thenReturn(Optional.of(Item1));

                // act
                
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem?id="+Item1.getId()))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(repo, times(1)).findById(Item1.getId());
                String expectedJson = mapper.writeValueAsString(Item1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exists() throws Exception {

        UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
            .diningCommonsCode("DLG")
            .name("Taco")
            .station("East")
            .build();
        long x=2121;        
        when(repo.findById(Item1.getId())).thenReturn(Optional.of(Item1));

                // act
                
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem?id=9"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                //verify(repo, times(1)).findById(Item1.getId());
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("UCSBDiningCommonsMenuItem with id 9 not found", json.get("message"));
        }
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_edit_an_existing_ucsbdate() throws Exception {
                // arrange

                UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
                                .name("Taco")
                                .station("East")
                                .diningCommonsCode("DLG")
                                .build();

                UCSBDiningCommonsMenuItem Item2 = UCSBDiningCommonsMenuItem.builder()
                                .name("Burrito")
                                .station("West")
                                .diningCommonsCode("Ortega")
                                .build();

                String requestBody = mapper.writeValueAsString(Item2);

                when(repo.findById(Item1.getId())).thenReturn(Optional.of(Item1));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ucsbdiningcommonsmenuitem?id="+Item1.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(repo, times(1)).findById(Item1.getId());
                verify(repo, times(1)).save(Item2); // should be saved with correct user
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_cannot_edit_item_that_does_not_exist() throws Exception {
                

                UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
                        .name("Taco")
                        .station("East")
                        .diningCommonsCode("DLG")
                        .build();

                String requestBody = mapper.writeValueAsString(Item1);

                when(repo.findById(Item1.getId())).thenReturn(Optional.of(Item1));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ucsbdiningcommonsmenuitem?id=5")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                long x=5;
                verify(repo, times(1)).findById(x);
                Map<String, Object> json = responseToJson(response);
                assertEquals("Restaurant with id 5 not found", json.get("message"));

        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_not_delete_nonexisiting_item() throws Exception {
                

                UCSBDiningCommonsMenuItem Item1 = UCSBDiningCommonsMenuItem.builder()
                        .name("Taco")
                        .station("East")
                        .diningCommonsCode("DLG")
                        .build();

                when(repo.findById(Item1.getId())).thenReturn(Optional.of(Item1));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbdiningcommonsmenuitem?id="+Item1.getId())
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(repo, times(1)).findById(Item1.getId());
                verify(repo, times(1)).delete(any());

                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItem with id 0 deleted", json.get("message"));
        }
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_tries_to_delete_non_existant_item_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(repo.findById(eq(14L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbdiningcommonsmenuitem?id=14")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(repo, times(1)).findById(14L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItem with id 14 not found", json.get("message"));
        }
}