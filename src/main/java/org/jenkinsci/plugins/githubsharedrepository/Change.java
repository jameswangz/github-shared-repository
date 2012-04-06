package org.jenkinsci.plugins.githubsharedrepository;

import java.util.Map;

public class Change {

	private final GithubUrl githubUrl;
	private final Map<String, String> map;

	public Change(GithubUrl githubUrl, Map<String, String> map) {
		this.githubUrl = githubUrl;
		this.map = map;
	}

	public String getCommitId() {
		return map.get("commit_id");
	}

	public String getCommitLink() {
		return githubUrl.commitId(getCommitId());
	}

	public String getAuthor() {
		return map.get("author");
	}

	public String getDate() {
		return map.get("date");
	}

	public String getMessage() {
		return map.get("message");
	}

	public Map<String, String> innerMap() {
		return map;
	}



	
	
}
