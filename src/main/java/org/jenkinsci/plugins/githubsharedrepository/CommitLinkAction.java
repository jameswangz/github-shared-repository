package org.jenkinsci.plugins.githubsharedrepository;

import hudson.model.Action;

public class CommitLinkAction implements Action {

	private final GithubUrl githubUrl;
	private final String commitId;

	public CommitLinkAction(GithubUrl githubUrl, String commitId) {
		this.githubUrl = githubUrl;
		this.commitId = commitId;
	}

	public String getIconFileName() {
        return "/plugin/" + Constants.PLUGIN_ID + "/git-48x48.png";
	}

	public String getDisplayName() {
		return "Commit Link";
	}
	
	public String getLongDisplayName() {
		return String.format("%s%s", "commit: ", commitId);
	}

	public String getUrlName() {
		return githubUrl.commitId(commitId);
	}

}
