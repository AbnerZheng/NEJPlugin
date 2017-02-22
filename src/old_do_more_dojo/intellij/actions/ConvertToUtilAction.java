package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.refactoring.ClassToUtilConverter;
import old_do_more_dojo.core.util.PsiFileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiFile;

public class ConvertToUtilAction extends JavaScriptAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final PsiFile psiFile = PsiFileUtil.getPsiFileInCurrentEditor(e.getProject());

        new ClassToUtilConverter().convertToUtilPattern(psiFile);
    }
}
