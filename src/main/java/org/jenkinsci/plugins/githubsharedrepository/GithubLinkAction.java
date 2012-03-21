package org.jenkinsci.plugins.githubsharedrepository;

import hudson.model.Action;

/**
 * Add the Github Logo/Icon to the sidebar.
 * 
 * @author Stefan Saasen <stefan@coravy.com>
 */
public final class GithubLinkAction implements Action {

    private final transient GithubSharedProjectProperty projectProperty;

    public GithubLinkAction(GithubSharedProjectProperty githubProjectProperty) {
        this.projectProperty = githubProjectProperty;
    }

    /*
     * (non-Javadoc)
     * @see hudson.model.Action#getDisplayName()
     */
    public String getDisplayName() {
        return "GitHub";
    }

    /*
     * (non-Javadoc)
     * @see hudson.model.Action#getIconFileName()
     */
    public String getIconFileName() {
        return "/plugin/github/logov3.png";
    }

    /*
     * (non-Javadoc)
     * @see hudson.model.Action#getUrlName()
     */
    public String getUrlName() {
        return projectProperty.getProjectUrl();
    }

}
