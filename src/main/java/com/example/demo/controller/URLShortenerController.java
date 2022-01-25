package com.example.demo.controller;

import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import static com.example.demo.dao.Dao.getOriginalURL;
import static com.example.demo.dao.Dao.getShortURL;

@RestController
public class URLShortenerController {

    @PostMapping(value = "/generate", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Response> generateLink(@RequestBody Request req) {
        return new ResponseEntity<>(new Response(getShortURL(req.getUrl())), HttpStatus.OK);
    }

    @GetMapping(value = "/goto/url")
    @ResponseBody
    public RedirectView gotoUrl(@RequestParam String urlShortener) {
        return new RedirectView(getOriginalURL(urlShortener));
    }
}