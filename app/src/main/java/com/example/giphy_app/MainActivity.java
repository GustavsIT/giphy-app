package com.example.giphy_app;



import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textViewForSearchData;
    private EditText inputSearchText;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Gif> gifList;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int REQUEST_DELAY_MILLISECONDS = 300;
    private GifAdapter adapter;
    private int offset = 0;
    private final int limit = 10;
    private boolean isLoading = false;
    private GifApiClient gifApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        setupRequestQueue();
        gifList = new ArrayList<>();
        adapter = new GifAdapter(this, gifList);
        recyclerView.setAdapter(adapter);
        setupRecyclerViewScrollListener();
        gifApiClient = new GifApiClient(this);
    }

    private void initializeViews() {
        textView = findViewById(R.id.textView);
        textViewForSearchData = findViewById(R.id.textViewForSearchData);
        inputSearchText = findViewById(R.id.inputSearchText);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRequestQueue() {
        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();
    }

    private void setupRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });
    }

    private String inputToString(){
        String searchText = inputSearchText.getText().toString().toLowerCase().replaceAll("\\s", "+");
        return searchText;
    }

    private void loadMoreData(){
        isLoading=true;
        offset+=limit;
        fetchGifs(inputToString());
    }

    public void updateData (View view){
        textViewForSearchData.setText("Search for: " + inputSearchText.getText());

        handler.removeCallbacksAndMessages(null);
        if (!inputToString().isEmpty()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifList.clear();
                    fetchGifs(inputToString());
                }
            }, REQUEST_DELAY_MILLISECONDS);
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchGifs(String searchQuery) {
        gifApiClient.fetchGifs(searchQuery, this::addGifs, this::handleError);
    }

    private void addGifs(List<Gif> gifs) {
        gifList.addAll(gifs);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    private void handleError(VolleyError error) {
        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

}