package com.netease.NEJ.core.amd.define.organizer;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;

public class SortedPsiElementAdapter
{
    private PsiElement element;
    private boolean inactive;
    private PsiComment ignoreComment;
    private PsiComment regularComment;

    public SortedPsiElementAdapter(PsiElement element, boolean inactive, PsiComment ignoreComment, PsiComment regularComment) {
        this.element = element;
        this.inactive = inactive;
        this.ignoreComment = ignoreComment;
        this.regularComment = regularComment;
    }

    public static SortedPsiElementAdapter fromParameter(SortItem item)
    {
        SortedPsiElementAdapter adapter = new SortedPsiElementAdapter(item.getParameter(), item.isInactive(), null, null);
        return adapter;
    }

    public static SortedPsiElementAdapter fromDefine(SortItem item)
    {
        SortedPsiElementAdapter adapter = new SortedPsiElementAdapter(item.getDefine(), item.isInactive(), item.getIgnoreComment(), item.getRegularComment());
        return adapter;
    }

    public PsiComment getRegularComment() {
        return regularComment;
    }

    public PsiComment getIgnoreComment() {
        return ignoreComment;
    }

    public PsiElement getElement() {
        return element;
    }

    public void setElement(PsiElement element) {
        this.element = element;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }
}
