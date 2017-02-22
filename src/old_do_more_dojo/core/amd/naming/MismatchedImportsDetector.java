package old_do_more_dojo.core.amd.naming;

import old_do_more_dojo.core.amd.importing.ImportReorderer;
import old_do_more_dojo.core.amd.objectmodel.AMDValidator;
import old_do_more_dojo.core.settings.NEJSettings;
import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;

public class MismatchedImportsDetector
{
    public class Mismatch
    {
        private int index;
        private PsiElement define;
        private PsiElement parameter;
        private String absolutePath;

        public Mismatch(PsiElement define, PsiElement parameter, int index, String absolutePath)
        {
            this.absolutePath = absolutePath;
            this.define = define;
            this.parameter = parameter;
            this.index = index;
        }

        public String getAbsolutePath() {
            return absolutePath;
        }

        public int getIndex() {
            return index;
        }

        public PsiElement getDefine() {
            return define;
        }

        public PsiElement getParameter() {
            return parameter;
        }
    }

    public List<Mismatch> matchOnList(PsiElement[] defines, PsiElement[] parameters, List<NameException> exceptions, NEJSettings NEJSettings, MismatchedImportsDetectorCache cache)
    {
        List<Mismatch> results = new ArrayList<Mismatch>();

        if(defines.length < parameters.length)
        {
            // special case where there are missing defines most likely
            for(int i=defines.length;i<parameters.length;i++)
            {
                results.add(new Mismatch(null, parameters[i], i, null));
            }
        }

        // we always go through parameters instead of defines, because parameter list will not include plugins
        // and other stuff
        for(int i=0;i<parameters.length;i++)
        {
            if(i >= defines.length)
            {
                continue; // we've already accounted for the mismatch here
            }

            String defineTest = defines[i].getText();
            String parameterTest = parameters[i].getText();
            String absolutePath = null;

            ImportReorderer reorderer = new ImportReorderer();
            String absoluteModulePath = cache.getAbsolutePath(defines[i].getContainingFile(), defines[i].getText());
            if(absoluteModulePath == null)
            {
                absoluteModulePath = reorderer.getPathSyntax(defines[i].getProject(), defines[i].getText(), defines[i].getContainingFile(), false);
                cache.put(defines[i].getContainingFile(), defines[i].getText(), absoluteModulePath);
            }

            if(absoluteModulePath != null)
            {
                defineTest = absoluteModulePath;
                absolutePath = absoluteModulePath;
            }

            if(!new AMDValidator().defineMatchesParameter(defineTest, parameterTest, exceptions))
            {
                results.add(new Mismatch(defines[i], parameters[i], i, absolutePath));
            }
        }

        return results;
    }
}
