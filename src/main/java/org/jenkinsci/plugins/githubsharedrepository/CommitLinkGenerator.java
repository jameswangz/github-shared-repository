package org.jenkinsci.plugins.githubsharedrepository;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.google.common.base.Throwables;

public class CommitLinkGenerator extends Builder {

	private String commitIdParamName;

	@DataBoundConstructor
	public CommitLinkGenerator(String commitIdParamName) {
		this.commitIdParamName = commitIdParamName;
	}

	
	@Override
	public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
		GithubSharedProjectProperty jobProperty = build.getParent().getProperty(GithubSharedProjectProperty.class);
		if (jobProperty != null) {
			try {
				EnvVars environment = build.getEnvironment(listener);
				String commitId = environment.get(commitIdParamName);
				build.addAction(new CommitLinkAction(jobProperty.getProjectUrl(), commitId));						
				build.addAction(new ChangesSinceLastBuildAction(environment.get("JOB_NAME"), jobProperty.getProjectUrl(), commitId));						
			} catch (Exception e) {
				throw Throwables.propagate(e);
			}
		}
		return true;
	}


	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) 
		throws InterruptedException, IOException {
		
		return true;
	}

	
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
	
    public String getCommitIdParamName() {
		return commitIdParamName;
	}

	@Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {
    	
		public String getDisplayName() {
			return "Generate Github Commit Link";
		}

        @Override
        public String getHelpFile() {
            return null;
        }

		@Override
		public CommitLinkGenerator newInstance(StaplerRequest req, JSONObject formData) 
			throws hudson.model.Descriptor.FormException {
			
			CommitLinkGenerator property = req.bindJSON(CommitLinkGenerator.class, formData);
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
