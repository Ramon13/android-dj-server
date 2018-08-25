package controller;

import dto.YoutubeVideo;
import service.ExternalBrowserService;
import service.VideoDurationService;
import service.player.Player;
import util.BrowserType;
import util.RequestType;

public class SocketRequest {

	/**
	 * Processa a String recebida via socket.
	 * Se a String vinher via http a aplicação reconhece que se trata de uma 
	 * requisição vinda do arquivo javascript em execução.
	 * Caso contrário trata-se de um link do youtube enviado pelo aplicativo cliente.
	 *  
	 * @param requestType enum representando o tipo da requisição recebida
	 * @param requestData conteúdo conteúdo recebido via socket
	 *  
	 */
	public void processRequestData(RequestType requestType, String requestData){
		if(requestType.equals(RequestType.HTTP)){
			String videoDuration = getVideoDuration(requestData);
			setVideoDuration(videoDuration);
			
		}else if(requestType.equals(RequestType.SOCKET)){
			String youtubeURL = new Player().getYoutubeBrowserURL(requestData);
			System.out.println("ytLink = " + youtubeURL);
			
			YoutubeVideo ytVideo = new YoutubeVideo(youtubeURL, null);
			
			if(MainFXMLController.browserType == BrowserType.EXTERNAL_BROWSER){
				ExternalBrowserService.getInstance().execute(ytVideo);
			}else
				if(MainFXMLController.browserType == BrowserType.JAVAFX_BROWSER){
					MainFXMLController.browserFXMLController.execute(ytVideo);
				}
		}
	}
	
	/**
	 * obtém a duração do vídeo em segundos.
	 * 
	 * @param requestData String recebida da conexão via socket.
	 * @return duração do vídeo em segundos
	 */
	private String getVideoDuration(String requestData){
		String videoDuration = new Player().getVideoDuration(requestData);
		System.out.println("video duracao = " + videoDuration);
		return videoDuration;
	}
	
	/**
	 * Seta a duração do vídeo em um objeto singleton para que possa ser acessado por outras classes.
	 * 
	 * @param videoDuration duração do  vídeo em segundos.
	 */
	private void setVideoDuration(String videoDuration){
		VideoDurationService.getInstance().setVideoDuration(videoDuration);
	}
	
}
