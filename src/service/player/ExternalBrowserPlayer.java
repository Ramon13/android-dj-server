package service.player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import dto.YoutubeVideo;

public class ExternalBrowserPlayer extends Player{

	private final File RUN_SO_DEFAULT_BROWSER = new File(appPath.getAbsolutePath() + "/bat/run-browser.bat");
	
	/**
	 * Executa o arquivo run-browser.bat para abrir o v�deo no navegador padr�o do windows.
	 * @param file o arquivo javascript que fecha automaticamente a aba do navegador ao terminar o v�deo.
	 * @throws IOException
	 */
	public void executeJSFile(File file) throws IOException{
		try(PrintWriter writer = new PrintWriter(RUN_SO_DEFAULT_BROWSER)){
			writer.println("start " + file.getAbsolutePath());
		}
		Runtime.getRuntime().exec(RUN_SO_DEFAULT_BROWSER.getAbsolutePath());
	}
	
	/**
	 * modifica as linhas que atribui a url e a dura��o do v�deo a vari�veis no arquivo javascript.
	 * @param ytVideo o v�deo do youtube
	 * @throws IOException
	 */
	public void modifyJSVarInPlayerFile(YoutubeVideo ytVideo)throws IOException{
		
		String text = getHTMLContent(JS_PLAYER_FILE);
		text = setYoutubeURLInJSVar(text, ytVideo.getURL(), 3);
		text = setYoutubeVideoLenghtInJsVar(text, ytVideo.getVideoDuration(), 4);
		saveJsFile(JS_PLAYER_FILE, text);
	}
}
