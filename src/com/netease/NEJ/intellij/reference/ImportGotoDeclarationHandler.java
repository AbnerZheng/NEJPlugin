package com.netease.NEJ.intellij.reference;

import com.netease.NEJ.core.amd.filesystem.DojoModuleFileResolver;
import com.netease.NEJ.core.amd.psi.AMDPsiUtil;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

/**
 * This action provides Goto ... declaration functionality for any AMD module references.
 *
 * There's some logic in here that I could have separated out, but since it is not reusable I just kept
 * it in here for the moment. In the future it can be separated out if necessary.
 */
public class ImportGotoDeclarationHandler extends DojoDeclarationHandler implements GotoDeclarationHandler
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

        PsiElement referencedDefine = AMDPsiUtil.resolveReferencedDefine(psiElement);
        if(referencedDefine == null)
        {
            return new PsiElement[0];
        }

        PsiFile referencedFile = new DojoModuleFileResolver().resolveReferencedFile(psiElement.getProject(), referencedDefine);

        if(referencedFile == null)
        {
            return new PsiElement[0];
        }

        return new PsiElement[] { referencedFile };
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext)
    {
        return "jump to AMD import";
    }
}
