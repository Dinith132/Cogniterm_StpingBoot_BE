package com.test.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.model.SavedApi;
import com.test.demo.service.ApiService;
import com.test.demo.controller.dto.SaveApiRequest;

@RestController
@RequestMapping("/api/gapi")
public class ApiController {

    private final ApiService savedApiService;

    public ApiController(ApiService savedApiService) {
        this.savedApiService = savedApiService;
    }

    // Save new API
    @PostMapping("/saveApi")
    public ResponseEntity<SavedApi> saveApi(@RequestBody SaveApiRequest request) {
        SavedApi savedApi = savedApiService.saveApi(request.getApiName(), request.getApiKey());
        return ResponseEntity.ok(savedApi);
    }

    @GetMapping("/getApi")
    public ResponseEntity<List<SavedApi>> listApis() {
        List<SavedApi> apis = savedApiService.getApisByUser();
        return ResponseEntity.ok(apis);
    }





}
