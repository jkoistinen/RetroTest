package com.example.jk.retrotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jk.retrotest.model.Repo;
import com.example.jk.retrotest.network.GitHubService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello = (TextView) findViewById(R.id.TextView_hello);

        //Converter
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        //Retrofit instance (should not be in this file)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //GitHubService instance
        GitHubService service = retrofit.create(GitHubService.class);


        Call<List<Repo>> repos = service.listRepos("mumindalen");

        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> body = response.body();

                for(int i = 0; i < body.size(); i++ ){
                 Repo repo = body.get(i);

                    Log.d("TAG", repo.sshUrl);

                }

                hello.setText("");
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                hello.setText(t.getMessage());
            }
        });

    }
}
