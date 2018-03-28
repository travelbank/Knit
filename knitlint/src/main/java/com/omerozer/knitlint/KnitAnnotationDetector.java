package com.omerozer.knitlint;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omerozer on 3/27/18.
 */

public class KnitAnnotationDetector extends Detector implements Detector.UastScanner {


    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return new ArrayList<Class<? extends UElement>>(){{
                add(UClass.class);
            }
        };
    }

    @Nullable
    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        return new DetectorHandler(context);
    }


}
