package fr.gso.bcupload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UploadService extends Service {

	public final static String VIDEO = "video";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getExtras() != null) {
			BCVideo value = (BCVideo) intent.getExtras().getSerializable(VIDEO);
			UploadVideo(value);
		}
		return START_STICKY;
	}

	/**
	 * Lance l'upload de la video en tache de fond.
	 * 
	 * @param videoToUpload La video à envoyer.
	 */
	public void UploadVideo(BCVideo videoToUpload) {
		new UploadTask(getApplicationContext(), videoToUpload).execute();
	}
}