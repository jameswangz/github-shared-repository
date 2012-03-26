# Goal
[Jenkins Github Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Github+Plugin) already has nice features to 
let Jenkins works well with Github, so why do we need this [Github shared repository plugin](https://github.com/jameswangz/github-shared-repository)? 
Well, this is because we have some special scenarios that github plugin couldn't handle. 

As everyone knows, for the algorithm consideration, from the enduser's perspective, git repository 
looks like a 'blob' object, that means it doesn't like subversion(or some other SCM tools) 
that we can check out or update just partial files from the repository, that is also the reason why
git is so fast, this is good but the multi-modules structure is very common in many projects, 
in this case we usually create separate Jenkins jobs for different modules, and we expect the code changes 
in some modules will only trigger corresponding jobs rather than trigger all of them(as the project size grows,
the building process is slower and slower), we have also considered using [Git submodules](http://help.github.com/submodules/) 
or [Git subtrees](https://github.com/apenwarr/git-subtree) to archive this goal, but we feel they are too
complicated for the beginners and will introduce additional complexities especially git itself has a 
high learning curve, so what we want to do is just leave the whole project as a single repository and 
find a way to configure separate Jenkins jobs for different modules and trigger only the affected jobs accordingly,
and this is what's this plugin done.   

# Summary 
