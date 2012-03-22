package org.jenkinsci.plugins.githubsharedrepository;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class GithubSharedProjectProperty extends JobProperty<AbstractProject<?,?>> {
	
	private String projectUrl;
	
	@DataBoundConstructor
	public GithubSharedProjectProperty(String projectUrl) {
        this.projectUrl = new GithubUrl(projectUrl).baseUrl();
	}

	@Override
	public Collection<? extends Action> getJobActions(AbstractProject<?, ?> job) {
		Collection<Action> actions = new ArrayList<Action>();
		actions.add(new GithubSharedLinkAction(this));
		return actions ;
	}

    /**
     * @return the projectUrl
     */
    public GithubUrl getProjectUrl() {
        return new GithubUrl(projectUrl);
    }

	
    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {

        public DescriptorImpl() {
            super(GithubSharedProjectProperty.class);
            load();
        }

        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        public String getDisplayName() {
            return "GitHub project page";
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req,
                JSONObject formData) throws FormException {
        	GithubSharedProjectProperty tpp = req.bindJSON(
        			GithubSharedProjectProperty.class, formData);
            if (tpp.projectUrl == null) {
                tpp = null; // not configured
            }
            return tpp;
        }

    }
	
}
