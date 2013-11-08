package com.awss3manager.main;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BucketActivity extends Activity{

	private static final String TAG = "BucketAct";
	private AmazonS3Client amazonS3Client;
	private List<Bucket> listBucket = new ArrayList<Bucket>();
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
    	PropertiesCredentials propCred;
    	BasicAWSCredentials basicCred;
    	
    	
        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.CRED_PATH);
        try {
			propCred = new PropertiesCredentials(new File(path));
			basicCred = new BasicAWSCredentials(propCred.getAWSAccessKeyId(), propCred.getAWSSecretKey());
			amazonS3Client = new AmazonS3Client(basicCred);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
                Log.d(TAG,"before list bucket");
        S3ListBucketTask bucketTask = new S3ListBucketTask();
        bucketTask.getMainThread(this);
        bucketTask.execute(amazonS3Client);

	}
	
	public void setBucketList(List<Bucket> list){
		listBucket = list;
	}
	public List<Bucket> getBucketList(){
		return listBucket;
	}
	
	public void genBucketSpinner(){
		Spinner spinner = (Spinner)findViewById(R.id.spinner_bucket);
		ArrayAdapter <String> adapter =
      		  new ArrayAdapter <String> (this, android.R.layout.simple_spinner_item );	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Iterator<Bucket> itrBucket;
		itrBucket = listBucket.iterator();
		if(listBucket.isEmpty())
        	;
        else{
        	while(itrBucket.hasNext())
        		adapter.add(itrBucket.next().getName().toString());
        }
        	
        
        spinner.setAdapter(adapter);
	}
	private class S3ListBucketTask extends AsyncTask<AmazonS3Client,Void,List<Bucket>>{

		private BucketActivity mMainThreadRef;
		public void getMainThread(BucketActivity BA){
			mMainThreadRef = BA;
		}
		@Override
		protected List<Bucket> doInBackground(AmazonS3Client... params) {
			// TODO Auto-generated method stub
			if(params == null)
				return null;
			AmazonS3Client awsS3Client = params[0];
			
			List<Bucket> resultListBucket = new LinkedList<Bucket>();
			resultListBucket = awsS3Client.listBuckets();
			return resultListBucket;
		}
		
		protected void onPostExecute(List<Bucket> result){
			mMainThreadRef.setBucketList(result);
			mMainThreadRef.genBucketSpinner();
		}
		
	}
	
	private class S3CreateBucketTask extends AsyncTask<AmazonS3Client, Void, Void>{

		@Override
		protected Void doInBackground(AmazonS3Client... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
