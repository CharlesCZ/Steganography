import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;


public class Main {


   public static BufferedImage encode(BufferedImage img,char[] message){

        int mes;
        int rgb;
        int pixel;
        int red;
        int green;
        int blue;


        int height = img.getHeight();
        int width = img.getWidth();
int bitPossition=0;
        int messageIndex=0;
        mes=message[messageIndex];
       //System.out.println("poczatek");
        for (int h = 1; h<height; h++) {
            for (int w = 1; w < width; w++) {
                rgb = img.getRGB(w, h);
                pixel = rgb & 0xFF000000; //alfa
                if(bitPossition>=8 && messageIndex<message.length) {
                    //System.out.println(" ");
                    //System.out.println(" ");

                    bitPossition=0;
                    ++messageIndex;
                    if(message.length>messageIndex)
                    mes = message[messageIndex];

                }

                if ((((mes >> bitPossition) & 0x00000001) == 1) &&  messageIndex<message.length ){

                    red = (byte) ((rgb >> 16) & 0x000000FF) | (1<<0);
                } else if( (((mes >> bitPossition) & 0x00000001) == 0) &&  messageIndex<message.length)
                    red = (byte) ((rgb >> 16) & 0x000000FF) & ~(1<<0); //popraw
                else   red = (rgb >> 16 ) & 0x000000FF;
                //System.out.println( (red & 1));
                ++bitPossition;
                pixel = pixel | ((red << 16) & 0x00FF0000);
                if(bitPossition>=8 && messageIndex<message.length) {
                    //System.out.println("  ");
                    //System.out.println(" ");

                    bitPossition=0;
                    ++messageIndex;
                    if(message.length>messageIndex)
                    mes = message[messageIndex];

                }

                if (((mes >> bitPossition) & 0x00000001) == 1) {
                    green = (byte) ((rgb >> 8) & 0x000000FF) | (1<<0);
                } else if( (((mes >> bitPossition) & 0x00000001) == 0) && messageIndex<message.length )
                    green = (byte) ((rgb >> 8) & 0x000000FF) & ~(1<<0);
              else  green = (rgb >> 8 ) & 0x000000FF;
                //System.out.println(green & 1);
                pixel = pixel | ((green << 8) & 0x0000FF00);
                ++bitPossition;

                if(bitPossition>=8 && messageIndex<message.length) {
                    //System.out.println(" ");
                    //System.out.println(" ");
                    //System.out.println("  ");

                    bitPossition=0;
                    ++messageIndex;
                    if(message.length>messageIndex)
                    mes = message[messageIndex];

                }

                if ((((mes >> bitPossition) & 0x00000001) == 1) &&  messageIndex<message.length) {
                    blue = (byte) ((rgb) & 0x000000FF) | (1<<0);
                } else if( (((mes >> bitPossition) & 0x00000001) == 0) && messageIndex<message.length)
                    blue = (byte) ((rgb) & 0x000000FF) & ~(1<<0);
                else  blue = (rgb) & 0x000000FF;
                //System.out.println(blue & 1);
                ++bitPossition;
                pixel = pixel | (blue & 0x000000FF);

                img.setRGB(w,h,pixel);
                pixel = rgb & 0xFF000000; //alfa





            }
        }
        /*
       //System.out.println("TEST");
       rgb = img.getRGB(1, 1);
       red = ((rgb >> 16 ) & 0x000000FF ) & 1;

       green = ((rgb >> 8 ) & 0x000000FF) & 1;

       blue = ((rgb) & 0x000000FF) & 1;

       //System.out.print((red & 1) +" "+ (green & 1)+" "+ (blue & 1));
       rgb = img.getRGB(2, 1);
       red = ((rgb >> 16 ) & 0x000000FF ) & 1;

       green = ((rgb >> 8 ) & 0x000000FF) & 1;

       blue = ((rgb) & 0x000000FF) & 1;
       //System.out.print((red & 1) +" "+  (green & 1)+" "+ (blue & 1));
       rgb = img.getRGB(3, 1);
       red = ((rgb >> 16 ) & 0x000000FF ) & 1;

       green = ((rgb >> 8 ) & 0x000000FF) & 1;

       blue = ((rgb) & 0x000000FF) & 1;
       //System.out.print(" "+ (red & 1) +" "+  (green & 1));

       //System.out.println("TEST");*/
        return img;

    }

    public static String decode(BufferedImage img){

       char[] message=new char[128];

        //System.out.println("DEKRYPT!!!!!!!!!!!");
for(int i=0;i<message.length;++i)
    message[i]=(char)0;

        int rgb;

        int red;
        int green;
        int blue;


        int height = img.getHeight();
        int width = img.getWidth();
        int charPosition=0;
        int index=0;
        for (int h = 1; h<height; h++) {
            for (int w = 1; w < width; w++) {

                rgb = img.getRGB(w, h);

                if(charPosition>7)
                {
                    //System.out.println(" ");
                    //System.out.println(" ");
                    //System.out.println(" ");
                    //System.out.println(" ");
                    charPosition=0;
                    ++index;
                    if(index>127) {
                        return new String(message);

                    }
                }

               red = (rgb >> 16 ) & 0x00000001;
                //System.out.println(red);
              message[index]=(char)  (message[index]  | ((red << charPosition) & 0x000000FF) );
            ++charPosition;

                if(charPosition>7)
                {
                    //System.out.println(" ");
                    //System.out.println(" ");
                    //System.out.println(" ");
                    charPosition=0;
                    ++index;
                    if(index>127) {
                        return new String(message);

                    }
                }




                green = (rgb >> 8 ) & 0x00000001;
                //System.out.println(green);
                message[index]= (char) ( message[index]  | ((green << charPosition) & 0x000000FF));
                ++charPosition;
                if(charPosition>7)
                {
                    //System.out.println(" ");
                    //System.out.println(" ");
                    charPosition=0;
                    ++index;
                    if(index>127) {
                        return new String(message);

                    }

                }

                blue = (rgb) & 0x00000001;
                //System.out.println(blue);
                message[index]= (char)  ( message[index]  | ((blue << charPosition)& 0x000000FF));
               ++charPosition;


            }
        }


        return new String(message);
    }
    /*
     *Creates a user space version of a Buffered Image, for editing and saving bytes
     *@param image The image to put into user space, removes compression interferences
     *@return The user space version of the supplied image
     */
    public static BufferedImage user_space(BufferedImage image)
    {
        //create new_img with the attributes of image
        BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose(); //release all allocated memory for this image
        return new_img;
    }
            public static void main(String[] args) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("test.bmp"));
                } catch (IOException e) {
                    //System.out.println(e.getMessage());
                }
                int height = img.getHeight();
                int width = img.getWidth();



                //System.out.println("rozmiary obrazka"+height  + "  " +  width + " " + img.getRGB(30, 30));
                char[] message=new String("secret message").toCharArray();
                BufferedImage imgAfter=encode(user_space(img),message);
                System.out.println(decode(imgAfter));
               


                File outputfile = new File("saved.png");
                try {
                    ImageIO.write(imgAfter, "png", outputfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


