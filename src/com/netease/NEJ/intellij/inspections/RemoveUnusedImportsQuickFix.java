package com.netease.NEJ.intellij.inspections;

import com.netease.NEJ.intellij.actions.UnusedImportsAction;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class RemoveUnusedImportsQuickFix implements LocalQuickFix {
    private PsiElement define;
    private PsiElement parameter;

    public RemoveUnusedImportsQuickFix(PsiElement define, PsiElement parameter) {
        this.define = define;
        this.parameter = parameter;
    }

    @NotNull
    @Override
    public String getName() {
        return "Delete all unused imports";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Needs More Dojo";
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
                        UnusedImportsAction.removeUnusedImports(define.getContainingFile());
                    }
                });
            }
        },
        "Remove unused imports",
        "Remove unused imports");
    }
}
