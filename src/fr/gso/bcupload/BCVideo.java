package fr.gso.bcupload;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BCVideo implements Serializable {
	private static final long serialVersionUID = 605328778506513618L;
	private String title;
	private String shortDescription;
	private String filepath;
	private String preparer;
	private String credit;
	private String writeToken;
	private String[] tags;
	private String uploadUrl;

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getPreparer() {
		return preparer;
	}

	public void setPreparer(String preparer) {
		this.preparer = preparer;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String ToJson() throws JSONException {
		JSONArray videoTags = new JSONArray();

		for (int i = 0; i < tags.length; i++) {
			videoTags.put(tags[i]);
		}

		JSONObject customFields = new JSONObject();
		customFields.put("preparer", preparer);
		customFields.put("credit", credit);
		customFields.put("isnew", "true");

		JSONObject video = new JSONObject();
		video.put("tags", videoTags);
		video.put("customFields", customFields);
		video.put("shortDescription", shortDescription);
		video.put("name", title);

		JSONObject params = new JSONObject();
		params.put("token", writeToken);
		params.put("video", video);
		params.put("create_multiple_renditions", "true");

		JSONObject request = new JSONObject();
		request.put("method", "create_video");
		request.put("params", params);

		return request.toString();
	}

	public String getWriteToken() {
		return writeToken;
	}

	public void setWriteToken(String writeToken) {
		this.writeToken = writeToken;
	}
}
