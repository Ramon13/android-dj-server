package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.Server;


public class MainFX extends Application{
	
	public static Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		new Thread( () -> new Server().start()).start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainFX.stage = primaryStage;
		
		BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/view/main.fxml"));
			
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/view/fx-style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setMaximized(false);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
	}
	
	

}

















