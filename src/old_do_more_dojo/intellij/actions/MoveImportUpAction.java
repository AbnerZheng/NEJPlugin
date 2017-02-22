package old_do_more_dojo.intellij.actions;

import old_do_more_dojo.core.amd.psi.AMDPsiUtil;

public class MoveImportUpAction extends ReorderAMDImportAction
{
    public MoveImportUpAction()
    {
        super(AMDPsiUtil.Direction.UP);
    }
}
