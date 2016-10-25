package com.netease.NEJ.intellij.reference;

import com.netease.NEJ.core.amd.filesystem.AttachPointResolver;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public class AttachPointGotoDeclarationHandler extends DojoDeclarationHandler implements GotoDeclarationHandler
{
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int i, Editor editor)
    {
        if(psiElement == null || !psiElement.getLanguage().equals(Language.findLanguageByID("JavaScript")))
        {
            return new PsiElement[0];
        }

        if(!isEnabled(psiElement.getProject()))
        {
            return new PsiElement[0];
        }

        return AttachPointResolver.getGotoDeclarationTargets(psiElement, i, editor);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext)
    {
        return "Jump to attach point";
    }
}
