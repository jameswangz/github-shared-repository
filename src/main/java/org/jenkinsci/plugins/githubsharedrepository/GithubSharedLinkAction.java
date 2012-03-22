package org.jenkinsci.plugins.githubsharedrepository;

import hudson.model.Action;

/**
 * Add the Github Logo/Icon to the sidebar.
 * 
 * @author Stefan Saasen <stefan@coravy.com>
 */
public final class GithubSharedLinkAction implements Action {

    private final transient GithubSharedProjectProperty projectProperty;

    public GithubSharedLinkAction(GithubSharedProjectProperty githubProjectProperty) {
        this.projectProperty = githubProjectProperty;
    }

    /*
     * (non-Javadoc)
     * @see hudson.model.Action#getDisplayName()
     */
    public String getDisplayName() {
        return "GitHub (Shared Repository)";
    }

    /*
     * (non-Javadoc)
     * @see hudson.model.Action#getIconFileName()
     */
    public String getIconFileName() {
    	//TODO get current plugin path from Jenkins API
        return "/plugin/github-shared-repository/github-logo.png";
    }

    /*
     * (non-Javadoc)
     * @see hudson.model.Action#getUrlName()
     */
    public String getUrlName() {
        return projectProperty.getProjectUrl().baseUrl();
    }

}
