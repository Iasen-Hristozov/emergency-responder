package com.discworld.guitarsongviewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.discworld.guitarsonglib.CSong;
import com.discworld.guitarsongviewer.dto.*;

public class Main extends Activity 
{
   public final static String SONGS_FOLDER = "Songs",
                              SONGS_SUFFIX = ".xml";
   private final static int SHOW_SONG_PAGER = 1,
                            SHOW_SONG_SCROLL = 2;

   private CApplication oApplication;
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.activity_main);
      
      oApplication = (CApplication) getApplication();
      
      oApplication.updateFromPreferences();
      
//      vShowSongPager();
      
      if(oApplication.getEnuDisplaySong() == CApplication.ENU_DISPLAY_SONG_PAGER)
         vShowSongPager();
      else if(oApplication.getEnuDisplaySong() == CApplication.ENU_DISPLAY_SONG_SCROLL)
         vShowSongScroll();
      
      File fSongsDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/" + SONGS_FOLDER);
      fSongsDir.mkdirs();      
      
      oApplication.setSong(getSongFromResources());
      
   }
   
   private CSong getSongFromResources() 
   {
      CSong oSong = null;
//      XmlResourceParser xmlSong = getResources().getXml(R.xml.antonina_2);
      
      try
      {
         InputStream oInputStream = getResources().openRawResource(R.raw.antonina_2);
   
         byte[] b = new byte[oInputStream.available()];
         oInputStream.read(b);
         oInputStream.close();
         String xmlSomg = new String(b, "UTF-8");
         
//         try
//         {
//            CSong.validate(xmlSomg);
            oSong = new CSong(xmlSomg);
//         } 
//         catch(SAXException e)
//         {
//            // TODO Auto-generated catch block
////            e.printStackTrace();
//            Log.e("CSong", e.getLocalizedMessage());
//         }
         
         
      } 
      catch(IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return oSong;
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
   {
      super.onActivityResult(requestCode, resultCode, intent);
      
      if(resultCode == CMain.RESULT_RELOAD)
      {
         if(requestCode == SHOW_SONG_PAGER)
            vShowSongScroll();
         else if(requestCode == SHOW_SONG_SCROLL)
            vShowSongPager();
      }
      else if(resultCode == RESULT_CANCELED)
      {
         finish();
      }
   }

   private void vShowSongPager()
   {
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), MainPager.class);
      startActivityForResult(intent, SHOW_SONG_PAGER);   
   }

   private void vShowSongScroll()
   {
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), MainScroll.class);
      startActivityForResult(intent, SHOW_SONG_SCROLL);   
   }
}
