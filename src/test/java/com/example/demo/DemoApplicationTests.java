package com.example.demo;

import com.example.demo.controller.URLShortenerController;
import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void getHello() throws Exception {
        var url = "abcde";
        var inputRequest = new Request(url);
        ObjectMapper Obj = new ObjectMapper();
        String jsonInputRequest = Obj.writeValueAsString(inputRequest);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/generate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonInputRequest);

        var response = mvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        var link = Obj.readValue(response.getResponse().getContentAsString(), Response.class).getUrlShortener();

        builder = MockMvcRequestBuilders
                .get("/goto/url?urlShortener={urlShortener}", link)
                .contentType(MediaType.ALL);

        var redirectMvc = standaloneSetup(new URLShortenerController())
                .alwaysExpect(status().isFound()).build();

        redirectMvc.perform(builder)
                .andExpect(redirectedUrl(url))
                .andExpect(status().isFound());
    }
}
