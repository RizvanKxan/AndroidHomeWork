package com.example.listofemployees;

import android.os.Handler;
import android.os.Looper;

import com.example.listofemployees.service.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RestService {
    private final Gson gson;
    private final ExecutorService executorService;

    private final Handler handler = new Handler(Looper.getMainLooper());

    public RestService(Gson gson, ExecutorService executorService) {
        this.gson = gson;
        this.executorService = executorService;
    }

    public void getUsers(Result<List<User>> listener) {
        executorService.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/users");
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(8000);
                connection.setRequestMethod("GET");

                inputStream = connection.getInputStream();
                String data = readInputStream(inputStream);

                Type type = new TypeToken<List<User>>() {
                }.getType();
                List<User> users = gson.fromJson(data, type);
                handler.post(() -> listener.onSuccess(users));
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> listener.onError(e));
            }
        });
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public interface Result<T> {
        void onSuccess(T t);
        void onError(Exception e);
    }
}
