package play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import network.ConnectionToServer;
import network.LocalServer;


public class BoardMulti extends JPanel implements ActionListener, KeyListener{
	
	//game screens
	static String state;
	Timer timer;
	int cnt;
	private static boolean isHost;
	static LocalServer gameServer;
	ConnectionToServer cs;
    
    //AESTHETICS
	public double Xdim;
	public double Ydim;
    public Color bgcolor;
    public Color ccolor;
    public double fps;
    public int gameSpd=1;
    
    //Players
    public static String position="Left";
    public static ArrayList<Paddle> players;
    public boolean player2=false;
    public boolean PCplayers;
    public boolean[] isPC;
    
    //Balls
    public int ball_num = 1;
    static ArrayList<Ball> balls ;
    
    //Scores/Lives
    public static int[] playerScores;
    
    //Controls
    public int[] keys;
        
    //KeyPresses
   // public boolean paused=false;
    public boolean[] pressed=new boolean[] {false,false,false,false,false,false};

    public BoardMulti() {
    	
    	this.Xdim=1000;
    	this.Ydim=1000;
    	this.bgcolor=Color.black;
    	this.ccolor=Color.white;
    	this.fps=60;
    	BoardMulti.state="Start";
    	this.keys=new int[] {KeyEvent.VK_Q,KeyEvent.VK_A,KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_C,KeyEvent.VK_V,KeyEvent.VK_O,KeyEvent.VK_P};;

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.grabFocus();
        
        addKeyListener(this);        
        setBackground(this.bgcolor);        
        timer = new Timer((int) (1000/this.fps), this);
        
        timer.start();
                
        this.addComponentListener( new ComponentAdapter() {
            public void componentShown( ComponentEvent e ) {
                BoardMulti.this.requestFocusInWindow();
            }
        });
        
    }
    
    public BoardMulti (LocalServer gs, int x, int y, String ownPosition,int ownLives,
			String GameMode,int ball_Num,int spd,boolean powerups,
			boolean player2, int[] keys, boolean[] isPC, boolean PCplayers,
			ArrayList<InetAddress> IPs, ArrayList<Integer> positions){    	
    	
    	BoardMulti.isHost=true;
    	BoardMulti.gameServer=gs;
    	
    	//appearance
    	this.Xdim=x;
    	this.Ydim=y;
    	this.bgcolor=Color.DARK_GRAY;
    	this.ccolor=Color.white;
    	this.fps=60;
    	
    	//balls
    	this.ball_num=ball_Num;
    	this.gameSpd=spd;
    	BoardMulti.balls=new ArrayList<Ball>();
    	BoardMulti.balls.add(new Ball(this.Xdim/2, this.Ydim/2, -4*gameSpd, 8*gameSpd,2));
    	if (ball_Num>1){
    		BoardMulti.balls.add(new Ball(this.Ydim/2, Xdim/2, 6*gameSpd, 12*gameSpd,1));
    		if (ball_Num>2){    			
				BoardMulti.balls.add(new Ball(this.Ydim/2, Xdim/2, 12*gameSpd, 6*gameSpd,4));
        	}
    	}
    	
    	//paddles
    	BoardMulti.position=ownPosition;
    	this.PCplayers=PCplayers;
    	this.isPC=isPC;
    	//System.out.println(isPC[3]);

    	//players
    	BoardMulti.players=new ArrayList<Paddle>();
    	this.player2=player2;    	
    	int k=get_pos(BoardMulti.position);
    	
    	Paddle P1=this.create_paddle(k+1,ownLives);
		BoardMulti.players.add(P1);
    	
    	Paddle P2=this.create_paddle((k+1)%4+1,ownLives);
        BoardMulti.players.add(P2);   		
    	
    	Paddle P3=this.create_paddle((k+2)%4+1,ownLives);
    	BoardMulti.players.add(P3);
    	
    	Paddle P4=this.create_paddle((k+3)%4+1,ownLives);
    	BoardMulti.players.add(P4);
    	
    	for (int i=0;i<4;i++){
    		Paddle p=fetch(i+1,players);
    		if (isPC[i] && !PCplayers){
    			p.lives=0;
    		}
    	}    	
		
    	BoardMulti.playerScores=new int[] {0,0,0,0};
    	
    	//focused window
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.grabFocus();
        
        //controls
        this.keys=keys;
        addKeyListener(this);        
        setBackground(this.bgcolor);        
        timer = new Timer((int) (1000/this.fps), this);
        timer.start();
        
        this.addComponentListener( new ComponentAdapter() {
            public void componentShown( ComponentEvent e ) {
                BoardMulti.this.requestFocusInWindow();
            }
        });
        
        BoardMulti.state="Playing";
        
    }
    
    public BoardMulti (ConnectionToServer cs, int x, int y, String ownPosition,int ownLives,
			String GameMode,int ball_Num,int spd,boolean powerups,
			boolean player2, int[] keys, boolean[] isPC, boolean PCplayers,
			ArrayList<InetAddress> IPs, ArrayList<Integer> positions){    	
    	
    	BoardMulti.isHost=false;
    	this.cs=cs;
    	
    	//appearance
    	this.Xdim=x;
    	this.Ydim=y;
    	this.bgcolor=Color.black;
    	this.ccolor=Color.white;
    	this.fps=60;
    	
    	//balls
    	this.ball_num=ball_Num;
    	this.gameSpd=spd;
    	BoardMulti.balls=new ArrayList<Ball>();
    	BoardMulti.balls.add(new Ball(this.Xdim/2, this.Ydim/2, -4*gameSpd, 8*gameSpd,2));
    	if (ball_Num>1){
    		BoardMulti.balls.add(new Ball(this.Ydim/2, Xdim/2, 6*gameSpd, 12*gameSpd,1));
    		if (ball_Num>2){    			
				BoardMulti.balls.add(new Ball(this.Ydim/2, Xdim/2, 12*gameSpd, 6*gameSpd,4));
        	}
    	}
    	
    	//paddles
    	BoardMulti.position=ownPosition;
    	this.PCplayers=PCplayers;
    	this.isPC=isPC;

    	//players
    	BoardMulti.players=new ArrayList<Paddle>();
    	this.player2=player2;    	
    	int k=get_pos(BoardMulti.position);
	
    	if (isPC[k] && !PCplayers){
    		
    	}
    	else{
    		Paddle P1=this.create_paddle(k+1,ownLives);
    		BoardMulti.players.add(P1);
    	}    	
	
    	if (isPC[(k+1)%4] && !PCplayers){
    		
        }
    	else{
    		Paddle P2=this.create_paddle((k+1)%4+1,ownLives);
        	BoardMulti.players.add(P2);
    	}
    	
    	if (isPC[(k+2)%4] && !PCplayers){
    		
        }
    	else{
    		Paddle P3=this.create_paddle((k+2)%4+1,ownLives);
        	BoardMulti.players.add(P3);
    	}
    	
    	if (isPC[(k+3)%4] && !PCplayers){
    		
    	}
    	else{
    		Paddle P4=this.create_paddle((k+3)%4+1,ownLives);
        	BoardMulti.players.add(P4);
    	}
		
    	BoardMulti.playerScores=new int[] {0,0,0,0};
    	
    	//focused window
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.grabFocus();
        
        //controls
        this.keys=keys;
        addKeyListener(this);        
        setBackground(this.bgcolor);        
        timer = new Timer((int) (1000/this.fps), this);
        timer.start();
        
        this.addComponentListener( new ComponentAdapter() {
            public void componentShown( ComponentEvent e ) {
                BoardMulti.this.requestFocusInWindow();
            }
        });
        
        BoardMulti.state="Playing";
        
    }
    
    private static int get_pos(String position) {
		switch (position){
			case "Left":
				return 0;
		    case "Right":
				return 1;
			case "Top":
				return 2;
			case "Bottom":
				return 3;
			}
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		step();	
	}
    
    public void step(){
    	
    	cnt=(cnt+1)%10;
    	
    	if(BoardMulti.state.equals("Playing")){
    		
    		Paddle P1=fetch(1,players);
            if (P1!=null){
            	if (BoardMulti.position.equals("Left")){
            		P1.set_cYvel(20*gameSpd);
            		if (pressed[0]) {
                        if (P1.cYpos-P1.cYvel > P1.Ydim/2 ) {
                            P1.set_Ypos(P1.cYpos-P1.cYvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P1.cYpos+P1.cYvel+P1.Ydim/2< this.Ydim) {
                        	P1.set_Ypos(P1.cYpos+P1.cYvel);
                        }
                    }
                    if (!isHost){
                    	cs.writeToServer("Pos:1:Y:"+P1.cYpos);
                    }
                    else{
                    	gameServer.writeToAllClients("Pos:1:Y:"+P1.cYpos);
                    }
            	}
            	else if (player2 && BoardMulti.position.equals("Right")){
            		P1.set_cYvel(20*gameSpd);
            		if (pressed[3]) {
                        if (P1.cYpos-P1.cYvel > P1.Ydim/2 ) {
                            P1.set_Ypos(P1.cYpos-P1.cYvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P1.cYpos+P1.cYvel+P1.Ydim/2< this.Ydim) {
                        	P1.set_Ypos(P1.cYpos+P1.cYvel);
                        }
                    }
            	}
            	else if (PCplayers && isPC[P1.pos-1]){
            		AIplayer2 ai1 = new AIplayer2() ;
            		ai1.moveAIplayer1(P1, balls, this, 20*gameSpd);
            	}
            	else{
            		//parseMessage();
            	}
            }
            
            Paddle P2=fetch(2,players);
            if (P2!=null){
            	if (BoardMulti.position.equals("Right")){
            		P2.set_cYvel(20*gameSpd);
            		if (pressed[0]) {
                        if (P2.cYpos-P2.cYvel > P2.Ydim/2 ) {
                            P2.set_Ypos(P2.cYpos-P2.cYvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P2.cYpos+P2.cYvel+P2.Ydim/2< this.Ydim) {
                        	P2.set_Ypos(P2.cYpos+P2.cYvel);
                        }
                    }
                    if (!isHost){
                    	cs.writeToServer("Pos:2:Y:"+P2.cYpos);
                    }
                    else{
                    	gameServer.writeToAllClients("Pos:2:Y:"+P2.cYpos);
                    }
            	}  
            	else if (player2 && BoardMulti.position.equals("Left")){
            		P2.set_cYvel(20*gameSpd);
            		if (pressed[3]) {
                        if (P2.cYpos-P2.cYvel > P2.Ydim/2 ) {
                            P2.set_Ypos(P2.cYpos-P2.cYvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P2.cYpos+P2.cYvel+P2.Ydim/2< this.Ydim) {
                        	P2.set_Ypos(P2.cYpos+P2.cYvel);
                        }
                    }
            	} 
            	else if (PCplayers && isPC[P2.pos-1]){
            			AIplayer2 ai1 = new AIplayer2() ;
            			ai1.moveAIplayer(P2, balls, this, 20*gameSpd);
            	}
            	else{
            		//parseMessage();
            	}
            }
            
            Paddle P3=fetch(3,players);
            if (P3!=null){
            	if (BoardMulti.position.equals("Top")){
            		P3.set_cXvel(20*gameSpd);
            		if (pressed[0]) {
                        if (P3.cXpos-P3.cXvel > P3.Xdim/2 ) {
                            P3.set_Xpos(P3.cXpos-P3.cXvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P3.cXpos+P3.cXvel+P3.Xdim/2< this.Xdim) {
                        	P3.set_Xpos(P3.cXpos+P3.cXvel);
                        }
                    }
                    if (!isHost){
                    	cs.writeToServer("Pos:3:X:"+P3.cXpos);
                    }
                    else{
                    	gameServer.writeToAllClients("Pos:3:X:"+P3.cXpos);
                    }
            	} 
            	else if (player2 && BoardMulti.position.equals("Bottom")){
            		P3.set_cXvel(20*gameSpd);
            		if (pressed[3]) {
                        if (P3.cXpos-P3.cXvel > P3.Xdim/2 ) {
                            P3.set_Xpos(P3.cXpos-P3.cXvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P3.cXpos+P3.cXvel+P3.Xdim/2< this.Xdim) {
                        	P3.set_Xpos(P3.cXpos+P3.cXvel);
                        }
                    }
            	} 
            	else if (PCplayers && isPC[P3.pos-1]){
            		AIplayer2 ai1 = new AIplayer2() ;
            		ai1.moveAIplayer4(P3, balls, this, 20*gameSpd);
            	}
            	else{
            		//parseMessage();
            	}
            }
               
            Paddle P4=fetch(4,players);
            if (P4!=null){
            	if (BoardMulti.position.equals("Bottom")){
            		P4.set_cXvel(20*gameSpd);
            		if (pressed[0]) {
                        if (P4.cXpos-P4.cXvel > P4.Xdim/2 ) {
                            P4.set_Xpos(P4.cXpos-P4.cXvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P4.cXpos+P4.cXvel+P4.Xdim/2< this.Xdim) {
                        	P4.set_Xpos(P4.cXpos+P4.cXvel);
                        }
                    }
                    if (!isHost){
                    	cs.writeToServer("Pos:4:X:"+P4.cXpos);
                    }
                    else{
                    	gameServer.writeToAllClients("Pos:4:X:"+P4.cXpos);
                    }
            	} 
            	else if (player2 && BoardMulti.position.equals("Top")){
            		P4.set_cXvel(20*gameSpd);
            		if (pressed[3]) {
                        if (P4.cXpos-P4.cXvel > P4.Xdim/2 ) {
                            P4.set_Xpos(P4.cXpos-P4.cXvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P4.cXpos+P4.cXvel+P4.Xdim/2< this.Xdim) {
                        	P4.set_Xpos(P4.cXpos+P4.cXvel);
                        }
                    }
            	}  
            	else if (PCplayers && isPC[P4.pos-1]){
            		AIplayer2 ai1 = new AIplayer2() ;
            		ai1.moveAIplayer3(P4, balls, this, 20*gameSpd);
            	}
            	else{
            		
            	}
            }
            // parseMessage();
            if (isHost){
            	String ballInfo="play.Ball";
	            for (int i=0;i<BoardMulti.balls.size();i++){
	            	Ball b=BoardMulti.balls.get(i);
	            	analyse(b);
	            	ballInfo += ":"+b.Xpos+":"+b.Ypos ;
	            }
	            gameServer.writeToAllClients(ballInfo);
	            
            }
			repaint();
    	}

    }
    
    public void analyse(Ball b){

    	//next positions of the ball
    	double nextLeftPos = b.Xpos - b.dia;
    	double nextRightPos = b.Xpos + b.dia;
    	double nextTopPos = b.Ypos - b.dia;
    	double nextBottomPos = b.Ypos + b.dia;
    	
    	//positions of the players
    	double playerOneRight = 0;
    	double playerOneTop = 0;
    	double playerOneBottom = 0;
    	
    	Paddle P1=BoardMulti.fetch(1, players);
    	if (P1!=null){
    		playerOneRight = P1.cXpos - P1.Xdim;
    		playerOneTop = P1.cYpos-(P1.Ydim/2);
    		playerOneBottom = playerOneTop+(P1.Ydim);
    	}    	
    	
    	double playerTwoLeft = 0;
    	double playerTwoTop = 0;
    	double playerTwoBottom = 0;
    	
    	Paddle P2=BoardMulti.fetch(2, players);
    	if (P2!=null){
    		playerTwoLeft = P2.cXpos + P2.Xdim;
        	playerTwoTop = P2.cYpos - (P2.Ydim/2);
        	playerTwoBottom = playerTwoTop + (P2.Ydim);
    	}
    	
    	double playerThreeRight = 0;
		double playerThreeLeft = 0;
		double playerThreeBottom = 0;
		
		Paddle P3=BoardMulti.fetch(3, players);
		//System.out.println(P3==null);
		if (P3!=null){
			playerThreeRight = P3.cXpos+(P3.Xdim/2);
			playerThreeLeft = P3.cXpos-(P3.Xdim/2);
			playerThreeBottom = P3.cYpos - P3.Ydim;
		}
		
		double playerFourRight = 0;
    	double playerFourLeft = 0;
    	double playerFourTop = 0;
    	
    	Paddle P4=BoardMulti.fetch(4, players);
		if (P4!=null){
			playerFourRight = P4.cXpos+(P4.Xdim/2);
			playerFourLeft = P4.cXpos-(P4.Xdim/2);
			playerFourTop = P4.cYpos + P4.Ydim;
		}
    	
		//will the ball go off the left side?
		if (P1!=null && nextLeftPos < playerOneRight) { 
            //is it going to miss the paddle?
            if (b.Ypos < playerOneTop || b.Ypos > playerOneBottom) {
            	
            	if (P1.lives>0){
            		Paddle tmp=fetch(b.origin,players);
            		if (tmp!=null && tmp.lives>0){
                    	BoardMulti.playerScores[tmp.pos-1]++;
                    	gameServer.writeToAllClients("Score:"+((Integer)tmp.pos-1));
                    }
                    P1.lives--;
                    gameServer.writeToAllClients("Lives:1");
            	}
            }
            else{
            	b.origin=1;
            }            
            b.Xvel *=-1;
        }
		else if (nextLeftPos < 0){
			//b.origin=1;
            b.Xvel *= -1;
		}
        //will the ball go off the right side?
		if (P2!=null && nextRightPos > playerTwoLeft) {
            //is it going to miss the paddle?
        	if (b.Ypos < playerTwoTop || b.Ypos > playerTwoBottom) {

        		if (P2.lives>0){
        			Paddle tmp=fetch(b.origin,players);
        			if (tmp!=null && tmp.lives>0){
                    	BoardMulti.playerScores[tmp.pos-1]++;
                    	gameServer.writeToAllClients("Score:"+((Integer)tmp.pos-1));
                    }
        			P2.lives--;
        			gameServer.writeToAllClients("Lives:2");
            	}
            }
        	else{
        		b.origin=2;
        	}
        	
            b.Xvel *= -1;
        }
		else if (nextRightPos > this.Xdim){
			//b.origin=2;
            b.Xvel *= -1;
		}
      //will the ball go off the top?
        if (P3!=null && nextTopPos < playerThreeBottom) {
            //is it going to miss the paddle?
        	if (b.Xpos > playerThreeRight || b.Xpos < playerThreeLeft) {
            	
        		if (P3.lives>0){   
        			
        			Paddle tmp=fetch(b.origin,players);
            		if (tmp!=null && tmp.lives>0){
                    	BoardMulti.playerScores[tmp.pos-1]++;
                    	gameServer.writeToAllClients("Score:"+((Integer)tmp.pos-1));
                    }
                    P3.lives--;
                    gameServer.writeToAllClients("Lives:3");
            	}
            }
        	else{
        		b.origin=3;
        	}
        	
            b.Yvel *= -1;
        }
        else if (nextTopPos < 0 ) {
        	//b.origin=3;
            b.Yvel *=-1;               
        }
      //will the ball go off the bottom?
        if (P4!= null && nextBottomPos > playerFourTop) {
            //is it going to miss the paddle?
        	if (b.Xpos > playerFourRight || b.Xpos < playerFourLeft) {

        		if (P4.lives>0){
        			Paddle tmp=fetch(b.origin,players);
        			if (tmp!=null && tmp.lives>0){
                    	BoardMulti.playerScores[tmp.pos-1]++;
                    	gameServer.writeToAllClients("Score:"+((Integer)tmp.pos-1));
                    }
        			P4.lives--;
        			gameServer.writeToAllClients("Lives:4");
            	}
            }
        	else{
        		b.origin=4;
        	}
        	
            b.Yvel *= -1;
        }
		//bounce off the bottom
        else if (nextBottomPos > this.Ydim) {
			//b.origin=4;
            b.Yvel *=-1;               
        }
        
        if (zeros(BoardMulti.players,players.size()-1)){
        	BoardMulti.state = "Done";
        	gameServer.writeToAllClients("Done");
        }
    	
    	//checking for collison condition between the balls 
        boolean collisionball = false ;
        Ball temp1 = b;
        Ball temp2 = null ; 
       	double res = 0  ;
        for(int j=0; j < balls.size(); j++){
        	temp2 = balls.get(j) ;
        	res = Math.sqrt(Math.pow((temp1.Xpos-temp2.Xpos),2)+Math.pow((temp1.Ypos-temp2.Ypos),2));
        		
        	if (res < ((temp1.dia)/2+(temp2.dia)/2) && res >0 ){
        			
        		collisionball = true ; 
        		break ;
        	}
        }        
        
        //handling collision
        	
        if(collisionball && b.type==0) {
        	if (b.colid == 1 && temp2.colid == 1 ){
        		if (res <= b.colparam){
        			b.Xpos += b.Xvel;
                    b.Ypos += b.Yvel;
        		}
        		else {
        			b.colid = 0 ;
        			temp2.colid = 0 ;
        			handlecollision(balls,b) ;
        		}
        	}       	
        	
        	handlecollision(balls,b) ;
        }
        else {
        	//if (b.type== -1) {b.type = 0 ;}
        	b.Xpos += b.Xvel;
            b.Ypos += b.Yvel;
        	//System.out.println(b.Xvel+","+b.Yvel);
        }
    }
    
    public void handlecollision(ArrayList<Ball> b,Ball b1){
    	Ball temp2 = null ;  
    	Ball temp1 = b1 ;
    	for(int j=0;j<balls.size();j++){
        		temp2 = balls.get(j) ;
        		double res = Math.sqrt(Math.pow((temp1.Xpos-temp2.Xpos),2)+Math.pow((temp1.Ypos-temp2.Ypos),2));
        		if (res < ((temp1.dia)/2+(temp2.dia)/2) && res > 0){
        			Ball.ballcollison(temp1,temp2);
        			b1.Xpos +=(temp1.Xvel);
        			b1.Ypos +=(temp1.Yvel);
        			b1.colparam = res ;
        			//b1.ballcollision = true ;
        			temp2.type = -1 ;
        			b1.colid = 1 ;
        			temp2.colid = 1 ;
        			temp2.colparam = res ;
        			 //b1.type = -1 ;
        			//temp2.Xpos += (temp2.Xvel);
        			//temp2.Ypos += (temp2.Yvel);
        			break;
        		}
    	}
    	
    }
    
  //paint the game screen
    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        g.setColor(ccolor);

        switch(state){
        		
			case "Playing":
				
				//draw "goal lines" on each side
	            g.drawLine((int)(5+Xdim/100), 0,(int) (5+Xdim/100),(int) (this.Ydim));
	            g.drawLine((int)(Xdim-(5+Xdim/100)), 0,(int) (Xdim-(5+Xdim/100)),(int) (this.Ydim));
				g.drawLine(0, (int)(5+this.Ydim/100), (int) (Xdim), (int)(5+this.Ydim/100));
				g.drawLine(0, (int)(this.Ydim-(5+this.Ydim/100)), (int) (Xdim), (int)(this.Ydim-(5+this.Ydim/100)));
				
				//draw paddles
				
				for (int i=0;i<BoardMulti.players.size();i++){
					Paddle b=BoardMulti.players.get(i);
					if (b.lives>0) {
						//System.out.println(b.pos);
						g.fillRect((int)(b.cXpos-b.Xdim/2),(int)(b.cYpos-b.Ydim/2),(int) (b.Xdim), (int) (b.Ydim));
					}
				}
				
				//draw balls
				for (int i=0;i<BoardMulti.balls.size();i++){
					Ball b=BoardMulti.balls.get(i);
					g.fillOval((int)(b.Xpos-b.dia/2),(int)(b.Ypos-b.dia/2),(int) (b.dia), (int) (b.dia));
				}
				
				//draw the scores
	            g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
	            Paddle P1=fetch(1,players);
	            if (P1!=null && P1.lives>0){
	            	g.drawString(String.valueOf(BoardMulti.playerScores[0]), (int)(this.Ydim/2-200), (int) (Xdim/2-100));
	 	            g.drawString(String.valueOf(P1.lives), (int)(this.Ydim/2-200), (int) (Xdim/2+100));
	 	        }
	            
	            Paddle P2=fetch(2,players);
	            if (P2!=null && P2.lives>0){
	            	g.drawString(String.valueOf(BoardMulti.playerScores[1]), (int)(this.Ydim/2+200), (int) (Xdim/2-100));
	            	g.drawString(String.valueOf(P2.lives), (int)(this.Ydim/2+200), (int) (Xdim/2+100));
	            }
	            
	            Paddle P3=fetch(3,players);
	            if (P3!=null && P3.lives>0){
	            	g.drawString(String.valueOf(BoardMulti.playerScores[2]), (int)(this.Ydim/2-100), (int) (Xdim/2-200));
			        g.drawString(String.valueOf(P3.lives), (int)(this.Ydim/2+100), (int) (Xdim/2-200));
			    }
	            
	            Paddle P4=fetch(4,players);
	            if (P4!=null && P4.lives>0){
	            	g.drawString(String.valueOf(BoardMulti.playerScores[3]), (int)(this.Ydim/2-100), (int) (Xdim/2+200));
				    g.drawString(String.valueOf(P4.lives), (int)(this.Ydim/2+100), (int) (Xdim/2+200));
		        }
	            
				break;
				
			case "Done":
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
				
				int winner=0;
				
				for (int i=0;i<players.size();i++){
					Paddle sc=BoardMulti.players.get(i);
					if (sc.lives>0){
						winner=sc.pos;
					}
				}				
				g.drawString("Player "+(winner)+" won!", (int)Xdim/2-100, (int)this.Ydim/2-200);
                g.drawString("Press 'Backspace' to play again", (int)Xdim/2-160, (int)this.Ydim/2);
				break;
        }
        
    }
    
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void keyPressed(KeyEvent e) {
		switch(state){
				
			case "Playing":				
				if (e.getKeyCode() == keys[0]) {
	                pressed[0] = true;
	            }
	            else if (e.getKeyCode() == keys[1]) {
	                pressed[1] = true;
	            }
	            else if (e.getKeyCode() == keys[2]) {
	                pressed[2] = true;
	            }
	            else if (e.getKeyCode() == keys[3]) {
	                pressed[3] = true;
	            }
	            else if (e.getKeyCode() == keys[4]) {
	                pressed[4] = true;
	            }
	            else if (e.getKeyCode() == keys[5]) {
	                pressed[5] = true;
	            }
	            /*if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	            	play.RXCardLayout cdl=(play.RXCardLayout) getParent().getLayout();
	            	cdl.show(getParent(), "MenuPanel");
	            }*/
	            repaint();
				break;
				
			case "Done":
				/*if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					this.state="Playing";
					this.playerScores=new int[] {0,0,0,0};
					for (int i=0;i<4;i++){
						int x=this.playerLives2[i];
						this.playerLives[i]=x;
					}
					this.balls=new ArrayList<play.Ball>();
					for (int i=0;i<this.balls2.size();i++){
						play.Ball b=this.balls2.get(i);
						play.Ball bb=new play.Ball(b.Xpos, b.Ypos, b.Xvel, b.Yvel, b.origin);
						this.balls.add(bb);
					}
					this.players=new ArrayList<play.Paddle>();
					for (int i=0;i<this.players2.size();i++){
						play.Paddle b=this.players2.get(i);
						play.Paddle bb=new play.Paddle(b.Xdim, b.Ydim, b.cXpos, b.cYpos,b.pos,b.lives);
						this.players.add(bb);
					}
	            }*/
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	            	RXCardLayout cdl=(RXCardLayout) getParent().getLayout();
	            	cdl.show(getParent(), "MenuPanel");
	            }
				repaint();
				break;
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		if (state.equals("Playing")) {            
            if (e.getKeyCode() == keys[0]) {
                pressed[0] = false;
            }
            else if (e.getKeyCode() == keys[1]) {
                pressed[1] = false;
            }
            else if (e.getKeyCode() == keys[2]) {
                pressed[2] = false;
            }
            else if (e.getKeyCode() == keys[3]) {
                pressed[3] = false;
            }
            else if (e.getKeyCode() == keys[4]) {
                pressed[4] = false;
            }
            else if (e.getKeyCode() == keys[5]) {
                pressed[5] = false;
            }
        }
		
	}
	
    public boolean zeros(ArrayList<Paddle> arr, int z){
    	int cnt = 0;
    	for (int i=0;i<arr.size();i++){
    		if (arr.get(i).lives==0){
    			cnt++;
    		}
    	}
    	return cnt==z;
    }   
    
    public static Paddle fetch(int i,ArrayList<Paddle> players){
    	for (int j=0;j<players.size();j++){
    		Paddle p=players.get(j);
    		if (p.pos==i){
    			return p;
    		}
    	}
    	return null;
    }
    
    public Paddle create_paddle (int k, int j){
    	switch (k){
    		case 1:
    			return new Paddle(Xdim/100, Ydim/5, 5+Xdim/100, this.Ydim/2,1,j);
    		case 2:
    			return new Paddle(Xdim/100, Ydim/5, Xdim-(5+Xdim/100), this.Ydim/2,2,j);
    		case 3:
    			return new Paddle(Xdim/5, Ydim/100, Xdim/2, 5+this.Ydim/100,3,j);
    		case 4:
    			return new Paddle(Xdim/5, Ydim/100, Xdim/2, this.Ydim-(5+this.Ydim/100),4,j);
    	}
    	return null;
    }
    
    public static boolean parse_packet(String response){
		// System.out.println(response);
    	if (response == null)
    		return false;
    	
    	String[] tokens= response.split(":");
		Paddle p;
    	switch(tokens[0]){
    		case "Pos":
    			p = fetch(Integer.parseInt(tokens[1]), players);
    			if (tokens[2].equalsIgnoreCase("X") && get_pos(position)+1!=Integer.parseInt(tokens[1]))
    				p.cXpos = Double.parseDouble(tokens[3]);
    			else if (tokens[2].equalsIgnoreCase("Y") && get_pos(position)+1!=Integer.parseInt(tokens[1]))
    				p.cYpos = Double.parseDouble(tokens[3]);
				if (isHost)
    				gameServer.writeToAllClients(response);
    			break;

    		case "play.Ball":
    			for (int i=0; i<balls.size(); i++) {
    				Ball b = balls.get(i);
    				b.Xpos = Double.parseDouble(tokens[2*i+1]);
    				b.Ypos = Double.parseDouble(tokens[2*i+2]);
    			}
    			break;

			case "Score":
				//p = fetch(Integer.parseInt(tokens[1])+1, players);
				//System.out.println(Integer.parseInt(tokens[1]));
				playerScores[Integer.parseInt(tokens[1])]++;
				break;

			case "Lives":
				p = fetch(Integer.parseInt(tokens[1]), players);
				System.out.println(Integer.parseInt(tokens[1]));
				p.lives--;
				break;

			case "Done":
				state = "Done";
				break;
    				
    	}
    	return true;
    }
    
}
