require './git_jenkins_remote_trigger'

include LogAnalyzer

def assert(message, &block)
	raise RuntimeError.new(message) unless yield
end

def assert_equals(expected, actual)
	assert("Assertion error, expected #{expected}, actual #{actual}") { expected == actual }
end


logs = %Q{
commit 90fee7075e7e33b48b54eb92165a105de16b77cf
Author: christian.chen@quest.com <christian.chen@quest.com>
Date:   Sat Mar 31 09:43:53 2012 +0800

    [master] [FGLEU-2444] [Rabbit] Finished "Delete Special Event"
    [master] [FGLEU-2561] [Rabbit] Resolved
    [master] [FGLEU-2642] [Rabbit] Resolved
    [master] [FGLEU-2001] [Christian] Modified scope radio list in custom field dialogue & related functions.
    [master] [FGLEU-2565] [Nacy] Begin working on Session Explorer - Detial Views.

commit 4bb306a57a7a553d1f86342aa4f738c49535b485
Author: Bill Wixted <bill.wixted@quest.com>
Date:   Fri Mar 30 11:38:54 2012 -0700

    [master][FGLEU-2001] clarified scope options

commit 0eeb87f703e0bbb01c4de164fc8028ce7fb02d56
Merge: 11e4ea8 46f2410
Author: Dave Wood <dave.wood@quest.com>
Date:   Wed Apr 24 16:56:08 2013 -0600

    Merge branch 'master' of github.com:GitQuest/1EU

commit b5169a763238bbe164b7129e02c1181fc02cb1ad
Author: Bill Wixted <bill.wixted@quest.com>
Date:   Fri Mar 30 10:31:48 2012 -0700

    [master][FGLEU-2554] added label to Sequence Analyzer Event wireframe
}

hashs = analyze_multiple_commit_logs(logs)
assert_equals 4, hashs.size()

first = hashs[0]
assert_equals '90fee7075e7e33b48b54eb92165a105de16b77cf', first['commit_id'] 
assert_equals 'christian.chen@quest.com <christian.chen@quest.com>', first['author'] 
assert_equals 'Sat Mar 31 09:43:53 2012 +0800', first['date'] 
#TODO multiple lines message hasn't been supported
assert_equals '[master] [FGLEU-2444] [Rabbit] Finished "Delete Special Event"', first['message'] 

second = hashs[1]
assert_equals '4bb306a57a7a553d1f86342aa4f738c49535b485', second['commit_id'] 
assert_equals 'Bill Wixted <bill.wixted@quest.com>', second['author'] 
assert_equals 'Fri Mar 30 11:38:54 2012 -0700', second['date'] 
assert_equals '[master][FGLEU-2001] clarified scope options', second['message'] 

third = hashs[2]
assert_equals '0eeb87f703e0bbb01c4de164fc8028ce7fb02d56', third['commit_id'] 
assert_equals 'Dave Wood <dave.wood@quest.com>', third['author'] 
assert_equals 'Wed Apr 24 16:56:08 2013 -0600', third['date'] 
assert_equals 'Merge branch \'master\' of github.com:GitQuest/1EU', third['message'] 

fourth = hashs[3]
assert_equals 'b5169a763238bbe164b7129e02c1181fc02cb1ad', fourth['commit_id'] 
assert_equals 'Bill Wixted <bill.wixted@quest.com>', fourth['author'] 
assert_equals 'Fri Mar 30 10:31:48 2012 -0700', fourth['date'] 
assert_equals '[master][FGLEU-2554] added label to Sequence Analyzer Event wireframe', fourth['message'] 
