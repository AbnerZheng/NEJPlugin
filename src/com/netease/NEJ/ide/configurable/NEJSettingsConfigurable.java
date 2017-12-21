package com.netease.NEJ.ide.configurable;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.netease.NEJ.core.settings.NEJSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

public class NEJSettingsConfigurable implements Configurable {
    private JComponent myComponent;
    private JPanel myPanel;
    private JTable parameterTable;
    private JButton addButton;
    private JButton delButton;
    private Project project;
    private NEJSettings settingsService;
    private boolean modified = false;
    private JScrollPane scrollPane;
    private ParameterTableBuilder parameterTableBuilder;

    @Nls
    @Override
    public String getDisplayName() {
        return "NEJ Path Configuration";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Configure to work correct with NEJ platform";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        myComponent = (JComponent) myPanel;
        DataContext context = DataManager.getInstance().getDataContext();
        project = PlatformDataKeys.PROJECT.getData(context);
        settingsService = ServiceManager.getService(project, NEJSettings.class);
        final LinkedHashMap<String, String> parameterMap = settingsService.getParameterMap();
        LinkedHashMap<String, String> parameterMapNow;
        if(parameterMap == null){
            parameterMapNow = new LinkedHashMap<String, String>();
        }else{
            parameterMapNow = (LinkedHashMap<String, String>) parameterMap.clone();
        }
        JTableHeader header = new JTableHeader();
        header.add(new JLabel("Parameter"));
        header.add(new JLabel("Value"));
        this.parameterTable.updateUI();
        scrollPane.setRowHeaderView(header);
        parameterTableBuilder = new ParameterTableBuilder(this.parameterTable, project, parameterMapNow);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modified = true;
                parameterTableBuilder.getTableModel().addRow(new String[]{"", ""});
            }
        });
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modified = true;
                parameterTableBuilder.getTableModel().removeRow(parameterTable.getSelectedRow());
            }
        });
        return myPanel;
    }

    @Override
    public boolean isModified() {
        if (modified) {
            return true;
        } else {
            LinkedHashMap<String, String> parameterMapBefore = settingsService.getParameterMap();
            LinkedHashMap<String, String> parameterMapNow = parameterTableBuilder.getTableModel().getParameterMap();
            for (String key : parameterMapNow.keySet()) {
                if (!parameterMapBefore.containsKey(key)) {
                    return true;
                } else {
                    if (parameterMapNow.get(key) != parameterMapBefore.get(key)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        modified = false;
        LinkedHashMap<String, String> parameterMapNow = parameterTableBuilder.getTableModel().getParameterMap();
        settingsService.setParameterMap(parameterMapNow);
    }


    @Override
    public void reset() {
        final LinkedHashMap<String, String> parameterMap = settingsService.getParameterMap();
        LinkedHashMap<String, String> parameterMapNow;
        if(parameterMap == null){
            parameterMapNow = new LinkedHashMap<>();
        }else {
            parameterMapNow = (LinkedHashMap<String, String>) parameterMap.clone();
        }
        parameterTableBuilder.updateMap(parameterMapNow);
        modified = false;
    }

    @Override
    public void disposeUIResources() {

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
