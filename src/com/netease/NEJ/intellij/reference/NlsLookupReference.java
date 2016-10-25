package com.netease.NEJ.intellij.reference;

import com.netease.NEJ.core.amd.filesystem.SourcesLocator;
import com.netease.NEJ.core.amd.psi.AMDPsiUtil;
import com.intellij.lang.javascript.psi.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NlsLookupReference extends PsiReferenceBase<JSLiteralExpression> {
    private PsiElement qualifier;
    private JSIndexedPropertyAccessExpression accessor;
    private PsiFile file;

    public NlsLookupReference(PsiElement qualifier, JSIndexedPropertyAccessExpression accessor, JSLiteralExpression sourceElement)
    {
        super(sourceElement);

        this.qualifier = qualifier;
        this.accessor = accessor;
    }

    public List<JSProperty> getI18nKeys(PsiFile file)
    {
        final List<JSProperty> keys = new ArrayList<JSProperty>();
        file.acceptChildren(new JSRecursiveElementVisitor() {
            @Override
            public void visitJSObjectLiteralExpression(JSObjectLiteralExpression node)
            {
                if(!node.getParent().getText().startsWith("root:"))
                {
                    super.visitJSObjectLiteralExpression(node);
                    return;
                }

                for(JSProperty property : node.getProperties())
                {
                    keys.add(property);
                }

                super.visitJSObjectLiteralExpression(node);
            }
        });

        return keys;
    }

    public PsiFile getFileContainingI18nKeys()
    {
        if(file != null)
        {
            return file;
        }

        // get the list of defines
        // find one that matches
        // check to see if it's an i18n file
        // resolve the reference to the file
        PsiElement correctDefine = AMDPsiUtil.getDefineForVariable(qualifier.getContainingFile(), qualifier.getText());

        // didn't get a define, so there is no reference to an i18n item
        if(correctDefine == null)
        {
            return null;
        }

        String defineText = correctDefine.getText();
        defineText = defineText.substring(defineText.lastIndexOf("!") + 1).replaceAll("'", "");

        PsiDirectory containingDirectory = correctDefine.getContainingFile().getParent();
        if(containingDirectory == null && correctDefine.getContainingFile().getOriginalFile() != null)
        {
            containingDirectory = correctDefine.getContainingFile().getOriginalFile().getContainingDirectory();
        }
        VirtualFile i18nFile = SourcesLocator.getAMDImportFile(correctDefine.getProject(), defineText + ".js", containingDirectory);

        if(i18nFile == null)
        {
            // probably not in the same directory as the dojo sources
            Logger.getLogger(NlsLookupReference.class).log(Priority.DEBUG, "can't find " + defineText + ".js");
            return file;
        }

        PsiFile templateFile = PsiManager.getInstance(correctDefine.getProject()).findFile(i18nFile);

        file = templateFile;
        return templateFile;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiFile templateFile = getFileContainingI18nKeys();

        if(templateFile == null)
        {
            return accessor.getIndexExpression();
        }

        for(JSProperty property : getI18nKeys(templateFile))
        {
            String propertyText = accessor.getIndexExpression().getText();
            propertyText = propertyText.substring(1, propertyText.length() - 1);

            if(property.getName().equals(propertyText))
            {
                return property;
            }
        }

        return accessor.getIndexExpression();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        PsiFile templateFile = getFileContainingI18nKeys();
        if(templateFile == null)
        {
            return new Object[0];
        }

        List<JSProperty> keys = getI18nKeys(file);
        List<Object> keyStrings = new ArrayList<Object>();

        for(JSProperty key : keys)
        {
            keyStrings.add(key.getName());
        }

        return keyStrings.toArray();
    }
}
