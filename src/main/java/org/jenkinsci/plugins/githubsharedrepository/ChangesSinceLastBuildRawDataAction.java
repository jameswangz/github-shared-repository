package org.jenkinsci.plugins.githubsharedrepository;

import hudson.util.FlushProofOutputStream;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;


public class ChangesSinceLastBuildRawDataAction extends AbstractChangesSinceLastBuildAction {

	public ChangesSinceLastBuildRawDataAction(String job, GithubUrl githubUrl, String buildId) {
		super(job, githubUrl, buildId);
	}

	public String getDisplayName() {
		return "Changes Since Last Build (Raw Data)";
	}

	public String getUrlName() {
		return "changesSinceLastBuildRawData";
	}
	
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
        rsp.setContentType("text/plain;charset=UTF-8");
        // Prevent jelly from flushing stream so Content-Length header can be added afterwards
        FlushProofOutputStream out = new FlushProofOutputStream(rsp.getCompressedOutputStream(req));
        out.write(getChangesSinceLastBuild().asText().getBytes());
        out.close();
    }
	


	

}
