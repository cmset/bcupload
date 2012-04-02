package fr.gso.bcupload;

import fr.gso.bcupload.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private static final int UPLOAD_FILE = 2;
	private static final int REQUEST_PICK_VIDEO = 3;
	private Button btnBrowseVideo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnBrowseVideo = (Button) findViewById(R.id.btn_BrowseVideos);
		btnBrowseVideo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_BrowseVideos:
			Intent videoPicker = new Intent(this, VideoPickerActivity.class);
			startActivityForResult(videoPicker, REQUEST_PICK_VIDEO);
			break;
		}
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
     }
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           case R.id.settings_option_item:
        	   Intent intent = new Intent(this,BCUploadPreferencesActivity.class);
        	   startActivity(intent);
              return true;
        }
        return false;}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_PICK_VIDEO:
				if (data.hasExtra(VideoPickerActivity.EXTRA_FILE_PATH)) {
					String videoFilePath = data.getStringExtra(VideoPickerActivity.EXTRA_FILE_PATH);
					Intent intent = new Intent(this, UploadActivity.class);
					intent.putExtra(UploadActivity.EXTRA_VIDEO_FILE_PATH, videoFilePath);
					startActivityForResult(intent, UPLOAD_FILE);
				}
				break;
			}
		}
	}
}