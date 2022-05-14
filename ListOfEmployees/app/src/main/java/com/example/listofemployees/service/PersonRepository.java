package com.example.listofemployees.service;

import com.example.listofemployees.service.entity.User;

import java.util.List;

import retrofit2.Call;

public class PersonRepository {
    private final ApiService apiService;

    public PersonRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<List<User>> getUsers() {
        return apiService.getUsers();
    }
}
