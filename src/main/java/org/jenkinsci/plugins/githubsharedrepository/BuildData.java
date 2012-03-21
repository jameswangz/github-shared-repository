package org.jenkinsci.plugins.githubsharedrepository;

import org.kohsuke.stapler.export.ExportedBean;

import hudson.Extension;
import hudson.Functions;
import hudson.model.Action;

@Extension
@ExportedBean
public class BuildData implements Action {

	public String getIconFileName() {
        return Functions.getResourcePath()+"/plugin/git-shared-repository/icons/git-32x32.png";
	}

	public String getDisplayName() {
		return "Git build data";
	}

	public String getUrlName() {
		return "Git shared repository";
	}

}
