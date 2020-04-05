package saturacja;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.jocl.CL.*;
import org.jocl.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class JOCLImageEffects {

    private BufferedImage inputImage;
    private BufferedImage outputImage;

    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_kernel kernel;
    private cl_mem inputImageMem;
    private cl_mem outputImageMem;
    private int imageSizeX;
    private int imageSizeY;

//OpenCl kernel
    private static supportOCL supportOCL = new supportOCL();
    private static String kernelPath = ".\\src\\saturacja\\changeBlueChannel.cl";
    private static String programSource = supportOCL.readOCLKernel(Paths.get(kernelPath));

    void initCL() {
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID 
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Check if images are supported
        int imageSupport[] = new int[1];
        clGetDeviceInfo(device, CL.CL_DEVICE_IMAGE_SUPPORT,
                Sizeof.cl_int, Pointer.to(imageSupport), null);
        System.out.println("Images supported: " + (imageSupport[0] == 1));
        if (imageSupport[0] == 0) {
            System.out.println("Images are not supported");
            System.exit(1);
            return;
        }

        // Create a command-queue for the selected device
        cl_queue_properties properties = new cl_queue_properties();
        properties.addProperty(CL_QUEUE_PROFILING_ENABLE, 1);
        properties.addProperty(CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE, 1);
        commandQueue = clCreateCommandQueueWithProperties(
                context, device, properties, null);

        // Create the program
        System.out.println("Creating program...");
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{programSource}, null, null);

        // Build the program
        System.out.println("Building program...");
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        System.out.println("Creating kernel...");
        kernel = clCreateKernel(program, "changeImageSaturation", null);

    }

    private void initImageMem() {
        // Create the memory object for the input- and output image
        DataBufferInt dataBufferSrc
                = (DataBufferInt) inputImage.getRaster().getDataBuffer();
        int dataSrc[] = dataBufferSrc.getData();

        cl_image_format imageFormat = new cl_image_format();
        imageFormat.image_channel_order = CL_RGBA;
        imageFormat.image_channel_data_type = CL_UNSIGNED_INT8;

        inputImageMem = clCreateImage2D(
                context, CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                new cl_image_format[]{imageFormat}, imageSizeX, imageSizeY,
                imageSizeX * Sizeof.cl_uint, Pointer.to(dataSrc), null);

        outputImageMem = clCreateImage2D(
                context, CL_MEM_WRITE_ONLY,
                new cl_image_format[]{imageFormat}, imageSizeX, imageSizeY,
                0, null, null);
    }

    Image changeImageBlueComponent(float delta, Image inputJXFImage) {
        initCL();
        inputImage = SwingFXUtils.fromFXImage(inputJXFImage, null);

        imageSizeX = inputImage.getWidth();
        imageSizeY = inputImage.getHeight();
        initImageMem();

        outputImage = new BufferedImage(
                imageSizeX, imageSizeY, BufferedImage.TYPE_INT_RGB);

        // Set up the work size and arguments, and execute the kernel
        long globalWorkSize[] = new long[2];
        globalWorkSize[0] = imageSizeX;
        globalWorkSize[1] = imageSizeY;
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(inputImageMem));
        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(outputImageMem));
        clSetKernelArg(kernel, 2, Sizeof.cl_float,
                Pointer.to(new float[]{delta}));
        clEnqueueNDRangeKernel(commandQueue, kernel, 2, null,
                globalWorkSize, null, 0, null, null);

        // Read the pixel data into the output image
        DataBufferInt dataBufferDst
                = (DataBufferInt) outputImage.getRaster().getDataBuffer();
        int dataDst[] = dataBufferDst.getData();
        clEnqueueReadImage(
                commandQueue, outputImageMem, true, new long[3],
                new long[]{imageSizeX, imageSizeY, 1},
                imageSizeX * Sizeof.cl_uint, 0,
                Pointer.to(dataDst), 0, null, null);

        clFlush(commandQueue);
        clFinish(commandQueue);
        return SwingFXUtils.toFXImage(outputImage, null);
    }
}
