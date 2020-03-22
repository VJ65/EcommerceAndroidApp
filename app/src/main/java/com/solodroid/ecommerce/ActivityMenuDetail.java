package com.solodroid.ecommerce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class ActivityMenuDetail extends Activity {
	
	ImageView imgPreview;
	TextView txtText, txtSubText;
	WebView txtDescription;
	Button btnAdd;
	ScrollView sclDetail;
	ProgressBar prgLoading;
	TextView txtAlert;
	
	// declare dbhelper object
	static DBHelper dbhelper;
	
	// declare ImageLoader object
	ImageLoader imageLoader;
	
	// declare variables to store menu data
	String Menu_image, Menu_name, Menu_serve, Menu_description;
	double Menu_price;
	int Menu_quantity;
	long Menu_ID;
	String MenuDetailAPI;
	int IOConnect = 0;
	
	// create price format
	DecimalFormat formatData = new DecimalFormat("#.##");
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_detail);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setTitle("Detail Menu");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);
        txtDescription = (WebView) findViewById(R.id.txtDescription);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        //btnShare = (Button) findViewById(R.id.btnShare);
        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);

        // get screen device width and height
        DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int wPix = dm.widthPixels;
		int hPix = wPix / 2 + 50;
		
		// change menu image width and height
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wPix, hPix);
        imgPreview.setLayoutParams(lp);
        
        imageLoader = new ImageLoader(ActivityMenuDetail.this);
        dbhelper = new DBHelper(this);
		
		// get menu id that sent from previous page
        Intent iGet = getIntent();
        Menu_ID = iGet.getLongExtra("menu_id", 0);
        
        // Menu detail API url
        MenuDetailAPI = Constant.MenuDetailAPI+"?accesskey="+Constant.AccessKey+"&menu_id="+Menu_ID;
        
        // call asynctask class to request data from server
        new getDataTask().execute();      
        
        // event listener to handle add button when clicked
        btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// show input dialog
				inputDialog();
			}
		});
        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.cart:
			// refresh action
			Intent iMyOrder = new Intent(ActivityMenuDetail.this, ActivityCart.class);
			startActivity(iMyOrder);
			overridePendingTransition (R.anim.open_next, R.anim.close_next);
			return true;
			
		case android.R.id.home:
            // app icon in action bar clicked; go home
        	this.finish();
        	overridePendingTransition(R.anim.open_main, R.anim.close_next);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
    
    // method to show number of order form
    void inputDialog(){
    	
    	// open database first
    	try{
			dbhelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	
    	alert.setTitle(R.string.order);
    	alert.setMessage(R.string.number_order);
    	alert.setCancelable(false);
    	final EditText edtQuantity = new EditText(this);
    	int maxLength = 3;    
    	edtQuantity.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
    	edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
    	alert.setView(edtQuantity);
    	
    	alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		String temp = edtQuantity.getText().toString();
    		int quantity = 0;
    		
    		// when add button clicked add menu to order table in database
    		if(!temp.equalsIgnoreCase("")){
    			quantity = Integer.parseInt(temp);
    			if(dbhelper.isDataExist(Menu_ID)){
    	        		dbhelper.updateData(Menu_ID, quantity, (Menu_price*quantity));
    	    		}else{
    	    			dbhelper.addData(Menu_ID, Menu_name, quantity, (Menu_price*quantity));
    	    		}
    		}else{
    			dialog.cancel();
    		}       	  		
    	  }
    	});

    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {

      			// when cancel button clicked close dialog
    		  	dialog.cancel();
    	  }
    	});

    	alert.show();
    }
    
    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void>{
    	
    	// show progressbar first
    	getDataTask(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(0);
				txtAlert.setVisibility(8);
    		}
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// parse json data from server in background
			parseJSONData();
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// when finish parsing, hide progressbar
			prgLoading.setVisibility(8);
			// if internet connection and data available show data
			// otherwise, show alert text
			if((Menu_name != null) && IOConnect == 0){
				sclDetail.setVisibility(0);
			
				imageLoader.DisplayImage(Constant.AdminPageURL+Menu_image, imgPreview);
				
				txtText.setText(Menu_name);
				txtSubText.setText("Price : " +Menu_price+" "+ActivityMenuList.Currency+"\n"+"Status : "+Menu_serve+"\n"+"Stock : "+Menu_quantity);
				txtDescription.loadDataWithBaseURL("", Menu_description, "text/html", "UTF-8", "");
		        txtDescription.setBackgroundColor(Color.parseColor("#e7e7e7"));
			}else{
				txtAlert.setVisibility(0);
			}
		}
    }

    // method to parse json data from server
    public void parseJSONData(){
    	
    	try {
    		// request data from menu detail API
	        HttpClient client = new DefaultHttpClient();
	        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
	        HttpUriRequest request = new HttpGet(MenuDetailAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();

			
			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
		        
	        String line;
	        String str = "";
	        while ((line = in.readLine()) != null){
	        	str += line;
	        }
        
	        // parse json data and store into tax and currency variables
			JSONObject json = new JSONObject(str);
			JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part
				
			for (int i = 0; i < data.length(); i++) {
			    JSONObject object = data.getJSONObject(i); 
			    
			    JSONObject menu = object.getJSONObject("Menu_detail");
			    
			    Menu_image = menu.getString("Menu_image");
			    Menu_name = menu.getString("Menu_name");
			    Menu_price = Double.valueOf(formatData.format(menu.getDouble("Price")));
			    Menu_serve = menu.getString("Serve_for");
			    Menu_description = menu.getString("Description");
			    Menu_quantity = menu.getInt("Quantity");
				    
			}
				
				
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
			IOConnect = 1;
		    e.printStackTrace();
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}	
    }

	
    // close database before back to previous page
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	dbhelper.close();
    	finish();
    	overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
	

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//imageLoader.clearCache();
    	super.onDestroy();
    }
	 
    
    @Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
    
    
}
