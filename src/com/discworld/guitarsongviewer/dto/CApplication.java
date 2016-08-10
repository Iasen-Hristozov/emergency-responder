package com.discworld.guitarsongviewer.dto;

import com.discworld.guitarsonglib.CSong;
import com.discworld.guitarsongviewer.Preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CApplication extends Application
{
   public final static int ENU_LNG_UNKNOWN = 0,
                           ENU_LNG_ENGLISH = 1,
                           ENU_LNG_BULGARIAN = 2,
                           ENU_LNG_RUSSIAN = 3;

   public final static String SONGS_FOLDER = "Songs";

   public final static String SONGS_SUFFIX = ".xml";
               
   public final static String LNG_ENGLISH = "en",
                              LNG_BULGARIAN = "bg",
                              LNG_RUSSIAN = "ru";

   public final static int ENU_DISPLAY_SONG_NONE = 0,
                           ENU_DISPLAY_SONG_PAGER = 1,
                           ENU_DISPLAY_SONG_SCROLL = 2;

   public final static int ENU_DISPLAY_CHORDS_NONE = 1,
                           ENU_DISPLAY_CHORDS_ALL = 0,
                           ENU_DISPLAY_CHORDS_RELATED = 2,
                           ENU_DISPLAY_CHORDS_ABOVE = 3;

   protected static final int SHOW_PREFERENCES = 1,
                              SHOW_ABOUT = 2,
                              SHOW_OPEN = 3;
   
   private int iLinesNbr = 0,
            iTextSize = 12,
            iEnuDisplayChords = 0,
            iEnuDisplaySong = 1;

   private CSong oSong;
   
   @Override
   public void onCreate()
   {
       super.onCreate();
       
//       updateFromPreferences();

//       SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
   }      
   
   public int getLinesNbr()
   {
      return iLinesNbr;
   }
   
   public void setLinesNbr(int iLinesNbr)
   {
      this.iLinesNbr = iLinesNbr;
   }
   
   public int getTextSize()
   {
      return iTextSize;
   }
   
   public void setTextSize(int iTextSize)
   {
      this.iTextSize = iTextSize;
   }
   
   public int getEnuDisplayChords()
   {
      return iEnuDisplayChords;
   }
   
   public void setEnuDisplayChords(int iEnuDisplayChords)
   {
      this.iEnuDisplayChords = iEnuDisplayChords;
   }
   
   public int getEnuDisplaySong()
   {
      return iEnuDisplaySong;
   }
   
   public void setEnuDisplaySong(int iEnuDisplaySong)
   {
      this.iEnuDisplaySong = iEnuDisplaySong;
   }
   
   public CSong getSong()
   {
      return oSong;
   }
   
   public void setSong(CSong oSong)
   {
      this.oSong = oSong;
   }
   
   public void updateFromPreferences()
   {
      Context context = getApplicationContext();
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
      
      iEnuDisplaySong = Integer.parseInt(prefs.getString(Preferences.PREF_DISPLAY_SONG, "1"));
      iEnuDisplayChords = Integer.parseInt(prefs.getString(Preferences.PREF_DISPLAY_CHORDS, "0"));
      int iTextSize = Integer.parseInt(prefs.getString(Preferences.PREF_TEXT_SIZE, "16"));
      
      if(this.iTextSize != iTextSize)
         this.iTextSize = iTextSize;
   }   
}
