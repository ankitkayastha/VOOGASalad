/**
 * 
 */
package engine.front_end;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import engine.events.EventManager;
import engine.events.IGameUpdatedHandler;
import engine.events.IRoomUpdatedHandler;
import engine.events.IVariablesChangeHandler;
import exceptions.ResourceFailedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import structures.run.IParameters;
import structures.run.RunRoom;
//uncomment for controller functionality
//import voogasalad.util.externalcontroller.ControllerListener;

/**
 * @author loganrooper
 */
public class FrontEnd implements IGameUpdatedHandler, IRoomUpdatedHandler, IVariablesChangeHandler {

	public static final String DEFAULT_RESOURCE_PACKAGE = "css/";
	public static final String STYLESHEET = ".css";
	public static final String DEFAULT_IMAGE_PACKAGE = "resources/";
	public static final String[] COLORS = {"Blue", "Green", "Grey", "Pink", "Purple", "Red", "Yellow"};

	private String styleSheetColor = "red";
	private Canvas myCanvas;
	private IDraw myCanvasDrawer;
	private Group myRoot;
	private Stage stage;
	private Scene playScene;
	private EventManager myEventManager;
	private String myCurrentGame;

	private VBox topContainer;
	private BorderPane borderPane;
	private HighScoreView myHighScoreView;
	private VariableView myVariableView;
	private ObjectInformationView myObjectInformationView;

	private int gameHeight, gameWidth;

	public FrontEnd(int width, int height, EventManager eventManager, Stage stage, String game) throws IOException, ResourceFailedException {
		gameHeight = height;
		gameWidth = width;
		myCurrentGame = game;
		borderPane = new BorderPane();
		topContainer = new VBox();
		myEventManager = eventManager;
		this.stage = stage;
		stage.setHeight(height + 100);
		stage.setWidth(500 + width);
		stage.centerOnScreen();
		stage.setY(stage.getY()-100);
		setupFramework(gameWidth, gameHeight);
		setupCanvas(width, height);
	}

	private void setupFramework(int gameWidth, int gameHeight) throws IOException, ResourceFailedException{
		myRoot = new Group();
		playScene = new Scene(borderPane, 200 + gameWidth, 100 + gameHeight);
		playScene.getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + styleSheetColor + STYLESHEET);
		stage.setScene(playScene);
		borderPane.setCenter(myRoot);
		makeMenu();
		makeToolBar();
		makeInfoBars();
	}

	private void makeMenu() {
		MenuBar myMenus = new MenuBar();
		myMenus.useSystemMenuBarProperty().set(true);
		Menu fileMenu = new Menu("File");
		MenuItem open = new MenuItem("Open");
		open.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose a Game");
			fileChooser.showOpenDialog(stage);
		});
		MenuItem reset = new MenuItem("Reset");
		reset.setOnAction(e -> {
			try {
				myEventManager.onReset();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		MenuItem save = new MenuItem("Save");
		save.setOnAction(e -> {
			myEventManager.onSave();
		});
		MenuItem load = new MenuItem("Load");
		load.setOnAction(e -> {
			myEventManager.onLoadSave("10");
		});
		MenuItem close = new MenuItem("Close");
		close.setOnAction(e -> {
			myEventManager.onSave();
			stage.close();
		});
		MenuItem pause = new MenuItem("Pause");
		pause.setOnAction(e -> {
			myEventManager.onPause();
		});
		Menu view = new Menu("View");
		MenuItem highScore = new MenuItem("Show High Scores");
		MenuItem showHelp = new MenuItem("Show Help");
		Menu option = new Menu("Options");
		Menu debugOption = new Menu("Debug Option");
		RadioMenuItem yes = new RadioMenuItem("Yes");
		ToggleGroup debugToggle = new ToggleGroup();
		yes.setOnAction(e -> myEventManager.setDebug(true));
		yes.setToggleGroup(debugToggle);
		RadioMenuItem no = new RadioMenuItem("No");
		no.setOnAction(e -> myEventManager.setDebug(false));
		no.setToggleGroup(debugToggle);
		no.setSelected(true);

		Menu colorOption = new Menu("Theme");

		ToggleGroup colorToggle = new ToggleGroup();
		for (String color : COLORS) {
			RadioMenuItem radioItem = new RadioMenuItem(color);
			radioItem.setOnAction(e -> processColorSelection(radioItem));
			radioItem.setToggleGroup(colorToggle);
			if (color.toLowerCase().equals(styleSheetColor)) radioItem.setSelected(true);
			colorOption.getItems().add(radioItem);
		}
		myMenus.getMenus().addAll(fileMenu, view, option);
		fileMenu.getItems().addAll(open, reset, save, load, close, pause);
		view.getItems().addAll(highScore, showHelp);
		option.getItems().addAll(debugOption, colorOption);
		debugOption.getItems().addAll(yes, no);
		topContainer.getChildren().add(myMenus);
	}

	public void processColorSelection(RadioMenuItem radioItem){
		processColorSelection(radioItem.getText());
	}

	public void processColorSelection(String color){
		styleSheetColor = color.toLowerCase();
		playScene.getStylesheets().clear();
		playScene.getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + styleSheetColor + STYLESHEET);
	}

	public Menu makeColorOptionMenu(ToggleGroup colorToggle) {
		Menu colorOption = new Menu("Theme");
		for (String color : COLORS) {
			RadioMenuItem radioItem = new RadioMenuItem(color);
			radioItem.setOnAction(e -> {styleSheetColor = radioItem.getText().toLowerCase();
			playScene.getStylesheets().clear();
			playScene.getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + styleSheetColor + STYLESHEET);});
			radioItem.setToggleGroup(colorToggle);
			if (color.equals(styleSheetColor)) radioItem.setSelected(true);
		}
		return colorOption;
	}

	public void makeToolBar() throws ResourceFailedException {
		HBox hbox = new HBox(8);
		hbox.setAlignment(Pos.CENTER);
		Button playButton = new Button();
		playButton.setGraphic(new ImageView(DEFAULT_IMAGE_PACKAGE + "play.png"));
		playButton.setOnMouseClicked(e -> {
			myEventManager.onResume();
		});

		Button pauseButton = new Button();            
		pauseButton.setGraphic(new ImageView(DEFAULT_IMAGE_PACKAGE + "pause.png"));
		pauseButton.setOnMouseClicked(e -> {
			myEventManager.onPause();
		});

		Button resetButton = new Button();
		resetButton.setGraphic(new ImageView(DEFAULT_IMAGE_PACKAGE + "reset.png"));
		resetButton.setOnMouseClicked(e -> {
			try {
				myEventManager.onReset();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		Button saveButton = new Button();
		saveButton.setGraphic(new ImageView(DEFAULT_IMAGE_PACKAGE + "save.png"));
		saveButton.setOnMouseClicked(e -> {
			myEventManager.onSave();
		});

		Button openButton = new Button();
		openButton.setGraphic(new ImageView(DEFAULT_IMAGE_PACKAGE + "open.png"));
		openButton.setOnMouseClicked(e -> {
			myEventManager.onLoadSave("10");
		});

		ToolBar tBar = new ToolBar(playButton, pauseButton, resetButton, saveButton, openButton);

		VBox change = new VBox(2);
		Text changeTitle = new Text("Change game");
		ChoiceBox<String> cb = new ChoiceBox<String>();
		cb.setFocusTraversable(false);
		cb.getItems().addAll(addGamesFromDirectory());
		//cb.setOnAction(e -> onGameChange(cb.getValue()));
		//cb.addEventHandler(ChoiceBox, e -> onGameChange(cb.getValue()));
		cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> source, String oldValue, String newValue)
			{
				onGameChange(cb.getValue());
			}});
		change.getChildren().addAll(changeTitle, cb);

		hbox.getChildren().add(tBar);
		hbox.getChildren().add(change);
		topContainer.getChildren().add(hbox);
		borderPane.setTop(topContainer);
	}

	private List<String> addGamesFromDirectory() {
		List<String> choices =  new ArrayList<String>();
		for (final File fileEntry : new File("Games/").listFiles()) {
			choices.add(fileEntry.getName());
		}
		return choices;
	}

	private void makeInfoBars() throws IOException{
		VBox myInfoGroups = new VBox(2);
		makeHighScoreBar(myInfoGroups);
		makeVariableBar(myInfoGroups);
		borderPane.setRight(myInfoGroups);
	}

	private void makeHighScoreBar(VBox container) {
		try{
			myHighScoreView = new HighScoreView(myCurrentGame, this);
		} catch(Exception e){
			myHighScoreView = new HighScoreView();
		}
		myHighScoreView.setPrefWidth(150);
		myHighScoreView.setFocusTraversable(false);
		myHighScoreView.setPrefHeight(borderPane.getHeight() / 2);
		container.getChildren().add(myHighScoreView);
	}

	private void makeVariableBar(VBox container){
		myVariableView = new VariableView(borderPane.getHeight() / 2);
		container.getChildren().add(myVariableView);
	}

	public void makeObjectInformationBar(IParameters parameterObject) {
		myObjectInformationView = new ObjectInformationView(parameterObject);
		myObjectInformationView.setPrefWidth(275);
		myObjectInformationView.setFocusTraversable(false);
		borderPane.setLeft(myObjectInformationView);
		stage.setWidth(1100);
	}

	private void setupCanvas(int width, int height) throws IOException{
		myCanvas = new Canvas(width, height);
		myCanvasDrawer = new Draw(myCanvas);
		StackPane pane = (StackPane) myCanvasDrawer;
		myRoot.getChildren().add(pane);
		setupUserInteraction(pane);
	}

	private void setupUserInteraction(StackPane pane) throws IOException{
		pane.addEventFilter(MouseEvent.MOUSE_PRESSED, myEventManager::onMouseEvent);
		stage.getScene().setOnMouseMoved(myEventManager::onMouseEvent);
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, myEventManager::onKeyEvent);
		stage.getScene().addEventFilter(KeyEvent.KEY_RELEASED, myEventManager::onKeyEvent);
		//uncomment for controller functionality
		//		ControllerListener controllerTest = new ControllerListener();
		//		if(controllerTest.getControllerConnected()){
		//			controllerTest.initialize(stage);
		//		}
	}

	public IDraw getDrawListener(){
		return myCanvasDrawer;
	}

	public IGameUpdatedHandler getFrontEndUpdateHandler(){
		return this;
	}

	public IRoomUpdatedHandler getRoomUpdateHandler(){
		return this;
	}

	public void onGameChange(String name) {
		try {
			myHighScoreView.setGame(name);
			myEventManager.onChangeGame(name);
		} catch (ResourceFailedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRoomChanged(RunRoom runRoom) {
		//		stage.setWidth(runRoom.getView().getView().width());
		//		stage.setHeight(runRoom.getView().getView().height());
	}

	@Override
	public void setHighScore(double highScore) {
		myHighScoreView.updateScore(highScore);
	}

	@Override
	public Double getHighScore() {
		return myHighScoreView.getHighScore();
	}

	public void clearLocalVariables(){
		myVariableView.clearLocalVariables();
	}

	public void clearGlobalVariables(){
		myVariableView.clearGlobalVariables();
	}

	@Override
	public void localVariableUpdate() {
		myVariableView.localVariableUpdate();
	}

	@Override
	public void globalVariableUpdate() {
		myVariableView.globalVariableUpdate();
	}

	@Override
	public void updateGlobalVariables(Map<String, Double> globalVars) {
		myVariableView.globalVariableAssign(globalVars);
	}

	@Override
	public void addLocalVariablesMap(long l, Map<String, Double> localVars) {
		myVariableView.addLocalVariables(l, localVars);
	}

	@Override
	public void removeLocalVariablesMap(long l) {
		myVariableView.removeLocalVariables(l);
	}
}