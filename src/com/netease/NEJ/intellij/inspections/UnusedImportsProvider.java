package com.netease.NEJ.intellij.inspections;

import com.intellij.codeInspection.InspectionToolProvider;

public class UnusedImportsProvider implements InspectionToolProvider
{
    public Class[] getInspectionClasses()
    {
        return new Class[] { UnusedImportsInspection.class};
    }
}
