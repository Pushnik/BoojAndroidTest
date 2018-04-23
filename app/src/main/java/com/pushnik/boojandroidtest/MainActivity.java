package com.pushnik.boojandroidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by John Pushnik on 4/21/2018.

 Mobile App Test

 Goal:
 Create a simple application that will parse the JSON returned in the link below and populate a list view.
 Each list view row should contain the realtor’s photo, name, and phone number.
 A second page should be displayed when a row is clicked, showing a larger image of the realtor and
 additional information (such as their office phone number, cell number, office name).
 Feel free to use any library and/or framework in your project to help with the JSON parsing and other
 asynchronous networking tasks etc.

 Realtor list link:  https://www.denverrealestate.com/rest.php/mobile/realtor/list?app_key=f7177163c833dff4b38fc8d2872f1ec6

 Notes:
 To reduce load times you can specify the size of the image you want returned from the server.  The
 list view should load quickly and have thumbnail images, so something like
 http://d24m66tiq5iban.cloudfront.net/pics/realtor/25477/7125/width/50
 may be beneficial here, where as the realtor details page could display a larger image with
 http://d24m66tiq5iban.cloudfront.net/pics/realtor/25477/7125/width/200


 */

public class MainActivity extends AppCompatActivity implements PopupCallback {

    public static final String TAG = "MainActivity";
    public static final String REALTOR_URL = "https://www.denverrealestate.com/rest.php/mobile/realtor/list?app_key=f7177163c833dff4b38fc8d2872f1ec6";
    private RecyclerView recyclerView;
    private RealtorAdapter realtorAdapter;
    private List<Realtor> realtorList = new ArrayList();
    private Picasso picasso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        realtorAdapter = new RealtorAdapter(realtorList, this);
        recyclerView.setAdapter(realtorAdapter);

        picasso = new Picasso.Builder(this)
                .build();

        loadJSONFromAsset();
    }

    public void loadJSONFromAsset() {
//        try {
//            InputStream is = getResources().openRawResource(R.raw.list);
//            readStream(is);
//            realtorAdapter.notifyDataSetChanged();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            String message = "no data loaded";
//            Log.e("exception ", message, ex);
//            return;
//        }
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(REALTOR_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                try {
                    readStream(inputStream);
                } catch (IOException ex) {
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        realtorAdapter.notifyDataSetChanged();
                    }
                });
            }

        });
    }

    public void readStream(InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        Realtor[] realtors = new Gson().fromJson(reader, Realtor[].class);
        realtorList.addAll(Arrays.asList(realtors));
    }

    @Override
    public void loadImage(ImageView imageView, String url, int width) {
        picasso
                .with(this)
                .load(url + "/width/" + width)
                .resize(width, width)
                .centerInside()
                .into(imageView);
    }

    @Override
    public void showPopup(final View view, Realtor realtor) {
        final PopupWindow popupWindow;

        LayoutInflater inflater = getLayoutInflater();
        final View inflatedView = inflater.inflate(R.layout.dialog_popup, null);

        TextView firstName = (TextView) inflatedView.findViewById(R.id.first_name);
        TextView lastName = (TextView) inflatedView.findViewById(R.id.last_name);
        TextView phoneNumber = (TextView) inflatedView.findViewById(R.id.phone);
        TextView title = (TextView) inflatedView.findViewById(R.id.title);
        TextView office = (TextView) inflatedView.findViewById(R.id.office);

        firstName.setText(realtor.getFirstName());
        lastName.setText(realtor.getLastName());
        phoneNumber.setText(realtor.getPhoneNumber());
        title.setText(realtor.getTitle());
        office.setText(realtor.getOffice());

        ImageView imageView = (ImageView) inflatedView.findViewById(R.id.image_realtor);
        loadImage(imageView, realtor.getPhoto(), 300);

        popupWindow = new PopupWindow(
                inflatedView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        View close = inflatedView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
