package com.discworld.guitarsongviewer.dto;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.discworld.guitarsonglib.CChordsVerse;
import com.discworld.guitarsonglib.CSong;
import com.discworld.guitarsongviewer.About;
import com.discworld.guitarsongviewer.Open;
import com.discworld.guitarsongviewer.Preferences;
import com.discworld.guitarsongviewer.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CMain extends FragmentActivity
{
   public static final int RESULT_RELOAD = 100;
   
   protected TextView tvTitle,
                      tvChords;
   
   protected CApplication oApplication;
   
   protected static final int   SHOW_PREFERENCES = 1,
                                SHOW_ABOUT = 2,
                                SHOW_OPEN = 3;   

   public final static int ENU_DISPLAY_CHORDS_NONE = 1,
                           ENU_DISPLAY_CHORDS_ALL = 0,
                           ENU_DISPLAY_CHORDS_RELATED = 2,
                           ENU_DISPLAY_CHORDS_ABOVE = 3;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);

      oApplication = (CApplication) getApplication();
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {  
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      // Handle item selection
      switch (item.getItemId()) 
      {
         case R.id.action_open:
            startActivityForResult(new Intent(this, Open.class), SHOW_OPEN);
            return true;

         
         case R.id.action_settings:
            startActivityForResult(new Intent(this, Preferences.class), SHOW_PREFERENCES);
            return true;

         case R.id.action_about:
            startActivityForResult(new Intent(this, About.class), SHOW_ABOUT);
            return true;
            
         default:
            return super.onOptionsItemSelected(item);
      }
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
   {
      super.onActivityResult(requestCode, resultCode, intent);
      
      if(requestCode == SHOW_PREFERENCES)
      {
         if(resultCode == Activity.RESULT_CANCELED)
         {
            int iEnuDisplaySongOld = oApplication.getEnuDisplaySong();
            updateFromPreferences();
            if(iEnuDisplaySongOld != oApplication.getEnuDisplaySong())
               vReturnReload();
//            else if(oApplication.getEnuDisplayChords() == ENU_DISPLAY_CHORDS_ALL)
//               setChords();            
            
         }
      }
      else if(requestCode == SHOW_OPEN)
      {
         if(resultCode == Activity.RESULT_OK)
         {
            Bundle extras = intent.getExtras();
//            String sFile = extras.getString("file", "");
            String sFile = extras.getString("file");
            if(!sFile.isEmpty())
               getSongFromFile(sFile);
         }
      }
   }   
   
   private void vReturnReload()
   {
      setResult(RESULT_RELOAD, new Intent());
      finish();
   }

   protected void updateFromPreferences()
   {
      oApplication.updateFromPreferences();
      
//      Context context = getApplicationContext();
//      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//      
//      oApplication.setEnuDisplaySong(Integer.parseInt(prefs.getString(Preferences.PREF_DISPLAY_SONG, "0")));
//      oApplication.setEnuDisplayChords(Integer.parseInt(prefs.getString(Preferences.PREF_DISPLAY_CHORDS, "1")));
//      int iTextSize = Integer.parseInt(prefs.getString(Preferences.PREF_TEXT_SIZE, "16"));
//      
//      if(oApplication.getTextSize() != iTextSize)
//         oApplication.setTextSize(iTextSize);
      
//      if(iEnuDisplayChords != ENU_DISPLAY_CHORDS_ALL)
//         tvChords.setVisibility(View.GONE);
//      else
//         tvChords.setVisibility(View.VISIBLE);
      
   }
   
   protected void getSongFromFile(String sFile) 
   {
      try
      {
         InputStream oInputStream =  new BufferedInputStream(new FileInputStream(sFile));
         byte[] b = new byte[oInputStream.available()];
         oInputStream.read(b);
         oInputStream.close();
         String xmlSomg = new String(b, "UTF-8");
         
         oApplication.setSong(new CSong(xmlSomg));
         
//         setTitle();
//         
//         if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_ALL)
//            setChords();
         
      } catch(FileNotFoundException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
//      catch(XmlPullParserException e)
//      {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      } 
      catch(IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   protected void setTitle()
   {
      tvTitle.setText(oApplication.getSong().sAuthor + " - " + oApplication.getSong().sTitle);
   }
   
   protected void setChords()
   {
      tvChords.setText(null);
      for(CChordsVerse oChordsVerse: oApplication.getSong().alChords)
      {
         tvChords.append(oChordsVerse.toString());
         tvChords.append("\n\n");
      }
   }
   
}
