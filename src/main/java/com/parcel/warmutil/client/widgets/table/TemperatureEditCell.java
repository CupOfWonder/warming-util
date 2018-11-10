package com.parcel.warmutil.client.widgets.table;

import com.parcel.warmutil.model.events.TableEditFinisher;
import com.parcel.warmutil.model.helpers.Constants;
import com.parcel.warmutil.model.options.TempRangeOptions;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;

import static com.parcel.warmutil.client.helpers.StringHelper.temperatureString;

public class TemperatureEditCell extends TableCell<TempRangeOptions, Integer> implements ExternalCommitableTableCell {

	private TableEditFinisher editFinisher = TableEditFinisher.getInstance();

	private final Spinner<Integer> spinner = new Spinner<>();

	public TemperatureEditCell() {
		super();
		spinner.setMinWidth(290);
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Constants.MIN_TEMP, Constants.MAX_TEMP));
		spinner.setEditable(true);

		editFinisher.registerEditableCell(this);
	}

	@Override
	public void startEdit() {
		if(!isEmpty()) {
			super.startEdit();

			spinner.getValueFactory().setValue(getItem());
			spinner.getEditor().setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					commitEdit(getSpinnerValue());
				}
			});
			spinner.requestFocus();
			spinner.getEditor().requestFocus();
			spinner.getEditor().selectAll();

			setGraphic(spinner);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}

	private Integer getSpinnerValue() {
		String text = spinner.getEditor().getText();
		try {
			Integer value = Integer.parseInt(text);

			if(value >= Constants.MAX_TEMP) {
				return Constants.MAX_TEMP;
			} else if (value <= Constants.MIN_TEMP) {
				return Constants.MIN_TEMP;
			} else {
				return value;
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	protected void updateItem(Integer item, boolean empty) {
		super.updateItem(item, empty);
		if(!empty) {
			if(isEditing()) {
				setText(null);
				setGraphic(spinner);

			} else {
				setText(temperatureString(item));
			}
		} else {
			setText(null);
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(temperatureString(getItem()));
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}


	@Override
	public void commitEdit(Integer newValue) {
		super.commitEdit(newValue);
	}

	@Override
	public void commitEdit() {
		if(isEditing()) {
			commitEdit(getSpinnerValue());
		}
	}
}


