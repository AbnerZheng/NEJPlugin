package old_do_more_dojo.intellij.refactoring;

import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import org.jetbrains.annotations.Nullable;

public class RenameRefactoringProvider implements RefactoringElementListenerProvider {
    @Nullable
    @Override
    public RefactoringElementListener getListener(PsiElement psiElement) {
        if(!(psiElement instanceof  PsiFile))
        {
            return null;
        }

        PsiFile file = (PsiFile) psiElement;
        String extension = file.getVirtualFile().getExtension();

        if(!ServiceManager.getService(file.getProject(), NEJSettings.class).isNeedsMoreDojoEnabled())
        {
            return null; // don't want to refactor if we've disabled Needs More Dojo.
        }

        if(!ServiceManager.getService(file.getProject(), NEJSettings.class).isRefactoringEnabled())
        {
            return null;
        }

        if(!extension.equals("js"))
        {
            return null;
        }

        if(!file.getText().contains("define"))
        {
            return null; // not a dojo module
        }

        return new RenameRefactoringListener(file, file.getName());
    }
}
