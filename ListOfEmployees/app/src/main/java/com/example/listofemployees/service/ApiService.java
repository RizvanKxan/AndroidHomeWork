package com.example.listofemployees.service;

import com.example.listofemployees.service.entity.User;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {

    @GET("users")
    Call<List<User>> getUsers();

    class Factory {

        public static ApiService create() {
            Gson gson = new Gson();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            return retrofit.create(ApiService.class);
        }
    }
}