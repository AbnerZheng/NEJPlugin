package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.amd.psi.AMDPsiUtil;

public class MoveImportDownAction extends ReorderAMDImportAction
{
    public MoveImportDownAction()
    {
        super(AMDPsiUtil.Direction.DOWN);
    }
}
