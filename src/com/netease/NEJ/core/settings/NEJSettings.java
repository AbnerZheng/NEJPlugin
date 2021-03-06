package com.netease.NEJ.core.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@State(
        name = "NEJConfiguration",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/NEJConfiguration.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class NEJSettings implements PersistentStateComponent<NEJSettings>
{
    private static final String CURRENT_VERSION = "0.1";
    private LinkedHashMap<String, String> amdImportNamingExceptions;
    private LinkedHashMap<String, String> parameterMap;
    private List<String> amdImportNamingExceptionsList;

    private String dojoSourcesDirectory;
    private String projectSourcesDirectory;
    private boolean preferRelativeImports;
    private boolean dojoSourcesShareProjectSourcesRoot;
    private boolean needsMoreDojoEnabled;
    private boolean addModuleIfThereAreNoneDefined;
    private boolean allowCaseInsensitiveSearch;
    private boolean setupWarningDisabled;
    private boolean refactoringEnabled;
    private String supportedFileTypes;
    private boolean singleQuotedModuleIDs;
    // this will be used for converting to module specific sources later
    private String version;

    public NEJSettings()
    {
        setupWarningDisabled = false;
        parameterMap = new LinkedHashMap<String, String>();
        amdImportNamingExceptionsList = new ArrayList<String>();
        amdImportNamingExceptionsList.add("dojo/sniff(has");
        amdImportNamingExceptionsList.add("doh/main(doh");

        refactoringEnabled = false;
        amdImportNamingExceptions = new LinkedHashMap<String, String>();
        amdImportNamingExceptions.put("dojo/sniff", "has");
        amdImportNamingExceptions.put("doh/main", "doh");
        preferRelativeImports = false;
        dojoSourcesShareProjectSourcesRoot = false;
        needsMoreDojoEnabled = true;
        addModuleIfThereAreNoneDefined = false;
        allowCaseInsensitiveSearch = true;
        supportedFileTypes = "jsp,js,php,html";
        singleQuotedModuleIDs = true;
    }

    @Deprecated
    public @NotNull LinkedHashMap<String, String> getExceptionsMap()
    {
        return amdImportNamingExceptions;
    }

    @Deprecated
    public @Nullable String getException(@NotNull String module)
    {
        if(amdImportNamingExceptions.containsKey(module))
        {
            return amdImportNamingExceptions.get(module);
        }
        else
        {
            return null;
        }
    }

    @Deprecated
    public LinkedHashMap<String, String> getAmdImportNamingExceptions() {
        return amdImportNamingExceptions;
    }

    public boolean isAllowCaseInsensitiveSearch() {
        return allowCaseInsensitiveSearch;
    }

    public void setAllowCaseInsensitiveSearch(boolean allowCaseInsensitiveSearch) {
        this.allowCaseInsensitiveSearch = allowCaseInsensitiveSearch;
    }

    @Deprecated
    public void setAmdImportNamingExceptions(LinkedHashMap<String, String> amdImportNamingExceptions) {
        this.amdImportNamingExceptions = amdImportNamingExceptions;
    }

    public String getDojoSourcesDirectory() {
        return dojoSourcesDirectory;
    }

    public void setDojoSourcesDirectory(String dojoSourcesDirectory) {
        this.dojoSourcesDirectory = dojoSourcesDirectory;
    }

    public String getProjectSourcesDirectory() {
        return projectSourcesDirectory;
    }

    public void setProjectSourcesDirectory(String projectSourcesDirectory) {
        this.projectSourcesDirectory = projectSourcesDirectory;
    }

    public boolean isPreferRelativeImports() {
        return preferRelativeImports;
    }

    public void setPreferRelativeImports(boolean preferRelativeImports) {
        this.preferRelativeImports = preferRelativeImports;
    }

    public LinkedHashMap<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(LinkedHashMap<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public boolean isDojoSourcesShareProjectSourcesRoot() {
        return dojoSourcesShareProjectSourcesRoot;
    }

    public boolean isAddModuleIfThereAreNoneDefined() {
        return addModuleIfThereAreNoneDefined;
    }

    public void setAddModuleIfThereAreNoneDefined(boolean addModuleIfThereAreNoneDefined) {
        this.addModuleIfThereAreNoneDefined = addModuleIfThereAreNoneDefined;
    }

    public void setDojoSourcesShareProjectSourcesRoot(boolean dojoSourcesShareProjectSourcesRoot) {
        this.dojoSourcesShareProjectSourcesRoot = dojoSourcesShareProjectSourcesRoot;
    }

    public String getSupportedFileTypes() {
        return supportedFileTypes;
    }

    public void setSupportedFileTypes(String supportedFileTypes) {
        this.supportedFileTypes = supportedFileTypes;
    }

    @Nullable
    @Override
    public NEJSettings getState() {
        return this;
    }

    @Override
    public void loadState(NEJSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isNeedsMoreDojoEnabled() {
        return needsMoreDojoEnabled;
    }

    public void setNeedsMoreDojoEnabled(boolean needsMoreDojoEnabled) {
        this.needsMoreDojoEnabled = needsMoreDojoEnabled;
    }

    public boolean isSetupWarningDisabled() {
        return setupWarningDisabled;
    }

    public void setSetupWarningDisabled(boolean setupWarningDisabled) {
        this.setupWarningDisabled = setupWarningDisabled;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRefactoringEnabled() {
        return refactoringEnabled;
    }

    public void setRefactoringEnabled(boolean refactoringEnabled) {
        this.refactoringEnabled = refactoringEnabled;
    }

    public  boolean isSingleQuotedModuleIDs() {
        return singleQuotedModuleIDs;
    }

    public void setSingleQuotedModuleIDs(boolean aSingleQuotedModuleIDs) {
        singleQuotedModuleIDs = aSingleQuotedModuleIDs;
    }
    public List<String> getAmdImportNamingExceptionsList() {
        return amdImportNamingExceptionsList;
    }

    public void setAmdImportNamingExceptionsList(List<String> amdImportNamingExceptionsList) {
        this.amdImportNamingExceptionsList = amdImportNamingExceptionsList;
    }

    public static NEJSettings getInstance(Project project){
        NEJSettings settings = ServiceManager.getService(project, NEJSettings.class);
        return settings;
    }

}
