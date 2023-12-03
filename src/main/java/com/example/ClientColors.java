package com.example;

// Class to have the colors to use in the output strings
public class ClientColors {
    public final String RESET = "\033[0m"; // Text Reset
    
    // Bold High Intensity
    public final String RED_BOLD_BRIGHT = "\033[1;91m"; // RED
    public final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public final String BLUE_BOLD_BRIGHT = "\033[1;94m"; // BLUE
    public final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public final String CYAN_BOLD_BRIGHT = "\033[1;96m"; // CYAN
    public final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m"; // CYAN
    public final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m"; //WHITE
}