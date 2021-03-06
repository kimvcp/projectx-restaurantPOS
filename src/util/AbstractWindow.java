package util;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * An abstract class that extends Application for subclass to contain method for
 * launching javafx scenes.
 * 
 * @author Piyawat & Vichapol
 *
 */
public abstract class AbstractWindow extends Application {

	private String fxmlfile = "";

	@Override
	public void start(Stage stage) {
		try {
			URL url = getClass().getResource(fxmlfile);
			if (url == null) {
				System.out.println("Couldn't find file: " + fxmlfile);
				Platform.exit();
			}
			// Load the FXML and get reference to the loader
			FXMLLoader loader = new FXMLLoader(url);
			// Create scene graph from file (UI)
			Parent root = loader.load();
			// Show the scene
			Scene scene = new Scene(root);
			// sets a beautiful theme
			stage.setScene(scene);
			stage.sizeToScene();
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the filename for getting the wanted resource used by FXMLLoader.
	 * 
	 * @param name
	 *            of fxml file
	 */
	public void setFilename(String name) {
		this.fxmlfile = name;
	}
}
