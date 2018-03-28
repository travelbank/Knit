package com.omerozer.knitlint;


import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

/**
 * Created by omerozer on 3/27/18.
 */

public class KnitIssues {

    public static final Issue PRESENTER_ANNOTATION_MISMATCH = Issue.create(
            "pr_an_mtc",
            "Knit: 'Presenter' annotation wrongly used",
            "Knit: 'Presenter' annotation used on an Class that doesn't extend 'KnitPresenter'",
            Category.CORRECTNESS,
            7,
            Severity.ERROR,
            new Implementation(KnitAnnotationDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    public static final Issue MODEL_ANNOTATION_MISMATCH = Issue.create(
            "mo_an_mtc",
            "Knit: 'Model' annotation wrongly used",
            "Knit: 'Model' annotation used on an Class that doesn't extend 'KnitModel'",
            Category.CORRECTNESS,
            7,
            Severity.ERROR,
            new Implementation(KnitAnnotationDetector.class, Scope.JAVA_FILE_SCOPE)
    );



}
