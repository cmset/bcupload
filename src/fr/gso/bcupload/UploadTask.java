package fr.gso.bcupload;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class UploadTask extends AsyncTask<Integer, Integer, String> {
	private static final String TAG = "UploadTask";
	private NotificationHelper _NotificationHelper;
	private BCVideo _Video;
	private Context _Context;
	private long _TotalSize;
	private int _LastProgressValue = 0;

	public UploadTask(Context context, BCVideo videoToUpload) {
		_NotificationHelper = new NotificationHelper(context);
		_Video = videoToUpload;
		_Context = context;
	}

	@Override
	protected void onPreExecute() {
		_NotificationHelper.createNotification();
	}

	@Override
	protected String doInBackground(Integer... params) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(_Video.getUploadUrl());

		try {
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new ProgressListener() {
				@Override
				public void transferred(long num) {
					publishProgress((int) ((num / (float) _TotalSize) * 100));
				}
			});

			Log.v("JSON", _Video.ToJson());
			multipartContent.addPart("JSON-RPC", new StringBody(_Video.ToJson(), Charset.forName("UTF-8")));
			multipartContent.addPart("file", new FileBody(new File(_Video.getFilepath())));
			_TotalSize = multipartContent.getContentLength();
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			return serverResponse;

		}

		catch (Exception e) {
			Log.e(TAG, "Erreur lors de l'upload", e);
			this.cancel(true);
			_NotificationHelper.error();
			_Context.stopService(new Intent(_Context, UploadService.class));
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		if ((progress[0] - _LastProgressValue) > 5) {
			_LastProgressValue = progress[0];
			_NotificationHelper.progressUpdate(progress[0]);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		_NotificationHelper.completed();
		_Context.stopService(new Intent(_Context, UploadService.class));
	}

}