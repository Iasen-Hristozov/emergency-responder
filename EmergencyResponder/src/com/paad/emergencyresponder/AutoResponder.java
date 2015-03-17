package com.paad.emergencyresponder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class AutoResponder extends Activity
{
   // test
   Spinner respondForSpinner;
   CheckBox locationCheckBox;
   EditText responseTextBox;
   PendingIntent intentToFire;
   public static final String alarmAction = "com.paad.emergencyresponder.AUTO_RESPONSE_EXPIRED";
   
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.autoresponder);
      
      respondForSpinner = (Spinner)findViewById(R.id.spinnerRespondFor);
      locationCheckBox = (CheckBox)findViewById(R.id.checkboxLocation);
      responseTextBox = (EditText)findViewById(R.id.responseText);
      
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.respondForDisplayItems, android.R.layout.simple_spinner_dropdown_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      respondForSpinner.setAdapter(adapter);
      
      Button okButton = (Button)findViewById(R.id.okButton);
      okButton.setOnClickListener(new View.OnClickListener()
      {
         public void onClick(View view)
         {
            savePreferences();
            setResult(RESULT_OK, null);
            finish();
         }
      });
      
      Button cancelButton = (Button)findViewById(R.id.cancelButton);
      cancelButton.setOnClickListener(new View.OnClickListener()
      {
         public void onClick(View view)
         {
            respondForSpinner.setSelection(-1);
            savePreferences();
            setResult(RESULT_CANCELED, null);
            finish();
         }
      });
      
   // Загрузите сохраненные настройки и обновите пользовательский интерфейс
      updateUIFromPreferences();
   }
   
   private void updateUIFromPreferences()
   {
      // Получите сохраненные настройки
      String preferencesName = getString(R.string.user_preferences);
      SharedPreferences sp = getSharedPreferences(preferencesName, 0);
      
      String autoResponsePref = getString(R.string.autoRespondPref);
      String responseTextPref = getString(R.string.responseTextPref);
      String autoLocPref = getString(R.string.includeLocationPref);
      String respondForPref = getString(R.string.respondForPref);
      
      boolean autoRespond = sp.getBoolean(autoResponsePref, false);
      String respondText = sp.getString(responseTextPref, "");
      boolean includeLoc = sp.getBoolean(autoLocPref, false);
      int respondForIndex = sp.getInt(respondForPref, 0);
      
      // Примените сохраненные настройки к пользовательскому интерфейсу
      if(autoRespond)
         respondForSpinner.setSelection(respondForIndex);
      else
         respondForSpinner.setSelection(0);
      
      locationCheckBox.setChecked(includeLoc);
      responseTextBox.setText(respondText);
      
   }
   
   private void savePreferences()
   {
      // Получите текущее состояние пользовательского интерфейса
      boolean autoRespond = respondForSpinner.getSelectedItemPosition() > 0;
      int respondForIndex = respondForSpinner.getSelectedItemPosition();
      boolean includeLoc = locationCheckBox.isChecked();
      String respondText = responseTextBox.getText().toString();
      
      // Сохраните его в файл Общих Настроек
      String preferenceName = getString(R.string.user_preferences);
      SharedPreferences sp = getSharedPreferences(preferenceName, 0);
      
      Editor editor = sp.edit();
      editor.putBoolean(getString(R.string.autoRespondPref), autoRespond);
      editor.putString(getString(R.string.responseTextPref), respondText);
      editor.putBoolean(getString(R.string.includeLocationPref), includeLoc);
      editor.putInt(getString(R.string.respondForPref), respondForIndex);
      editor.commit();

      // Отмените автоматические ответы с помощью метода setAlarm
      setAlarm(respondForIndex);
   }

   private void setAlarm(int respondForIndex)
   {
      // Создайте Сигнализацию и зарегистрируйте
      // для нее Приемник широковещательных намерений.      
      
      AlarmManager alarms = (AlarmManager)getSystemService(ALARM_SERVICE);
      
      if(intentToFire == null)
      {
         Intent intent = new Intent(alarmAction);
         intentToFire = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
         
         IntentFilter filter = new IntentFilter(alarmAction);
         
         registerReceiver(stopAutoResponderReceiver, filter);
      }
      
      if(respondForIndex < 1)
      {
         // Если автоответчик отключен, отмените Сигнализацию.
         alarms.cancel(intentToFire);
      }
      else
      {
         // В ином случае получите временной интервал,
         // выбранный в настройках, и сделайте так, чтобы
         // Сигнализация сработала по его истечении.
         Resources r = getResources();
         
         int[] respondForValues = r.getIntArray(R.array.respondForValues);
         int respondFor = respondForValues[respondForIndex];
         
         long t = System.currentTimeMillis();
         t = t + respondFor*1000*60;
         
         // Установите Сигнализацию.
         alarms.set(AlarmManager.RTC_WAKEUP, t, intentToFire);
      }
   }
   
   private BroadcastReceiver stopAutoResponderReceiver = new BroadcastReceiver()
   {
      @Override
      public void onReceive(Context context, Intent intent)
      {
         if(intent.getAction().equals(alarmAction))
         {
            String preferenceName = getString(R.string.user_preferences);
            SharedPreferences sp = getSharedPreferences(preferenceName, 0);
            
            Editor editor = sp.edit();
            editor.putBoolean(getString(R.string.autoRespondPref), false);
            editor.commit();
         }
      }
   };
}
