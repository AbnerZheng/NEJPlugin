package com.netease.NEJ.core.amd.objectmodel;

import com.netease.NEJ.core.amd.naming.NameException;
import com.netease.NEJ.core.amd.naming.NameResolver;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.psi.PsiElement;

import java.util.List;

public class AMDValidator
{
    public static final String DOJO_LEGACY_DECLARE = "dojo.declare";
    public static final String DOJO_DECLARE = "declare";

    public static boolean isDeclareFunction(PsiElement element)
    {
        return element.getText().equals(DOJO_DECLARE) || element.getText().equals(DOJO_LEGACY_DECLARE);
    }

    /**
     * determines if an AMD literal in a define statement matches its corresponding parameter
     *
     * @param define
     * @param parameter
     * @param exceptions a map of <module, parameter> exceptions to test for when normal naming conventions are
     * insufficient
     * @return whether or not the define and parameter are consistently named
     */
    public boolean defineMatchesParameter(String define, String parameter, List<NameException> exceptions)
    {
        // simple case can be taken care of by just matching the stuff after / with the parameter
        // also case insensitive because the programmer can use any casing for the parameter
        String defineComparison = define.replaceAll("'|\"", "").replace("\"", "");
        String parameterComparison = parameter.toLowerCase();

        // if an exception like dojo/sniff -> has yields a match, then return true. Else, just continue
        // because they could also use a more standard convention like dojo/sniff -> sniff
        for(NameException exception : exceptions)
        {
            if(exception.getLiteral().equals(defineComparison) && parameterComparison.equals(exception.getParameter().toLowerCase()))
            {
                return true;
            }
        }

        defineComparison = defineComparison.toLowerCase();

        if(defineComparison.contains("/_base/fx"))
        {
            return parameterComparison.equals("basefx") || parameterComparison.equals("fx");
        }

        if(defineComparison.indexOf('/') != -1)
        {
            String defineName = defineComparison.substring(defineComparison.lastIndexOf('/') + 1);

            if(defineName.equals(parameterComparison))
            {
                return true;
            }

            // stuff like dom-construct is often referenced as domConstruct and should be considered valid
            if(defineName.contains("-") && defineName.replace("-", "").equals(parameterComparison))
            {
                return true;
            }

            // other stuff such as _WidgetBase needs to be accounted for
            if(defineName.startsWith("_") && defineName.substring(1).equals(parameterComparison))
            {
                return true;
            }
        }
        else
        {
            if(defineComparison.equals(parameterComparison))
            {
                return true;
            }
        }

        // there is a hard-coded comparison against dojo/text since it is used to define templates
        if(defineComparison.startsWith(NameResolver.TEXTPLUGIN) || defineComparison.startsWith(NameResolver.I18NPLUGIN))
        {
            // grab everything after the !
            String fileName = defineComparison.substring(defineComparison.lastIndexOf('!') + 1);

            if(fileName.indexOf('/') != -1)
            {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }

            if(fileName.indexOf('.') != -1)
            {
                fileName = fileName.substring(0, fileName.indexOf('.'));
            }

            boolean isText = defineComparison.startsWith(NameResolver.TEXTPLUGIN);
            boolean isI18n = defineComparison.startsWith(NameResolver.I18NPLUGIN);

            if(isText && parameterComparison.contains(fileName + "template"))
            {
                return true;
            }
            else if(isText && parameterComparison.equals("template"))
            {
                return true;
            }
            // can't really enforce a stricter convention in this case
            else if(isI18n && parameterComparison.startsWith("i18n"))
            {
                return true;
            }
            else if (isI18n && parameterComparison.startsWith("resources"))
            {
                return true;
            }
            else if(isI18n && parameterComparison.startsWith("nls"))
            {
                return true;
            }
            else if(isI18n && parameterComparison.startsWith("_nls"))
            {
                return true;
            }

            return fileName.contains(parameterComparison);
        }
        // a custom amd plugin
        else if (defineComparison.contains("!"))
        {
            // grab everything after the !
            String resourceId = defineComparison.substring(defineComparison.lastIndexOf('!') + 1);
            // get everything before the !
            String pluginId = defineComparison.substring(0, defineComparison.lastIndexOf('!'));

            if(resourceId.indexOf('/') != -1)
            {
                resourceId = resourceId.substring(resourceId.lastIndexOf('/') + 1);
            }

            if(resourceId.indexOf('.') != -1)
            {
                resourceId = resourceId.substring(0, resourceId.indexOf('.'));
            }

            return resourceId.equals(parameterComparison) || parameterComparison.equals(pluginId) || parameterComparison.equals(NameResolver.getModuleName(pluginId));
        }

        // By the time you get here, there is one more check needed. If there are two imported modules a/b/module1 and b/c/module1,
        // module1, bModule1, and cModule1 should be accepted as valid
        if(defineComparison.indexOf('/') > 0)
        {
            String[] result = defineComparison.split("/");

            if(result.length > 1)
            {
                String prefix = result[result.length - 2];
                String name = result[result.length - 1];
                return parameterComparison.equals((prefix + name).replaceAll("_|-", ""));
            }
        }

        return false;
    }

    public static boolean elementIsAttachPoint(PsiElement element)
    {
        /*
            It's hard to detect when an element is an attach point, because of the use of this inside other functions

            this.attachpoint
            that.attachpoint

            ideally we would parse the template file in the beginning and cache all of the attach points,
            maybe that's a todo item...
         */
        if(element == null || element.getParent() == null || !(element.getParent() instanceof JSReferenceExpression))
        {
            return false;
        }

        // we can exclude JSCallExpressions at least because you will never reference an attach point like
        // this.attachpoint(...)
        if(element.getParent().getParent() instanceof JSCallExpression)
        {
            return false;
        }

        return true;
    }
}
