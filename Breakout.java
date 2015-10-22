/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 60;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		setup();
		while(turns <= NTURNS) {
			moveBall();
			pause(10);
		}
		
		label.setLabel("Game Over!!");
		label.setFont("Times New Roman-24");
		add(label, (WIDTH - label.getWidth())/2, (HEIGHT - label.getAscent())/2);
	}
	
	private void setup() {
		drawBricks();
		drawPaddle();
	}
	
	private void drawBricks() {
		
		Color c;
		
		for( int i=0; i<NBRICK_ROWS; i++) {
			brickX = startingX;
			
			switch( i/2 ) {
				case 0 : c = Color.red;
					 	 break;
				case 1 : c = Color.ORANGE;
					 	 break;
				case 2 : c = Color.YELLOW;
			 		 	 break;
				case 3 : c = Color.GREEN;
			 		 	 break;
				case 4 : c = Color.CYAN;
			 		  	 break;
				default : c = Color.GRAY;
			}
			
			for( int j=0; j<NBRICKS_PER_ROW; j++) {
				brick = new GRect(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				brick.setColor(c);
				add(brick);
				brickX+=(BRICK_SEP + BRICK_WIDTH);
			}
			brickY+=(BRICK_SEP + BRICK_HEIGHT);
		}
	}
	
	private void drawPaddle() {
		paddle = new GRect(xPaddle, yPaddle, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	public void mouseMoved(MouseEvent e) {
		paddle.setLocation(e.getX(), yPaddle);
	}
	
	public void mouseClicked(MouseEvent e) {
		vx = 3; vy = -3;
		if(ball == null && turns <= NTURNS) {
			ball = new GOval(xBall, yBall, 2*BALL_RADIUS, 2*BALL_RADIUS);
			ball.setFilled(true);
			add(ball, e.getX()+PADDLE_WIDTH/2-BALL_RADIUS, yBall);
			ball.move(vx, vy);
		}
	}
	
	private void moveBall() {
		
		if (ball != null) {
			vx = rgen.nextDouble(1.0, 3.0);
			checkForCollision();
			if (ball != null) {
				ball.move(vx, vy);
			}
		}
	}
	
	private void checkForCollision() {
		if(ball.getX() > WIDTH - BALL_RADIUS) {
			bounce = -bounce;
		}
		
		if(ball.getX() < BALL_RADIUS) {
			bounce = -bounce;
		}
		
		vx = bounce * vx;
		
		if(ball.getY() < BALL_RADIUS) {
			vy = -vy;
		}
		
		
		collider = getCollidingObject();
		if( collider == paddle) {
			vy = -vy;;
		} else if(collider == null) {
		  } else {
				vy = -vy;
				remove(collider);
				bricksCount++;
				if(bricksCount == NBRICK_ROWS*NBRICKS_PER_ROW) {
					label.setLabel("Congrats!!! You have won the game.");
					label.setFont("Times New Roman-24");
					add(label, (WIDTH - label.getWidth())/2, (HEIGHT - label.getAscent())/2);
				 }
			}
		
		if(ball.getY() > HEIGHT - 2*BALL_RADIUS) {
			turns++;
			ball.setVisible(false);
			ball = null;
		}
		
	}
	
	private GObject getCollidingObject() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(getElementAt(ball.getX() + i*2*BALL_RADIUS, ball.getY() + j*2*BALL_RADIUS) != null) {
					return getElementAt(ball.getX() + i*2*BALL_RADIUS, ball.getY() + j*2*BALL_RADIUS);
				}
			}
		}
		return null;
	}
	
	// private instance variables
	
	GRect brick;
	double startingX = BRICK_SEP;
	double startingY = BRICK_Y_OFFSET;
	double brickX;
	double brickY = startingY;
	
	private GRect paddle;
	// initial x and y location of the paddle
	private double xPaddle = (WIDTH - PADDLE_WIDTH)/2;
	private double yPaddle = HEIGHT - PADDLE_Y_OFFSET -PADDLE_HEIGHT;
	
	private GOval ball;
	// initial x and y location of the ball
	private double xBall = (WIDTH - 2*BALL_RADIUS)/2;
	private double yBall = yPaddle - 2*BALL_RADIUS;
	
	private int turns = 1;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	GLabel label = new GLabel("");;
	private GObject collider;
	private int bounce = 1;
	private int bricksCount = 0;
}
