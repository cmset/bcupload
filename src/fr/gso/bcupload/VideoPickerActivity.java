package fr.gso.bcupload;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fr.gso.bcupload.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoPickerActivity extends Activity implements OnItemClickListener {

	public final static String EXTRA_FILE_PATH = "file_path";
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_list);

		ListView listView = (ListView) this.findViewById(R.id.list_Videos);
		String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID };
		String[] mediaColumns = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.SIZE };
		cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, MediaStore.Video.Media.DATE_MODIFIED + " DESC");
		ArrayList<VideoViewInfo> videoRows = new ArrayList<VideoViewInfo>();
		int numberOfVideosToKeep = 10;
		int videoCounter = 1;
		if (cursor.moveToFirst()) {
			do {

				VideoViewInfo newVVI = new VideoViewInfo();
				int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
				Cursor thumbCursor = managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);
				if (thumbCursor.moveToFirst()) {
					newVVI.thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
				}
				newVVI.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				newVVI.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
				newVVI.duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
				newVVI.size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
				videoRows.add(newVVI);
				videoCounter++;
			} while (cursor.moveToNext() && videoCounter <= numberOfVideosToKeep);
		}
		listView.setAdapter(new VideoGalleryAdapter(this, videoRows));
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setFastScrollEnabled(true);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		if (cursor.moveToPosition(position)) {
			int fileColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			String videoFilePath = cursor.getString(fileColumn);
			Intent extra = new Intent();
			File newFile = new File(videoFilePath);
			extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath());
			setResult(RESULT_OK, extra);
			finish();

		}
	}
}

class VideoViewInfo {
	String filePath;
	String thumbPath;
	String title;
	String size;
	String duration;
}

class VideoGalleryAdapter extends BaseAdapter {
	private Context context;
	private List<VideoViewInfo> videoItems;

	LayoutInflater inflater;

	public VideoGalleryAdapter(Context _context, ArrayList<VideoViewInfo> _items) {
		context = _context;
		videoItems = _items;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return videoItems.size();
	}

	public Object getItem(int position) {
		return videoItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View videoRow = inflater.inflate(R.layout.video_list_row, null);
		ImageView videoThumb = (ImageView) videoRow.findViewById(R.id.img_VideoThumbNail);
		String thumbpath = videoItems.get(position).thumbPath;

		if (thumbpath != null && thumbpath.length() > 0) {
			File file = new File(thumbpath);
			if (file.exists()) {
				videoThumb.setImageURI(Uri.parse(videoItems.get(position).thumbPath));
			}
			Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoItems.get(position).filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
			videoThumb.setImageBitmap(bMap);
		} else {
			Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoItems.get(position).filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
			videoThumb.setImageBitmap(bMap);
		}

		TextView videoTitle = (TextView) videoRow.findViewById(R.id.txt_VideoTitle);
		videoTitle.setText(videoItems.get(position).title);

		// la taille est en byte
		long fileSize = 0;
		if (videoItems.get(position).size != null) {
			fileSize = Long.parseLong(videoItems.get(position).size);
		}

		TextView videoSize = (TextView) videoRow.findViewById(R.id.txt_VideoSize);
		videoSize.setText("Taille : " + readableFileSize(fileSize));

		// la durée est en millisecondes.
		long videoDuration = 0;
		if (videoItems.get(position).duration != null) {
			videoDuration = Long.parseLong(videoItems.get(position).duration) / 1000;
		}
		TextView videoLenght = (TextView) videoRow.findViewById(R.id.txt_VideoLenght);
		videoLenght.setText("Durée : " + String.format("%d:%02d:%02d", videoDuration / 3600, (videoDuration % 3600) / 60, (videoDuration % 60)));

		return videoRow;
	}

	private String readableFileSize(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}