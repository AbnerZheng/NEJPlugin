package old_do_more_dojo.core.amd.filesystem;

import old_do_more_dojo.core.amd.objectmodel.TemplatedWidgetUtil;
import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.lang.Language;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

public class AttachPointResolver
{
    @Nullable
    public static PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int i, @Nullable Editor editor)
    {
        if(psiElement == null || !psiElement.getLanguage().equals(Language.findLanguageByID("JavaScript")))
        {
            return new PsiElement[0];
        }

        NEJSettings settings = ServiceManager.getService(psiElement.getProject(), NEJSettings.class);
        if(!settings.isNeedsMoreDojoEnabled())
        {
            return new PsiElement[0];
        }

        PsiFile templateFile = new TemplatedWidgetUtil(psiElement.getContainingFile()).findTemplatePath();

        if(templateFile == null)
        {
            return new PsiElement[0];
        }

        PsiElement attachPoint = TemplatedWidgetUtil.getAttachPointElementInHtmlFile(psiElement, templateFile);
        if(attachPoint == null)
        {
            return new PsiElement[0];
        }

        return new PsiElement[] { attachPoint };
    }

}
