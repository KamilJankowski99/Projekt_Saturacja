const sampler_t samplerIn =
    CLK_NORMALIZED_COORDS_FALSE |
    CLK_ADDRESS_CLAMP |
    CLK_FILTER_NEAREST;

const sampler_t samplerOut =
    CLK_NORMALIZED_COORDS_FALSE |
    CLK_ADDRESS_CLAMP |
    CLK_FILTER_NEAREST;

#define E 0.0000001f

bool fequal(float x, float y)
{
    return (x+E > y && x-E < y);
}

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
    float X;
    float hue_prim;
    float red;
    float green;
    float blue;
    float red_prim;
    float green_prim;
    float blue_prim;
    float rgb_min_component;
    float rgb_max_component;
    float chroma;
    uint4 pixel = read_imageui(sourceImage, samplerIn, posIn);
    red = pixel.z/255.0f;
    green = pixel.y/255.0f;
    blue = pixel.x/255.0f;
    
    rgb_min_component = fmin(fmin(red, green), blue);
    rgb_max_component = fmax(fmax(red, green), blue);

    chroma = rgb_max_component - rgb_min_component;
    lightness = (rgb_max_component + rgb_min_component)/2.0f;
    
    hue = 0.0f;
    if(fequal(rgb_max_component, red)) {
        hue = (green - blue)/chroma + 0.0f;
    }else if(fequal(rgb_max_component, green)){
        hue = fmod(((blue - red)/chroma), 6.0f) + 2.0f;
    }else if(fequal(rgb_max_component, blue)){
        hue = fmod(( (red - green)/chroma), 6.0f) + 4.0f;
    }
    hue = hue * 60.0f;
    if(fequal(lightness, 0.0f) || fequal(lightness, 1.0f)) {
    HSL_saturation = 0.0f;
    } else {
    HSL_saturation = chroma/(1.0f - fabs(2.0f*lightness - 1.0f));
    }
    
    hue = (int)hue + deltaHue;
    HSL_saturation = HSL_saturation + deltaSaturation;
    lightness = lightness + deltaLightness;

    chroma = (1.0f - fabs(2.0f * lightness - 1.0f)) * HSL_saturation;
    hue_prim = (hue / 60.0f);
    X = chroma * (1.0f - fabs(fmod(hue_prim, 2.0f) - 1.0f));

    if ((int)hue_prim == 0 || (int)hue_prim == 6){
        red_prim = chroma;
        green_prim = X;
        blue_prim = 0;
    } else if ((int)hue_prim == 1){
        red_prim = X;
        green_prim = chroma;
        blue_prim = 0;
    } else if ((int)hue_prim == 2){
        red_prim = 0;
        green_prim = chroma;
        blue_prim = X;
    } else if ((int)hue_prim == 3){
        red_prim = 0;
        green_prim = X;
        blue_prim = chroma;
    } else if ((int)hue_prim == 4){
        red_prim = X;
        green_prim = 0;
        blue_prim = chroma;
    } else if ((int)hue_prim == 5){
        red_prim = chroma;
        green_prim = 0;
        blue_prim = X;
    } else {
        red_prim = 0;
        green_prim = 0;
        blue_prim = 0;
    }

    m = lightness - (0.5f * chroma);

    pixel.z = (int)((red_prim + m) * 255);
    pixel.y = (int)((green_prim + m) * 255);
    pixel.x = (int)((blue_prim + m) * 255);

    write_imageui(targetImage, posOut, pixel);
};
