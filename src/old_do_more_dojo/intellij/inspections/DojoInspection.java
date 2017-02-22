package old_do_more_dojo.intellij.inspections;

import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

public class DojoInspection extends LocalInspectionTool
{
    protected boolean isEnabled(Project project)
    {
        NEJSettings settings = ServiceManager.getService(project, NEJSettings.class);
        return settings.isNeedsMoreDojoEnabled();
    }
}
