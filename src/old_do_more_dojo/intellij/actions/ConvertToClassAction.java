package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.refactoring.UtilToClassConverter;
import old_do_more_dojo.core.util.PsiFileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiFile;

public class ConvertToClassAction extends JavaScriptAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final PsiFile psiFile = PsiFileUtil.getPsiFileInCurrentEditor(e.getProject());

        new UtilToClassConverter().convertToClassPattern(psiFile);
    }
}
