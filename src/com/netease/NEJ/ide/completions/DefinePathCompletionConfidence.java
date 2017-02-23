package com.netease.NEJ.ide.completions;

import com.intellij.codeInsight.completion.CompletionConfidence;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThreeState;
import com.netease.NEJ.core.util.NEJUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abnerzheng on 2017/2/23.
 */
public class DefinePathCompletionConfidence extends CompletionConfidence {
    @NotNull
    @Override
    public ThreeState shouldSkipAutopopup(@NotNull PsiElement contextElement, @NotNull PsiFile psiFile, int offset) {
        if(NEJUtil.isDefineCall(contextElement.getParent())){
            return ThreeState.NO;
        }
        return ThreeState.UNSURE;
    }
}
