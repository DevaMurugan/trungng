package com.rainmoon.podcast.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.rainmoon.podcast.MainTabActivity;
import com.rainmoon.podcast.R;
import com.rainmoon.podcast.Strings;
import com.rainmoon.podcast.service.RefreshService;

@TargetApi(11)
public class PrefsFragment extends PreferenceFragment {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.layout.preferences);

    SharedPreferences prefs = PreferenceManager
        .getDefaultSharedPreferences(getActivity());
    prefs.registerOnSharedPreferenceChangeListener(new PrefsChangeListener(
        getActivity()));
  }

  final class PrefsChangeListener implements OnSharedPreferenceChangeListener {
    private final Context mContext;

    public PrefsChangeListener(Context context) {
      super();
      mContext = context;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
        String key) {
      boolean newValue = sharedPreferences.getBoolean(key, false);
      if (key.equals(Strings.SETTINGS_REFRESHENABLED)) {
        if (newValue) {
          new Thread() {
            public void run() {
              mContext.startService(new Intent(mContext, RefreshService.class));
            }
          }.start();
        } else {
          mContext.stopService(new Intent(mContext, RefreshService.class));
        }
      } else if (key.equals(Strings.SETTINGS_SHOWTABS)) {
        if (MainTabActivity.INSTANCE != null) {
          MainTabActivity.INSTANCE.setTabWidgetVisible(newValue);
        }
      }
    }
  }
  
}
