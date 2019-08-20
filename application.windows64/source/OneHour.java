import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class OneHour extends PApplet {

//Global vars
Clock clock;

//Fonts must be located in the "data" dir.
PFont defaultFont;
PFont dialFont;

public void setup(){
  //Set window size and title
  surface.setTitle("One Hour 1.0.0");
  
 
  //Create fonts
  defaultFont = createFont("Arial",30);
  dialFont = createFont("Sylfaen",30);
  textFont(defaultFont);
  
  //Create clock
  clock = new Clock(55, new PVector(width/2,height/2));
}
 
public void draw() {  
  //Set background color
  background(255);
  
  //Show HMS time
  printTimeHMS(hour(),minute(),second());
    
  //Update and draw the clock
  clock.update();
  clock.drawClock();
  clock.drawInfo();
}

public void printTimeHMS(int h, int m, int s){
  String apm = h<12 ? "AM" : "PM";
  fill(0);
  textAlign(RIGHT,TOP);
  textSize(18);
  text(str(h) + ":" + str(m) +":" + str(s) + apm, width-10, 10);  
}

public void mouseClicked(){
  if(mouseButton==LEFT){
     clock.changeTime();
  }
}

//**    toolbox    **// 
public void printVector(PVector v){
   print("(" + v.x +", "+ v.y +")");
}

public void printFontList(){
  PFont.list();
  String[] fontList = PFont.list();
  printArray(fontList);
}

public void resetStyle(){
  //rgba 0 ~ 255
  stroke(0);
  fill(0);
  textSize(10);
  textAlign(LEFT,BASELINE);
}
class Clock{

  private Mark mark_;
  private float timeLeft_;
  private float startingTime_;
  private float maxTime_ = 3600;
  private int wantedMin_ = 0;
  private boolean isEnded = false;
  
  private PVector origin_;
  private float radius_;

  public Clock(int min, PVector origin){
    origin_ = origin;
    radius_ = width/4;
    mark_ = new Mark(this, origin_, radius_);
    resetTimer(PApplet.parseFloat(min*60));
  }
  
  public void setWantedMin(int min){
    wantedMin_ = min;
  }
  
  public void resetTimer(float timeLeft){
    startingTime_ = second();
    isEnded = false;
    timeLeft_= timeLeft;
  }
  
  public void increaseTimeLeft(){
    if(timeLeft_ + 300 >= maxTime_)
      resetTimer(maxTime_);
    else
      resetTimer(timeLeft_ + 300);
  }
  
  public void decreaseTimeLeft(){
   if(timeLeft_ - 300 <= 0) 
     resetTimer(0);
   else
     resetTimer(timeLeft_-300);
  }
        
  public void update(){ 
    // Compute elapsed time and subtract it from timeLeft_
    if(timeLeft_ > 0){
      float elapsedTime = 0;
      float curTime = second();
    
      if(curTime < startingTime_)
        elapsedTime = 60 - startingTime_ - curTime;
      else 
        elapsedTime = curTime - startingTime_;
        
      startingTime_ = curTime;
      timeLeft_ -=elapsedTime;
    }else if(timeLeft_<=0)
      isEnded = true;
  }
  
  public void drawInfo(){
    fill(0);
    textSize(28);
    textAlign(CENTER,CENTER);
    
    String info;
    if(isEnded){
      info = "Time's up";
    }else
      info = "Time Left : " + str(PApplet.parseInt(timeLeft_/60)) + " min (" + str(timeLeft_)+" sec )";
    text(info, width/2 ,height-28);
  }
  
  public void drawClock(){
    
    //Draw box-shape border 
    float margin = radius_/2;
    float rx = (origin_.x - radius_) - margin;
    float ry = (origin_.y - radius_) - margin;
    float rw = (origin_.x - rx) * 2;
    float rh = (origin_.y - rx) * 2;
    stroke(0);
    fill(255);
    strokeWeight(25);
    rect(rx, ry, rw, rh, 16, 16, 16, 16);
    
    //Draw shadow of top border
    noStroke();
    fill(120,120,120,100);
    rect(rx+12, ry+12, rw-24, 15);
    
    //Draw reflection of top border
    stroke(255,255,255,200);
    strokeWeight(3);
    line(rx+60, ry-12, rx+rw-60, ry-12);
    
    //Draw mark
    mark_.drawMark();
    
    //Draw arc as moving parts of clock
    //start of the arc should start with -90 degrees, and 
    //the hand of clock moves 0.1 degrees per 1 sec.  
    fill(255,0,0, 230);
    noStroke();
    arc(origin_.x, origin_.y, 2 * radius_, 2 * radius_, 
        -HALF_PI, -HALF_PI + radians(timeLeft_ * 0.1f), PIE);
   
   //Draw the hand
    fill(100,100,100,120);
    ellipse(origin_.x+2 , origin_.y+9, radius_/3+3, radius_/3);
    
     //Draw hand
    PVector hand = new PVector(origin_.x + radius_* cos(-HALF_PI + radians(timeLeft_ * 0.1f)),
                               origin_.y + radius_* sin(-HALF_PI + radians(timeLeft_ * 0.1f))).lerp(origin_, 0.75f);
    stroke(100,100,100,120);
    strokeWeight(10);
    line(origin_.x +5, origin_.y+10, hand.x+3, hand.y  +10);
    
    stroke(255);
    strokeCap(ROUND);
    strokeWeight(15);
    line(origin_.x, origin_.y, hand.x, hand.y);
    noStroke();
    
    fill(255);
    ellipse(origin_.x, origin_.y, radius_/3, radius_/3);
             
    //Draw the case reflection effect
    noStroke();
    fill(200,200,200,90);
    rect(rx,ry,rw,rh,16,16,16,16);
  }
  
  public void changeTime(){
    resetTimer(wantedMin_ * 60);    
  }
   
}
class Mark{
  
  PVector positions[];
  PVector origin_;
  Clock clock_;

  public Mark(Clock clock, PVector origin, float radius){
    clock_ = clock;
    origin_ = origin;
    positions = new PVector[60];
    
    //Set every minites' position on the circle
    for(int i=0; i<positions.length; i++){  
      positions[i] =  new PVector(origin_.x + radius * cos(radians(6*i) - HALF_PI),
                                  origin_.y + radius * sin(radians(6*i) - HALF_PI));
    }
  }
  public void drawMark(){
    //Draw minutes' mark lines and dials
    float portion;
    int weight;
    
    for(int min = 0; min < positions.length; min++){
       portion = 0.9f;
       weight = 1;
      
      if((min%5) == 0){  
         drawDial(min, PVector.lerp(origin_, positions[min], 1.20f));
         portion = 0.8f;
         weight = 2;
      }
    
      PVector begin = PVector.lerp(origin_, positions[min], portion);
      PVector end   = PVector.lerp(origin_, positions[min], 1.05f); 
      stroke(0);
      strokeWeight(weight);
      line(begin.x, begin.y, end.x, end.y);  
    }
    
  }
  
  private void drawDial(int min, PVector pos){
    int textSize = 30;
    fill(0);
    textFont(dialFont);
    textSize(textSize);
    textAlign(CENTER, CENTER);
    text(str(min), pos.x, pos.y);
    textFont(defaultFont);
    
    //When mouse is over this dial, draw an circle on it, and set the min to clock.
    if(mouseX >= pos.x-textSize/2 && mouseX <pos.x+textSize &&
       mouseY >  pos.y-textSize/2 && mouseY < pos.y+textSize){   
      if(min == 0) 
        min = 60;  
      clock.setWantedMin(min);
      
      noStroke();
      fill(255,0,0,30);
      ellipse(pos.x, pos.y, textSize, textSize);
    }
  
 } 
 
}
  public void settings() {  size(512, 512); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "OneHour" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
