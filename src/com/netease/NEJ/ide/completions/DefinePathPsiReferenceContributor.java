package com.netease.NEJ.ide.completions;

import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.patterns.StandardPatterns;
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
    }
}
