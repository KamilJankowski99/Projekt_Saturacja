/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przeliczanie;

import java.util.Scanner;
import java.lang.Math;
import static java.lang.Math.abs;
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

public class Piksel {
    public int r, g, b;
    public int r1, g1, b1;
    public int m;
    public Double HSV_value;
    public Double chroma;
    public Double lightness;
    public Double HSV_hue;
    public Double RGB_min_component;
    public Double HSV_saturation;
    public Double HSL_saturation;
    public Double hue, saturationv, saturationl, v, l;
    public Double hueprim;
    public Double x;    
        
       
    Piksel rgb2hsv(){
       
        
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
       }else if(HSV_value == r) {
           HSV_hue = 60 * (0 + (g - b)/chroma);
           System.out.println("Hue: " + HSV_hue);
       }else if(HSV_value == g){
           HSV_hue = 60 * (2 + (b - r)/chroma);
           System.out.println("Hue: " + HSV_hue);
       }else if(HSV_value == b){
           HSV_hue = 60 * (4 + (r - g)/chroma);
           System.out.println("Hue: " + HSV_hue);
       }
              
       
       if(HSV_value == 0){
           HSV_saturation = 0.0;
           System.out.println("Saturation V: " + HSV_saturation);
       }else {
           HSV_saturation = chroma / HSV_value;
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
        };
//    rgb2hsl;
//    hsv2rgb;
//    hsl2rgb;
//    hsl2hsv;
//    hsv2hsl;
      return this;
    }
    
    Piksel hsv2rgb(){
        
        chroma = HSV_value * HSV_saturation;
        
        hueprim = HSV_hue / 60;
        
        x = chroma * (1 - (Math.abs(hueprim %2 - 1)));
                
        if(hueprim>= 0 && hueprim <= 1 ){
            r1 = (int) Math.round(chroma.doubleValue());
            g1 = (int) Math.round(x.doubleValue());
            b1 = 0;
        }else if(hueprim>= 1 && hueprim <= 2 ){
            r1 = (int) Math.round(x.doubleValue());
            g1 = (int) Math.round(chroma.doubleValue());
            b1 = 0;
        }else if(hueprim>= 2 && hueprim <= 3 ){
            r1 = 0;
            g1 = (int) Math.round(chroma.doubleValue());
            b1 = (int) Math.round(x.doubleValue());
        }else if(hueprim>= 3 && hueprim <= 4 ){
            r1 = 0;
            g1 = (int) Math.round(x.doubleValue());
            b1 = (int) Math.round(chroma.doubleValue());
        }else if(hueprim>= 5 && hueprim <= 6 ){
            r1 = (int) Math.round(chroma.doubleValue());
            g1 = 0;
            b1 = (int) Math.round(chroma.doubleValue());
        }
        
        m = (int) Math.round(HSV_value.doubleValue()) - (int) Math.round(chroma.doubleValue());
        
        r = r1 + m;
        g = g1 + m;
        b = b1 + m;
        
        System.out.println(r);
        System.out.println(g);
        System.out.println(b);
        
        return this;
    }
}