package com.example.dailyselfie;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Picture implements Parcelable {
	
	private String fileName;
	private String filePath;
	private Bitmap mBitmap;
	
	public Picture(String fileName, Bitmap mBitmap, String filePath){
		this.fileName = fileName;
		this.mBitmap = mBitmap;
		this.filePath = fileName;
	}
	
	private Picture(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeValue(fileName);
		dest.writeValue(filePath);
		dest.writeValue(mBitmap);
		
	}
	
	public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
