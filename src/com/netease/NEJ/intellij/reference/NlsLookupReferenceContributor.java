package com.netease.NEJ.intellij.reference;

import com.netease.NEJ.core.settings.DojoSettings;
import com.intellij.javascript.JavaScriptReferenceContributor;
import com.intellij.lang.javascript.psi.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class NlsLookupReferenceContributor extends JavaScriptReferenceContributor
{
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar)
    {
        ElementPattern<JSLiteralExpression> pattern = PlatformPatterns.psiElement(JSLiteralExpression.class);

        registrar.registerReferenceProvider(pattern, new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
                PsiElement parent = psiElement.getParent();

                DojoSettings settings = ServiceManager.getService(psiElement.getProject(), DojoSettings.class);
                if(!settings.isNeedsMoreDojoEnabled())
                {
                    return new PsiReference[0];
                }

                if(parent instanceof JSIndexedPropertyAccessExpression) {
                    JSIndexedPropertyAccessExpression accessor = (JSIndexedPropertyAccessExpression) parent;
                    PsiElement qualifier = accessor.getQualifier();

                    JSLiteralExpression literal = (JSLiteralExpression) psiElement;
                    if(!literal.isQuotedLiteral())
                    {
                        return new PsiReference[0];
                    }

                    return new PsiReference[] { new NlsLookupReference(qualifier, accessor, (JSLiteralExpression) psiElement) };
                }

                return new PsiReference[0];
            }
        });
    }
}
