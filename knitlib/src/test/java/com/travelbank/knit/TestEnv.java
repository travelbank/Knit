package com.travelbank.knit;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public class TestEnv {
    public static int totalModels(){
        return 4;
    }

    public static int totalPresenters(){
        return 2;
    }

    public static int totalViews(){
        return 2;
    }

    public static int totalPresentersWithDependencies(){
        return 1;
    }

    public static int nonSingleTonModels(){
        return 3;
    }
}
