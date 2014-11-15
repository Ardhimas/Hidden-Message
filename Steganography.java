import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.nio.file.*;
import java.io.*;

public class Steganography{

    public static void main(String[] args) throws IOException{
        if (args[0].equals("-E")){
            encrypt(args[1], args[2]);
        }
        else if (args[0].equals("-D")){
            decrypt(args[1], args[2]);
        }
        else{
            System.out.println("invalid arguments");
        }
    }

    public static void encrypt(String imageFilename, String inputFilename) throws IOException{
        File data = new File(inputFilename);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imageFilename));
        } catch (IOException e) {

        }

        ByteArrayInputStream by = new ByteArrayInputStream(Files.readAllBytes(data.toPath()));
        int totalbits = 0;
        String bitString = "";

        int height = img.getHeight();
        int width = img.getWidth();
        int type = img.getType();

        int xpixel = 0;
        int ypixel = 0;

        

        while (by.available() != 0){
            int byte_ = (int)by.read() & 0xFF;
            totalbits += 8;
            bitString += String.format("%8s", Integer.toBinaryString(byte_)).replace(' ', '0');
        }

        bitString += "00000000";

        char[] bitArray = bitString.toCharArray();
        int colorturn = 0; //red, green, or blue's turn
        int rcolor = -1;
        int gcolor = -1;
        int bcolor = -1;
        Color c = new Color(img.getRGB(xpixel, ypixel));
        for (char b : bitArray){
            if (colorturn == 3){
                colorturn = 0;
                c = new Color(img.getRGB(xpixel, ypixel));
            }
            int x = b - '0';            
            switch (colorturn){
                case 0:
                    rcolor = c.getRed();
                    if (rcolor == 255){
                        rcolor -= (1-x);
                    }
                    else if (rcolor % 2 == 0){
                        rcolor += x;
                    }else{
                        rcolor += (1-x);
                    }
                    break;                                
                case 1:
                    gcolor = c.getGreen();
                    if (gcolor == 255){
                        gcolor -= (1-x);
                    }
                    else if (gcolor % 2 == 0){
                        gcolor += x;
                    }else{
                        gcolor += (1-x);
                    }
                    break;                    
                case 2:
                    bcolor = c.getBlue();
                    if (bcolor == 255){
                        bcolor -= (1-x);
                    }
                    else if (bcolor % 2 == 0){
                        bcolor += x;
                    }else{
                        bcolor += (1-x);
                    }
                    Color newcolor = new Color(rcolor, gcolor, bcolor);
                    img.setRGB(xpixel, ypixel, newcolor.getRGB());
                    rcolor = -1;
                    gcolor = -1;
                    bcolor = -1;
                    if (xpixel == width-1){
                        if (ypixel != height-1){
                            ++ypixel;
                        }
                        else{//if pixels too full, start filling from beginning again
                            xpixel = 0; 
                            ypixel = 0;
                        }
                        xpixel = 0;
                    }
                    else{
                        ++xpixel;
                    }
                    break;
                }
            ++colorturn;
        }

        if (gcolor == -1){
            Color x = new Color(rcolor, c.getGreen(), c.getBlue());
            img.setRGB(xpixel, ypixel, x.getRGB());
        }
        else if (bcolor == -1){
            Color x = new Color(rcolor, gcolor, c.getBlue());
            img.setRGB(xpixel, ypixel, x.getRGB());
        }

        int amountPixel = 0;
        int colors = img.getRGB(30, 30);

	// This prints the image height and width and a specific pixel. 
        String ext = imageFilename.substring(imageFilename.length() -3);
        File outputimage = new File(imageFilename.substring(0, imageFilename.length()-4)+ "-steg." + ext);
        ImageIO.write(img, ext, outputimage);
    }
    
    public static void decrypt(String inputImagename, String outputFilename) throws IOException{
        PrintWriter writer = new PrintWriter(outputFilename);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(inputImagename));
        } catch (IOException e) {

        }
        int height = img.getHeight();
        int width = img.getWidth();
        int type = img.getType();

        int xpixel = 0;
        int ypixel = 0;

        String bitString = "";
        while (true){
            if (bitString.length() >= 8){
                String byteString = bitString.substring(0,8);
                if (byteString.equals("00000000")){
                    break;
                }
                Integer intByte = Integer.parseInt(byteString,2);
                writer.append((char)(byte)(int)intByte);
                String remainder = bitString.substring(8, bitString.length());
                bitString = remainder;
            }
            Color c = new Color(img.getRGB(xpixel, ypixel));
            if (c.getRed() % 2 == 0){
                bitString += 0;
            }
            else{
                bitString += 1;
            }
            if (c.getGreen() % 2 == 0){
                bitString += 0;
            }
            else{
                bitString += 1;
            }
            if (c.getBlue() % 2 == 0){
                bitString += 0;
            }
            else{
                bitString += 1;
            }
            if (xpixel == width-1){
                if (ypixel != height-1){
                    ++ypixel;
                }
                else{//if pixels too full, start filling from beginning again
                    xpixel = 0; 
                    ypixel = 0;
                }
                xpixel = 0;
            }
            else{
                ++xpixel;
            }
        }
        writer.close();
    }
}