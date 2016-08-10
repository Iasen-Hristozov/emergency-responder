package com.discworld.guitarsongviewer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity
{
   public static final String PREF_DISPLAY_SONG = "PREF_SHOW_SONG";
   public static final String PREF_DISPLAY_CHORDS = "PREF_SHOW_CHORDS";
	public static final String PREF_TEXT_SIZE = "PREF_TEXT_SIZE";
	public static final String PREF_SCROLL_SPEED = "PREF_SCROLL_SPEED";
	
	SharedPreferences prefs;
	
	@SuppressWarnings("deprecation")
   @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreferences);
	}
}
