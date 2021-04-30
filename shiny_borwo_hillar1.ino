
const int led1R = 4;
const int led2G = 5;
const int led3Y = 6;
const int PIR = 7;
const int RButton = 8;
const int LButton = 9;
int time = 0;
String codeString = "";
int state = 1; //Start in LOCKED

boolean code = false;

void setup()
{
   Serial.begin(9600);
  pinMode(RButton, INPUT);
  pinMode(LButton, INPUT);
  pinMode(led1R, OUTPUT);
  pinMode(led2G, OUTPUT);
  pinMode(led3Y, OUTPUT);
  pinMode(PIR, INPUT);


}

void loop()
{
  
  int pirActive = digitalRead(PIR);
    bool LButtonPressed = false;
    bool RButtonPressed = false;
  if(pirActive == LOW && state != 4) 
    state = 1;
  if(pirActive == HIGH)
    state = 2;
 
   if(state == 4) {
     while(time<10) {
        
    
    digitalWrite(led3Y, LOW);
    digitalWrite(led1R, HIGH);
    digitalWrite(led2G, LOW);
    delay(100);
    digitalWrite(led1R, LOW);
	delay(100);
    time++;
     }
     time = 0;
     state = 1;
   
  }
  
  if(state == 1) {
    
    digitalWrite(led3Y, LOW);
    digitalWrite(led1R, HIGH);
    digitalWrite(led2G, LOW);
    delay(250);
  }
  
  if(state == 2) {
    digitalWrite(led3Y, HIGH);
    digitalWrite(led1R, LOW);
    digitalWrite(led2G, LOW);
    codeString = "";
    state = 4;
 
    
      while(time<20)  //Loop that checks button inputs and adds them to the codeString
    {
      int LButtonState = digitalRead(LButton);
      int RButtonState = digitalRead(RButton);

      if(LButtonState == 1) 
        LButtonPressed = true;
      
      
      
      if(RButtonState == 1) 
      	RButtonPressed = true; 		
      
     	
       	Serial.println(RButtonState);
      	

     delay(100);
     time++;
  	}
    
  

  }
 
    if(LButtonPressed && RButtonPressed)
     state = 3;
  
   if(state == 3) {
 	digitalWrite(led3Y, LOW);
    digitalWrite(led1R, LOW);
    digitalWrite(led2G, HIGH);
     
    delay(10000); //Unlocked for 10seconds.
  }


delay(500);
time = 0;
  
}
