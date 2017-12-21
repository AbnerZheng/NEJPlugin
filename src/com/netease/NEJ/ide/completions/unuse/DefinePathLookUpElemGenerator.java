package com.netease.NEJ.ide.completions;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * ππ‘Ïloopup element
 * Created by abnerzheng on 2017/2/16.
 */
public class DefinePathLookUpElemGenerator {
    private final Language elemLanguage;
    private final String filePath;

    public DefinePathLookUpElemGenerator(
            @NotNull Language elemLanguage,
            @NotNull Project currentProject,
            @NotNull String filePath) {
        this.elemLanguage = elemLanguage;
        this.filePath = filePath;
    }

//    @NotNull
//    LookupElement generate(){
//    }
//
//    @NotNull
//    private String generateJSPathString(){
//
//    }
}
