package old_do_more_dojo.core.amd.define;

import old_do_more_dojo.core.amd.CompletionCallback;
import old_do_more_dojo.core.amd.importing.InvalidDefineException;
import com.intellij.lang.javascript.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 解释NEJ define语法
 * [NEJ define 规则](https://github.com/genify/nej/blob/master/doc/DEPENDENCY.md#define)
 */
public class DefineResolver
{
    private Logger logger = Logger.getLogger(DefineResolver.class);

    /**
     *
     * @param existingImportBlock
     * @param defines
     * @param parameters
     * @throws InvalidDefineException
     */
    public void addDefinesAndParametersOfImportBlock(JSCallExpression existingImportBlock, final List<PsiElement> defines, final List<PsiElement> parameters) throws InvalidDefineException
    {
        JSExpression[] arguments = existingImportBlock.getArguments(); //获取现存importBlock的参数

        DefineStatement items = getDefineStatementItemsFromArguments(arguments, existingImportBlock); //传入parse解析，获取一个define items对象
        if(items == null)
        {
            throw new InvalidDefineException();
        }

        // get the first argument which should be an array literal
        JSArrayLiteralExpression literalExpressions = items.getArguments();
        Collections.addAll(defines, literalExpressions.getExpressions());

        // get the second argument which should be a function
        JSFunctionExpression function = items.getFunction();
        Collections.addAll(parameters, function.getParameters());
    }

    /**
     * @deprecated use gatherDefineAndParameters instead.
     * @param defines
     * @param parameters
     * @param defineVisitor
     * @return
     */
    protected JSRecursiveElementVisitor getDefineAndParametersVisitor( final List<PsiElement> defines, final List<PsiElement> parameters, final PsiElementVisitor defineVisitor)
    {
        return new JSRecursiveElementVisitor() {
            @Override
            public void visitJSCallExpression(JSCallExpression element)
            {
                // if the user entered invalid syntax we don't want to account for every case, so just catch and log it
                try
                {
                    if(!element.getMethodExpression().getText().equals("define"))
                    {
                        return;
                    }

                    try
                    {
                        addDefinesAndParametersOfImportBlock(element, defines, parameters); //这里是获取开头为define的函数元素
                    }
                    catch(InvalidDefineException exc)
                    {
                        super.visitJSCallExpression(element); //直接给上级使用
                        return;
                    }
                }
                catch(Exception e)
                {
                    logger.log(Priority.INFO, "exception ecountered in DefineResolver ", e);
                }

                super.visitJSCallExpression(element);//上级
            }
        };
    }

    /**
     * @deprecated use gatherDefineAndParameters instead.
     * @param defines
     * @param parameters
     * @return
     */
    protected JSRecursiveElementVisitor getDefineAndParametersVisitor( final List<PsiElement> defines, final List<PsiElement> parameters)
    {
        return getDefineAndParametersVisitor(defines, parameters, null);
    }



    public void gatherDefineAndParameters(PsiFile psiFile, final List<PsiElement> defines, final List<PsiElement> parameters)
    {
        psiFile.accept(getDefineAndParametersVisitor(defines, parameters));
    }

    /**
     * Returns a visitor that will search a dojo module for its define statement, and execute a callback
     * when it finds it
     *
     * @deprecated use getDefineStatementItems instead.
     * @param onDefineFound the callback
     * @return the visitor
     */
    public JSRecursiveElementVisitor getDefineVisitor(final CompletionCallback onDefineFound)
    {
        return new JSRecursiveElementVisitor() {
            @Override
            public void visitJSCallExpression(JSCallExpression element)
            {
                if(!element.getMethodExpression().getText().equals("define"))
                {
                    super.visitJSCallExpression(element);
                    return;
                }

                // get the function
                DefineStatement items = getDefineStatementItemsFromArguments(element.getArguments(), element);
                if(items == null)
                {
                    onDefineFound.run(null);
                    return;
                }

                onDefineFound.run(new Object[] { element });
            }
        };
    }

    /**
     * Wrapper around the deprecated method getDefineVisitor. Using 'callbacks' was possibly the worst decision ever,
     * but a lot of the code uses this class. So this method wraps up the nasty callback syntax.
     *
     * @param file the PsiFile to retrieve the define statement from
     * @return the define statement and its items
     */
    public @Nullable DefineStatement getDefineStatementItems(PsiFile file)
    {
        final DefineStatement[] items = new DefineStatement[1];

        file.acceptChildren(getDefineVisitor(new CompletionCallback() {
            @Override
            public void run(Object[] result) {
                if(result == null || result[0] == null)
                {
                    items[0] = null;
                    return;
                }

                JSCallExpression callExpression = (JSCallExpression) result[0];
                items[0] = getDefineStatementItemsFromArguments(callExpression.getArguments(), callExpression);
            }
        }));

        if(items[0] == null)
        {
            return null;
        }

        return items[0];
    }

    /**
     * 获取该element最近的define块
     *
     * @param element
     * @return
     */
    public @Nullable DefineStatement getNearestImportBlock(PsiElement element)
    {
        PsiElement parent = element.getParent();
        while(parent != null)
        {
            if(parent instanceof JSCallExpression)
            {
                JSCallExpression statement = (JSCallExpression) parent;
                String methodText = statement.getMethodExpression().getText();
                if(statement.getMethodExpression() != null && (methodText.equals("NEJ.define") || methodText.equals("define"))) // 找到NEJ.define
                {
                    return getDefineStatementItemsFromArguments(statement.getArguments(), statement);
                }
            }

            parent = parent.getParent();
        }

        return null;
    }

    /**
     * 解析出define语句
     * NEJ.define([
     *      'util/ajax/tag',
     *      'util/template/jst'
     *    ],function(j,e,p,o,f,r){
     *
     * })
     * @param arguments define函数的参数
     * @param original 整个define函数的原型
     * @return 定义语句
     */
    public DefineStatement getDefineStatementItemsFromArguments(JSExpression[] arguments, JSCallExpression original)
    {
        if(!(arguments.length == 2 && arguments[0] instanceof JSArrayLiteralExpression && arguments[1] instanceof JSFunctionExpression)) // 参数为两个，且第一个参数为Array, 第二个参数为function
        {
            return null;
        }

        JSArrayLiteralExpression literalExpressions = (JSArrayLiteralExpression) arguments[0]; //依赖文件数组

        JSFunctionExpression function = (JSFunctionExpression) arguments[1]; //获取后面的匿名函数

        return new DefineStatement(literalExpressions, function, original);
    }

    /**
     * 从一个给定文件获取所有的define函数块
     *
     * @param file
     * @return
     */
    public Set<JSCallExpression> getAllImportBlocks(PsiFile file)
    {
        final Set<JSCallExpression> listOfDefinesToVisit = new LinkedHashSet<JSCallExpression>();
        JSRecursiveElementVisitor defineVisitor = new JSRecursiveElementVisitor() {
            @Override
            public void visitJSCallExpression(JSCallExpression expression)
            {
                if(expression != null && expression.getMethodExpression() != null && expression.getMethodExpression().getText().equals("define"))
                {
                    listOfDefinesToVisit.add(expression);
                }
                super.visitJSCallExpression(expression);
            }
        };

        file.accept(defineVisitor); //向该文件注册一个递归访问函数，筛选出所有define的部分

        return listOfDefinesToVisit;
    }
}
