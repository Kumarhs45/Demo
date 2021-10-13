package in.kumar.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String dataLink = "https://demo3231717.mockable.io/coinlist";
    RelativeLayout progressBar;
    RecyclerView recyclerView;
    CoinAdapter coinAdapter;
    List<CoinModelClass> coinModelClasses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (RelativeLayout) findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        coinModelClasses = new ArrayList<>();
        getTheDataFromServer();
    }

    private void getTheDataFromServer() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, dataLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("frenj", response.toString());
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("result");
                    String msg = json.getString("msg");
                    String data = json.getString("data");
                    JSONObject dataJson = new JSONObject(data);
                    String totalItems = dataJson.getString("totalItems");
                    String startIndex = dataJson.getString("startIndex");
                    String itemsPerPage = dataJson.getString("itemsPerPage");
                    String list = dataJson.getString("list");

                    parseDetailsFromServer(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Please Check your connectivity", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(MainActivity.this).add(request);
    }
    private void parseDetailsFromServer(String response) {
        try {
            JSONArray array = new JSONArray(response);
            Log.d("demoResponse", "--" + response.toString());
            for (int i = 1; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                String _id = jsonObject.getString("_id");

                String pictures = jsonObject.getString("pictures");
                JSONObject pictureJson = new JSONObject(pictures);
                String front = pictureJson.getString("front");
                JSONObject frontImageJson = new JSONObject(front);
                String key = frontImageJson.getString("key");
                String url = frontImageJson.getString("url");
                String sizeInMegaByte = frontImageJson.getString("sizeInMegaByte");

                String back = pictureJson.getString("back");
                JSONObject backImageJson = new JSONObject(back);
                String key2 = backImageJson.getString("key");
                String url2 = backImageJson.getString("url");
                String sizeInMegaByte2 = backImageJson.getString("sizeInMegaByte");

                String price = jsonObject.getString("price");
                String age = jsonObject.getString("age");
                String isGraded = jsonObject.getString("isGraded");
                String isSold = jsonObject.getString("isSold");
                String isCoin = jsonObject.getString("isCoin");
                String tags = jsonObject.getString("tags");
                String name = jsonObject.getString("name");
                String history = jsonObject.getString("history");
//                String year = jsonObject.getString("year");

                coinModelClasses.add(new CoinModelClass(url,name));

                }


        } catch (JSONException ex) {
            Log.d("demoResponseError",ex.getMessage());
            ex.printStackTrace();
        }
        coinAdapter = new CoinAdapter(coinModelClasses, MainActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerView.setAdapter(coinAdapter);
    }

}