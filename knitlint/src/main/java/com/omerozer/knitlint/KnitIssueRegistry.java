package com.omerozer.knitlint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by omerozer on 3/27/18.
 */

public class KnitIssueRegistry extends IssueRegistry {

    @NotNull
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(KnitIssues.PRESENTER_ANNOTATION_MISMATCH,KnitIssues.MODEL_ANNOTATION_MISMATCH);
    }
}
