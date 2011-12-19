package fr.gso.bcupload;

import fr.gso.bcupload.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotificationHelper {
	private int NOTIFICATION_ID = 25687654;
	private Context _Context;
	private Notification _Notification;
	private NotificationManager _NotificationManager;

	public NotificationHelper(Context context) {
		_Context = context;
	}

	public void createNotification() {
		_Notification = new Notification(android.R.drawable.stat_sys_upload, "Envoi d'une vidéo", System.currentTimeMillis());
		_Notification.contentView = new RemoteViews(_Context.getPackageName(), R.layout.upload_progress);
		_Notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_launcher);
		_Notification.contentView.setTextViewText(R.id.status_text, "Envoi en cours...");
		_Notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);
		_NotificationManager = (NotificationManager) _Context.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent();
		PendingIntent mContentIntent = PendingIntent.getActivity(_Context, 0, notificationIntent, 0);
		_Notification.contentIntent = mContentIntent;

		_NotificationManager.notify(NOTIFICATION_ID, _Notification);
	}

	public void progressUpdate(int percentageComplete) {
		_Notification.contentView.setProgressBar(R.id.status_progress, 100, percentageComplete, false);
		_NotificationManager.notify(NOTIFICATION_ID, _Notification);
	}

	public void cancel() {
		_NotificationManager.cancel(NOTIFICATION_ID);
	}

	public void error() {
		_Notification.icon = android.R.drawable.stat_notify_error;
		_Notification.contentView.setTextViewText(R.id.status_text, "Erreur lors de l'envoi");
		_Notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);
		_NotificationManager.notify(NOTIFICATION_ID, _Notification);
	}

	public void completed() {
		_Notification.icon = android.R.drawable.stat_sys_upload_done;
		_Notification.contentView.setTextViewText(R.id.status_text, "Envoi terminé");
		_Notification.contentView.setProgressBar(R.id.status_progress, 100, 100, false);
		_NotificationManager.notify(NOTIFICATION_ID, _Notification);
	}
}
