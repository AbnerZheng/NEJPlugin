package com.netease.NEJ.core.define;

import com.intellij.lang.javascript.psi.JSArrayLiteralExpression;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSFunctionExpression;

/**
 * 表示define block
 */
public class DefineStatement
{
    private JSArrayLiteralExpression arguments; //参数
    private JSFunctionExpression function; //函数体
    private JSCallExpression callExpression;

    public DefineStatement(JSArrayLiteralExpression arguments, JSFunctionExpression function, JSCallExpression originalParent) {
        this.arguments = arguments;
        this.function = function;
        this.callExpression = originalParent;
    }

    public JSArrayLiteralExpression getArguments() {
        return arguments;
    }

    public JSFunctionExpression getFunction() {
        return function;
    }

    public JSCallExpression getCallExpression() {
        if(callExpression == null)
        {
            throw new RuntimeException("callExpression was not set but is being accessed");
        }

        return callExpression;
    }
}
