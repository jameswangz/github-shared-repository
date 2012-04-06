package org.jenkinsci.plugins.githubsharedrepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import hudson.model.Action;

public abstract class AbstractChangesSinceLastBuildAction implements Action, Constants {

	protected final GithubUrl githubUrl;
	protected final String buildId;
	protected final String job;
	
	public AbstractChangesSinceLastBuildAction(String job, GithubUrl githubUrl, String buildId) {
		this.job = job;
		this.githubUrl = githubUrl;
		this.buildId = buildId;
	}
	
	public String getIconFileName() {
	    return "/plugin/" + PLUGIN_ID + "/github-logo.png";
	}

	public ChangesSinceLastBuild getChangesSinceLastBuild() {		
		try {
			URL url = new File(String.format("%s/%s/%s.yml", System.getProperty("user.home"), WORKING_DIR, job)).toURI().toURL();
			return new ChangesSinceLastBuild(url, githubUrl, buildId);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}



}
