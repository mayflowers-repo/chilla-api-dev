package com.mayflowertech.chilla.config;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

public class MailjetEmail {
    public static void main(String[] args) throws MailjetException, MailjetSocketTimeoutException, JSONException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        
        // Initialize the client
        client = new MailjetClient("4a1eb0e49b7fa9af762fdca14b0d116b", "11a3def575afbd137fdbfc8fca4cd6ef", new ClientOptions("v3.1"));
        
        // Create the request
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "ananniachilla@gmail.com")
                                        .put("Name", "Karuthal Team"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "maymonandroid@gmail.com")
                                                .put("Name", "passenger 1")))
                                .put(Emailv31.Message.TEMPLATEID, 6352835)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Verify Your Email with Anannia")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                	    .put("name", "James Bond")
                                	    .put("otp", "680015")))); 

        // Send the request
        response = client.post(request);
        
        // Output the response
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
