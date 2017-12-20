package com.netease.NEJ.ide.completions;

import com.intellij.lang.javascript.psi.*;
import com.intellij.lang.javascript.psi.impl.JSFileImpl;
import com.intellij.lang.javascript.psi.util.JSStubBasedPsiTreeUtil;
import com.intellij.lang.javascript.psi.util.JSTreeUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.netease.NEJ.core.define.DefineDepPath;
import com.netease.NEJ.core.define.NEJRecursiveReturnVistor;
import com.netease.NEJ.core.util.NEJUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * define 参数的解析
 * Created by abnerzheng on 2017/2/24.
 */
public class DefineParameterPsiReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (!NEJUtil.isDefineCallParamer(element)) {
            return PsiReference.EMPTY_ARRAY; //当不是Define调用的时候，直接返回
        }
        final JSFunctionExpression parentOfType = PsiTreeUtil.getParentOfType(element, JSFunctionExpression.class);
        final JSArrayLiteralExpression jsArrayLiteralExpression = PsiTreeUtil.getPrevSiblingOfType(parentOfType, JSArrayLiteralExpression.class);
        final JSParameterList jsParameterList = PsiTreeUtil.getParentOfType(element, JSParameterList.class);

        final JSLiteralExpression[] jsLiteralExpressions= PsiTreeUtil.getChildrenOfType(jsArrayLiteralExpression, JSLiteralExpression.class);
        final JSParameter[] jsParameters = PsiTreeUtil.getChildrenOfType(jsParameterList, JSParameter.class);

        for(int i = 0; i < jsLiteralExpressions.length; i++){
            if(jsParameters[i] == element){//找到文件
                JSLiteralExpression jsLiteralExpression = jsLiteralExpressions[i];
                List<PsiReference> references = new ArrayList<>();
                references.add( new DefinePathReference(jsLiteralExpression, new TextRange(0, jsLiteralExpression.getTextLength()), true));
                return references.toArray(new PsiReference[references.size()]);

//                final String text = jsLiteralExpressions[i].getText();


//                final DefineDepPath defineDepPath = new DefineDepPath(text, element.getProject());
//                final Path resolvePath = defineDepPath.getResolvePath();
//
//                if (resolvePath == null) {
//                    break;
//                }
//
//                Path path;
//                if (resolvePath.isAbsolute()) {
//                    path = resolvePath;
//                } else {
//                    String elementFile = element
//                            .getContainingFile()
//                            .getOriginalFile()
//                            .getVirtualFile().getParent().getPath();
//                    path = Paths.get(elementFile, String.valueOf(resolvePath.normalize())).normalize();
//                }
//                final VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(String.valueOf(path));
//                if (fileByPath == null) {
//                    return null;
//                }
//                if (fileByPath.isDirectory()) {
//                    break;
//                }
//                final PsiFile file = PsiManager.getInstance(element.getProject()).findFile(fileByPath);
//                final NEJRecursiveReturnVistor visitor = new NEJRecursiveReturnVistor();
//                file.accept(visitor);
//                final List<PsiElement> jsReturnStatement = visitor.getJsReturnStatement();
//                return jsReturnStatement.toArray(new PsiReference[jsReturnStatement.size()]);
//                if(file instanceof JSFile){
//                    final JSFile file1 = (JSFile) file;
//                    final JSFunctionExpression jsFunctionExpression = (JSFunctionExpression) PsiTreeUtil.collectElementsOfType(file1.getStatements()[0], JSFunctionExpression.class).toArray()[0];
//                    final Collection<JSReferenceExpression> returnedExpressions = JSStubBasedPsiTreeUtil.findReturnedExpressions(jsFunctionExpression, JSReferenceExpression.class);
//                    return returnedExpressions.toArray(new PsiReference[returnedExpressions.size()]);
//                }
            }
        }
        return new PsiReference[0];
    }
}
