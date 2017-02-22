package com.netease.NEJ.ide.completions;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * Created by abnerzheng on 2017/2/16.
 */
public class DefinePathInsertHandler implements InsertHandler {
    private static final DefinePathInsertHandler instace = new DefinePathInsertHandler();

    @Override
    public void handleInsert(InsertionContext insertionContext, LookupElement lookupElement) {
       insertionContext.getDocument().replaceString(
               lookupElement.getPsiElement().getTextOffset() + 1,
               insertionContext.getTailOffset(),
               lookupElement.getLookupString()
       );
    }

    public static DefinePathInsertHandler getInstance(){
        return instace;
    }

}
