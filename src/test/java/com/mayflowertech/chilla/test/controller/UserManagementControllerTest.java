package com.mayflowertech.chilla.test.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mayflowertech.chilla.TeamWebApplication;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.test.AbstractTest;

//
//@SpringBootTest(classes = TeamWebApplication.class)
//@ExtendWith(SpringExtension.class)
//@AutoConfigureMockMvc

public class UserManagementControllerTest extends AbstractTest<User> {

    @BeforeEach
    public void setUp() {
    	System.out.println("====UserManagementControllerTest=======");    
    	 mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        
        // Send POST request to the login API
        ResultActions resultActions = submitGet("/usermanagement/users");
        String responseJson = getJsonResponse(resultActions);
        System.out.println(responseJson);
        
        // Check if the status is OK (200)
        resultActions.andExpect(status().isOk());

      
    }
}
