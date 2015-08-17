package greenmoonsoftware.tidewater.scm.git

import org.testng.annotations.Test

class GitCloneTests {
    @Test
    void shouldReturnAllInputs() {
        def url = 'git://your.url.here'
        def ref = 'branches/your-branch'
        def dir = '/home/yournamehere'

        def actual = new GitClone(url:url, dir: dir, ref: ref).inputs

        assert actual.size() == 3
        assert actual['url'] == url
        assert actual['ref'] == ref
        assert actual['dir'] == dir
    }

    @Test
    void shouldReturnAllOutputs() {
        def sha = '16c15084b201598eb6ca16888aef02685b5d5a8a'
        def actual = new GitClone(sha: sha).outputs

        assert actual.size() == 1
        assert actual['sha'] == sha
    }
}
