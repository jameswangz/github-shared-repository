package org.jenkinsci.plugins.githubsharedrepository;

public class Change {

	private final GithubUrl githubUrl;
	private final String commitId;
	private final String author;
	private final String date;
	private final String message;

	public Change(GithubUrl githubUrl, String commitId, String author, String date, String message) {
		this.githubUrl = githubUrl;
		this.commitId = commitId;
		this.author = author;
		this.date = date;
		this.message = message;
	}

	public String getCommitId() {
		return commitId;
	}

	public String getCommitLink() {
		return githubUrl.commitId(commitId);
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}
	
	
}
