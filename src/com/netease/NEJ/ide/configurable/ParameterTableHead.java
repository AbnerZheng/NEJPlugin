package com.netease.NEJ.ide.configurable;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.Enumeration;

/**
 * Created by abnerzheng on 2016/10/25.
 */
public class ParameterTableHead implements TableColumnModel{
    private TableColumn column;

    @Override
    public void addColumn(TableColumn aColumn) {

    }

    @Override
    public void removeColumn(TableColumn column) {

    }

    @Override
    public void moveColumn(int columnIndex, int newIndex) {

    }

    @Override
    public void setColumnMargin(int newMargin) {

    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Enumeration<TableColumn> getColumns() {
        return null;
    }

    @Override
    public int getColumnIndex(Object columnIdentifier) {
        return 0;
    }

    @Override
    public TableColumn getColumn(int columnIndex) {
        return null;
    }

    @Override
    public int getColumnMargin() {
        return 0;
    }

    @Override
    public int getColumnIndexAtX(int xPosition) {
        return 0;
    }

    @Override
    public int getTotalColumnWidth() {
        return 0;
    }

    @Override
    public void setColumnSelectionAllowed(boolean flag) {

    }

    @Override
    public boolean getColumnSelectionAllowed() {
        return false;
    }

    @Override
    public int[] getSelectedColumns() {
        return new int[0];
    }

    @Override
    public int getSelectedColumnCount() {
        return 0;
    }

    @Override
    public void setSelectionModel(ListSelectionModel newModel) {

    }

    @Override
    public ListSelectionModel getSelectionModel() {
        return null;
    }

    @Override
    public void addColumnModelListener(TableColumnModelListener x) {

    }

    @Override
    public void removeColumnModelListener(TableColumnModelListener x) {

    }
}
