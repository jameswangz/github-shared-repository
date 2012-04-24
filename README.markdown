# Goals
[Jenkins Github Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Github+Plugin) already has nice features to 
let Jenkins works well with Github, so why do we need this [Github shared repository plugin](https://github.com/jameswangz/github-shared-repository)? 
Well, this is because we have some special scenarios that github plugin couldn't handle, actually I've asked this
question on [Stack Overflow](http://stackoverflow.com/questions/9374028/how-to-trigger-a-individual-job-in-jenkins-with-github-repository). 

As everyone knows, for the algorithm consideration, from the enduser's perspective, git repository 
looks like a 'blob' object, that means it doesn't like subversion(or some other SCM tools) 
that we can check out or update just partial files from the repository, this is also the reason why
git is so fast, it's nice but the multi-modules structure is very common in many projects, 
in this case we usually create separate Jenkins jobs for different modules, and we expect the code changes 
in some modules will only trigger corresponding jobs rather than trigger all of them(as the project size grows,
the building process is slower and slower), we have also considered using [Git submodules](http://help.github.com/submodules/) 
or [Git subtrees](https://github.com/apenwarr/git-subtree) to achieve this goal, but we feel they are too
complicated for the beginners and will introduce additional complexities especially git itself has a 
high learning curve, so what we want to do is just leave the whole project as a single repository and 
find a way to configure separate Jenkins jobs for different modules and trigger only the affected jobs accordingly,
and this is what's this plugin done.   

# Features 
This plugin has 3 features

* The trigger scripts(written with Ruby) will trigger the affected jobs remotely (by using the 'git log --quiet HEAD^..HEAD module' command) 
* The plugin will generate the project and commit hyperlinks to Github(almost same with the original Github plugin)
* The plugin will generate the list of changes since last build 

The trigger scripts could also be part of the plugin but currently it works well, if I'm
in scheduler I may move the feature to the plugin to decrease the installation complexity. 

The idea is alrough we have multiple jobs we'll only have one shared git repository(and this is where this Plugin's name comes from), 
this is helpful because if we have a full repository copy for each job the disk usage will be quite large, I'll
explain how to create a shared git repository in the Configuration part.  

# Installation Guide
## Install the trigger scripts
The trigger scripts are written with ruby, most of the Linux releases have provided the builtin ruby environment, 
if you are deploying your Jenkins server on windows or some Linux releases not included ruby please install them 
from [the Ruby website](http://www.ruby-lang.org/en/downloads/).

To install the trigger script you just copy [the 2 ruby files](https://github.com/jameswangz/github-shared-repository/tree/master/trigger_scripts) 
to your project root folder, we need to do some configurations later but just leave them there at the moment.

## Install the Plugin
[Download the latest version hpi (currently 1.0.2-SNAPSHOT)](https://github.com/jameswangz/github-shared-repository/tree/master/downloads)
and install it on Jenkins UI(Manage Jenkins -> Manage Plugins -> Advanced -> Upload Plugin, I may put the artifacts 
in Jenkins Plugin Repository but currently please install it manully), restart Jenkins to enable the plugin.

## Install the Parameterized Trigger Plugin
Since we are using the remote api to trigger the Jenkins jobs, we will pass the git commit id by using the Jenkins 
job parameters to generate the commit hyperlink, however, the parameters can't be propogated to the downstream jobs 
by default, so we need install the [Parameterized Trigger Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Parameterized+Trigger+Plugin) 
to achieve it, to install this Plugin you use the online Plugin installation feature. 

# Configuration
## Configure the trigger scripts
There are 2 ruby files but we only need to modify the jenkins_trigger.rb, this script inclues all the configurations
to make the trigger feature works, the git_jenkins_remote_trigger.rb will do all the things for you.

Let's look at the sample jenkins_trigger.rb

	#!/usr/bin/ruby
	
	require './git_jenkins_remote_trigger'
	
	if __FILE__ == $0
		jenkins = 'http://localhost:8080/jenkins'
		module_job_mappings = { 'api' => 'api', 'impl/src' => 'impl-src' }
		running_options = { :only_once => false, :interval => 5 }
		auth_options = { :required => true, :username => 'james', :api_token => 'dcebe4f09bdc324d2d9567780f04a0c1' }
		other_options = { :COMMIT_ID_PARAM_NAME => 'BUILD_ID', :MAX_TRACKED_BUILDS => 10 }
		GitJenkinsRemoteTrigger.new(jenkins, module_job_mappings, running_options, auth_options, other_options).run
	end

Some of them are obviously,  I'll explain them one by one  

* jenkins : your jenkins server address
* module_job_mappings : the mappings between your modules and Jenkins job names, multiple levels folders are allowed
  like 'impl/src', left side is the module name and the right side is Jenkins job name.
* running_options : here you can specify the running behaviour of the script, if you want to let Jenkins manage the 
  running scheduler(create a master job and run the script periodically) just specify the :only_once value to true
  and you don't need the :interval value, if you want to run the ruby files in a separate process(nohup ./jenkins_trigger.rb &)
  you specify the :only_once to false and specify a reasonable value for the :interval value, here I specify it as 5 seconds.
* auth_options : if your Jenkins server doesn't need authentication just specify the :required value to false, on the contrary,
  specify the :required value to true and provide the :username and :api_token values(the api token can be got from user profile page).
* other_options[:COMMIT_ID_PARAM_NAME] : here your can specify the git commit id parameter name, this parameter is very important, it will be 
  used in the Jenkins job configuration as we will explain later.   
* other_options[:MAX_TRACKED_BUILDS] : the maximum number of tracked builds, 10 is a resonable number, for more information please
  refer to [the More About Changes Since Last Build part](#changes_since_last_build).  

At this point you have configured the trigger script successfully, save it and get ready to configure the plugin in Jenkins.

## Configure the Plugin
Suppose we have a multi-modules project which has 3 modules : api, impl and web, the impl and web modules depends on api module,
and we have created 3 jobs on Jenkins named 'api', 'impl' and 'web', we expect the 'impl' and 'web' jobs are 
the downstream jobs of 'api', we need to configure the following options for the 'api' job

* Github Project (Shared Repository) : the URL for the GitHub hosted project, please note you are not configuring the 
  Github Project property if you have the original Github Plugin installed.
* Job Parameters : check 'This build is parameterized' and add a String Parameter, the parameter name must be same with
  the value of the :COMMIT_ID_PARAM_NAME we configured in the trigger script, optionally you can specify a default value
  for this parameter like 'Manually', this parameter will be exported to environment variable by Jenkins thus you can 
  use it in the build process for some versioning purpose.  
* Generate Github Commit Link And Changes Since Last Build : add a build step and choose 'Generate Github Commit Link 
  And Changes Since Last Build', set the Git Commit Id Parameter Name to the same value of the :COMMIT_ID_PARAM_NAME. 
* Trigger Parameterized : check 'Trigger parameterized build on other projects', fill in 'impl,web' for 'Projects to build',
  click 'Add Parameters' and choose 'Current build parameters'.

Click Save that we are done, the configurations for the 'impl' and 'web' jobs are almost same with the 'api' job 
except for they don't need the Trigger Parameterized configuration because they don't have downstream jobs.

# Make them work
## Verify Github Project (Shared Repository) hyperlink works 
Open any job there should be a Github (Shared Repository) link in the panel which links to the github project you configured before.
 
## Create the shared git repository
To create a shared git repository, firstly you use the 'git clone' command to get the project manully, alternatively, 
install the Git plugin and create a job named 'master', configure the Git Repository and build the job manually, this
job will only be built once because the trigger script will pull the new changes.

Now we have the git repository in place, we have 2 ways to make it as a shared repository

* Configure the workspaceDir in JENKINS_HOME/config.xml, set the value to the git repository folder, this solution
  only works for the situation that the Jenkins server is dedicated for only 1 project. 
* Create soft links for all jobs, source ->  git repository folder, target -> job/workspace 
 
## Schedule the trigger script
There are 2 ways to schedule the trigger script

* If you specifed the :only_once value of :running_options as false, just run it in the backend(nohup ./jenkins_trigger.rb &)
  it should work properly
* If you specifed the :only_once value of :running_options as true, you need to create another Jenkins job and configure it run periodically, 
  what's the job do is just run the trigger script, you are not supposed to build the existing master job periodically because the git plugin
  will pull from the repository before the script runs, this will prevent the trigger script from detecting changes.

## Verify all features

* Make a change in the 'impl' or 'web' module, push the changes to github, the jobs should be triggered accordingly,
  the commit hyperlink should be generated in the build summary page.
* Make a change in the 'api'module, push the changes to github, both the 'api' and downstream jobs should be triggered,
  the commit hyperlinks should be generated in the build summary page of all jobs.
* The Changes Since Last Build should work properly.

# <a name="changes_since_last_build"></a>More About Changes Since Last Build
The trigger script will create a yml file for each job, it located in ${user_home}/.github_shared_repository/${job_name}.yml,
this file will track the recent builds history, the maximum trakced number is specified in other_options[:MAX_TRACKED_BUILDS], 
you can read the content this file for some artifact purpose(don't make any change on it).

Since the Plugin will read the yml files in the user home, to enable the Changes Since Last Build feature you must run
the trigger script on the same machine with the Jenkins server(and also run with the same user).

The reason we must keep multiple builds history rather than just track the latest one is there will be a delay between
the trigger action and the building process, in the building process another trigger action may occur, if we just track the 
latest build the plugin we may get the wrong build id(because the last build has been overwritten), actually the plugin will 
look for the corresponding build in the yml according to the build id to get the changes since last build, the old build history 
will be rolled up if it exceeds the value of other_options[:MAX_TRACKED_BUILDS] , if you want to keep the history longer, 
consider adjust this option to a larger value in the trigger script.
   
# Known Issues 
Currently the trigger script hardcoded the git repository branch to 'master', it may be specified as expected.

# Contact
If you have any questions, please contact me at <james.wang.z81@gmail.com>. 
