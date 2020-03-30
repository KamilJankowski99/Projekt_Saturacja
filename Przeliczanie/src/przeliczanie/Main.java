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
        piksel.r = 10;
        piksel.g = 125;
        piksel.b = 170;
        piksel.rgb2hsv();
        
        piksel.HSV_saturation = piksel.HSV_saturation - 0.1;
        
       System.out.println(piksel.HSV_hue); 
       piksel.hsv2rgb();
    }               
    }

