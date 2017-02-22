package com.netease.NEJ.core.define;

import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.netease.NEJ.core.settings.NEJSettings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * ��ʾһ��define������path
 * Created by abnerzheng on 2017/2/21.
 */
public class DefineDepPath {
    private final String pathOri;
    private static final HashSet<String> knowExt = new HashSet<String>() {
        {
            add("json");
            add("text");
            add("regular");
            add("css");
        }
    };
    private final Project project;
    private String fileTypeMark;
    private Path pathSol;

    public DefineDepPath(String path, Project project) {
        this(path, project, true);
    }

    public DefineDepPath(String path, Project project, boolean complete) {
        path = path.replaceAll("\"", "").replaceAll("'", "");
        this.pathOri = path;
        this.project = project;
        resolve(complete);
    }

    public Path getResolvePath() {
        return pathSol;
    }

    /**
     * ��·���Ľ���
     * 'base/element',
     * 'pro/extend/util',
     * '/path/to/file.js',
     * '{platform}patch.js',
     * 'text!/path/to/file.css',
     * 'text!/path/to/file.html'
     * 'base/element',
     * 'lib/base/element',
     * '{lib}base/element.js'
     */
    private void resolve(boolean complete) {
        final String[] split = pathOri.split("/");
        final ArrayList<String> strings = Lists.newArrayList(split);
        String first = strings.get(0);
        if (first.contains("!")) {
            // mask
            final String[] split1 = first.split("!");
            if (knowExt.contains(split1[0])) { //˵�����ֶ�
                strings.remove(0);
                this.fileTypeMark = split1[0];
                if(split1.length > 1){ //������Ǿ��Ե�ַ
                    strings.add(0, split1[1]);
                }
                first = strings.get(0);
            }else{
                // �������ļ���
                // ���账��
            }
        }

        if (first.equals("..") || first.equals(".") || first.equals("")) {//���Ի����·��
            this.pathSol = Paths.get(String.join("/", strings));
            return;
        } else {
            NEJSettings settingsService = ServiceManager.getService(project, NEJSettings.class);
            final LinkedHashMap<String, String> parameterMap = settingsService.getParameterMap();
            String suff = ".js";
            if (first.startsWith("{")) { //ʹ�û����ű�ʶ���ò���
                final String s = first.replaceFirst("\\{", "");
                final String[] split1 = s.split("}");
                strings.remove(0);
                for (int i = split1.length - 1; i > -1; --i) {
                    strings.add(0, split1[i]);
                }
                first = strings.get(0);
                suff = "";
            }
            if (!complete || this.fileTypeMark != null) {
                suff = "";
            }

            if (parameterMap.containsKey(first)) {
                strings.remove(0);
                this.pathSol = Paths.get(project.getBasePath(), parameterMap.get(first), String.join("/", strings) + suff);
            } else if (parameterMap.containsKey("lib")) {
                this.pathSol = Paths.get(project.getBasePath(), parameterMap.get("lib"), String.join("/", strings) + suff);
            } else {
                this.pathSol = null;
            }
        }

    }

    /**
     * �ж��Ƿ��ǺϷ�·��
     *
     * @return {boolean}
     */
    boolean isValidPath() {
        return true;
    }
}
