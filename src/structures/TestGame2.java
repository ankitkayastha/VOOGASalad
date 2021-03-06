package structures;

import java.util.Collections;
import java.util.List;

import exceptions.ParameterParseException;
import exceptions.ResourceFailedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import structures.data.*;
import structures.data.events.CollisionEvent;
import structures.data.actions.move.MoveTo;
import structures.data.actions.move.SetVelocityInDirection;
import structures.data.actions.script.RunScript;
import structures.data.events.KeyPressedEvent;
import structures.data.events.ObjectCreateEvent;
import structures.data.events.StepEvent;
import structures.data.interfaces.IAction;
import utils.Vector;
import utils.rectangle.Rectangle;

/*
    This class generates a sample game object. The game consists
    of three rooms.

    The first room is displayed on startup and has a background image.

    The second room contains two objects. A player that responds to keyboard
    inputs and a coin that, when hit by the player, advances the player to the
    next room (the win screen);

    The win screen is a static screen with a background image.
 */

public class TestGame2 {

    /*
     Events are not associated with actions, they will be when
     the action library has been built out in the game engine
     */

    public DataGame getTestGame(String directory){
        DataGame testGame = new DataGame("TestGame2", directory + "TestGame2/");

        DataObject wall = new DataObject("Wall");
        wall.setSolid(true);

        DataSprite wallSprite = new DataSprite("Wall", "beaten_brick_tiled.png");
        wall.setSprite(wallSprite);
        
        DataObject coin = new DataObject("Coin");
        DataSprite coinSprite = new DataSprite("Coin", "coin.png");
        coin.setSprite(coinSprite);

        DataObject mario = new DataObject("Mario");

        DataSprite marioSprite = new DataSprite("Mario", "mario.png");
        mario.setSprite(marioSprite);
        mario.setSolid(true);


        try {
			wallSprite.load(testGame.getName());
			marioSprite.load(testGame.getName());
		} catch (ResourceFailedException e) {
			e.printStackTrace();
		}


        MoveTo left = new MoveTo();
        MoveTo right = new MoveTo();
        MoveTo origin = new MoveTo();
        MoveTo up = new MoveTo();
        MoveTo down = new MoveTo();
        RunScript step = new RunScript();
        RunScript collide = new RunScript();
        SetVelocityInDirection jump = new SetVelocityInDirection();

        String stepScript = "" +
        "library.set_scroller_x(current(), 0.5);\n" +
        "if (library.key_down('LEFT') && !library.key_down('RIGHT')) {\n" +
        "current().set_velocity(180,1, true); \n"+
        "} \n" +
        "if (library.key_down('Right') && !library.key_down('left')) {\n"+
        "current().set_velocity(0, 1, true);\n" +
        "}\n";
        
        String createScript = "" +
        "current().asdf = 3.14159\n" + 
        "library.print(current().asdf);\n";
        
        String collideScript = 
        		"with(event.other);\n" +
        "current().move_to(20, 0, true);\n end(); \n current().move_to(-20, 0, true);\n";
        
        RunScript create = new RunScript();


        try {

        	jump.getParameters().get(0).parse("-90");
        	jump.getParameters().get(1).parse("20");
        	jump.getParameters().get(2).parse("true");

	        left.getParameters().get(0).parse("-10");
	        left.getParameters().get(1).parse("0");
	        left.getParameters().get(2).parse("true");


	        right.getParameters().get(0).parse("10");
	        right.getParameters().get(1).parse("0");
	        right.getParameters().get(2).parse("true");

	        up.getParameters().get(0).parse("0");
	        up.getParameters().get(1).parse("-10");
	        up.getParameters().get(2).parse("true");

	        down.getParameters().get(0).parse("0");
	        down.getParameters().get(1).parse("10");
	        down.getParameters().get(2).parse("true");

	        origin.getParameters().get(0).parse("20");
	        origin.getParameters().get(1).parse("20");
	        origin.getParameters().get(2).parse("false");

	        step.getParameters().get(0).parse(stepScript);
	        
	        create.getParameters().get(0).parse(createScript);
	        
	        collide.getParameters().get(0).parse(collideScript);

        } catch (ParameterParseException ex) {
        }

        List<IAction> leftActions = Collections.singletonList(left);
        ObservableList<IAction> leftActionsO = FXCollections.observableList(leftActions);
        List<IAction> rightActions = Collections.singletonList(right);
        ObservableList<IAction> rightActionsO = FXCollections.observableList(rightActions);
        List<IAction> upActions = Collections.singletonList(up);
        ObservableList<IAction> upActionsO = FXCollections.observableList(upActions);
        List<IAction> downActions = Collections.singletonList(down);
        ObservableList<IAction> downActionsO = FXCollections.observableList(downActions);
        List<IAction> originActions = Collections.singletonList(origin);
        ObservableList<IAction> originActionsO = FXCollections.observableList(originActions);
       
        List<IAction> stepActions = Collections.singletonList(step);
        ObservableList<IAction> stepActions0 = FXCollections.observableList(stepActions);
        List<IAction> createActions = Collections.singletonList(create);
        ObservableList<IAction> createActions0 = FXCollections.observableList(createActions);
        List<IAction> jumpActions = Collections.singletonList(jump);
        ObservableList<IAction> jumpActions0 = FXCollections.observableList(jumpActions);
        
        List<IAction> collideActions = Collections.singletonList(collide);
        ObservableList<IAction> collisionActions0 = FXCollections.observableList(collideActions);

        //mario.bindEvent(new KeyPressedEvent(KeyCode.LEFT), leftActionsO);
        //mario.bindEvent(new KeyPressedEvent(KeyCode.RIGHT), rightActionsO);
        //mario.bindEvent(new KeyPressedEvent(KeyCode.UP), upActionsO);
        //mario.bindEvent(new KeyPressedEvent(KeyCode.DOWN), downActionsO);
        mario.bindEvent(new KeyPressedEvent(KeyCode.F), jumpActions0);
        //mario.bindEvent(new CollisionEvent(wall), blockActions0);
        mario.bindEvent(StepEvent.event, stepActions0);
        mario.bindEvent(ObjectCreateEvent.event, createActions0);
        mario.bindEvent(new CollisionEvent(coin), collisionActions0);


        //player.addEvent(new CollisionEvent(coin));

        //startScreenBackground.addEvent(startScreenChange);

        DataObject winScreenBackground = new DataObject("WinScreenBackground");

        DataSprite winScreenSprite = new DataSprite("Win Screen", "WinScreen.png");
        winScreenBackground.setSprite(winScreenSprite);

        DataRoom level1 = new DataRoom("Level 1", 2000, 500);
        level1.getView().setView(new Rectangle(0, 0, 500, 500));
        level1.setBackgroundColor("TestGame/background.png");
        level1.addObjectInstance(new DataInstance(wall, 0, 200));
        level1.addObjectInstance(new DataInstance(wall, 64, 200));
        level1.addObjectInstance(new DataInstance(wall, 128, 200));
        level1.addObjectInstance(new DataInstance(wall, 128, 136));
        level1.addObjectInstance(new DataInstance(wall, 128, 72));
        level1.addObjectInstance(new DataInstance(wall, 192, 200));
        level1.addObjectInstance(new DataInstance(wall, 460, 200));
        level1.addObjectInstance(new DataInstance(wall, 524, 264));
        level1.addObjectInstance(new DataInstance(wall, 255, 200));
        level1.addObjectInstance(new DataInstance(wall, 319, 200));
        level1.addObjectInstance(new DataInstance(coin, 100, 100));

        for (int i=0; i<30; i++) {
        	level1.addObjectInstance(new DataInstance(wall, 64 * i, 328));
        }

        DataInstance marioInstance = new DataInstance(mario, 200, 20);
        marioInstance.setGravity(new Vector(0, 2));
        marioInstance.setFriction(.3);
        level1.addObjectInstance(marioInstance);


        testGame.addObject(mario);
        testGame.addObject(wall);
        testGame.addObject(coin);

        testGame.addSprite(wallSprite);
        testGame.addSprite(marioSprite);
        testGame.addSprite(coinSprite);

        testGame.addRoom(level1);
        testGame.setStartRoom(level1);

        return testGame;
    }
}
