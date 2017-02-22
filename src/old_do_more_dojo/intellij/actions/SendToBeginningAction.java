package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.amd.AMDImport;
import old_do_more_dojo.core.amd.define.DefineStatement;
import old_do_more_dojo.core.amd.importing.ImportCreator;
import old_do_more_dojo.core.amd.psi.AMDPsiUtil;
import com.intellij.lang.javascript.psi.JSElement;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;

import java.util.List;

public class SendToBeginningAction extends SendToAction
{
    @Override
    protected String getName()
    {
        return "Send AMD Import to Beginning";
    }

    @Override
    protected void moveAction(AnActionEvent e, PsiElement define, PsiElement parameter, List<PsiElement> defines, List<PsiElement> parameters, DefineStatement defineStatement)
    {
        String parameterText = parameter.getText();

        AMDPsiUtil.removeSingleImport(new AMDImport((JSElement) define, (JSElement) parameter));

        // define is already quoted, so passing an empty quote character
        new ImportCreator().createImport(define.getText(), "", parameterText, defineStatement.getArguments(), defineStatement.getFunction().getParameterList());

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        int index = defineStatement.getArguments().getFirstChild().getTextOffset();
        editor.getScrollingModel().scrollVertically(index);
    }
}
