package org.jenkinsci.plugins.githubsharedrepository;



public class ChangesSinceLastBuildAction extends AbstractChangesSinceLastBuildAction {

	public ChangesSinceLastBuildAction(String job, GithubUrl githubUrl, String buildId) {
		super(job, githubUrl, buildId);
	}

	public String getDisplayName() {
		return "Changes Since Last Build";
	}

	public String getUrlName() {
		return "changesSinceLastBuild";
	}
	

}
