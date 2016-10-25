package com.netease.NEJ.intellij.actions;

import com.netease.NEJ.core.amd.psi.AMDPsiUtil;

public class MoveImportUpAction extends ReorderAMDImportAction
{
    public MoveImportUpAction()
    {
        super(AMDPsiUtil.Direction.UP);
    }
}
