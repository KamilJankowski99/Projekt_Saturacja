/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przeliczanie;
import java.util.Scanner;
import java.lang.Number;
import java.lang.Math;
/**
 *
 * @author Kamil
 */
 

public class Main {
               
    public static void main(String[] args) {
        Piksel piksel = new Piksel();
        piksel.r = 255;
        piksel.g = 0;
        piksel.b = 0;
        piksel.rgb2hsv();
        
        piksel.HSV_saturation = piksel.HSV_saturation - 0.8;
        
       System.out.println(piksel.HSV_hue); 
       piksel.hsv2rgb();
    }               
    }

