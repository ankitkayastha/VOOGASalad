package authoring_environment.ParamPopups;

import java.util.ArrayList;
import java.util.List;

import authoring_environment.PopUpError;
import exceptions.ParameterParseException;

import javafx.scene.Node;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextInputControl;

import javafx.scene.layout.HBox;
import structures.data.interfaces.IAction;


public class ParamController {

	private ParamPopupView view;
	private ParamModel model;

	public ParamController(IAction e,List<IAction> action, int index){
		view = new ParamPopupView(e.getParameters());
		model = new ParamModel(e,action, index);
		init();
	}
	private void init() {
		view.getSaveButton().setOnAction(e -> onSave());
		view.getSaveButton().setDefaultButton(true);
	}
	private void onSave() {
		List<HBox> fieldList = view.getFieldList();
		List<String> save = new ArrayList<String>();
		if(model.editing()){
			for(int i = 0; i<model.getListsize();i++){
				save.add(model.getList().get(i).getOriginal());
			}
		}
		try {
			for(int j = 0; j<model.getListsize();j++){
				String input = getInput(fieldList.get(j+1));
				if(input == null){
					throw new ParameterParseException(model.getBundle().getString("Error"));
				}
					model.getList().get(j).parse(input);


			}
			if(!model.editing()){
				model.addAction();
			}
			view.close();
		}
		catch (ParameterParseException e) {
			new PopUpError(e.getMessage());
			if(model.editing()){
			try {
				refreshToOld(save);
			} catch (ParameterParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		}
		//configure.close();
	}
	private void refreshToOld(List<String> save) throws ParameterParseException {
		if(model.editing()){
			for(int k = 0; k<model.getListsize();k++){
				model.getList().get(k).parse(save.get(k));
			}
		}
	}
	@SuppressWarnings("unchecked")
	private String getInput(HBox field) {
		Node node= field.getChildren().get(1);
		String str = null;
		try{
			str =((TextInputControl) node).getText();
		}
		catch(ClassCastException e){
			try{
				Boolean bol = ((CheckBox)node).isSelected();
				str = bol.toString();
			}
			catch(ClassCastException e1){
				str = (String) ((ComboBox<String>) node).getValue();
			}
		}

		return str;
	}

	public void showAndWait() {
		// TODO Auto-generated method stub
		view.showAndWait();

	}
}
