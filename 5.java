class Particle {
  float mass;
  float x,y,z,xv,yv,zv;
  float xf,yf,zf,xa,ya,za=0;
  float[] tx=new float[trails];
  float[] ty=new float[trails];
  float[] tz=new float[trails];
  Particle(float m, float inx, float iny, float inz, float inxv, float inyv, float inzv) {
    mass = m;
    x=inx; y=iny; z=inz;
    xv=inxv; yv=inyv; zv=inzv;
  }
}

/////////////////////////////////////////////////////////////

import peasy.*;
PeasyCam cam;

int pointsTotal=50;
float maxAbsVelocity=1;
float gravitationalConstant=.001;
float maxMass=15;
float minMass=10;
int trails=12;

int r=200;

Particle[] particles=new Particle[pointsTotal];

void setup(){
  size(640, 360, P3D);
  cam = new PeasyCam(this, height);
  
  noStroke();
  fill(200, 200);
  
  for(int i=0;i<pointsTotal;i++){
    particles[i] = new Particle(
      random(minMass,maxMass),
      random(-r,r), random(-r,r), random(-r,r),
      random(-maxAbsVelocity,maxAbsVelocity), random(-maxAbsVelocity,maxAbsVelocity), random(-maxAbsVelocity,maxAbsVelocity));
  }
}

void draw(){
  background(0);
  for(int i=0;i<pointsTotal;i++){
    particles[i].xf=0;
    particles[i].yf=0;
    particles[i].zf=0;
  }
  for(int i=0;i<pointsTotal;i++){
    for(int j=i+1;j<pointsTotal;j++){
      float ddx,ddy,ddz,d,mainF,xc,yc,zc;
      ddx=(particles[j].x-particles[i].x);
      ddy=(particles[j].y-particles[i].y);
      ddz=(particles[j].z-particles[i].z);
      d=sqrt((ddx*ddx)+(ddy*ddy)+(ddz*ddz));
      mainF=(gravitationalConstant*particles[i].mass*particles[j].mass)/d;
      xc=mainF*ddx;
      yc=mainF*ddy;
      zc=mainF*ddz;
      particles[i].xf+=xc;
      particles[i].yf+=yc;
      particles[i].zf+=zc;
      particles[j].xf+=-xc;
      particles[j].yf+=-yc;
      particles[j].zf+=-zc;
    }
    particles[i].xa=(particles[i].xf/particles[i].mass);
    particles[i].ya=(particles[i].yf/particles[i].mass);
    particles[i].za=(particles[i].zf/particles[i].mass);
  }
  for(int i=0;i<pointsTotal;i++){
    particles[i].xv+=particles[i].xa; 
    particles[i].yv+=particles[i].ya; 
    particles[i].zv+=particles[i].za;
    particles[i].x+= particles[i].xv; 
    particles[i].y+= particles[i].yv; 
    particles[i].z+= particles[i].zv;
  }

  lights();
  for(int i=0;i<pointsTotal-1;i++){
    int spot=frameCount%trails;
    beginShape(POLYGON);
    for(int j=1;j<trails-1;j++){
      vertex( particles[i].tx[j],
      particles[i].ty[j],
      particles[i].tz[j]);
    }
    particles[i].tx[spot]=particles[i].x;
    particles[i].ty[spot]=particles[i].y;
    particles[i].tz[spot]=particles[i].z;
    endShape();
  }
}
