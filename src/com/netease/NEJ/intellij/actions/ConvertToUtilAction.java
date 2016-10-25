package com.netease.NEJ.intellij.actions;

import com.netease.NEJ.core.refactoring.ClassToUtilConverter;
import com.netease.NEJ.core.util.PsiFileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiFile;

public class ConvertToUtilAction extends JavaScriptAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final PsiFile psiFile = PsiFileUtil.getPsiFileInCurrentEditor(e.getProject());

        new ClassToUtilConverter().convertToUtilPattern(psiFile);
    }
}
