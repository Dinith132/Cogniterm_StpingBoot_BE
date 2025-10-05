package com.test.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.model.SavedApi;
import com.test.demo.service.ApiService;

@RestController
@RequestMapping("/api/gapi")
public class ApiController {

    @Autowired
    private ApiService savedApiService;

    // Save new API
    @PostMapping("/save")
    public ResponseEntity<SavedApi> saveApi(
            // @RequestParam UUID userId,
            @RequestParam String apiName,
            @RequestParam String apiKey) {

        SavedApi savedApi = savedApiService.saveApi(apiName, apiKey);
        return ResponseEntity.ok(savedApi);
    }

@GetMapping("/list")
public ResponseEntity<List<SavedApi>> listApis() {
    List<SavedApi> apis = savedApiService.getApisByUser();
    return ResponseEntity.ok(apis);
}



}
