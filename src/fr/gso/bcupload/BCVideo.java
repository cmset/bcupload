package fr.gso.bcupload;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is a container for a video.
 * 
 * @author c.mousset
 * 
 */
public final class BCVideo implements Serializable {
	private static final long serialVersionUID = 605328778506513618L;
	private String title;
	private String shortDescription;
	private String filepath;
	private String preparer;
	private String credit;
	private String writeToken;
	private String[] tags;
	private String uploadUrl;

	/**
	 * @return String The Upload URL
	 */
	public String getUploadUrl() {
		return uploadUrl;
	}

	/**
	 * @param value
	 *            String The Upload URL
	 */
	public void setUploadUrl(final String value) {
		this.uploadUrl = value;
	}

	/**
	 * @return String The Video Title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param value
	 *            String The Video Title
	 */
	public void setTitle(final String value) {
		this.title = value;
	}

	/**
	 * @return String The video description
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param value
	 *            String The video description
	 */
	public void setShortDescription(final String value) {
		this.shortDescription = value;
	}

	/**
	 * @return String The video file path
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * @param value
	 *            String The video file path
	 */
	public void setFilepath(final String value) {
		this.filepath = value;
	}

	/**
	 * @return String The Video Preparer (Custom field)
	 */
	public String getPreparer() {
		return preparer;
	}

	/**
	 * @param value
	 *            String The Video Preparer (Custom field)
	 */
	public void setPreparer(final String value) {
		this.preparer = value;
	}

	/**
	 * @return String The Video Credit (Custom field)
	 */
	public String getCredit() {
		return credit;
	}

	/**
	 * @param value
	 *            String The Video Credit (Custom field)
	 */
	public void setCredit(final String value) {
		this.credit = value;
	}

	/**
	 * @return String[] The video tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * @param value
	 *            String[] The video tags
	 */
	public void setTags(final String[] value) {
		this.tags = value;
	}

	/**
	 * @return String The write token
	 */
	public String getWriteToken() {
		return writeToken;
	}

	/**
	 * @param value
	 *            String The Write Token
	 */
	public void setWriteToken(final String value) {
		this.writeToken = value;
	}

	/**
	 * Serialize the video into JSON string.
	 * 
	 * @return String The video serialized as JSON
	 * @throws JSONException
	 *             Serialization Error
	 */
	public String toJson() throws JSONException {
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
}
