package com.netease.NEJ.ide.completions;

import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.JSParameter;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abnerzheng on 2017/2/16.
 */
public class DefinePathPsiReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        final DefinePathPsiReferenceProvider definePathPsiReferenceProvider = new DefinePathPsiReferenceProvider();
        registrar.registerReferenceProvider(StandardPatterns.instanceOf(JSLiteralExpression.class),definePathPsiReferenceProvider);

//        final DefineParameterPsiReferenceProvider defineParameterPsiReferenceProvider = new DefineParameterPsiReferenceProvider();
//        registrar.registerReferenceProvider(StandardPatterns.instanceOf(JSParameter.class), defineParameterPsiReferenceProvider);

//        final DefineVariantPsiReferenceProvider defineVariantPsiReferenceProvider = new DefineVariantPsiReferenceProvider();
//        registrar.registerReferenceProvider(StandardPatterns.instanceOf(PsiElement.class), defineVariantPsiReferenceProvider);
    }
}
