package com.netease.NEJ.core.util;

import com.intellij.lang.javascript.psi.*;
import com.intellij.lang.javascript.psi.impl.JSCallExpressionImpl;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abnerzheng on 2017/2/16.
 */
public class NEJUtil {
    public static boolean isDefineCall(PsiElement element) {
        if (element.getParent() instanceof JSArrayLiteralExpression) {
        }
        PsiElement prevEl = element.getParent();
        if (prevEl != null && prevEl instanceof JSArrayLiteralExpression) {
            prevEl = prevEl.getParent();
            if (prevEl != null && prevEl instanceof JSArgumentList) {
                prevEl = prevEl.getParent();
                if (prevEl != null && prevEl instanceof JSCallExpression) {
                    String methodExpr = ((JSCallExpression) prevEl).getMethodExpression().getText();
                    return methodExpr.contains("define");
                }
            }
        }

        return false;
    }

    public static boolean isDefineCallParamer(PsiElement element) {
        PsiElement parent = element.getParent();
        if (!(parent instanceof JSParameterList)) {
            return false;
        }

        parent = parent.getParent();
        if(!(parent instanceof JSFunctionExpression)){ // 对instanceOf检查来说，null没有必要检查
            return false;
        }
        parent = parent.getParent();
        if(!(parent instanceof JSArgumentList)){
            return false;
        }
        parent = parent.getParent();
        if(!(parent instanceof JSCallExpression)){
            return false;
        }
        final JSCallExpression parent1 = (JSCallExpression) parent;
        final String text = parent1.getMethodExpression().getText();
        return text.equals("define") || text.equals("NEJ.define");
    }

    public static List<PsiElement> getFilesToCompletion(PsiElement element) {
        List<PsiElement> completions = new ArrayList<PsiElement>();
        String value = element.getText().replace("'", "").replace("\"", ""); // 获取文本内容
        String valuePath = value;
        boolean prefixMark = value.contains("!"); //是否有前缀

        VirtualFile elementFile = element
                .getContainingFile()
                .getOriginalFile()
                .getVirtualFile().getParent();

        String type;
        if (prefixMark) {
            String[] exclamationMarkSplit = valuePath.split("!"); // 提取后面的内容
            type = exclamationMarkSplit[0];
            if (exclamationMarkSplit.length == 2) {
                valuePath = exclamationMarkSplit[1];
            } else {
                valuePath = "";
            }
        }

        boolean isRelative = valuePath.startsWith(".");
        if (isRelative) {
            final LocalFileSystem instance = LocalFileSystem.getInstance();
            final Path path = Paths.get(elementFile.getPath(), valuePath).normalize();
            final String pathString = String.valueOf(path);
            final VirtualFile fileByPath = instance.findFileByPath(pathString);
            if (fileByPath == null) {//不存在
                final String directory = pathString.subSequence(0, pathString.lastIndexOf('/')).toString(); //获取目录
                final VirtualFile fileByPath1 = instance.findFileByPath(directory);
                if (fileByPath1.isDirectory()) {
                    final VirtualFile[] children = fileByPath1.getChildren();
                    for (VirtualFile v : children) {
                        if (v.getPath().contains(pathString)) {
                            final PsiDirectory directory1 = PsiManager.getInstance(element.getProject()).findDirectory(v);
                            completions.add(directory1);
                        }
                    }
                }

            } else if (fileByPath.isDirectory()) { //是目录
                final PsiElement[] children = PsiManager.getInstance(element.getProject()).findDirectory(fileByPath).getChildren();
                for (PsiElement e : children) {
                    completions.add(e);
                }
            }
        }


//        PsiDirectory fileDirectory = element
//                .getContainingFile()
//                .getOriginalFile()
//                .getContainingDirectory();
//        if (null == fileDirectory) {
//            return completions;
//        }
//        String filePath = fileDirectory
//                .getVirtualFile()
//                .getPath();
//
//        boolean startSlash = valuePath.startsWith("/");
//        if (startSlash) {
//            valuePath = valuePath.substring(1);
//        }
//
//        boolean oneDot = valuePath.startsWith("./");
//        if (oneDot) {
//            if (filePath.isEmpty()) {
//                valuePath = valuePath.substring(2);
//            } else {
//                valuePath = valuePath.replaceFirst(".", filePath);
//            }
//        }
//
//        if (valuePath.startsWith("..")) {
//            doubleDotCount = FileUtils.getDoubleDotCount(valuePath);
//            String[] pathsOfPath = filePath.split("/");
//            if (pathsOfPath.length > 0) {
//                if (doubleDotCount > 0) {
//                    if (doubleDotCount > pathsOfPath.length || filePath.isEmpty()) {
//                        return new ArrayList<String>();
//                    }
//                    pathOnDots = FileUtils.getNormalizedPath(doubleDotCount, pathsOfPath);
//                    dotString = StringUtil.repeat("../", doubleDotCount);
//                    if (valuePath.endsWith("..")) {
//                        notEndSlash = true;
//                    }
//                    if (valuePath.endsWith("..") || !StringUtil.isEmpty(pathOnDots)) {
//                        dotString = dotString.substring(0, dotString.length() - 1);
//                    }
//                    valuePath = valuePath.replaceFirst(dotString, pathOnDots);
//                }
//            }
//        }
//
//        List<String> allFiles = FileUtils.getAllFilesInDirectory(
//                getWebDir(elementFile),
//                getWebDir(elementFile).getPath() + '/',
//                ""
//        );
//        List<String> aliasFiles = requirePaths.getAllFilesOnPaths();
//        aliasFiles.addAll(packageConfig.getAllFilesOnPackages());
//
//        String requireMapModule = FileUtils.removeExt(element
//                                                              .getContainingFile()
//                                                              .getOriginalFile()
//                                                              .getVirtualFile()
//                                                              .getPath()
//                                                              .replace(
//                                                                      getWebDir(elementFile).getPath() + '/',
//                                                                      ""
//                                                              ),
//                                                      ".js"
//        );
//
//        completions.addAll(requireMap.getCompletionByModule(requireMapModule));
//
//        String valuePathForAlias = valuePath;
//        if (!oneDot && 0 == doubleDotCount && !startSlash && !getBaseUrl().isEmpty()) {
//            valuePath = FileUtils.join(getBaseUrl(), valuePath);
//        }
//
//        for (String file : allFiles) {
//            if (file.startsWith(valuePath)) {
//                 Prepare file path
//                if (oneDot) {
//                    if (filePath.isEmpty()) {
//                        file = "./" + file;
//                    } else {
//                        file = file.replaceFirst(filePath, ".");
//                    }
//                }
//
//                if (doubleDotCount > 0) {
//                    if (!StringUtil.isEmpty(valuePath)) {
//                        file = file.replace(pathOnDots, "");
//                    }
//                    if (notEndSlash) {
//                        file = '/' + file;
//                    }
//                    file = dotString + file;
//                }
//
//                if (!oneDot && 0 == doubleDotCount && !startSlash && !getBaseUrl().isEmpty()) {
//                    file = file.substring(getBaseUrl().length() + 1);
//                }
//
//                if (startSlash) {
//                    file = '/' + file;
//                }
//
//                addToCompletion(completions, file, exclamationMark, type);
//            }
//        }
//
//        for (String file : aliasFiles) {
//            if (file.startsWith(valuePathForAlias)) {
//                addToCompletion(completions, file, exclamationMark, type);
//            }
//        }
//
        return completions;

    }
}
