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
    resetTimer(float(min*60));
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
      info = "Time Left : " + str(int(timeLeft_/60)) + " min (" + str(timeLeft_)+" sec )";
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
        -HALF_PI, -HALF_PI + radians(timeLeft_ * 0.1), PIE);
   
   //Draw the hand
    fill(100,100,100,120);
    ellipse(origin_.x+2 , origin_.y+9, radius_/3+3, radius_/3);
    
     //Draw hand
    PVector hand = new PVector(origin_.x + radius_* cos(-HALF_PI + radians(timeLeft_ * 0.1)),
                               origin_.y + radius_* sin(-HALF_PI + radians(timeLeft_ * 0.1))).lerp(origin_, 0.75f);
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
