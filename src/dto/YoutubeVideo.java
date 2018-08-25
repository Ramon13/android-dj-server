package dto;


public class YoutubeVideo {
	
	private String url;
	private Long videoDuration;
	
	public YoutubeVideo(String link, Long videoDuration) {
		this.url = link;
		this.videoDuration = videoDuration;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String link) {
		this.url = link;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}
	
	
	
}
