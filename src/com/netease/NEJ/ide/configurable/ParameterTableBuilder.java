package com.netease.NEJ.ide.configurable;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.util.LinkedHashMap;

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
