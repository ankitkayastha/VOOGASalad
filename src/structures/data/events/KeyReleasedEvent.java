package structures.data.events;

import javafx.scene.input.KeyCode;
import structures.data.interfaces.IDataEvent;

import java.util.HashMap;
import java.util.Map;

public class KeyReleasedEvent implements IDataEvent {

    public final KeyCode keyCode;

    public KeyReleasedEvent(KeyCode code) {
        keyCode = code;
    }

    public int hashCode() {
        return this.getClass().hashCode() ^ this.keyCode.hashCode();
    }

    public boolean equals(Object obj) {
    	if(obj !=null){
    	if (obj.getClass().equals(this.getClass())) {
    		if (((KeyReleasedEvent)obj).keyCode.hashCode() == this.keyCode.hashCode()) {
    			return true;
    		}
    	}
    }
    	return false;
    }

    public String getName() {
        return String.format("Key Release %s", keyCode.getName());
    }
    @Override
    public String toString(){
    	return this.getName();
    }

    @Override
    public Map<String, String> dumpContents() {
        Map<String, String> ret = new HashMap<>();
        ret.put("class", getClass().getName());
        ret.put("keyCode", keyCode.toString());
        return ret;
    }
}
