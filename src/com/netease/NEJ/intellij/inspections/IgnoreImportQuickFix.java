package com.netease.NEJ.intellij.inspections;

import com.netease.NEJ.core.amd.importing.UnusedImportsRemover;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.javascript.psi.impl.JSChangeUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class IgnoreImportQuickFix implements LocalQuickFix {
    private PsiElement define;
    private PsiElement parameter;

    public IgnoreImportQuickFix(PsiElement define, PsiElement parameter) {
        this.define = define;
        this.parameter = parameter;
    }

    @NotNull
    @Override
    public String getName() {
        return "Don't flag " + define.getText() + " as unused";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Needs More Dojo";
    }

    private void addIgnoreStatement()
    {
        PsiElement element = JSChangeUtil.createJSTreeFromText(define.getProject(), UnusedImportsRemover.IGNORE_COMMENT).getPsi();
        define.getParent().addAfter(element, define);
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor)
    {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        addIgnoreStatement();
                    }
                });
            }
        },
        "Ignore unused import",
        "Ignore unused import");
    }
}
