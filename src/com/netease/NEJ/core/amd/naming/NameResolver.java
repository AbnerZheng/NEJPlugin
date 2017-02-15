package com.netease.NEJ.core.amd.naming;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.psi.PsiFile;
import com.netease.NEJ.core.settings.NEJSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NameResolver {
    public static final String I18NPLUGIN = "dojo/i18n!";
    public static final String TEXTPLUGIN = "dojo/text!";

    public static String defineToParameter(String define, List<NameException> exceptions) {
        return defineToParameter(define, exceptions, false, null);
    }

    public static String defineToParameter(String define, List<NameException> exceptions, boolean useModulePath, String absoluteModulePath) {
        define = define.replaceAll("\"|'", "");

        // since there are two fx modules we have this exception
        if (define.contains("/_base/fx")) {
            return "baseFx";
        }

        String firstModuleException = null;
        String absolutePathException = null;
        for (NameException exception : exceptions) {
            if (exception.getLiteral().equals(define)) {
                firstModuleException = exception.getParameter();
            }
            if (useModulePath && exception.getLiteral().equals(absoluteModulePath)) {
                absolutePathException = exception.getParameter();
            }
        }
        // check all exceptions
        if (firstModuleException != null) {
            return firstModuleException;
        } else if (useModulePath && absoluteModulePath != null && absolutePathException != null) {
            return absolutePathException;
        }

        if (define.startsWith(TEXTPLUGIN) || define.startsWith(I18NPLUGIN)) {
            String postPlugin = define.substring(define.indexOf('!') + 1);

            if (postPlugin.indexOf('/') != -1) {
                postPlugin = postPlugin.substring(postPlugin.lastIndexOf('/') + 1);
            }

            if (postPlugin.indexOf('.') != -1) {
                postPlugin = postPlugin.substring(0, postPlugin.indexOf('.'));
            }

            if (!define.startsWith(I18NPLUGIN)) {
                postPlugin = postPlugin.toLowerCase() + "Template";
            }

            return postPlugin;
        }

        String result = define.substring(define.lastIndexOf("/") + 1);
        if (result.contains("-")) {
            int index = result.indexOf('-');
            result = result.replace("-", "");
            result = result.substring(0, index) + ("" + result.charAt(index)).toUpperCase() + result.substring(index + 1);
        }

        result = result.replaceAll("_", "");

        if (result.contains("!")) {
            result = result.substring(0, result.indexOf('!'));
        }

        if (useModulePath && absoluteModulePath != null) {
            String[] parts = absoluteModulePath.split("/");
            if (parts.length > 1) {
                String prefix = parts[parts.length - 2].replaceAll("_", "");
                String name = parts[parts.length - 1].replaceAll("_", "");

                if (name.contains("-")) {
                    int index = name.indexOf('-');
                    name = name.replace("-", "");
                    name = name.substring(0, index) + ("" + name.charAt(index)).toUpperCase() + name.substring(index + 1);
                }

                result = prefix + ("" + name.charAt(0)).toUpperCase() + name.substring(1);
                return result;
            }
        }

        return result;
    }

    /**
     * if a module uses the plugin syntax, returns only the actual plugin name
     *
     * @param module the possible plugin module
     * @return the module name
     */
    public static String getAMDPluginNameIfPossible(String module) {
        if (module.indexOf('!') > 0 && module.indexOf('/') != 0) {
            String moduleName = getModuleName(module);
            return moduleName;
        } else if (module.indexOf('!') > 0) {
            return module.substring(0, module.lastIndexOf('!'));
        } else {
            return module;
        }
    }

    /**
     * @param module
     * @param fileContainingDefine
     * @return
     * @author {Abnerzheng}
     */
    public static Path getMergedPath(String module, PsiFile fileContainingDefine) {
        final NEJSettings service = ServiceManager.getService(fileContainingDefine.getProject(), NEJSettings.class);
        LinkedHashMap<String, String> pathSetting = service.getParameterMap();
//        pathSetting.put("eutil", "./node_modules/@study/edu-front-util/src/");
//        pathSetting.put("eui", "./node_modules/@study/edu-front-ui/src/js/");
//        pathSetting.put("rui", "./node_modules/@study/edu-front-regularUI/src/js/");
//        pathSetting.put("own","./src/javascript/com/web/");
//        pathSetting.put("pool", "./lib/");
//        pathSetting.put("pro", "./src/javascript/");
//        pathSetting.put("lib", "./lib/nej/src/");
        if(!module.substring(module.lastIndexOf("/")).contains(".")){
            module += ".js";
        }

        Project project = fileContainingDefine.getProject();
        String substring = module.substring(0, module.indexOf("/"));
        if(pathSetting.containsKey(substring)){
            return Paths.get(project.getBasePath(), pathSetting.get(substring),module.substring(substring.length()+1));
        }else if(!(module.startsWith(".") || module.startsWith("/"))){
            return Paths.get(project.getBasePath(), pathSetting.get("lib"),module);
        }else{
            try{
                return Paths.get(fileContainingDefine.getVirtualFile().getParent().getPath ()).resolve(module).normalize();
            }catch (NullPointerException e){
                return null;
            }
        }
    }

    /**
     * given a module, returns the resource id if it's an AMD plugin
     *
     * @param module
     * @param includeExclamationPoint
     * @return
     */
    public static String getAMDPluginResourceIfPossible(String module, boolean includeExclamationPoint) {
        if (module.indexOf('!') > 0) {
            if (includeExclamationPoint) {
                return module.substring(module.indexOf('!'));
            } else {
                return module.substring(module.indexOf('!') + 1);
            }
        } else {
            return "";
        }
    }

    /**
     * 处理诸如Regular!, text!, json!等写法，获取真正文本
     *
     * @param modulePath
     * @return
     * @author{AbnerZheng}
     */
    public static String getModuleName(String modulePath) {
        modulePath = modulePath.replaceAll("'|\"", ""); //删除所有引号

        if (modulePath.contains("!")) { //包含 "!"
            String moduleWithoutPlugin = modulePath.substring(modulePath.indexOf('!') + 1, modulePath.length());
            return moduleWithoutPlugin;
        } else {
            return modulePath;
        }
    }

    /**
     * converts a module to its correct hyphenated form if possible.
     * <p>
     * For example, domClass becomes dom-class, domAttr becomes dom-attr, etc.
     *
     * @param module
     * @return the hyphenated module, if possible. OR null if it wasn't converted
     */
    public static
    @Nullable
    String getPossibleHyphenatedModule(@NotNull String module) {
        try {
            // the obvious ones we're going for
            if (module.startsWith("dom")) {
                return "dom-" + module.substring(3).toLowerCase();
            }

            // otherwise convert it if you use a camel-case convention
            // http://stackoverflow.com/a/7599674/324992
            String[] terms = module.split("(?<=[a-z])(?=[A-Z])");
            StringBuilder result = new StringBuilder(terms[0]);
            result.append("-");

            for (int i = 1; i < terms.length; i++) {
                result.append(terms[i].toLowerCase());
            }

            return result.toString();
        } catch (Exception e) {
            // Yes, this is a catch all, but for a reason. If anything bad happens in here,
            // we just want to return null. We don't want to have to worry about this method
            // failing where this is being consumed.
            return null;
        }
    }

    /**
     * Dojo relative paths use "./" for same level and "../" for additional levels.
     * This will ensure that a relative path passed to the method returns a dojo-friendly version
     *
     * @param relativePath
     * @return
     */
    public static String convertRelativePathToDojoPath(String relativePath) {
        if (relativePath != null) {
            // need to use dojo syntax when two files are in the same directory
            if (relativePath.equals(".")) {
                relativePath = "./";
            } else if (relativePath.charAt(0) != '.' && relativePath.charAt(0) != '/') {
                // top level module
                relativePath = "./" + relativePath;
            }

            return relativePath;
        }

        return null;
    }

    public static String getModulePath(String fullModulePath) {
        String modulePath = fullModulePath;
        if (modulePath.contains("!")) {
            modulePath = modulePath.substring(0, modulePath.lastIndexOf('!'));
        }
        modulePath = modulePath.substring(0, modulePath.lastIndexOf('/') + 1);

        return modulePath;
    }

    public static String getModuleAndPathWithoutPluginResourceId(String fullModulePath) {
        String modulePath = fullModulePath;
        if (modulePath.contains("!")) {
            modulePath = modulePath.substring(0, modulePath.lastIndexOf('!'));
        }
        return modulePath;
    }
}
