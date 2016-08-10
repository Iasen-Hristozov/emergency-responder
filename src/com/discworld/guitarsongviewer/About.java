package com.discworld.guitarsongviewer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.discworld.guitarsongviewer.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity
{
   public static final String FORMAT_SHORT_DATE = "dd/MM/yyyy";   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_about_details);
      
      TextView tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
      
      try
      {
         PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
         tvAppVersion.setText(pInfo.versionName);
      } 
      catch(NameNotFoundException e)
      {
         e.printStackTrace();
      }
      
      TextView tvAppReleaseDateTime = (TextView) findViewById(R.id.tvAppReleaseDateTime);
//      tvAppReleaseDateTime.setText(CApplication.RELEASE_DATE);
      tvAppReleaseDateTime.setText(getReleaseDate());
      
   }

   @Override
   public void onBackPressed()
   {
      setResult(RESULT_OK, new Intent());
      finish();
   }
   
   public String getReleaseDate()
   {
      String sDate = null;
      try
      {
         ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
         ZipFile zf = new ZipFile(ai.sourceDir);
         ZipEntry ze = zf.getEntry("classes.dex");
         long time = ze.getTime();
         zf.close();
         
         SimpleDateFormat dfShortDate = new SimpleDateFormat(FORMAT_SHORT_DATE, Locale.US);         
         sDate = dfShortDate.format(new java.util.Date(time));

      }
      catch(Exception e)
      {
      }
      return sDate;
   }
}
