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


    public String getDisplayName() {
        return "GitHub (Shared Repository)";
    }


    public String getIconFileName() {
        return "/plugin/" + Constants.PLUGIN_ID + "/github-logo.png";
    }


    public String getUrlName() {
        return projectProperty.getProjectUrl().baseUrl();
    }
    

}
