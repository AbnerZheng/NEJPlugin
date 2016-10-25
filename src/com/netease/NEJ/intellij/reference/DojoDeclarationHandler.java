package com.netease.NEJ.intellij.reference;

import com.netease.NEJ.core.settings.DojoSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

public class DojoDeclarationHandler
{
    protected boolean isEnabled(Project project)
    {
        DojoSettings settings = ServiceManager.getService(project, DojoSettings.class);
        return settings.isNeedsMoreDojoEnabled();
    }
}
