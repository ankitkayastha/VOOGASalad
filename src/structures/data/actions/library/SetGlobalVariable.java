package structures.data.actions.library;

import structures.data.actions.DataAction;
import structures.data.actions.params.CheckboxParam;
import structures.data.actions.params.DoubleParam;
import structures.data.actions.params.StringParam;

public class SetGlobalVariable extends DataAction {

	public SetGlobalVariable(){
		init(new StringParam("EditVariableKey"),
				new DoubleParam("EditVariableValue"),
				new CheckboxParam("RelativeVariable?"));
	}

	@Override
	public String getTitle() {
		return "SetGlobalVariable";
	}

	@Override
	public String getDescription() {
		if((boolean) get("RelativeVariable?").getValue()){
			return String.format("change %s by %.2f", get("EditVariableKey"), get("EditVariableValue"));
		}
		else{
			return String.format("make %s equal %.2f", get("EditVariableKey"), get("EditVariableValue"));
		}
	}

	@Override
	protected String getSyntax() {
		return "library.set_variable('%s', %f, %b);";
	}

}
