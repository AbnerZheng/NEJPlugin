package com.netease.NEJ.intellij.actions;

import com.netease.NEJ.core.amd.psi.AMDPsiUtil;

public class MoveImportDownAction extends ReorderAMDImportAction
{
    public MoveImportDownAction()
    {
        super(AMDPsiUtil.Direction.DOWN);
    }
}
