package fr.gso.bcupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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
			Log.v("JSON", _Video.toJson());
			multipartContent.addPart("JSON-RPC", new StringBody(_Video.toJson(), Charset.forName("UTF-8")));
			multipartContent.addPart("file", new FileBody(new File(_Video.getFilepath())));
			_TotalSize = multipartContent.getContentLength();
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			HttpEntity entity = response.getEntity();
			String serverResponse = null;
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					String line = "";
					StringBuilder total = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
					while ((line = reader.readLine()) != null) {
						total.append(line);
					}
					serverResponse = total.toString();
					Log.v("Response", serverResponse);
				} catch (IOException ex) {
					throw ex;
				} catch (RuntimeException ex) {
					httpPost.abort();
					throw ex;
				} finally {
					instream.close();
				}
				httpClient.getConnectionManager().shutdown();
			}
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
		if (result == null) {
		} else {
			if (result.startsWith("{\"result\":")) {
				_NotificationHelper.completed();
			} else {
				_NotificationHelper.error();
			}
		}
		_Context.stopService(new Intent(_Context, UploadService.class));
	}
}
