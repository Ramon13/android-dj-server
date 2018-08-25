package service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import dto.YoutubeVideo;
import dto.YoutubeVideoQueue;
import service.player.ExternalBrowserPlayer;

public class ExternalBrowserService {

	private static ExternalBrowserService instance;
	private ExternalBrowserService(){};
	
	
	private YoutubeVideoQueue ytQueue = YoutubeVideoQueue.getInstance();
	private YoutubeVideo ytVideo;
	private ExternalBrowserPlayer player = new ExternalBrowserPlayer();
	private boolean nextVideo = false;
	
	
	public void execute(YoutubeVideo youTubeVideo){
		Thread t = new Thread(() -> {
			if(ytQueue.queueIsEmpty()){
				ytQueue.addYoutubeLink(youTubeVideo);
				try {
					executeVideosInQueue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				ytQueue.addYoutubeLink(youTubeVideo);
			}
		});
		t.start();
	}
	
	private void executeVideosInQueue() throws IOException, URISyntaxException, ExecutionException, InterruptedException{
		while(!ytQueue.queueIsEmpty()){
			ytVideo = ytQueue.getHead();
			
			//reescreve e carrega o arquivo javascript para calcular o tempo total do vídeo.
			player.setVideoIdInJsFile(ytVideo);
			player.executeJSFile(player.JS_YOUTUBE_API);
			Long videoDuration = player.waitVideoDuration() + 5L;
			
			//reescreve o arquivo javascript com o tempo do vídeo e em seguida abre o vídeo no youtube em uma nova aba
			ytVideo.setVideoDuration(videoDuration);
			player.modifyJSVarInPlayerFile(ytVideo);
			player.executeJSFile(player.JS_PLAYER_FILE);
		
			//ao finalizar o vídeo, retira o topo da pilha e executa o próximo vídeo
			LocalTime endVideo = LocalTime.now().plusSeconds(videoDuration);
			System.out.println(endVideo);
			while(!LocalTime.now().isAfter(endVideo)){
				if(nextVideo == true){
					nextVideo = false;
					break;
				}
				Thread.sleep(1000);
			}
			
			ytQueue.removeQueueHead();
		}
	}
	
	public boolean isNextVideo() {
		return nextVideo;
	}
	
	public void setNextVideo(boolean nextVideo) {
		this.nextVideo = nextVideo;
	}
	
	public synchronized static ExternalBrowserService getInstance(){
		if(instance == null){
			instance = new ExternalBrowserService();
		}
		return instance;
	}
}
