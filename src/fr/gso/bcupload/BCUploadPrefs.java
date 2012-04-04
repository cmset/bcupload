package fr.gso.bcupload;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BCUploadPrefs {
	public final static String PREFS_NAME = "bcupload_prefs";

	public static String getLibraryToken(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString(context.getString(R.string.pref_library_token_key), context.getString(R.string.undefined));
	}

	public static void setLibraryToken(Context context, int newValue) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putInt(context.getString(R.string.pref_library_token_key), newValue);
		prefsEditor.commit();
	}
	
	public static String getTags(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString(context.getString(R.string.pref_tags_key), context.getString(R.string.undefined));
	}

	public static void setTags(Context context, String newValue) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putString(context.getString(R.string.pref_tags_key), newValue);
		prefsEditor.commit();
	}
	
	public static String getCredit(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString(context.getString(R.string.pref_credit_key), context.getString(R.string.undefined));
	}

	public static void setCredit(Context context, String newValue) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putString(context.getString(R.string.pref_credit_key), newValue);
		prefsEditor.commit();
	}
}