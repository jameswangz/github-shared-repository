package org.jenkinsci.plugins.githubsharedrepository;

import hudson.model.Action;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ChangesSinceLastBuildAction implements Action, Constants {

	private final GithubUrl githubUrl;
	private final String buildId;
	private final String job;

	public ChangesSinceLastBuildAction(String job, GithubUrl githubUrl, String buildId) {
		this.job = job;
		this.githubUrl = githubUrl;
		this.buildId = buildId;
	}

	public String getIconFileName() {
        return "/plugin/" + Constants.PLUGIN_ID + "/github-logo.png";
	}

	public String getDisplayName() {
		return "Changes Since Last Build";
	}

	public String getUrlName() {
		return "changesSinceLastBuild";
	}
	
	public ChangesSinceLastBuild getChangesSinceLastBuild() {		
		try {
			URL url = new File(String.format("%s/%s/%s.yml", System.getProperty("user.home"), WORKING_DIR, job)).toURI().toURL();
			return new ChangesSinceLastBuild(url.openStream(), githubUrl, buildId);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
