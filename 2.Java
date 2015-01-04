float dist(float[] a, float[] b) {
  float diffssum = 0;
  for(int i=0; i<a.length; i++)
    diffssum += sq(b[i] - a[i]);
  return sqrt(diffssum);
}
 
float[] clonef(float[] arr) {
  float[] out = new float[arr.length];
  for(int i = 0; i < arr.length; i++)
    out[i] = arr[i];
  return out;}
   
float[] avg(float[] a, float[] b, float weight) {
  float[] out = new float[a.length];
  for(int i = 0; i < out.length; i ++)
    out[i] = (a[i] * (1-weight))  + (b[i] * weight);
  return out;
}
 
void line(float[] a, float[] b) {
  if(a.length == 3)
    line(a[0],a[1],a[2],b[0],b[1],b[2]);
  else
    line(a[0],a[1],b[0],b[1]);
}
//////////////////////////////////////////////////////////////
class Kmeans {
  float rate;
  Vector clusters = new Vector();
  Vector datapoints = new Vector();
  Kmeans(int nclusters, float rate) {
    this.rate = rate;
    for(int i=0; i<nclusters; i++) addCluster();
  }
  void addCluster() {
    clusters.add(new Cluster());
  }
  void addDataPoint(float[] data) {
    datapoints.add(new DataPoint(data,clusters.size()));
  }
  void update() {
    Average[] avgs = new Average[clusters.size()];
    for(int i=0; i<clusters.size(); i++) {
      Cluster cur = (Cluster) clusters.get(i);
      cur.resetCount();
      avgs[i] = new Average();
    }
    // compute cluster centers
    for(int i=0; i<datapoints.size(); i++) {
      DataPoint cur = (DataPoint) datapoints.get(i);
      Cluster curc = (Cluster) clusters.get(cur.cluster);
      curc.tot++;
      avgs[cur.cluster].add(cur.data);
    }
    // move cluster centers
    for(int i=0; i<clusters.size(); i++) {
      Cluster cur = (Cluster) clusters.get(i);
      if(avgs[i].tot > 0) cur.moveTowards(avgs[i].avg, rate);
    }
    // reclassify datapoints
    for(int i=0; i<datapoints.size(); i++)
      ((DataPoint) datapoints.get(i)).reAssign(clusters); 
  }
}
//////////////////////////////////////////////////////////////

class DataPoint {
  float[] data;
  int cluster;
  DataPoint(float[] data, int k) {
    this.data = clonef(data);
    cluster = (int) random(0,k);
  }
  void reAssign(Vector clusters) {
    Cluster cur = (Cluster) clusters.get(cluster);
    float mindist = dist(cur.center, data);
    for(int i=0; i<clusters.size(); i++) {
      cur = (Cluster) clusters.get(i);
      float curdist = dist(cur.center, data);
      if(curdist < mindist) {
        mindist = curdist;
        cluster = i;
      }
    }
    moveTowards((Cluster) clusters.get(cluster));
  }
  void moveTowards(Cluster c) {
    data = avg(data,c.center,.01);
  }
}
//////////////////////////////////////////////////////////////////

class Cluster {
  float[] center = {};
  int tot = 0;
  Cluster() {}
  void moveTowards(float[] position, float rate) {
    if(center.length == 0)
      center = clonef(position);
    else
      for(int i = 0; i<position.length; i++)
        center[i] = (center[i] * (1 - rate)) + (position[i] * rate);
  }
  void resetCount() {
    tot = 0;
  }
}

///////////////////////////////////////////////////////////////////////

class Average{
  float[] avg;
  float tot = 0;
  Average() {}
  void add(float[] data) {
    if(tot == 0)
      avg = clonef(data);
    else
      for(int i = 0; i<data.length; i++)
        avg[i] = ((avg[i] * tot) + data[i]) / (tot + 1);
    tot++;
  }
}
////////////////////////////////////////////////////////////////////

Kmeans k;
 
void setup() {
  size(640,360);
  colorMode(RGB,1.0);
  k = new Kmeans(int(random(2,6)),0.01);
  int tot = int(random(100,300));
  for(int i=0; i<tot; i++)
    k.addDataPoint(new float[] {random(0,width),random(0,height)});
  background(1);
  stroke(0,0,0,0.01);
}
 
void draw() {
  k.update();
  stroke(0,0,0,0.01);
  for(int i = 0; i<k.datapoints.size(); i++) {
    DataPoint cur = (DataPoint) k.datapoints.get(i);
    Cluster curc = (Cluster) k.clusters.get(cur.cluster);
    line(cur.data,curc.center);
  }
}
 
void mousePressed() {
  setup();
}
