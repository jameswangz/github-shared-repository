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

import org.kohsuke.stapler.StaplerRequest;

@SuppressWarnings("unchecked")
public class HyperlinkGenerator extends Recorder {

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) 
		throws InterruptedException, IOException {
		
		//TODO pass parameters
		build.addAction(new GenerateHyperlinkAction());
		return true;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    	
		public String getDisplayName() {
			return "Github Hyperlink Generator";
		}

        @Override
        public String getHelpFile() {
            return null;
        }

		@Override
		public Publisher newInstance(StaplerRequest req, JSONObject formData) 
			throws hudson.model.Descriptor.FormException {
			
			return new HyperlinkGenerator();
		}

		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}
    }
	
	
}
