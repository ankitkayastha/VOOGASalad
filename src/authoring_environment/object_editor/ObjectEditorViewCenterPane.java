package authoring_environment.object_editor;

import java.util.ResourceBundle;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ObjectEditorViewCenterPane {
	private ResourceBundle centerResources = ResourceBundle.getBundle("authoring_environment/object_editor/CenterPaneResources");
	private Image sprite;
	private double xPos, yPos;
	private Button spriteUpdate;
	private GraphicsContext centerGC;
	private String spriteName;

	public ObjectEditorViewCenterPane() {
		init();
	}

	public Group init() {
		Group root = new Group();
		Canvas c = new Canvas(Integer.parseInt(centerResources.getString("canvasWidth")), Integer.parseInt(centerResources.getString("canvasHeight")));
		centerGC = c.getGraphicsContext2D();
		sprite = addSprite(spriteName);
		xPos = Integer.parseInt(centerResources.getString("canvasWidth"))/2 - sprite.getWidth()/2;
		yPos = Integer.parseInt(centerResources.getString("canvasHeight"))/2 - sprite.getHeight()/2;
		centerGC.drawImage(sprite, xPos, yPos); //, myController.getSize()[0]*spriteWidth, myController.getSize()[1]*spriteHeight);
		spriteUpdate = new Button(centerResources.getString("buttonText"));
		root.getChildren().addAll(c,spriteUpdate);
		return root;
	}

	public Image addSprite(String s) {
		try {
			return new Image(getClass().getClassLoader().getResourceAsStream(s)) ;
		}
		catch (NullPointerException e) {
			return new Image(getClass().getClassLoader().getResourceAsStream(centerResources.getString("marioImage"))) ;
		}
	}
	
	public void update(String name) {
		centerGC.clearRect(0, 0, Integer.parseInt(centerResources.getString("canvasWidth")), Integer.parseInt(centerResources.getString("canvasHeight")));
		sprite = addSprite(name);
		xPos = Integer.parseInt(centerResources.getString("canvasWidth"))/2 - sprite.getWidth()/2;
		yPos = Integer.parseInt(centerResources.getString("canvasHeight"))/2 - sprite.getHeight()/2;
		centerGC.drawImage(sprite, xPos, yPos);

	}

	public GraphicsContext getGC() {
		return centerGC;
	}

	public Button getSpriteUpdateButton() {
		return spriteUpdate;
	}
}