package com.netease.NEJ.ide.completions;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.netease.NEJ.core.define.DefineDepPath;
import com.netease.NEJ.core.util.NEJUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abnerzheng on 2017/2/16.
 */
public class DefinePathReference extends PsiReferenceBase<PsiElement> {

    private boolean complete;

    public DefinePathReference(PsiElement element, TextRange textRange, boolean complete) {
        super(element, textRange);
        this.complete = complete;
    }

    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        final List<PsiElement> filesToCompletion = NEJUtil.getFilesToCompletion(myElement);
        List<ResolveResult> results = new ArrayList<>();

        for (PsiElement p : filesToCompletion) {
            results.add(new PsiElementResolveResult(p));
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        final String substring = myElement.getText().substring(0, getRangeInElement().getEndOffset());
        DefineDepPath defineDepPath = new DefineDepPath(substring, myElement.getProject(), complete);
        final Path resolvePath = defineDepPath.getResolvePath();
        if (resolvePath == null) {
            return null;
        }

        Path path;
        if (resolvePath.isAbsolute()) {
            path = resolvePath;
        } else {
            String elementFile = myElement
                    .getContainingFile()
                    .getOriginalFile()
                    .getVirtualFile().getParent().getPath();
            path = Paths.get(elementFile, String.valueOf(resolvePath.normalize())).normalize();
        }
        final VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(String.valueOf(path));
        if (fileByPath == null) {
            return null;
        }
        if (fileByPath.isDirectory()) {
            return PsiManager.getInstance(myElement.getProject()).findDirectory(fileByPath);
        }
        return PsiManager.getInstance(myElement.getProject()).findFile(fileByPath);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        ArrayList<LookupElement> completionResultSet = new ArrayList<LookupElement>();
        final String eleText = getRangeInElement().replace(myElement.getText(), "");
        final DefineDepPath defineDepPath = new DefineDepPath(eleText, myElement.getProject(), false);
        final Path resolvePath = defineDepPath.getResolvePath();
        Path path;
        if (resolvePath.isAbsolute()) {
            path = resolvePath;
        } else {
            String elementFile = myElement
                    .getContainingFile()
                    .getOriginalFile()
                    .getVirtualFile().getParent().getPath();
            path = Paths.get(elementFile, String.valueOf(resolvePath));
        }

        final VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(String.valueOf(path.normalize()));

        final PsiManager instance = PsiManager.getInstance(myElement.getProject());

        for (VirtualFile file : fileByPath.getChildren()) {
//                            .withTypeText(file.getName())

            if (file.isDirectory()) {//文件夹应该都要
                final PsiDirectory file1 = instance.findDirectory(file);
                completionResultSet.add(
                        LookupElementBuilder
                                .create(file1, file1.getName())
                                .withIcon(file1.getIcon(Iconable.ICON_FLAG_VISIBILITY))
                                .withInsertHandler(new DefinePathInsertHandler())

                );
            } else {
                final PsiFile file1 = instance.findFile(file);
                if (file1 != null) {
                    completionResultSet.add(
                            LookupElementBuilder
                                    .create(file1)
                                    .withIcon(file1.getIcon(Iconable.ICON_FLAG_VISIBILITY)).withInsertHandler(new DefinePathInsertHandler(defineDepPath.isShouldntHaveExt()))

                    );
                }

            }
        }
        return completionResultSet.toArray();
    }
}
