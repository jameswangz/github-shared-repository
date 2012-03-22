package org.jenkinsci.plugins.githubsharedrepository;

import hudson.model.Action;

public class GenerateHyperlinkAction implements Action {

	public String getIconFileName() {
        return "/plugin/" + Constants.PLUGIN_ID + "/git-48x48.png";
	}

	public String getDisplayName() {
		return "Commit link";
	}

	public String getUrlName() {
		//TODO dynamic
		return "https://github.com/jameswangz/github-shared-repository/commit/4b7a5b2ba595e265312c62db2292105c63d7f483";
	}

}
