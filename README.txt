README

Hidden Message
Made by Ardhimas Kamdani
ardhimas.k@gmail.com;

This program takes three arguments as its input - a flag, an image file, and either an input or an output file. The flag "-E" indicates an encryption, whereas the flag "-D" indicates a decryption.

During encryption, using an image file, the program will take an input file containing a message and encode it into a duplicate image where the RGB values of every pixel have been systematically altered to reflect upon the bits that were encoded into the image. If the bit is a 0, the color value is made even, whereas if the bit is a 1, the color value is made odd. This is repeated until the whole message has been transmitted, and the encoded message is terminated by encoding a series of 8 0 bits to indicate a 0 byte. 

During decryption, taking the duplicated image and an output file name as its arguments, the program will systematically read the duplicated image for the encoded bits and then print each character of the message into the output file. This repeats until the program encounters the 0 byte indicating the end of the message, in which case it terminates.
