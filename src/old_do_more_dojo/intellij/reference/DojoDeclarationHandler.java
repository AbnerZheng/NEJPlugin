package old_do_more_dojo.intellij.reference;

import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

public class DojoDeclarationHandler
{
    protected boolean isEnabled(Project project)
    {
        NEJSettings settings = ServiceManager.getService(project, NEJSettings.class);
        return settings.isNeedsMoreDojoEnabled();
    }
}
