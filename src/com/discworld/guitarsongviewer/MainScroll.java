package com.discworld.guitarsongviewer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.discworld.guitarsonglib.CChordsVerse;
import com.discworld.guitarsonglib.CTextVerse;
import com.discworld.guitarsonglib.CVerse;
import com.discworld.guitarsongviewer.dto.CApplication;
import com.discworld.guitarsongviewer.dto.CMain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainScroll extends CMain
{
   private int verticalScrollMax,
               scrollPos = 0,
               iScrollSpeed,
               heightToScroll = 1,
               scrollPeriod = 20;
   
   private final static int seekBarMax = 50;
   
   private ScrollView svMain;
   private RelativeLayout rlText;
   private TextView tvText,
                    tvChordsRelated;
   private Timer scrollTimer     =  null;
   private TimerTask scrollerSchedule;
   private SeekBar sbSpeed;
   
   private final Runnable Timer_Tick  =  new Runnable() 
   {
      public void run() 
      {
         moveScrollView();
      }
   };
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main_scroll);
      
      tvTitle = (TextView) findViewById(R.id.tvTitle);
      tvChords = (TextView) findViewById(R.id.tvChords);
      
      svMain  =   (ScrollView) findViewById(R.id.svMain);
      rlText = (RelativeLayout)findViewById(R.id.llText);
      tvText  = (TextView)findViewById(R.id.tvText);
      tvChordsRelated = (TextView) findViewById(R.id.tvChordsRelative);
      
      tvText.setTextSize(oApplication.getTextSize());
      tvChordsRelated.setTextSize(oApplication.getTextSize());
      
      setTitle();
      
      setSong();
      
      setDisplayChords();
      
      iScrollSpeed = loadScrollSpeed();
      
      ViewTreeObserver vto =  rlText.getViewTreeObserver();
      vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
      {
          @SuppressWarnings("deprecation")
          @Override
          public void onGlobalLayout() 
          {
             rlText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
             getScrollMaxAmount();
//             startAutoScrolling(scrollPeriod);
             
//             if(iScrollSpeed != seekBarMax)
                startAutoScrolling(iScrollSpeed);
             
          }
      });
      
      sbSpeed = (SeekBar) findViewById(R.id.sbSpeed);
      sbSpeed.setMax(seekBarMax);
      
      sbSpeed.setProgress(seekBarMax - iScrollSpeed);
      
      sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
      { 
         @Override 
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
         { 
            if(scrollTimer !=  null)
            {
               scrollTimer.cancel();
               scrollTimer = null;
            }

            iScrollSpeed = seekBar.getMax() - progress;
            saveScrollSpeed(iScrollSpeed);
            
//            if(progress != 0)
               startAutoScrolling(iScrollSpeed);
         } 

         @Override 
         public void onStartTrackingTouch(SeekBar seekBar) 
         { 
            // TODO Auto-generated method stub 
         } 

         @Override 
         public void onStopTrackingTouch(SeekBar seekBar) 
         { 
            // TODO Auto-generated method stub 
         } 
       }); 
   }

   private void setDisplayChords()
   {
      if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_NONE || oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_ABOVE)
      {
         tvChordsRelated.setVisibility(View.GONE);
         tvChords.setVisibility(View.GONE);
      }
      else if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_ALL)
      {
         tvChords.setVisibility(View.VISIBLE);
         tvChordsRelated.setVisibility(View.GONE);
         setChords();
      }
      else if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_RELATED)
      {
         tvChords.setVisibility(View.GONE);
         tvChordsRelated.setVisibility(View.VISIBLE);
      }
   }
   
   private void setSong()
   {
      CChordsVerse oChordsVerse;
      
      tvText.setText(null);
      
      if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_RELATED)
         tvChordsRelated.setText(null);
      
      ArrayList<? extends CVerse> alVerses = (oApplication.getEnuDisplayChords() == ENU_DISPLAY_CHORDS_ABOVE ? oApplication.getSong().getChordsTextVerses() : oApplication.getSong().oText);
      CVerse oVerse;
      
      for(int iNdx = 0; iNdx < alVerses.size(); iNdx++)
      {
         oVerse = alVerses.get(iNdx);
         tvText.append(oVerse.toString() + "\n\n");
         
         if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_RELATED && !((CTextVerse) oVerse).sChordsVerseID.isEmpty())
         {
            oChordsVerse = oApplication.getSong().alChords.get(oApplication.getSong().htChordsIdNdx.get(((CTextVerse) oVerse).sChordsVerseID));
            tvChordsRelated.append(oChordsVerse.toString() + "\n\n");
         }
      }
   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      cancelAutoScrolling();
      
      return super.onOptionsItemSelected(item);
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
   {
      int iEnuDisplayChordsOld = oApplication.getEnuDisplayChords();
      int iTextSizeOld = oApplication.getTextSize();

      super.onActivityResult(requestCode, resultCode, intent);
      
      if(requestCode == SHOW_PREFERENCES)
      {
         if(resultCode == Activity.RESULT_CANCELED)
         {
            if(iEnuDisplayChordsOld != oApplication.getEnuDisplayChords())
            {
               setDisplayChords();
               setSong();
               svMain.scrollTo(0, 0);
            }
            if(iTextSizeOld != oApplication.getTextSize())
            {
               tvText.setTextSize(oApplication.getTextSize());
               if(oApplication.getEnuDisplayChords() == CApplication.ENU_DISPLAY_CHORDS_RELATED)
                  tvChordsRelated.setTextSize(oApplication.getTextSize());
               svMain.scrollTo(0, 0);
            }
         }
      }
      else if(requestCode == SHOW_OPEN)
      {
         setDisplayChords();
         setSong();
         svMain.scrollTo(0, 0);
      }
      
      startAutoScrolling(iScrollSpeed);
   }   

   public void getScrollMaxAmount()
   {
//      int actualWidth = (llText.getMeasuredHeight()-(256*3));
      int actualWidth = (rlText.getMeasuredHeight());
      verticalScrollMax   = actualWidth;
   }
   
   public void startAutoScrolling(int scrollPeriod)
   {
      if(scrollPeriod == seekBarMax)
         return;
      
      if(scrollerSchedule != null)
      {
         scrollerSchedule.cancel();
         scrollerSchedule = null;
      }
      
      scrollerSchedule = new TimerTask()
      {
         @Override
         public void run()
         {
            runOnUiThread(Timer_Tick);
         }
      };
      
      scrollTimer = new Timer();      
//      scrollTimer.schedule(scrollerSchedule, 30, scrollPeriod);
      scrollTimer.schedule(scrollerSchedule, 0, scrollPeriod);
   }
   
   public void moveScrollView()
   {
      scrollPos = (int) (svMain.getScrollY() + heightToScroll);
      
      if(scrollPos >= verticalScrollMax)
      {
//         scrollPos = 0;
         return;
      }
      svMain.scrollTo(0, scrollPos);
//      Log.e("moveScrollView","moveScrollView");    
   }

//   public void onBackPressed()
//   {
//      super.onBackPressed();
//      finish();
//   }
   
   public void onPause() 
   {
//      try
//      {
//         scrollTimer.wait();
//         scrollerSchedule.wait();
//      } catch(InterruptedException e)
//      {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
      super.onPause();
//      finish();
   }
   
   public void onDestroy()
   {
      cancelAutoScrolling();
      
      super.onDestroy();
   }
   
   private void saveScrollSpeed(int iScrollSpeed)
   {
      SharedPreferences oPrf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
      Editor editor = oPrf.edit();
      editor.putInt(Preferences.PREF_SCROLL_SPEED, iScrollSpeed);
      editor.commit();
   }
   
   private int loadScrollSpeed()
   {
      SharedPreferences oPrf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
      return oPrf.getInt(Preferences.PREF_SCROLL_SPEED, seekBarMax);
   }
   
   private void cancelAutoScrolling()
   {
      if(scrollerSchedule != null)
      {
         scrollerSchedule.cancel();
         scrollerSchedule = null;
      }
      
      if(scrollTimer != null)
      {
         scrollTimer.cancel();
         scrollTimer = null;
      }
   }
}
