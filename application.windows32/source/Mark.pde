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
       portion = 0.9;
       weight = 1;
      
      if((min%5) == 0){  
         drawDial(min, PVector.lerp(origin_, positions[min], 1.20));
         portion = 0.8;
         weight = 2;
      }
    
      PVector begin = PVector.lerp(origin_, positions[min], portion);
      PVector end   = PVector.lerp(origin_, positions[min], 1.05); 
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
