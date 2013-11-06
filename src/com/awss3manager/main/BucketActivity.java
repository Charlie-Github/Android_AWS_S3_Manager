package com.awss3manager.main;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BucketActivity extends Activity{

	private AmazonS3Client amazonS3Client;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
    	PropertiesCredentials propCred;
    	BasicAWSCredentials basicCred;
    	List<Bucket> listBucket = new ArrayList<Bucket>();
    	Iterator<Bucket> itrBucket;
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
		
        Spinner spinner = (Spinner)findViewById(R.id.spinner_bucket);
        ArrayAdapter <CharSequence> adapter =
        		  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );	
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listBucket = amazonS3Client.listBuckets();
        itrBucket = listBucket.iterator();
        
        
        spinner.setAdapter(adapter);
	}

}
