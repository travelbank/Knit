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

    public static final String ANNOTATION_CLASS_MISMATCH = "Knit : Annotation Wrongly Used";

    public static final Issue PRESENTER_ANNOTATION_MISMATCH = Issue.create(
            "presenter ant mismatch",
            "Knit: 'Presenter' annotation wrongly used",
            "Knit: 'Presenter' annotation used on an Class that doesn't extend 'KnitPresenter'",
            Category.USABILITY,
            7,
            Severity.ERROR,
            new Implementation(KnitAnnotationDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    public static final Issue MODEL_ANNOTATION_MISMATCH = Issue.create(
            "model ant mismatch",
            "Knit: 'Model' annotation wrongly used",
            "Knit: 'Model' annotation used on an Class that doesn't extend 'KnitModel'",
            Category.USABILITY,
            7,
            Severity.ERROR,
            new Implementation(KnitAnnotationDetector.class, Scope.JAVA_FILE_SCOPE)
    );



}
