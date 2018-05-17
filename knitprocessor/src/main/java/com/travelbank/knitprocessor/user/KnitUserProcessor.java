package com.travelbank.knitprocessor.user;

import com.travelbank.knitprocessor.KnitBaseProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by omerozer on 2/4/18.
 */

public class KnitUserProcessor extends KnitBaseProcessor {

    List<UserMirror> userMirrors;
    UserWriter userWriter;


    public KnitUserProcessor(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
        this.userMirrors = new ArrayList<>();
        this.userWriter = new UserWriter();
    }


    public boolean process(Set<UserMirror> userMirrors) {
        createUsers(userMirrors);
        return true;
    }


    private void createUsers(Set<UserMirror> users) {
        for (UserMirror userMirror : users) {
            userWriter.write(getEnv().getFiler(), userMirror);
        }
    }


}
