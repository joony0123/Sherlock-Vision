package com.sherlockvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity {


    public static ArrayList<String> strArray;
    public static ArrayAdapter<String> arrayAdapter;
    public static ListView serieslistview;
    public ListView mListView;
    ImageView logoPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //get extras
        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("imageURL");
        final String logoName = intent.getStringExtra("logoName");
         String companyURL = intent.getStringExtra("companyURL");

        String logoContents = intent.getStringExtra("logoContents");

        //for logo image, if there is url received
        if(imageURL.length() > 0) {
            logoPic = (ImageView) findViewById(R.id.logoPic);
            new DownloadImage().execute(imageURL);
//            Picasso.with(getApplicationContext()).load(imageURL).placeholder(R.mipmap
//                    .ic_launcher).into(logoPic);
        }

        TextView nameView = (TextView) findViewById(R.id.logoName);

        // make it scrollable
        TextView contentsView = (TextView) findViewById(R.id.logoContents);
        contentsView.setMovementMethod(new ScrollingMovementMethod());

        TextView website = (TextView) findViewById(R.id.website);
        if(logoName.length() > 0)
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://news.google.com/news/search/section/q/"+ logoName+ "/"+ logoName+ "?hl=en&gl=US&ned=us"));
                    startActivity(browserIntent);
                }
            });

        nameView.setText(logoName);
        contentsView.setText(logoContents);

        if (!companyURL.startsWith("http://") && !companyURL.startsWith("https://"))
            companyURL = "http://" + companyURL;
        final String urlForCompany = companyURL;

        if(logoPic != null)
        logoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlForCompany));
                startActivity(browserIntent);
            }
        });

//        strArray = new ArrayList<String>();
//
//        //Button btaddnewseries = (Button) findViewById(R.id.button1);
//        serieslistview = (ListView) findViewById(R.id.list1);
//
//        strArray.add("Item One");
//
//        arrayAdapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, strArray);
//
//        serieslistview.setAdapter(arrayAdapter);
//        arrayAdapter.notifyDataSetChanged();

        // Get data to display
       // final ArrayList<News> recipeList = News.getRecipesFromFile("recipes.json", this);





//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                // Do something after 5s = 5000ms
                final ArrayList<News> recipeList = News.newsList;
                // Create adapter
                NewsAdapter adapter = new NewsAdapter(getApplicationContext(), recipeList);

                // Create list view
                mListView = (ListView) findViewById(R.id.newsList);
                mListView.setAdapter(adapter);
                TextView emptyText = (TextView)findViewById(R.id.empty);
                emptyText.setText("No related news articles found for " + logoName + "\n\n** Please use 'More News...' link for further search.");
                emptyText.setTextSize(30);
        mListView.setEmptyView(emptyText);

//            }
//        }, 1000);



        // Set what happens when a list view item is clicked
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News selectedRecipe = recipeList.get(position);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedRecipe.instructionUrl));
                startActivity(browserIntent);


            }

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
          //  mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
        //    mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
          //  mProgressDialog.setMessage("Loading...");
          //  mProgressDialog.setIndeterminate(false);
            // Show progressdialog
           // mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            logoPic.setImageBitmap(result);
            // Close progressdialog
            //mProgressDialog.dismiss();
        }
    }

}
