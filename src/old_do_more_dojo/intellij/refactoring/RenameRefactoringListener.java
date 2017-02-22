package old_do_more_dojo.intellij.refactoring;

import old_do_more_dojo.core.amd.filesystem.SourceLibrary;
import old_do_more_dojo.core.amd.filesystem.SourcesLocator;
import old_do_more_dojo.core.amd.importing.ImportResolver;
import old_do_more_dojo.core.refactoring.ModuleImporter;
import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.jetbrains.annotations.NotNull;

/**
 *  provides a rename operation for dojo modules that will rename the define literal and parameters,
 *  while using the correct path syntax.
 */
public class RenameRefactoringListener implements RefactoringElementListener {
    private String originalFile = null;
    private PsiFile[] possibleFiles = new PsiFile[0];

    public RenameRefactoringListener(PsiFile originalPsiFile, String originalFile)
    {
        this.originalFile = originalFile;

        possibleFiles = new ImportResolver().getPossibleDojoImportFiles(originalPsiFile.getProject(), originalFile.substring(0, originalFile.indexOf('.')), true, false);
    }

    @Override
    public void elementMoved(@NotNull PsiElement psiElement) {
    }

    @Override
    public void elementRenamed(@NotNull final PsiElement psiElement)
    {
        final String moduleName = originalFile.substring(0, originalFile.indexOf('.'));

        CommandProcessor.getInstance().executeCommand(psiElement.getProject(), new Runnable() {
            @Override
            public void run() {
                new ModuleImporter(possibleFiles,
                        moduleName,
                        (PsiFile) psiElement,
                        new SourcesLocator().getSourceLibraries(psiElement.getProject()).toArray(new SourceLibrary[0]),
                        ServiceManager.getService(psiElement.getProject(),
                                NEJSettings.class).getNamingExceptionList())
                        .findFilesThatReferenceModule(SourcesLocator.getProjectSourceDirectories(psiElement.getProject(), true), true);
            }
        },
        "Rename Dojo Module",
        "Rename Dojo Module");
    }
}
