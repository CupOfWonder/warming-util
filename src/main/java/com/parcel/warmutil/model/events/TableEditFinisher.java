package com.parcel.warmutil.model.events;

import com.parcel.warmutil.client.widgets.table.ExternalCommitableTableCell;

import java.util.ArrayList;
import java.util.List;

public class TableEditFinisher {

	private static TableEditFinisher instance;

	private List<ExternalCommitableTableCell> registeredCells = new ArrayList<>();

	private TableEditFinisher() {

	}

	public static TableEditFinisher getInstance() {
		if(instance == null) {
			instance = new TableEditFinisher();
		}
		return instance;
	}

	public void registerEditableCell(ExternalCommitableTableCell cell) {
		registeredCells.add(cell);
	}

	public void finishEdit() {
		registeredCells.forEach(ExternalCommitableTableCell::commitEdit);
	}

}
