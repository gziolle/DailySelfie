package com.example.dailyselfie;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final String TAG = "LABZIOLLE";
	
	private PicturesListAdapter mAdapter;
	private AlarmManager mAlarmManager;
	private Intent mNotificationAlarmIntent;
	private PendingIntent pendingIntent;
	private String mCurrentPhotoPath;
	private long mTimeInterval = 120000;
	private String mFileName;
	private String mPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createAlarm();
		
		ListView picturesListView = getListView();
		
		picturesListView.setOnItemClickListener(new OnItemClickListener() {   
            @SuppressWarnings("unused")
            public void onItemClick(AdapterView<?> parent, View view, int position, long Id) {
            	Picture pic = mAdapter.getItem(position);
            	Intent intent = new Intent().setAction(Intent.ACTION_VIEW).setDataAndType(Uri.parse(pic.getFilePath()), "image/*");
            	startActivity(intent);
            }});
		
		mAdapter = new PicturesListAdapter(getApplicationContext());
		if(savedInstanceState!=null){
			ArrayList<Picture> pics = savedInstanceState.getParcelableArrayList("pictureList");
			for(int k=0;k<pics.size();k++){
				mAdapter.add(pics.get(k));
			}
		}
		setListAdapter(mAdapter);

	}
	
	private void createAlarm(){
		
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		mNotificationAlarmIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mNotificationAlarmIntent, 0);
		
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), mTimeInterval, pendingIntent);
	}
	
	private void sendTakePictureIntent(){
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        	ex.printStackTrace();
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	        	Log.d(TAG, "photoFile was created");
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        }
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	    	Log.d(TAG,"Entered onActivityResult method");
	    	mAdapter.add(new Picture(mFileName,setPic(),mPhotoPath));
	    }
	}
	
	private Bitmap setPic() {
	    // Get the dimensions of the View
	    int targetW = 200;
	    int targetH = 200;

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    //bmOptions.inPurgeable = true;

	    return BitmapFactory.decodeFile(mPhotoPath, bmOptions);
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    mFileName = imageFileName;
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    mPhotoPath = image.getAbsolutePath();
	    return image;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.camera_button){
			sendTakePictureIntent();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSaveInstanceState(Bundle savedState) {
		savedState.putParcelableArrayList("pictureList", mAdapter.getList());
	    super.onSaveInstanceState(savedState);

	}
}
