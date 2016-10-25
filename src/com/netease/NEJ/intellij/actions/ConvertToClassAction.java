package com.netease.NEJ.intellij.actions;

import com.netease.NEJ.core.refactoring.UtilToClassConverter;
import com.netease.NEJ.core.util.PsiFileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiFile;

public class ConvertToClassAction extends JavaScriptAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final PsiFile psiFile = PsiFileUtil.getPsiFileInCurrentEditor(e.getProject());

        new UtilToClassConverter().convertToClassPattern(psiFile);
    }
}
