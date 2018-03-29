package com.omerozer.knitlint;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;

import com.intellij.psi.PsiElement;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;

/**
 * Created by omerozer on 3/28/18.
 */

public class DetectorHandler extends UElementHandler {

    private JavaContext context;

    public DetectorHandler(JavaContext context){
        this.context = context;
    }

    @Override
    public void visitClass(UClass node) {
        super.visitClass(node);
        checkForAnnotations(node);
    }

    private void checkForAnnotations(UClass node){

        if(node.findAnnotation(AnnotationsToCover.PRESENTER)!=null){
            handlePresenter(node,node.findAnnotation(AnnotationsToCover.PRESENTER));
            return;
        }

        if(node.findAnnotation(AnnotationsToCover.MODEL)!=null){
            handleModel(node,node.findAnnotation(AnnotationsToCover.MODEL));
            return;
        }

        context.report(KnitIssues.PRESENTER_ANNOTATION_MISMATCH,node,context.getLocation((PsiElement) node),KnitIssues.ANNOTATION_CLASS_MISMATCH);

    }

    private void reportMismatch(UClass node,UAnnotation annotation,Issue issue){
        context.report(issue,node,context.getLocation(annotation),KnitIssues.ANNOTATION_CLASS_MISMATCH);
    }

    private void handlePresenter(UClass node,UAnnotation annotation){
        UClass clazz = node;
        while (clazz!=null){
            if(clazz.getQualifiedName().contains(ClassesToLookFor.PRESENTER)){
                return;
            }
            clazz = clazz.getSuperClass();
        }
        reportMismatch(node,annotation,KnitIssues.PRESENTER_ANNOTATION_MISMATCH);
    }

    private void handleModel(UClass node,UAnnotation annotation ){
        UClass clazz = node;
        while (clazz!=null){
            if(clazz.getQualifiedName().contains(ClassesToLookFor.MODEL)){
                return;
            }
            clazz = clazz.getSuperClass();
        }
        reportMismatch(node,annotation,KnitIssues.MODEL_ANNOTATION_MISMATCH);
    }
}
