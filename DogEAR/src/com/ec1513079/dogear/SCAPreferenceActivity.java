package com.ec1513079.dogear;

import java.util.List;
import java.util.zip.Inflater;

import com.ec1513079.dogear.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.widget.Toast;

public class SCAPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new SCAPreferenceFragment()).commit();
		
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
	}
	
	static public class SCAPreferenceFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference);
			
			findPreference("preference_app_version").setSummary(Util.getApplicationVersion(getActivity()));
			findPreference(getResources().getString(R.string.preference_key_category_update))
			.setSummary(Util.getCategoryUpdate(getActivity()));
			findPreference("preference_contents_data_size").setSummary(Util.getContentsSize(getActivity()));
			findPreference("preferecnce_chach_size").setSummary(Util.getCacheSize(getActivity()));
			findPreference("preference_usalbe_size").setSummary(Util.getUsableSize(getActivity()));

			findPreference("preference_contents_reset")
			.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {	
				@Override
				public boolean onPreferenceClick(Preference preference) {
					new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.error)
					.setTitle("コンテンツをリセット")
					.setMessage("ダウンロードされたコンテンツをリセットします。\nコンテンツを利用するには再ダウンロードが必要です。")
					.setPositiveButton(
							"リセット",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Toast.makeText(getActivity(), "コンテンツをリセットしました", Toast.LENGTH_LONG).show();
								}
							})
					.setNegativeButton(
							"キャンセル", 
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							})
					.show();
					return true;
				}
			});

			findPreference("preference_cache_clear")
			.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {	
				@Override
				public boolean onPreferenceClick(Preference preference) {
					new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.error)
					.setTitle("キャッシュをクリア")
					.setMessage("キャッシュをクリアすると次回読み込み時に時間がかかることがあります。")
					.setNegativeButton(
							"クリア",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Toast.makeText(getActivity(), "キャッシュをクリアしました", Toast.LENGTH_LONG).show();
								}
							})
					.setPositiveButton(
							"キャンセル", 
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							})
					.show();
					return true;
				}
			});

			findPreference("preference_copyright_law")
			.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {	
				@Override
				public boolean onPreferenceClick(Preference preference) {
					new AlertDialog.Builder(getActivity())
					.setTitle("著作権情報")
					.setView(LayoutInflater.from(getActivity()).inflate(R.layout.license_view_layout, null))
					.setPositiveButton(
							"閉じる", 
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							})
					.show();
					return true;
				}
			});
		}
	}
}
