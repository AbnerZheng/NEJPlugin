package old_do_more_dojo.core.util;

import com.intellij.lang.ASTNode;
import com.intellij.lang.javascript.psi.impl.JSChangeUtil;
import com.intellij.lang.javascript.psi.util.JSUtils;
import com.intellij.psi.PsiElement;

public class JSUtil
{
    public static PsiElement addStatementBeforeElement(PsiElement parent, PsiElement element, String statement, String whitespace)
    {
        ASTNode node = JSChangeUtil.createStatementFromText(parent.getProject(), statement, JSUtils.getDialect(parent.getContainingFile()));
        parent.addBefore(node.getPsi(), element);

        if(!whitespace.equals(""))
        {
            parent.addBefore(JSChangeUtil.createJSTreeFromText(parent.getProject(), whitespace).getPsi(), element);
        }

        return node.getPsi();
    }

    public static PsiElement addStatementAfterElement(PsiElement parent, PsiElement element, String statement, String whitespace)
    {
        if(!whitespace.equals(""))
        {
            //parent.addAfter(JSChangeUtil.createExpressionFromText(parent.getProject(), statement).getPsi(), comma);
        }

        return null;
    }

    public static PsiElement addStatementBeforeElement(PsiElement parent, PsiElement element, String statement)
    {
        return addStatementBeforeElement(parent, element, statement, "\n\n");
    }

    public static PsiElement createStatement(PsiElement parent, String statement)
    {
        ASTNode node = JSChangeUtil.createStatementFromText(parent.getProject(), statement, JSUtils.getDialect(parent.getContainingFile()));
        return node.getPsi();
    }

    public static PsiElement createExpression(PsiElement parent, String statement)
    {
        ASTNode node = JSChangeUtil.createExpressionFromText(parent.getProject(), statement, JSUtils.getDialect(parent.getContainingFile()));
        return node.getPsi();
    }

    public static PsiElement addStatement(PsiElement parent, String statement)
    {
        ASTNode node = JSChangeUtil.createStatementFromText(parent.getProject(), statement, JSUtils.getDialect(parent.getContainingFile()));
        parent.add(node.getPsi());

        return node.getPsi();
    }

    public static PsiElement addExpression(PsiElement parent, String statement)
    {
        ASTNode node = JSChangeUtil.createExpressionFromText(parent.getProject(), statement, JSUtils.getDialect(parent.getContainingFile()));
        parent.add(node.getPsi());

        return node.getPsi();
    }
}
