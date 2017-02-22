package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.amd.AMDImport;
import old_do_more_dojo.core.amd.define.NearestAMDImportLocator;
import old_do_more_dojo.core.amd.psi.AMDPsiUtil;
import old_do_more_dojo.core.util.PsiFileUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class DeleteSingleImportAction extends JavaScriptAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final PsiFile psiFile = PsiFileUtil.getPsiFileInCurrentEditor(e.getProject());
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if(editor == null)
        {
            return;
        }

        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());

        final AMDImport amdImport = new NearestAMDImportLocator().findNearestImport(element, psiFile);

        if(amdImport == null)
        {
            Notifications.Bus.notify(new Notification("needsmoredojo", "Remove Import", "No valid literal/parameter pair found to delete", NotificationType.WARNING));
            return;
        }

        CommandProcessor.getInstance().executeCommand(psiFile.getProject(), new Runnable() {
            @Override
            public void run() {
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    AMDPsiUtil.removeSingleImport(amdImport);
                }
            });
            }
        },
        "Remove Import",
        "Remove Import");
    }
}
