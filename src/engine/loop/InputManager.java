package engine.loop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import engine.events.EventManager;
import engine.events.IInputHandler;
import engine.loop.groovy.GroovyClickEvent;
import exceptions.LibraryArgumentException;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import structures.data.interfaces.IDataEvent;
import structures.run.RunObject;
import structures.run.RunRoom;
import utils.Point;

public class InputManager implements IInputHandler {
	
	private Queue<InputEvent> myInputQueue;
	private Map<KeyCode, Boolean> myKeyMap;
	private boolean myQueueEnabled;
	private double myMouseX, myMouseY;
	private boolean myMousePrimary, myMouseSecondary;
	
	public InputManager(EventManager eventManager, boolean queueEnabled) {
		myInputQueue = new LinkedList<>();
		myKeyMap = new HashMap<>();
		myQueueEnabled = queueEnabled;
		eventManager.addUserInputInterface(this);
		
		myMouseX = 0.0;
		myMouseY = 0.0;
		myMousePrimary = false;
		myMouseSecondary = false;
	}
	
	/**
	 * Calls each InputEvent on the correct corresponding
	 * RunObject, and then clears the events.
	 * 
	 */
	public void processInputEvents(IGameEventHandler gameHandler) {
		InputEvent e;
		while ((e = myInputQueue.poll()) != null) {	

			List<IDataEvent> runEvents = InputEventFactory.getEvents(e);
			for (IDataEvent runEvent : runEvents){
				GroovyClickEvent event = new GroovyClickEvent(runEvent);
				if (event.hasXY()){
					event.setCoordinates(correctForView(gameHandler.getCurrentRoom(), InputEventFactory.getCoordinates(e)));
				}
				List<RunObject> os = gameHandler.getRegistered(runEvent);
				int size = os.size();
				for (int i = 0; i < size; i++) {
					RunObject o = os.get(i);
					if (event.getLocalCheck()){
						if (o.getBounds().contains(event.getCoordinates())){
							gameHandler.fire(o, runEvent, event);
						}
					} else {
						gameHandler.fire(o, runEvent, event);
					}
				}
			}
		}
	}
	
	private Point correctForView(RunRoom room, Point p) {
		return new Point(	p.x + room.getView().getView().x(),
							p.y + room.getView().getView().y());
	}
	
	public void clearQueue() {
		myInputQueue.clear();
	}
	
	public boolean checkKey(KeyCode code) {
		return myKeyMap.getOrDefault(code, false);
	}
	
	public boolean checkKey(String keyCode) throws LibraryArgumentException {
		try {
			KeyCode code = KeyCode.valueOf(keyCode);
			return checkKey(code);
		} catch (IllegalArgumentException ex) {
			throw new LibraryArgumentException("Invalid KeyCode: '%s'", keyCode);
		}
	}
	
	public boolean mousePrimaryDown() {
		return myMousePrimary;
	}
	
	public boolean mouseSecondaryDown() {
		return myMouseSecondary;
	}
	
	public double mouseX() {
		return myMouseX;
	}
	
	public double mouseY() {
		return myMouseY;
	}

	@Override
	public void onMouseEvent(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
			myMouseX = event.getSceneX();
			myMouseY = event.getSceneY();
			return;
		}
		if (myQueueEnabled) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				myInputQueue.add(event);
			}
		}
		myMousePrimary = event.isPrimaryButtonDown();
		myMouseSecondary = event.isSecondaryButtonDown();
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			if (myQueueEnabled && !checkKey(event.getCode())) {
				myInputQueue.add(event);
			}
			myKeyMap.put(event.getCode(), true);
		} else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
			if (myQueueEnabled && checkKey(event.getCode())) {
				myInputQueue.add(event);
			}
			myKeyMap.put(event.getCode(), false);
		}
	}
}
