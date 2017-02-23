package com.netease.NEJ.ide.completions;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by abnerzheng on 2017/2/16.
 */
public class DefinePathInsertHandler implements InsertHandler {
    private boolean withoutExt;

    @Override
    public void handleInsert(InsertionContext insertionContext, LookupElement lookupElement) {
        String lookupString = lookupElement.getLookupString();
        if(withoutExt && FilenameUtils.getExtension(lookupString).equals("js")){
            lookupString = FilenameUtils.getBaseName(lookupString);
        }
        insertionContext.getDocument().replaceString(
               insertionContext.getStartOffset(),
               insertionContext.getTailOffset(),
               lookupString
       );
    }

    public DefinePathInsertHandler(boolean withoutExt){
        this.withoutExt= withoutExt;
    }

    public DefinePathInsertHandler(){
        this.withoutExt = false;
    }
}
