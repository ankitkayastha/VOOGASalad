package structures.run;

import java.util.Map;
import structures.IObject;
import structures.data.events.IDataEvent;
import utils.Vector;

public class RunObject {
	
	public final String name;
	public double scaleX;
	public double scaleY;
	public double angle;
	public Vector velocity;
	
	private RunSprite mySprite;
	private Map<IDataEvent, RunAction> myEvents;
	
	public double x;
	public double y;
	
	public RunObject(String name) {
		this.name = name;
		this.x = 0.0;
		this.y = 0.0;
		this.scaleX = 1.0;
		this.scaleY = 1.0;
		this.angle = 0.0;
		this.velocity = Vector.ZERO;
	}
	
	protected void bindEvent(IDataEvent event, RunAction action) {
		myEvents.put(event, action);
	}
	
	protected void setSprite(RunSprite sprite) {
		mySprite = sprite;
	}
	
	public void trigger(IDataEvent event) {
		// TODO: Groovy run event
	}
	
	public IObject toData() {
		// TODO: What the hell is this method for?
		return null;
	}
	
	public void change_sprite(){
		//parameters?
	}
	
	public void destroy(){
		
	}
	
	public void movement_angle(double angle, double acceleration, boolean relative){
		
	}
	
	public void movement_towards(double x, double y, double acceleration, boolean relative){
		
	}

	public void move_to(double x, double y, boolean relative){
		double xOffset = 0;
		double yOffset = 0;
		if(relative){
			xOffset = this.x;
			yOffset = this.y;
		}
		this.x = xOffset + x;
		this.y = yOffset + y;
	}
	
	public void move_to_random(){
		//TODO: is this a parameter or do we calculate it here?
	}
	
	public void run_script(String script){
		
	}
	
	public void scale_sprite(double width, double height){
		
	}
	
	public void set_acceleration(double acceleration){
		
	}
	
	public void set_friction(double friction){
		
	}
	
	public void set_random_number_and_choose(double odds){
		//TODO: I don't know how to make this work
	}
	
	public void sleep(double time){
		
	}
	
	public void wrap_around_room(boolean value){
		
	}

}