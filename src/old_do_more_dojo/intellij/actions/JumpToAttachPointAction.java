package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.amd.objectmodel.AMDValidator;
import old_do_more_dojo.core.amd.objectmodel.TemplatedWidgetUtil;
import old_do_more_dojo.core.settings.NEJSettings;
import old_do_more_dojo.core.util.HighlightingUtil;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;

public class JumpToAttachPointAction extends JavaScriptAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile file = e.getData(LangDataKeys.PSI_FILE);

        if(editor == null || file == null)
        {
            return;
        }

        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());

        if(element == null)
        {
            return;
        }

        PsiFile templateFile = new TemplatedWidgetUtil(file).findTemplatePath();

        if(templateFile == null)
        {
            Notifications.Bus.notify(new Notification("needsmoredojo", "Jump To Attach Point", "No attach point found for " + element.getText(), NotificationType.INFORMATION));
            return;
        }

        jumpToElementInTemplate(templateFile, element);
    }

    private void jumpToElementInTemplate(PsiFile templateFile, PsiElement sourceElement)
    {
        if(!AMDValidator.elementIsAttachPoint(sourceElement))
        {
            Notifications.Bus.notify(new Notification("needsmoredojo", "Jump To Attach Point", "Element is not an attach point or is in an invalid statement with an attach point: '" + sourceElement.getText() + "'", NotificationType.INFORMATION));
            return;
        }

        FileEditorManager.getInstance(templateFile.getProject()).openFile(templateFile.getVirtualFile(), true, true);
        Editor editor = EditorFactory.getInstance().getEditors(PsiDocumentManager.getInstance(templateFile.getProject()).getDocument(templateFile))[0];

        PsiElement templateElement = TemplatedWidgetUtil.getAttachPointElementInHtmlFile(sourceElement, templateFile);

        if(templateElement == null)
        {
            Notifications.Bus.notify(new Notification("needsmoredojo", "Jump To Attach Point", "Attach point not found in " + templateFile.getVirtualFile().getName() + ": '" + sourceElement.getText() + "'", NotificationType.INFORMATION));
        }

        int index = templateElement.getTextOffset();
        editor.getCaretModel().moveToOffset(index);
        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);

        List<PsiElement> elementsToHighlight = new ArrayList<PsiElement>();
        elementsToHighlight.add(templateElement);
        if(templateElement.getNextSibling() != null)
        {
            elementsToHighlight.add(templateElement.getNextSibling());
            if(templateElement.getNextSibling().getNextSibling() != null)
            {
                elementsToHighlight.add(templateElement.getNextSibling().getNextSibling());
            }
        }

        HighlightingUtil.highlightElement(editor, templateFile.getProject(), elementsToHighlight.toArray(new PsiElement[0]));
    }

    @Override
    public void update(AnActionEvent e)
    {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if(editor == null)
        {
            e.getPresentation().setEnabled(false);
            return;
        }

        PsiFile file = e.getData(LangDataKeys.PSI_FILE);

        if(file == null)
        {
            e.getPresentation().setEnabled(false);
            return;
        }

        if(!ServiceManager.getService(file.getProject(), NEJSettings.class).isNeedsMoreDojoEnabled())
        {
            e.getPresentation().setEnabled(false);
            return;
        }
        else
        {
            e.getPresentation().setEnabled(true);
        }

        e.getPresentation().setEnabled(true);

        if(!(file.getFileType() instanceof JavaScriptFileType))
        {
            e.getPresentation().setEnabled(false);
            return;
        }

        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        if(!AMDValidator.elementIsAttachPoint(element))
        {
            e.getPresentation().setEnabled(false);
        }
    }
}
