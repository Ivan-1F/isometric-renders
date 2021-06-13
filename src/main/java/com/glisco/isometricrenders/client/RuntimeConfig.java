package com.glisco.isometricrenders.client;

public class RuntimeConfig {

    //Transform Options
    public static int rotation = 225;
    public static int angle = 30;
    public static int renderScale = 150;
    public static int renderHeight = 0;

    //Atlas Options
    public static int atlasColumns = 12;
    public static int atlasShift = 115;
    public static int atlasHeight = 115;
    public static float atlasScale = 2.5f;

    //Render Options
    public static int backgroundColor = 0x0000ff;

    //Export Options
    public static boolean useExternalRenderer = false;
    public static boolean allowMultipleNonThreadedJobs = false;
    public static boolean allowInsaneResolutions = false;
    public static boolean dumpIntoRoot = false;
    public static int exportResolution = 2048;
    public static int exportOpacity = 100;

}
