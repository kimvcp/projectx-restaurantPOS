package controller;

import java.util.List;
import java.util.Map;
import java.util.Observable;

import application.CSTable;
import application.CheckBill;
import application.Main;
import application.Tableview;
import database.DBManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Menu;
import model.Order;
import util.ImageFactory;
import util.ScreenController;
import util.UserManager;

/**
 * OrderController(manager/employee) contains method for handling all event
 * receive from the UserInterface. Contains method for viewing and ordering
 * customer orders. (With help form TA for spacing nodes)
 * 
 * @author Piyawat & Vichaphol & P'Jacky
 *
 */
public class CSOrderController {
	@FXML
	private Button order;
	@FXML
	private Button clear;
	@FXML
	private Button remove;
	@FXML
	private Button back;
	@FXML
	private Button exit;
	@FXML
	private Button checkBill;
	@FXML
	private TextField total;
	@FXML
	private FlowPane foodpane;
	@FXML
	private FlowPane drinkpane;
	@FXML
	private TextArea display;
	@FXML
	private TextArea display2;

	private static String tablenumber;
	private Alert alert;

	// for single instantiation
	private static List<Menu> foods;
	private static List<Menu> drinks;
	// instance of classes
	private static UserManager um = UserManager.getInstance();
	private static Order o = Order.getInstance();
	private static DBManager dbm = DBManager.getInstance();
	private static ImageFactory instance = ImageFactory.getInstance();

	private int tmpTotal;

	@FXML
	public void initialize() {
		// adding buttons to each pane
		// setDisplayProp();
		System.out.println("Entering table: " + tablenumber);
		instance.getFoodButton().forEach(x -> foodpane.getChildren().add(x));
		instance.getDrinkButton().forEach(x -> drinkpane.getChildren().add(x));

	}

	/**
	 * Handler for order button. When event receive orders are sent out to the
	 * database.
	 * 
	 * @param event
	 */
	public void orderButtonHandler(MouseEvent event) {
		// if order list is empty
		if (o.getOrders().isEmpty()) {
			alert = new Alert(AlertType.ERROR, "Must order atleast one item!", ButtonType.OK);
			alert.show();
		}
		// order confirmation
		else {
			alert = new Alert(AlertType.CONFIRMATION, "Are you sure to order?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES) {
					Map<Menu, Integer> temp = o.getOrders();
					dbm.orderToDB(tablenumber, temp);
					o.clearOrders();
					setDisplay2();
					display.setText("");
				}
			});
		}
	}

	/**
	 * Handler for clear button. When event receive current orders from
	 * Map<Menu,QTY> is removed.
	 * 
	 * @param event
	 */
	public void clearButtonHandler(MouseEvent event) {
		o.clearOrders();
		setTotal();
		display.setText("");
	}

	/**
	 * Handler for back button. When event receive the CS table scene is shown.
	 * 
	 */
	public void backButtonHandler(ActionEvent event) {
		ScreenController.switchWindow((Stage) back.getScene().getWindow(), new CSTable());
	}

	/**
	 * Handler for logout button. When event receive the Start up scene is
	 * shown.
	 * 
	 */
	public void exitButtonHandler(ActionEvent event) {
		ScreenController.switchWindow((Stage) exit.getScene().getWindow(), new Main());
	}

	/**
	 * Static method for scene before opening this scene to get the button text
	 * and set as table number.
	 * 
	 * @param buttonText
	 */
	public static void setTable(String buttonText) {
		tablenumber = buttonText;
	}

	/**
	 * Static method for scene before opening this scene to get list of menu
	 * names and set the List<Menu> attribute above.
	 * 
	 * @param List
	 *            of menu names List<Menu>
	 */
	public static void setMenu(List<Menu> arg, List<Menu> arg2) {
		foods = arg;
		drinks = arg2;
	}

	// set the current total
	private void setTotal() {
		String temp = "" + (o.getTotal() + tmpTotal);
		total.setText(temp);

	}

	/*
	 * set the temporary total attribute which is use to display the current
	 * total
	 */
	private void setTempTotal(Map<Menu, Integer> map) {
		tmpTotal = o.getTotal(map);
	}

	// set the display properties
	private void setDisplayProp() {
		display.setDisable(true);
		display.setText(tablenumber);
		display2.setDisable(true);
		setDisplay2();
		setTotal();
	}

	// set the top display in the UI
	private void setDisplay() {
		String text = o.orderToText(o.getOrders());
		display.setText(text);
	}

	// set the lower display in the UI
	private void setDisplay2() {
		Map<Menu, Integer> temp = dbm.getDBOrders(tablenumber);
		String text = o.orderToText(temp);
		display2.setText(text);
		setTempTotal(temp);
		o.clearOrders();
	}
}
