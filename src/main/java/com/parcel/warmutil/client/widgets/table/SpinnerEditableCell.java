package com.parcel.warmutil.client.widgets.table;

import com.parcel.warmutil.model.events.TableEditFinisher;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;

/**
 * Created by nemo on 10.11.18.
 */
public abstract class SpinnerEditableCell<S> extends TableCell<S, Integer> implements ExternalCommitableTableCell {

	private TableEditFinisher editFinisher = TableEditFinisher.getInstance();

	protected final Spinner<Integer> spinner = new Spinner<>();

	public SpinnerEditableCell() {
		spinner.setMinWidth(290);
		spinner.setEditable(true);
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(getMinValue(), getMaxValue()));
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

			setGraphic(spinner);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			spinner.requestFocus();
			spinner.getEditor().selectAll();
		}
	}

	private Integer getSpinnerValue() {
		String text = spinner.getEditor().getText();
		try {
			Integer value = Integer.parseInt(text);

			if(value >= getMaxValue()) {
				return getMaxValue();
			} else if (value <= getMinValue()) {
				return getMinValue();
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
				setText(itemToString(item));
			}
		} else {
			setText(null);
		}
	}

	@Override
	public void cancelEdit() {
		commitEdit();	//Необходимо, чтобы значение подтверждалось при любой отмене
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

	public abstract String itemToString(Integer item);

	public abstract int getMinValue();

	public abstract int getMaxValue();
}
