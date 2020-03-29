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
 class MaxMin {
    
    double max(double first, double second, double third){
        double max;
        
        if(first - second > 0){
            max = first;
        }else {
            max = second;
        }
        if(third - max > 0){
            max = third;
        }
        
        return max;
    }
    
    double min(double first, double second, double third){
        double min;
        
        if(first - second < 0){
            min = first;
        }else {
            min = second;
        }
        if(third - min < 0){
            min = third;
        }        
        return min;
    }
    
    double min(double first, double second){
        double min;
        
        if(first - second < 0){
            min = first;
        }else {
            min = second;   
        }
        return min;
    }
}

public class Main {
        
    static int max(int first, int second){
        int max;
        
        if(first - second > 0){
            max = first;
        }else {
            max = second;
        }
        
        return max;
    }
    
    
    static int max(int first, int second, int third){
        int max;
        
        max =  max(first, second);
        max = max(max, third);
        
        return max;
    }
    
    static int min(int first, int second){
        int min;
        
        min = min(first, second);
        if(first - second < 0){
            min = first;
        }else {
            min = second;
        }
        
                return min;
    }
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
  
        System.out.println("Podaj wartość R:");
        Double r =  sc.nextDouble();
        System.out.println("Podaj wartość G:");
        Double g =  sc.nextDouble();
        System.out.println("Podaj wartość B:");
        Double b =  sc.nextDouble();
        
        Piksel piksel = new Piksel();
        piksel.r = r.intValue();
        piksel = piksel.rgb2hsv(piksel);
        Double HSV_value;
        Double chroma;
        Double lightness;
        Double HSV_hue;
        Double RGB_min_component;
        Double HSV_saturation;
        Double HSL_saturation;
        
        
        MaxMin comparator = new MaxMin();
        HSV_value = comparator.max(r, g, b);    
        RGB_min_component = comparator.min(r, g, b);
       
       System.out.println("Największa wartość: " + HSV_value);
       System.out.println("Najmniejsza wartość: " + RGB_min_component);
        
       chroma = HSV_value - RGB_min_component;
       lightness = (HSV_value + RGB_min_component)/2;
       
       System.out.println("Chroma: " + chroma);
       System.out.println("Lightness: " + lightness);
       
       
       if(chroma == 0){
           HSV_hue = 0.0;
           System.out.println("Hue: " + HSV_hue);
       }else if(HSV_value.compareTo(r) == 0) {
           HSV_hue = 60 * (0 + (g - b)/chroma);
           System.out.println("Hue: " + HSV_hue);
       }else if(HSV_value.doubleValue() == g.doubleValue()){
           HSV_hue = 60 * (2 + (b - r)/chroma);
           System.out.println("Hue: " + HSV_hue);
       }else if(HSV_value.doubleValue() == b.doubleValue()){
           HSV_hue = 60 * (4 + (r + g)/chroma);
           System.out.println("Hue: " + HSV_hue);
       }
              
       
       if(HSV_value == 0){
           HSV_saturation = 0.0;
           System.out.println("Saturation V: " + HSV_saturation);
       }else {
           HSV_saturation = chroma/HSV_value;
            System.out.println("Saturation V: " + HSV_saturation);
       }
       
       if(lightness == 0){
           HSL_saturation = 0.0;
           System.out.println("Saturation L: " + HSL_saturation);
       }else if(lightness == 1){
           HSL_saturation = 0.0;
            System.out.println("Saturation L: " + HSL_saturation);
       }else {
           HSL_saturation = (HSV_value - lightness) / comparator.min(lightness, 1 - lightness);                         //(RGB_min_component * (lightness) + RGB_min_component * (1 - lightness));
            System.out.println("Saturation L: " + HSL_saturation);
       }               
    }
}
