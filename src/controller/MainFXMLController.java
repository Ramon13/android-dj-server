package controller;

import javafx.fxml.FXML;
import main.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import util.BrowserType;

public class MainFXMLController extends MainFX {

	@FXML
	private RadioButton javaBrowser;
	@FXML
	private RadioButton externalBrowser;
	
	public static BrowserType browserType;
	public static FXBrowserFXMLController browserFXMLController;
	
	@FXML
	private void initialize(){
		javaBrowser.setSelected(true);
	}
	
	@FXML
	public void runServer() throws Exception{
		BorderPane root = null;
		
		if(javaBrowser.isSelected()){
			//call PLayerFXMLController
			FXMLLoader fxmlLoader = new FXMLLoader();
			browserType = BrowserType.JAVAFX_BROWSER;
			root = (BorderPane) fxmlLoader.load(getClass().getResource("/view/player-fxml.fxml").openStream());
			browserFXMLController = (FXBrowserFXMLController) fxmlLoader.getController();
	
		}else
			if(externalBrowser.isSelected()){
				browserType = BrowserType.EXTERNAL_BROWSER;
				root = (BorderPane) FXMLLoader.load(getClass().getResource("/view/run-on-external-browser.fxml"));
			}
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/view/fx-style.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(false);
		stage.show();
	}
}
