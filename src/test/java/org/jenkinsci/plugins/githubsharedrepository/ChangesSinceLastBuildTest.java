package org.jenkinsci.plugins.githubsharedrepository;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;


public class ChangesSinceLastBuildTest extends Assert {
	
	private String projectUrl = "https://github.com/jameswangz/github-shared-repository";

	@Test
	public void workingFileNotFoundFromClassPath() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("none-exists.yml");
		ChangesSinceLastBuild build = new ChangesSinceLastBuild(url, null, null);
		assertTrue(build.workingFileNotFound());
	}
	
	@Test
	public void workingFileNotFoundFromFile() throws MalformedURLException {
		URL url = new File("/none-exists.yml").toURI().toURL();
		ChangesSinceLastBuild build = new ChangesSinceLastBuild(url, null, null);
		assertTrue(build.workingFileNotFound());
	}

	@Test
	public void buildNotFound() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("api.yml");
		ChangesSinceLastBuild build = new ChangesSinceLastBuild(url, null, "INVALID_BUILD_ID");
		assertTrue(build.buildNotFound());
	}
	
	@Test
	public void success() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("api.yml");
		GithubUrl githubUrl = new GithubUrl(projectUrl);
		ChangesSinceLastBuild build = new ChangesSinceLastBuild(url, githubUrl, "548f465f1c4748741736a05caa5f7cb818d2e4b0");
		assertTrue(build.hasContent());
		assertEquals(2, build.getChanges().size());
		Iterator<Change> iterator = build.getChanges().iterator();
		Change firstChange = iterator.next();
		assertEquals("548f465f1c4748741736a05caa5f7cb818d2e4b0", firstChange.getCommitId());
		assertEquals(projectUrl + "/commit/548f465f1c4748741736a05caa5f7cb818d2e4b0", firstChange.getCommitLink());
		assertEquals("James Wang <jameswangz81@gmail.com>", firstChange.getAuthor());
		assertEquals("Wed Mar 28 16:42:05 2012 +0800", firstChange.getDate());
		assertEquals("test 27", firstChange.getMessage());
		Change secondChange = iterator.next();
		assertEquals("aecd916e27e490ed7d8a9a535adabb2849d9417b", secondChange.getCommitId());
		assertEquals(projectUrl + "/commit/aecd916e27e490ed7d8a9a535adabb2849d9417b", secondChange.getCommitLink());
		assertEquals("James Wang <jameswangz81@gmail.com>", secondChange.getAuthor());
		assertEquals("Wed Mar 28 16:41:59 2012 +0800", secondChange.getDate());
		assertEquals("test 26", secondChange.getMessage());
	}
	
}
