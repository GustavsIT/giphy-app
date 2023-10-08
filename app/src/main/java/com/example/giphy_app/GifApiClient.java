package com.example.giphy_app;

import android.content.Context;
import android.net.Uri;
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
import java.util.function.Consumer;

public class GifApiClient {
    private Context context;
    private RequestQueue requestQueue;

    public GifApiClient(Context context) {
        this.context = context;
        this.requestQueue = VolleySingleton.getmInstance(context).getRequestQueue();
    }

    public void fetchGifs(String searchQuery, Consumer<List<Gif>> onSuccess, Consumer<VolleyError> onError) {
        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(searchQuery, onSuccess, onError);
        requestQueue.add(jsonObjectRequest);
    }

    private String generateUrl(String searchQuery) {
        Uri.Builder builder = Uri.parse(Constants.BASE_URL).buildUpon();
        builder.appendQueryParameter("q", searchQuery)
                .appendQueryParameter("api_key", Constants.API_KEY)
                .appendQueryParameter("limit", String.valueOf(10))
                .appendQueryParameter("offset", String.valueOf(0));

        return builder.build().toString();
    }

    private JsonObjectRequest createJsonObjectRequest(String searchQuery, Consumer<List<Gif>> onSuccess, Consumer<VolleyError> onError) {
        String url = generateUrl(searchQuery);
        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Gif> gifs = parseGifData(response);
                        onSuccess.accept(gifs);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onError.accept(error);
                    }
                });
    }

    private List<Gif> parseGifData(JSONObject response) {
        List<Gif> gifs = new ArrayList<>();
        try {
            JSONArray dataArray = response.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                JSONObject images = dataObject.getJSONObject("images");
                JSONObject fixedHeight = images.getJSONObject("fixed_height");
                String gifUrl = fixedHeight.getString("url");
                String title = dataObject.getString("title");
                Gif gif = new Gif(gifUrl, title);
                gifs.add(gif);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gifs;
    }
}
