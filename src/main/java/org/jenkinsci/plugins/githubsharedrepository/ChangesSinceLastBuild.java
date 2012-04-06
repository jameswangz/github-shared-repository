package org.jenkinsci.plugins.githubsharedrepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ChangesSinceLastBuild {

	private final URL url;
	private final GithubUrl githubUrl;
	private final String buildId;
	
	private boolean workingFileNotFound;
	private boolean buildNotFound;
	private List<Change> changes = new ArrayList<Change>();

	public ChangesSinceLastBuild(URL url, GithubUrl githubUrl, String buildId) {
		this.url = url;
		this.githubUrl = githubUrl;
		this.buildId = buildId;
		process();
	}

	@SuppressWarnings("unchecked")
	private void process() {
		InputStream inputStream = null;
		try {
			if (url == null) {
				workingFileNotFound = true;
				return;
			}
			inputStream = url.openStream();
			if (inputStream == null) {
				workingFileNotFound = true;
				return;
			}
			Yaml yaml = new Yaml();
			Map<String, List<Map<String, Object>>> loaded = (Map<String, List<Map<String, Object>>>) yaml.load(inputStream);
			List<Map<String, Object>> recentChanges = loaded.get("recent_builds");
			if (recentChanges == null) {
				recentChanges = new ArrayList<Map<String,Object>>();
			}
			try {
				Map<String, Object> found = Iterables.find(recentChanges, new Predicate<Map<String, Object>>() {
					public boolean apply(Map<String, Object> input) {
						return input.get("build_id").equals(buildId);
					}
				});		
				List<Map<String, String>> changeList = (List<Map<String, String>>) found.get("changes_since_last_build");
				changes = Lists.transform(changeList, new Function<Map<String, String>, Change>() {
					public Change apply(Map<String, String> input) {
						return new Change(githubUrl, input);
					}
				});
			} catch (NoSuchElementException e) {
				buildNotFound = true;
				return;
			}			
			inputStream.close();
		} catch (FileNotFoundException e) {
			this.workingFileNotFound = true;
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean workingFileNotFound() {
		return workingFileNotFound;
	}

	public boolean buildNotFound() {
		return buildNotFound;
	}

	public boolean hasContent() {
		return !workingFileNotFound && !buildNotFound;
	}

	public List<Change> getChanges() {
		return changes ;
	}

	public URL getUrl() {
		return url;
	}

	public String getBuildId() {
		return buildId;
	}

	public String asText() {
		if (changes.isEmpty()) {
			return "";
		}
		List<Map<String, String>> maps = Lists.transform(changes, new Function<Change, Map<String, String>>() {
			public Map<String, String> apply(Change input) {
				return input.innerMap();
			}
		});
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(FlowStyle.BLOCK);
		options.setDefaultScalarStyle(ScalarStyle.PLAIN);
		return new Yaml(options).dumpAll(maps.iterator());
	}


}
