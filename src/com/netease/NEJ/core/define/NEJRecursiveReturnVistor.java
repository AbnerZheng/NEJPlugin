package com.netease.NEJ.core.define;

import com.intellij.lang.javascript.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abnerzheng on 2017/2/24.
 */

public class NEJRecursiveReturnVistor extends JSRecursiveElementVisitor {
    private List<PsiElement> jsReturnStatements = new ArrayList<PsiElement>();
    private boolean visited = false;

    @Override
    public void visitJSReturnStatement(@NotNull JSReturnStatement node) {
        final PsiElement[] children = node.getChildren();
        if(children.length > 0){
            jsReturnStatements.add(children[0]);
        }
    }

    @Override
    public void visitJSFunctionExpression(@NotNull JSFunctionExpression node) {
        if(visited){return;}
        PsiElement parent = node.getParent();
        if(parent instanceof JSArgumentList){
            parent = parent.getParent();
            if(parent instanceof  JSCallExpression){
                final String text = ((JSCallExpression) parent).getMethodExpression().getText();
                if(text.equals("define") || text.equals("NEJ.define")){
                    this.visited = true;
                    super.visitJSFunctionExpression(node);
                }
            }
        }

    }

    @Override
    public void visitJSParameterList(JSParameterList node) {
    }

    @Override
    public void visitJSCallExpression(JSCallExpression node) {
        final String text = node.getMethodExpression().getText();

        if(text.equals("define") || text.equals("NEJ.define")){
            super.visitJSCallExpression(node);
        }
    }

    @Override
    public void visitJSBlock(JSBlockStatement node) {
        final JSCallExpression parentOfType = PsiTreeUtil.getParentOfType(node, JSCallExpression.class);
        final String text = parentOfType.getMethodExpression().getText();
        if(text.equals("define") || text.equals("NEJ.define")){
            super.visitJSBlock(node);
        }
    }

    public List<PsiElement> getJsReturnStatement() {
        return jsReturnStatements;
    }
}

