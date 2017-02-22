package old_do_more_dojo.intellij.refactoring;

import old_do_more_dojo.core.amd.filesystem.SourceLibrary;
import old_do_more_dojo.core.amd.filesystem.SourcesLocator;
import old_do_more_dojo.core.amd.importing.ImportResolver;
import old_do_more_dojo.core.refactoring.MatchResult;
import old_do_more_dojo.core.refactoring.ModuleImporter;
import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoveRefactoringListener implements RefactoringElementListener
{
    private String originalFile = null;
    private PsiFile[] possibleFiles = new PsiFile[0];
    private List<MatchResult> matches = new ArrayList<MatchResult>();
    private List<MatchResult> moduleReferences = new ArrayList<MatchResult>();
    private ModuleImporter renamer = null;

    public MoveRefactoringListener(PsiFile originalPsiFile, String originalFile)
    {
        this.originalFile = originalFile;
        possibleFiles = new ImportResolver().getPossibleDojoImportFiles(originalPsiFile.getProject(), originalFile.substring(0, originalFile.indexOf('.')), true, false);

        renamer = new ModuleImporter(possibleFiles,
                originalFile.substring(0, originalFile.indexOf('.')),
                originalPsiFile,
                new SourcesLocator().getSourceLibraries(originalPsiFile.getProject()).toArray(new SourceLibrary[0]),
                ServiceManager.getService(originalPsiFile.getProject(),
                        NEJSettings.class).getNamingExceptionList());


        moduleReferences = renamer.findFilesThatModuleReferences(originalPsiFile);

        // here is where we need to go through, find all of the modules that reference this module, and produce a list of MatchResults
        matches = renamer.findFilesThatReferenceModule(SourcesLocator.getProjectSourceDirectories(originalPsiFile.getProject(), true), false);
    }

    /**
     * in this method, we've already identified which modules we need to update references in.
     * So, we are just going to go through each one and re-import the module using its new path and name
     *
     * @param psiElement
     */
    @Override
    public void elementMoved(@NotNull PsiElement psiElement)
    {
        PsiFile file = (PsiFile) psiElement;

        for(MatchResult result : matches)
        {
            renamer.reimportModule(result, file);
        }

        for(MatchResult result : moduleReferences)
        {
            renamer.reimportModule(result.getIndex(), file, result.getQuote(), result.getPath(), result.getModule(), result.getPluginResourceId(), false, result.getPluginResourceFile(), result.getCallExpression());
        }
    }

    @Override
    public void elementRenamed(@NotNull PsiElement psiElement) {
    }
}
