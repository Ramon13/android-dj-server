package controller;

import dto.YoutubeVideoQueue;
import javafx.fxml.FXML;
import service.ExternalBrowserService;
import main.*;


/**
 * Classe de controle da plataforma javaFX (arquivos FXML)
 * Respons�vel por gerenciar e executar os v�deos na fila em um navegador externo.
 * 
 * @author ramoncosta
 * @version 1.0
 */
public class ExternalBrowserFXMLController extends MainFX{

	private YoutubeVideoQueue ytQueue = YoutubeVideoQueue.getInstance();
	
	@FXML
	public void playNextVideo(){
		if(!ytQueue.queueIsEmpty())
			ExternalBrowserService.getInstance().setNextVideo(true);
	}

}
