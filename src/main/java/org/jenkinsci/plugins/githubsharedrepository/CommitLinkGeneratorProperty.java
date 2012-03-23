package org.jenkinsci.plugins.githubsharedrepository;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

@SuppressWarnings("unchecked")
public class CommitLinkGeneratorProperty extends Recorder {

	private String commitIdParamName;

	@DataBoundConstructor
	public CommitLinkGeneratorProperty(String commitIdParamName) {
		this.commitIdParamName = commitIdParamName;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) 
		throws InterruptedException, IOException {
		
		//FIXME pass from {@link GithubSharedProjectProperty}
		String projectUrl = "https://github.com/jameswangz/github-shared-repository/";
		String commitId = build.getEnvironment(listener).get(commitIdParamName);
		build.addAction(new CommitLinkGenerator(projectUrl, commitId));
		return true;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
	
    public String getCommitIdParamName() {
		return commitIdParamName;
	}

	@Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    	
		public String getDisplayName() {
			return "Github Commit Link Generator";
		}

        @Override
        public String getHelpFile() {
            return null;
        }

		@Override
		public Publisher newInstance(StaplerRequest req, JSONObject formData) 
			throws hudson.model.Descriptor.FormException {
			
			CommitLinkGeneratorProperty property = req.bindJSON(CommitLinkGeneratorProperty.class, formData);
            if (property.commitIdParamName == null) {
                property = null; // not configured
            }
            return property;
		}

		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}
    }
	
	
}
