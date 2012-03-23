package org.jenkinsci.plugins.githubsharedrepository;

import hudson.model.Action;

public class CommitLinkGenerator implements Action {

	private final String projectUrl;
	private final String commitId;

	public CommitLinkGenerator(String projectUrl, String commitId) {
		this.projectUrl = projectUrl;
		this.commitId = commitId;
	}

	public String getIconFileName() {
        return "/plugin/" + Constants.PLUGIN_ID + "/git-48x48.png";
	}

	public String getDisplayName() {
		return "Commit link";
	}

	public String getUrlName() {
		return String.format("%s%s%s", projectUrl, "commit/", commitId);
	}

}
