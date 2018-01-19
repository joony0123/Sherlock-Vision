package com.sherlockvision;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.os.AsyncTaskCompat;

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

public class News {


    public String title;
    public String description;
    public String imageUrl;
    public String instructionUrl;
    public String label;
    public static String logoName;

    public static ArrayList<News> newsList;

    public static ArrayList<News> temp(){
        ArrayList<News> recipeList = new ArrayList<>();

        for(int i = 0; i < 5; i++) {

            News recipe = new News();

            recipe.title = "title";
            recipe.description = "description";
            recipe.imageUrl = "image";
            recipe.instructionUrl = "url";
            recipe.label = "dietLabel";

            recipeList.add(recipe);
        }


        return recipeList;

    }

    public static void receiveNews(String name, String source){
        logoName = name;
        String url = "https://newsapi.org/v2/everything?apiKey=3e308dcceb67477a89367421726dd4cd&sortBy=relevance&q=" + name + "&sources=" + source;
        ReceiveNewsTask receive = new ReceiveNewsTask();
        AsyncTaskCompat.executeParallel(receive, url);

    }



    private static class ReceiveNewsTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... datas){
            try{

                // for debug worker thread
                if(android.os.Debug.isDebuggerConnected())
                    android.os.Debug.waitForDebugger();

                // create the file with the given file path
                File file = new File(datas[0]);
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while((readed = in.readLine()) != null){
                        JSONObject jObject = new JSONObject(readed);
                        //jObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                        return jObject;
                    }
                } else{
                    return null;
                }
                return null;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null){

                String url = "";


                try{
                    //iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    //   sentence = result.getJSONObject("query").getJSONObject("pages").getJSONObject("21721040").getString("extract");
//                    JSONArray pages = result.getJSONObject("query").getJSONObject("pages").names();
//                    JSONObject insidePages = result.getJSONObject("query").getJSONObject("pages").getJSONObject(pages.get(0).toString());
//                    JSONObject thumbnail = insidePages.getJSONObject("thumbnail");
//                    url = (String) thumbnail.get("source");

                    newsList = new ArrayList<>();

                    JSONArray articles = result.getJSONArray("articles");
                    int numberOfArticles = articles.length();
                    for(int i = 0; i < numberOfArticles; i++){
                        JSONObject article = articles.getJSONObject(i);

                        News news = new News();

                        news.title = article.getString("title");
                        news.description = article.getString("description");
                        news.imageUrl = article.getString("urlToImage");
                        news.instructionUrl = article.getString("url");

                        newsList.add(news);

                    }

                    if(newsList.size() == 0){
                        receiveNews(logoName, "business-insider");
                    }


                } catch (JSONException e){
                    e.printStackTrace();
                }






            }
        }


    } // end of class ReceiveWeather Task


    public static ArrayList<News> getRecipesFromFile(String filename, Context context){
        final ArrayList<News> recipeList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("recipes.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray recipes = json.getJSONArray("recipes");

            // Get Recipe objects from data
            for(int i = 0; i < recipes.length(); i++){
                News recipe = new News();

                recipe.title = recipes.getJSONObject(i).getString("title");
                recipe.description = recipes.getJSONObject(i).getString("description");
                recipe.imageUrl = recipes.getJSONObject(i).getString("image");
                recipe.instructionUrl = recipes.getJSONObject(i).getString("url");
                recipe.label = recipes.getJSONObject(i).getString("dietLabel");

                recipeList.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

}
