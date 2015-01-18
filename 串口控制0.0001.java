  
import processing.serial.*;
int alph=0,alp_dir=1,img_dir = 1,img_w;

Serial myPort;  
PImage img;

void setup() 
{
  img = loadImage("qmsht.bmp");
  size(displayWidth,img.height);
  noStroke();
  img_w = width-img.width;
  
  println(Serial.list());

  myPort = new Serial(this, Serial.list()[1], 4800);
  
//  frameRate(0);
}

void draw() 
{
  fill(255,0,0,alph);
  image(img,img_w,0);
  ellipse(width/2,height/2,height/2,height/2);
  
//  alph += alp_dir;
  img_w += img_dir;
//  if(alph==255 || alph ==0 )
//    alp_dir = -alp_dir;
  if(img_w == 0 || img_w == width-img.width)
    img_dir = -img_dir;
//  println();
  while (myPort.available() > 0) 
  {
    char inBuffer = myPort.readChar();   
//    if (inBuffer != '') 
    {
      println((int)inBuffer);
      alph = ((int)inBuffer)*2;
    }
  }
}

