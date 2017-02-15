package com.netease.NEJ.intellij.configurable;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.LinkedHashMap;

/**
 * Created by abnerzheng on 2016/10/25.
 */
public class ParameterTableBuilder {
    private final JTable table;
    private final Project project;
    private ParameterTableModel model;

    public ParameterTableBuilder(JTable table, Project project, LinkedHashMap<String, String> map){
        this.table = table;
        this.project = project;
        this.model = new ParameterTableModel(project, map);
        table.setModel(model);
        table.updateUI();
    }

    public ParameterTableModel getTableModel(){
        return this.model;
    }

    public void updateMap(LinkedHashMap<String, String> map){
        this.model = new ParameterTableModel(project, map);
        this.table.setModel(model);
    }
}
