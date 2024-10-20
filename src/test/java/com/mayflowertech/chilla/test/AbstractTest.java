package com.mayflowertech.chilla.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayflowertech.chilla.entities.User;

public abstract class AbstractTest<T> {  
	
  private static String cachedToken;
  protected String token;
  protected MockMvc mvc;
  protected String baseUri = "/karuthal/api/v1/";
  protected ObjectMapper objectMapper;
  @Autowired
  protected WebApplicationContext webApplicationContext;
    
    protected void setUp() throws Exception {             
           mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
           token = TokenAuthenticationServiceForTest.createToken("systemadmin", "systemadmin");  
           objectMapper =  new ObjectMapper();
           objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
           
           if (cachedToken == null) {
               // Example login to set up token once
               User user = new User();
               user.setUsername("systemadmin");
               user.setPassword("systemadmin");
               String userJson = mapToJson(user);
               ResultActions resultActions = submitPost("/usermanagement/login", userJson);
               resultActions.andExpect(status().isOk());
               String responseJson = getJsonResponse(resultActions);
               User loggedInUser = objectMapper.readValue(responseJson, User.class);
               cachedToken = loggedInUser.getAuthtoken();
           }
    }
    
    
    protected String mapToJson(Object obj) throws JsonProcessingException {      
       return objectMapper.writeValueAsString(obj);
    }

    protected ResultActions submitDelete(String uri, String payload, String...pathparams) throws Exception {
        System.out.println("DELETE "+baseUri+uri);
        System.out.println("payload :"+payload);
        for(String param : pathparams) {
            System.out.println("pathparam :"+param);
        }
                ResultActions result = mvc.perform(MockMvcRequestBuilders.delete(baseUri+uri, pathparams)
                 .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer  "+token)); 
        //result.andDo(print());
        System.out.println(result.andReturn().getResponse().getStatus()+"  " +result.andReturn().getResponse().getContentAsString());
        return result;
    }

    
    protected ResultActions submitPut(String uri,  String payload, String...pathparams) throws Exception {
        System.out.println("PUT "+baseUri+uri);
        System.out.println("payload :"+payload);
        for(String param : pathparams) {
            System.out.println("pathparam :"+param);
        }
                ResultActions result = mvc.perform(MockMvcRequestBuilders.put(baseUri+uri, pathparams)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer  "+token)); 
        //result.andDo(print());
        System.out.println(result.andReturn().getResponse().getStatus() +"  " +result.andReturn().getResponse().getContentAsString());
        return result;
    }

    protected ResultActions submitPostWithQueryParam(String uri, Map<String, String> queryMap,  String...pathparams) throws Exception {
        System.out.println("POST "+baseUri+uri);
        for(String param : pathparams) {
            System.out.println("pathparam :"+param);
        }
        
     MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(baseUri+uri, pathparams)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer  "+token);
     
     for(String key : queryMap.keySet()) {
         requestBuilder.param(key, queryMap.get(key));
         System.out.println("Attaching  query param "+key+"  \n "+queryMap.get(key));
     }
    ResultActions result = mvc.perform(requestBuilder);  
                
        System.out.println(result.andReturn().getResponse().getStatus()+  "  "+result.andReturn().getResponse().getContentAsString());   
        return result;
    }

    
    protected ResultActions submitPost(String uri,  String payload, String...pathparams) throws Exception {
        System.out.println("POST "+baseUri+uri);
        System.out.println("payload :"+payload);
        for(String param : pathparams) {
            System.out.println("pathparam :"+param);
        }
        ResultActions result = mvc.perform(MockMvcRequestBuilders.post(baseUri+uri, pathparams)
        .content(payload)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer  "+token)); 
        //result.andDo(print());
                
        System.out.println(result.andReturn().getResponse().getStatus()+  "  "+result.andReturn().getResponse().getContentAsString());   
        return result;
    }
    
    protected ResultActions submitGet(String uri,  String...pathparams) throws Exception {
            System.out.println("GET "+baseUri+uri);
            if(pathparams != null)
            for(String param : pathparams) {
                System.out.println("pathparam :"+param);
            }
          ResultActions result = mvc.perform(MockMvcRequestBuilders.get(baseUri+uri, pathparams)
                    .header("Authorization", "Bearer  "+token));
          System.out.println(result.andReturn().getResponse().getStatus());  
          return result;
    }
    
    protected String getJsonResponse(ResultActions actions) throws Exception {
        return actions.andReturn().getResponse().getContentAsString();
    }
    
    
   
    
    protected List<T> getListOfObjectsFromJson(String json) throws Exception{
        List<T> list = objectMapper.readValue(json, new TypeReference<List<T>>() {});
        return list;
    }
    
    public static  String getRandomName(String name) {
        String id = UUID.randomUUID().toString();
        return name+"-"+id.substring(0, 15);
    }

}
