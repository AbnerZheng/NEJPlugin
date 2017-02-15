package com.netease.NEJ.intellij.actions;

import com.netease.NEJ.core.amd.importing.UnusedImportBlockEntry;
import com.netease.NEJ.core.amd.importing.UnusedImportsRemover;
import com.netease.NEJ.core.settings.NEJSettings;
import com.netease.NEJ.core.util.PsiFileUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *  runs when the user wants to remove all unused imports
 */
public class UnusedImportsAction extends JavaScriptAction {
    protected boolean deleteMode = false;

    public static void removeUnusedImports(PsiFile psiFile)
    {
        final UnusedImportsRemover detector = new UnusedImportsRemover();
        final List<UnusedImportBlockEntry> results = detector.filterUsedModules(psiFile, ServiceManager.getService(psiFile.getProject(), NEJSettings.class).getParameterMap());

        int numDeleted = 0;

        for(UnusedImportBlockEntry entry : results)
        {
            if(entry.getDefines() == null || entry.getParameters() == null || entry.getDefines().size() == 0 || entry.getParameters().size() == 0)
            {
                continue;
            }

            UnusedImportsRemover.RemovalResult result = detector.removeUnusedParameters(entry.getParameters(), entry.getDefines());
            numDeleted += result.getElementsToDelete().size();

            if(result.getElementsToDelete().size() > 0)
            {
                Notifications.Bus.notify(new Notification("needsmoredojo", "Remove Unused Imports", result.getDeletedElementNames(), NotificationType.INFORMATION));
            }
        }

        if(numDeleted == 0)
        {
            Notifications.Bus.notify(new Notification("needsmoredojo", "Remove Unused Imports", "No unused imports were detected to delete", NotificationType.INFORMATION));
        }
    }

    public void actionPerformed(@NotNull final AnActionEvent e)
    {
        final PsiFile psiFile = PsiFileUtil.getPsiFileInCurrentEditor(e.getProject());

        if(this.deleteMode)
        {
            CommandProcessor.getInstance().executeCommand(psiFile.getProject(), new Runnable() {
                @Override
                public void run() {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            removeUnusedImports(psiFile);
                        }
                    });
                }
            },
            "Remove Unused Imports",
            "Remove Unused Imports");

        }
    }
}
