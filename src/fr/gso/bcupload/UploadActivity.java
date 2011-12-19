package fr.gso.bcupload;

import fr.gso.bcupload.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UploadActivity extends Activity implements OnClickListener {

	public final static String EXTRA_VIDEO_FILE_PATH = "video_file_path";

	private Button btnUpload;
	private EditText edtTitle;
	private EditText edtDescription;
	private EditText edtAuthor;
	private String videoFilePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);
		btnUpload = (Button) findViewById(R.id.btn_Upload);
		btnUpload.setOnClickListener(this);
		edtTitle = (EditText) findViewById(R.id.edt_Title);
		edtDescription = (EditText) findViewById(R.id.edt_Description);
		edtAuthor = (EditText) findViewById(R.id.edt_Author);
		if (getIntent().hasExtra(EXTRA_VIDEO_FILE_PATH)) {
			videoFilePath = getIntent().getStringExtra(EXTRA_VIDEO_FILE_PATH);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_Upload:
			if (ValidInputs()) {
				
				BCVideo video = new BCVideo();
				video.setTitle(edtTitle.getText().toString().trim());
				video.setShortDescription(edtDescription.getText().toString().trim());
				video.setPreparer("mobile_reporter");
				video.setCredit(edtAuthor.getText().toString().trim());
				video.setTags(new String[] { "depot_mobile_reporter" });
				video.setFilepath(videoFilePath);
				video.setWriteToken(getString(R.string.write_token));
				video.setUploadUrl(getString(R.string.upload_url));
					
				Intent intent = new Intent(this,UploadService.class); 
				intent.putExtra(UploadService.VIDEO, video); 
				startService(intent);
				setResult(RESULT_OK);
				finish();
		}
			break;
		}

	}

	/**
	 * Validation des entrées utilisateurs
	 * @return Status de validation
	 */
	protected boolean ValidInputs() {
		if (edtTitle.getText().toString().trim().length() == 0 || edtDescription.getText().toString().trim().length() == 0 || edtAuthor.getText().toString().trim().length() == 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.mandatory_fields), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * Nettoie le formulaire de saisie
	 */
	protected void CleanInputs() {
		edtDescription.setText("");
		edtAuthor.setText("");
		edtTitle.setText("");
		edtTitle.requestFocus();
	}
}
