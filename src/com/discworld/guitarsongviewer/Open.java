package com.discworld.guitarsongviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.discworld.guitarsonglib.CSong;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


// test
public class Open extends ListActivity 
{
 
   /* (non-Javadoc)
    * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
    */
   private List<String> fileList = new ArrayList<String>();
   private List<String> lsSongsTitles = new ArrayList<String>();

   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      
      String path = Environment.getExternalStorageDirectory().toString()+"/" + Main.SONGS_FOLDER;
        
//        File root = new File(Environment
//          .getExternalStorageDirectory()
//          .getAbsolutePath());
      File root = new File(path);
      
      ListDir(root);

    }
   
   @Override
   protected void onListItemClick(ListView l, View v, int position, long id)
   {
      super.onListItemClick(l, v, position, id);
      
      Intent intent = new Intent();
      intent.putExtra("file", fileList.get(position));
      setResult(RESULT_OK,intent);
      finish();      
   }
   
   void ListDir(File f)
   {
      File[] files = f.listFiles();
      String sSongTitle;
      fileList.clear();
      lsSongsTitles.clear();
      for(File file : files)
      {
         if(file.getName().endsWith(Main.SONGS_SUFFIX))
         {
            if(!(sSongTitle = getSongTitle(file.getPath())).isEmpty())
            {
               fileList.add(file.getPath());
               lsSongsTitles.add(sSongTitle);
            }
         }
      }
     
      ArrayAdapter<String> songsList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lsSongsTitles);
       
      setListAdapter(songsList); 
   }
    
   private String getSongTitle(String sFile) 
   {
      String sFullTitle = "";

      try
      {
         FileInputStream oFileInputStream = new FileInputStream(sFile);
         
         XmlPullParserFactory oXmlPullParserFactory = XmlPullParserFactory.newInstance();
         XmlPullParser xmlSong = oXmlPullParserFactory.newPullParser();

         xmlSong.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
         xmlSong.setInput(oFileInputStream, null);       
         
          int eventType = -1;
          while (sFullTitle.isEmpty() && eventType != XmlPullParser.END_DOCUMENT) 
          {
             if(eventType == XmlPullParser.START_TAG) 
             { 
                String strNode = xmlSong.getName();
                if(strNode.equals(CSong.TAG_SONG))
                {
                   String sTitle = xmlSong.getAttributeValue(null, CSong.ATR_TITLE);
                   String sAuthor = xmlSong.getAttributeValue(null, CSong.ATR_AUTHOR);
                   
                   sFullTitle = !sAuthor.isEmpty() ? sAuthor + " - " + sTitle : sTitle; 
                   
                }
             }
                eventType = xmlSong.next();
          }         
         
         
      } 
      catch(FileNotFoundException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } 
      catch(IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (XmlPullParserException e) 
      {
         e.printStackTrace();
      } 
      return sFullTitle;
    }    
}