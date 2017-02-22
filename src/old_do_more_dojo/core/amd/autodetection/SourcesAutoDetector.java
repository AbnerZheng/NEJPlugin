package old_do_more_dojo.core.amd.autodetection;

import old_do_more_dojo.core.amd.filesystem.DojoModuleFileResolver;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.*;

public class SourcesAutoDetector
{
    public List<String> getPossibleSourceRoots(Project project)
    {
        DojoModuleFileResolver fileResolver = new DojoModuleFileResolver();

        Set<String> possibleDirectories = new HashSet<String>();

        /**
         * here's how we guess where the project root is:
         * open index.html to search for amd module references
         * open javascript files and look for dojo modules inside
         */

        PsiFile[] files = FilenameIndex.getFilesByName(project, "index.html", GlobalSearchScope.projectScope(project));
        List<PsiFile> potentialFiles = new ArrayList<PsiFile>();

        for(PsiFile file : files)
        {
            String directory = file.getContainingDirectory().getVirtualFile().getCanonicalPath();
            if(!fileResolver.isInDojoSources(directory))
            {
                potentialFiles.add(file);
            }
        }

        Set<String> possibleSourceModules = new HashSet<String>();
        for(PsiFile file : potentialFiles)
        {
            possibleSourceModules.addAll(fileResolver.getDojoModulesInHtmlFile(file));
        }
        possibleDirectories.addAll(fileResolver.getDirectoriesForDojoModules(project, possibleSourceModules));
        possibleDirectories.addAll(fileResolver.getDirectoriesForDojoModules(project, fileResolver.getDojoModulesInJavaScriptFiles(project)));

        List<String> choices = new ArrayList<String>(possibleDirectories);

        Collections.sort(choices, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });

        return choices;
    }
}
