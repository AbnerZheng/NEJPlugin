package old_do_more_dojo.intellij.inspections;

import com.intellij.codeInspection.InspectionToolProvider;

public class MismatchedImportsProvider implements InspectionToolProvider
{
    public Class[] getInspectionClasses()
    {
        return new Class[] { MismatchedImportsInspection.class};
    }
}