//Global vars
Clock clock;

//Fonts must be located in the "data" dir.
PFont defaultFont;
PFont dialFont;

void setup(){
  //Set window size and title
  surface.setTitle("One Hour 1.0.0");
  PImage icon = loadImage("icon.png");
  surface.setIcon(icon);
  size(512, 512);
 
  //Create fonts
  defaultFont = createFont("Arial",30);
  dialFont = createFont("Sylfaen",30);
  textFont(defaultFont);
  
  //Create clock
  clock = new Clock(55, new PVector(width/2,height/2));
}
 
void draw() {  
  //Set background color
  background(255);
  
  //Show HMS time
  printTimeHMS(hour(),minute(),second());
    
  //Update and draw the clock
  clock.update();
  clock.drawClock();
  clock.drawInfo();
}

void printTimeHMS(int h, int m, int s){
  String apm = h<12 ? "AM" : "PM";
  fill(0);
  textAlign(RIGHT,TOP);
  textSize(18);
  text(str(h) + ":" + str(m) +":" + str(s) + apm, width-10, 10);  
}

void mouseClicked(){
  if(mouseButton==LEFT){
     clock.changeTime();
  }
}

//**    toolbox    **// 
void printVector(PVector v){
   print("(" + v.x +", "+ v.y +")");
}

void printFontList(){
  PFont.list();
  String[] fontList = PFont.list();
  printArray(fontList);
}

void resetStyle(){
  //rgba 0 ~ 255
  stroke(0);
  fill(0);
  textSize(10);
  textAlign(LEFT,BASELINE);
}
