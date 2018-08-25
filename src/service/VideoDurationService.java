package service;

public class VideoDurationService {

	private String videoDuration = null;
	private static VideoDurationService instance;
	
	private VideoDurationService(){}

	public synchronized static VideoDurationService getInstance(){
		if(instance == null){
			instance = new VideoDurationService();
		}
		return instance;
	}
	
	public String getVideoDuration() {
		return videoDuration;
	}

	public synchronized void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
	}
}
