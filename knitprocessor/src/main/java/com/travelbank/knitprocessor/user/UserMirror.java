package com.travelbank.knitprocessor.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by omerozer on 2/5/18.
 */

public class UserMirror {

    public String packageElement;

    public String enclosingClass;

    public String qualifiedName;

    public Map<String,ExecutableElement> methodMap = new HashMap<>();

    public Map<String,String> userMethodNames = new HashMap<>();

    public Set<String> requiredValues = new HashSet<>();

    public Map<String, ExecutableElement> getterMap = new HashMap<>();
}
