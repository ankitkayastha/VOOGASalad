package structures.data.actions.params;

import java.util.List;

import exceptions.ParameterParseException;

public class DoubleParam implements IParameter {

	private double myValue;
	private String myString;
	private String myTitle;

	public DoubleParam(String title) {
		myTitle = title;
	}

	@Override
	public void parse(String string) throws ParameterParseException {
		try {
			myValue = Double.parseDouble(string);
			myString = string;
		} catch (NumberFormatException ex) {
			throw new ParameterParseException(String.format("Not a number: '%s'", string));
		}
	}

	@Override
	public String getOriginal() {
		return myString;
	}

	public Double getValue() {
		return myValue;
	}

	@Override
	public String getTitle() {
		return myTitle;
	}

	@Override
	public IParameter.type getType() {
		return IParameter.type.DOUBLE;
	}



}
