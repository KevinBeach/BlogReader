package com.keseni.blogreader;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainListActivity extends ListActivity {

    protected String[] mBlogPostTitles;
    public static final int NUMBER_OF_POSTS = 20;
    public static final String TAG = MainListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

         if(isNetworkAvailable()) {
             GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
             getBlogPostsTask.execute();
         }
        else{
             Toast.makeText(this,"Network is unavailable!",Toast.LENGTH_LONG).show();
         }


    }

    //method to check if there is a network
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        Boolean isAvailable = false;
        //if network isn't present at all the networkInfo object will be null
        //isConnected method returns true if connectd to internet
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // ASync task uses Object for parameter in donbackground, void for the task measurement, sting for the return
    private class GetBlogPostsTask extends AsyncTask<Object,Void,String>{

        @Override
        protected String doInBackground(Object[] params) {
            int responseCode = -1;
            try {
                URL blogFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
                HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
                connection.connect();
                responseCode = connection.getResponseCode();
                Log.i(TAG, "Code : " + responseCode);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Exception caught: ",e);
                //e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "Exception caught: ",e);
                e.printStackTrace();
            } catch (Exception e){
                Log.e(TAG, "Exception caught: ",e);
            }
            return "Code: " + responseCode;
        }
    }
}

