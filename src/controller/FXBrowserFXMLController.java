package controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import dto.YoutubeVideo;
import dto.YoutubeVideoQueue;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import service.player.FXBrowserPlayer;
import main.*;

public class FXBrowserFXMLController extends MainFX{

	@FXML
	private WebView browserWebView;
	private WebEngine browserWebEngine;
	@FXML
	private StackPane stackPane;
	
	@FXML
	private HBox hbox;
	
	private Label countQueue;
	
	FXBrowserPlayer player = new FXBrowserPlayer();
	private YoutubeVideoQueue ytQueue = YoutubeVideoQueue.getInstance();
	private YoutubeVideo ytVideo;
	
	private boolean playNextVideo = false;
	
	@FXML
	private void initialize(){
		try {
			countQueue = new Label("0");
			hbox.getChildren().add(countQueue);
			browserWebEngine = browserWebView.getEngine();
			showEmptyQueueMessage("empty queue...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void playNextVideo(){
		if(!ytQueue.queueIsEmpty()){
			playNextVideo = true;
		}
	}
	

	/**
	 * Executa um link do youtube em um componente WebView .
	 * Em um loop sem final determinado busca um link na fila e ent�o o executa.
	 * Antes de executar o v�deo realiza chamadas a api Iframe player do youtube para obter o tamanho
	 * do v�deo em segundos.
	 * Com base no tamanho do v�deo(segundos), aguarda at� o video chegar no final.
	 * Ent�o busca e executa o pr�ximo v�deo da fila.
	 * @return Um novo objeto Thread para executar simultaneamente ao javaFX 
	 */
	public void execute(YoutubeVideo youtubeVideo){
		Thread t = new Thread(() -> {
			if(ytQueue.queueIsEmpty()){
				ytQueue.addYoutubeLink(youtubeVideo);
				try {
					executeVideosInQueue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				ytQueue.addYoutubeLink(youtubeVideo);
			}
			
		});
		t.start();
	}
	
	private void executeVideosInQueue() throws URISyntaxException, IOException, ExecutionException, InterruptedException{
		addWebViewToStackPane();
		while(!ytQueue.queueIsEmpty()){
			ytVideo = ytQueue.getHead();
			
			//reescreve e carrega o arquivo javascript para calcular o tempo total do v�deo.
			player.setVideoIdInJsFile(ytVideo);
			executeJSFile(player.JS_YOUTUBE_API);
			Long videoDuration = player.waitVideoDuration() + 5L;
			
			//executa o v�deo
			LocalTime endVideo = LocalTime.now().plusSeconds(videoDuration);
			Platform.runLater(() -> {
				browserWebEngine.load(ytVideo.getURL());
			});
			
			//ao finalizar o v�deo, retira o topo da pilha e executa o pr�ximo v�deo
			//se o bot�o next for ativado executa o proximo video imediatamente
			System.out.println(endVideo);
			while(!LocalTime.now().isAfter(endVideo)){
				updateCountQueue();
				if(playNextVideo == true){
					playNextVideo = false;
					break;
				}
				Thread.sleep(1000);
			}
			
			ytQueue.removeQueueHead();
		}
		
		showEmptyQueueMessage("empty queue...");
	}
	
	public void updateCountQueue(){
		Platform.runLater(() -> {
			countQueue.setText(String.valueOf(ytQueue.getSize()));
		});
	}
	
	private void showEmptyQueueMessage(String message){
		Platform.runLater(() -> {
			browserWebEngine.load(null);
			browserWebView.getEngine().load(null);
			stackPane.getChildren().clear();
			stackPane.setAlignment(Pos.CENTER);
			stackPane.getChildren().add(new Label(message));
		});
	}
	
	private void executeJSFile(File file) throws IOException{
		URL url = file.toURI().toURL();
		Platform.runLater(() -> {
			browserWebEngine.load(url.toExternalForm());
		});
	}
	
	private void addWebViewToStackPane(){
		Platform.runLater(() -> {
			browserWebEngine = browserWebView.getEngine();
			stackPane.getChildren().clear();
			stackPane.getChildren().add(browserWebView);
		});
	}

	public WebEngine getBrowserWebEngine() {
		return browserWebEngine;
	}
	
	
}













