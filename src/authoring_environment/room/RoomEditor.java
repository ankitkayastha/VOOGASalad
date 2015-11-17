package authoring_environment.room;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
<<<<<<< HEAD
import structures.IObject;
import structures.data.DataInstance;
=======
import structures.data.DataObject;
>>>>>>> 263c6f5fa4cb5314c3c7ff0ea2adf81d11b4aff5

public class RoomEditor {
	private static final String ROOM_EDITOR_TITLE = "RoomEditorTitle";
	private static final String ROOM_EDITOR_WIDTH = "RoomEditorWidth";
	private static final String ROOM_EDITOR_HEIGHT = "RoomEditorHeight";
	
	private ResourceBundle myResources;
	private RoomController myRoomController;
<<<<<<< HEAD
	private Map<String, IObject> myObjects;
=======
	private Map<String, DataObject> myObjects;

>>>>>>> 263c6f5fa4cb5314c3c7ff0ea2adf81d11b4aff5
	
	private Stage myEditor;
	private Group myRoot;
	private ObjectListContainer myObjectsList;
	private RoomPreview myPreview;
	private ButtonToolbar myToolbar;
	
	
	/**
	 * for TESTING purposes
	 */
	public RoomEditor(ResourceBundle resources) {
		myResources = resources;
		myRoot = new Group();
		createEditor();
	}
	
	/**
	 * Map passed in as unmodifiable collection
	 */
	public RoomEditor(ResourceBundle resources, RoomController controller, Map<String, DataObject> objects) {
		myResources = resources;
		myRoomController = controller;
		myObjects = objects;
		myRoot = new Group();
		createEditor();
	}
	
	public void createEditor() {
		myEditor = new Stage();
		//TODO populate the entire dialog
		initializeEditor();
		fillEditorWithComponents();
		Scene scene = new Scene(myRoot);
		myEditor.setScene(scene);
		myEditor.show();
	}

	private void initializeEditor() {
		myEditor.setWidth(Double.parseDouble(myResources.getString(ROOM_EDITOR_WIDTH)));
		myEditor.setHeight(Double.parseDouble(myResources.getString(ROOM_EDITOR_HEIGHT)));
		myEditor.setTitle(myResources.getString(ROOM_EDITOR_TITLE));
		//myEditor.setTitle(myResources.getString(ROOM_EDITOR_TITLE) + " - " + myRoomController.getName());
	}
	
	private void fillEditorWithComponents() {
		VBox totalPane = new VBox();
		initializeObjectListAndPreview(totalPane);
		initializeButtonsToolbar(totalPane);
		myRoot.getChildren().add(totalPane);
	}
	
	private void initializeObjectListAndPreview(VBox totalPane) {
		HBox objectsAndPreview = new HBox();
		initializeObjectList();
		//TODO CLEANUP
		Group theory = new Group();
		myPreview = new RoomPreview(myResources);
		CreateView view = new CreateView(myResources);
		theory.getChildren().addAll(myPreview, view.create());
		///
		objectsAndPreview.getChildren().addAll(myObjectsList, theory);
		totalPane.getChildren().addAll(objectsAndPreview);
	}
	
	private void initializeObjectList() {
		myObjectsList = new ObjectListContainer(myResources, myObjects);
		Consumer<MouseEvent> dragStarterFunction = e -> startObjectDrag(e);
		myObjectsList.setOnMouseClicked(dragStarterFunction);
	}
	
	private void startObjectDrag(MouseEvent event) {
		ObjectInstance objectInstance = myObjectsList.startObjectDragAndDrop(event);
		ImageView spriteInstance = objectInstance.getImageView();
		if (spriteInstance != null) {
			myRoot.getChildren().add(spriteInstance);
			dragSpriteIntoPreview(objectInstance);
		}
	}
	
	private void dragSpriteIntoPreview(ObjectInstance objectInstance) {
		ImageView sprite = objectInstance.getImageView();
		sprite.setOnMousePressed(e -> setUpDraggingBehavior(objectInstance));
	}
	
	private void setUpDraggingBehavior(ObjectInstance objectInstance) {
		ImageView sprite = objectInstance.getImageView();
		sprite.setOnMouseDragged(e -> objectInstance.updateSpritePosition(e));
		sprite.setOnMouseDragReleased(e -> addSpriteToRoom(e, objectInstance));
	}
	
	private void addSpriteToRoom(MouseEvent e, ObjectInstance objectInstance) {
		double sceneX = e.getSceneX();
		double sceneY = e.getSceneY();
		if (objectInstance.inRoomBounds()) {
			//TODO write object x,y to IObject
			//myPreview.addNode(objectInstance.getImageView());
			myRoot.getChildren().remove(objectInstance.getImageView());

			myRoomController.addObject(new DataInstance(objectInstance.getObject(), sceneX, sceneY));
		} else {
			//TODO get rid of the object
		}
	}
	
	private void initializeButtonsToolbar(VBox totalPane) {
		ButtonHandler handler = new ButtonHandler(myResources, myPreview);
		myToolbar = new ButtonToolbar(myResources, handler.getButtons());
		totalPane.getChildren().add(myToolbar);
	}
	
}
