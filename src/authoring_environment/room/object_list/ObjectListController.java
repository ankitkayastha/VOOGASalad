package authoring_environment.room.object_list;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import authoring_environment.room.PotentialObjectInstance;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import structures.data.DataObject;

public class ObjectListController extends VBox {
	private ResourceBundle myResources;
	private List<DataObject> myObjects;
	private ObjectListContainer myObjectList;
	
	public ObjectListController(ResourceBundle resources, List<DataObject> objects) {
		super();
		myResources = resources;
		myObjects = objects;
		myObjectList = new ObjectListContainer(myResources);
		List<String> objectNames = myObjects.stream()
				.map(e -> e.getName())
				.collect(Collectors.toList());
		myObjectList.setObjectsList(FXCollections.<String>observableArrayList(objectNames));
	}
	
	public ObjectListContainer getObjectListContainer() {
		return myObjectList;
	}
	
	public void setOnMouseClicked(Consumer<MouseEvent> f) {
		myObjectList.getObjectListView().setOnMousePressed(e -> f.accept(e));
	}
	
	public PotentialObjectInstance startObjectDragAndDrop(MouseEvent event, Node dragContext) {
		int selectedIdx = myObjectList.getObjectListView().getSelectionModel().getSelectedIndex();
		if (selectedIdx != -1) {
			PotentialObjectInstance object = new PotentialObjectInstance(myResources, myObjects.get(selectedIdx));
			object.updateSpritePosition(event);
			event.consume();
			dragContext.setOnMouseMoved(e -> object.updateSpritePosition(e));
			myObjectList.getObjectListView().getSelectionModel().select(-1);
			return object;
		}
		return null;
	}
	
}
