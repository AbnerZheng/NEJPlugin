package com.netease.NEJ.core.define;

import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.netease.NEJ.core.settings.NEJSettings;

import java.util.LinkedHashMap;

/**
 * Created by abnerzheng on 2017/2/23.
 */
public class NEJTestCase extends CodeInsightFixtureTestCase {
    protected void addPathSetting(String s, String t){
        LinkedHashMap<String, String> parameterMap = NEJSettings.getInstance(myFixture.getProject()).getParameterMap();
        parameterMap.put(s, t);
    }

    protected LinkedHashMap<String, String> getPathSetting(){
        return NEJSettings.getInstance(myFixture.getProject()).getParameterMap();
    }
}
