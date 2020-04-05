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
    float angle)
{
    int gidX = get_global_id(0);
    int gidY = get_global_id(1);
    int w = get_image_width(sourceImage);
    int h = get_image_height(sourceImage);
    int2 posIn = {gidX, gidY};
    int2 posOut = {gidX, gidY};
    uint4 pixel = read_imageui(sourceImage, samplerIn, posIn);
    pixel.x = pixel.x + angle; //blue
    pixel.y = pixel.y; //green
    pixel.z = pixel.z; //red
    pixel.w = pixel.w; //alpha 
    write_imageui(targetImage, posOut, pixel);
};