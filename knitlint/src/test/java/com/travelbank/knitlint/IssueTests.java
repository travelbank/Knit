package com.travelbank.knitlint;

import com.android.tools.lint.checks.infrastructure.TestFile;

import org.junit.Test;

/**
 * Created by omerozer on 3/28/18.
 */


public class IssueTests {

    private String testPresenterClass = ""
            + "package foo;"
            + ""
            + "import com.omerozer.knit.Presenter;"
            + ""
            + "@KnitPresenter()"
            + "public class TestPresenter{}";

    @Test
    public void presenterMismatchTest(){
//        lint()
//                .allowMissingSdk()
//                .files(java(testPresenterClass))
//                .issues(KnitIssues.PRESENTER_ANNOTATION_MISMATCH)
//                .run()
//                .expectClean();
    }

    private TestFile java(String source){
        return TestFile.JavaTestFile.create(source);
    }

}
