package com.mayflowertech.chilla.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayflowertech.chilla.TeamWebApplication;
import com.mayflowertech.chilla.entities.User;


@SpringBootTest(classes = TeamWebApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc

public class IntegrationTest extends AbstractTest<User> {
	 private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);
	 
    @BeforeEach
    public void setUp() {
    	System.out.println("====Integration suite =======");    
    	 mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void testIntegration() throws Exception {
    	testGetUser();
    	testExisitngUser();
    	testLogin();
    }
    

    public void testGetUser() throws Exception {
        // Prepare the username to query
        String username = "admin1"; 
        
        // Send POST request to retrieve user details
        ResultActions resultActions = submitPost("/usermanagement/users/" + username, "");
        String responseJson = getJsonResponse(resultActions);
        logger.info(responseJson);
        resultActions.andExpect(status().isOk());
    }
    
    
    public void testExisitngUser() throws Exception {       
    	String payload = "{   \"username\" : \"junituser1\", \"password\" : \"junituser1\", \"email\" : \"junituser1@test.com\"  }";
        // Send POST request to retrieve user details
        ResultActions resultActions = submitPost("/usermanagement/users/",  payload);
        String responseJson = getJsonResponse(resultActions);
        logger.info(responseJson);
        resultActions.andExpect(result -> {
            int status = result.getResponse().getStatus();
            if (status == 409 || status == 405) {
                logger.info("User exists");
            } else if (status != 200 && status != 201 && status != 202) {
                throw new AssertionError("Expected status 200, 201, or 202 but got " + status);
            }
        });
    }
 
    
    public void testLogin() throws Exception {
        String payload = "{ \"username\" : \"junituser1\", \"password\" : \"junituser1\" }";
        
        ResultActions resultActions = submitPost("/usermanagement/login", payload);
        
        String responseJson = getJsonResponse(resultActions);
        logger.info(responseJson);

        resultActions.andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("\"authtoken\""), "Response should contain an auth token");

            // Optionally, you can parse the JSON and assert specific fields
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            String authToken = jsonNode.get("authtoken").asText();
            assertNotNull(authToken, "Auth token should not be null");

            // Additional assertions based on your business logic
            assertEquals("junituser1", jsonNode.get("username").asText(), "Username should match");
            
        });
    }
    
    

 
    
    
}