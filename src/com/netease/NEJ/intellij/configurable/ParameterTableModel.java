package com.netease.NEJ.intellij.configurable;

import com.intellij.openapi.project.Project;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedHashMap;

/**
 * Created by abnerzheng on 2016/10/25.
 * 参数管理表Model
 */
public class ParameterTableModel extends AbstractTableModel {
    private final String[] COLLUMNS_NAME = new String[]{"Parameter", "Value"};

    public LinkedHashMap<String, String> getParameterMap() {
        return parameterMap;
    }

    private LinkedHashMap<String , String > parameterMap;

    public ParameterTableModel(Project project, LinkedHashMap<String, String> parameterMap){
        this.parameterMap = parameterMap;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public int getRowCount() {
        return parameterMap.size();
    }

    @Override
    public String getColumnName(int column) {
        return COLLUMNS_NAME[column];
    }

    @Override
    public int getColumnCount() {
        return COLLUMNS_NAME.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){//key
            return parameterMap.keySet().toArray(new String[0])[rowIndex];
        }else{
            return  parameterMap.values().toArray(new String[0])[rowIndex];
        }
    }

    public void addRow(String[] rowData){
        if(rowData.length!=2){
            return;
        }
        parameterMap.put(rowData[0], rowData[1]);
        fireTableDataChanged();
    }

    public void removeRow(int row){
        String key = parameterMap.keySet().toArray(new String[0])[row];
        parameterMap.remove(key);
        fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final String oldKey = parameterMap.keySet().toArray(new String[0])[rowIndex];
        if(columnIndex == 0){
            String oldValue = parameterMap.get(oldKey);
            parameterMap.remove(oldKey);
            parameterMap.put((String) aValue, oldValue);
        }else{
            parameterMap.replace(oldKey, (String) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
