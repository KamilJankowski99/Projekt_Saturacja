const sampler_t samplerIn =
    CLK_NORMALIZED_COORDS_FALSE |
    CLK_ADDRESS_CLAMP |
    CLK_FILTER_NEAREST;

const sampler_t samplerOut =
    CLK_NORMALIZED_COORDS_FALSE |
    CLK_ADDRESS_CLAMP |
    CLK_FILTER_NEAREST;

__kernel void changeImageSaturation(
    __read_only  image2d_t sourceImage,
    __write_only image2d_t targetImage,
    float deltaHue,
    float deltaSaturation,
    float deltaLightness)
{
    int gidX = get_global_id(0);
    int gidY = get_global_id(1);
    int w = get_image_width(sourceImage);
    int h = get_image_height(sourceImage);
    int2 posIn = {gidX, gidY};
    int2 posOut = {gidX, gidY};
    float hue;
    float HSV_value;
    float HSV_saturation;
    float HSL_saturation;
    float lightness;
    float m;
    int red;
    int green;
    int blue;
    int red_prim;
    int green_prim;
    int blue_prim;
    int rgb_min_component;
    float rgb_max_component;
    int chroma;
    uint4 pixel = read_imageui(sourceImage, samplerIn, posIn);
    blue = pixel.x;
    green = pixel.y;
    red = pixel.z;
    
    rgb_min_component = min(min(red, green), blue);
    rgb_max_component = max(max(red, green), blue);

    chroma = rgb_max_component - rgb_min_component;
    lightness = (rgb_max_component + rgb_min_component)/2;

    if(chroma == 0){
        hue = 0.0;
    }else if(HSV_value == r) {
        hue = 60 * (0 + (green - blue)/chroma);
    }else if(HSV_value == g){
        hue = 60 * (2 + (blue - red)/chroma);
    }else if(HSV_value == b){
        hue = 60 * (4 + (red - green)/chroma);
    }

    if(HSV_value == 0){
        HSV_saturation = 0.0;
    }else {
        HSV_saturation = chroma / HSV_value;
    }

    if(lightness == 0){
        HSL_saturation = 0.0;

    }else if(lightness == 1){
        HSL_saturation = 0.0;
    }else {
        HSL_saturation = (HSV_value - lightness) / min(lightness, 1 - lightness);             
    }

    hue = hue + deltaHue;
    HSL_saturation = HSL_saturation + deltaSaturation;
    lightness = lightness +deltaLightness;

    chroma = (1 - abs(2 * lightness) -1) * HSL_saturation;
    hue_prim = hue / 60;
    X = chroma * (1 - abs(hue_prim%2 - 1));

    if (hue_prim == 1){
        red_prim = chroma;
        green_prim = X;
        blue_prim = 0;
    } else if (hue_prim == 2){
        red_prim = X;
        green_prim = chroma;
        blue_prim = 0;
    } else if (hue_prim == 3){
        red_prim = 0;
        green_prim = chroma;
        blue_prim = X;
    } else if (hue_prim == 4){
        red_prim = 0;
        green_prim = X;
        blue_prim = chroma;
    } else if (hue_prim == 5){
        red_prim = X;
        green_prim = 0;
        blue_prim = chroma;
    } else if (hue_prim == 6){
        red_prim = chroma;
        green_prim = 0;
        blue_prim = X;
    } else {
        red_prim = 0;
        green_prim = 0;
        blue_prim = 0;
    }

    m = lightness - (chroma / 2);

    pixel.z = red_prim + m;
    pixel.y = green_prim + m;
    pixel.x = blue_prim + m;

    write_imageui(targetImage, posOut, pixel);
};
