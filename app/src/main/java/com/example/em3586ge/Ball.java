package com.example.em3586ge;

import android.widget.FrameLayout;

public class Ball {
    private final float[] acceleration;
    private boolean leaningX = false;
    private boolean leaningY = false;
    private boolean hasNewCollision = false;
    private int xMax, yMax = 0;


    private final float[] position;
    private final float[] velocity;
    private final FrameLayout container;


    public Ball(FrameLayout container){
        this.acceleration = new float[2];
        this.velocity = new float[2];
        this.position = new float[2];
        this.container = container;
    }

    // Updates the ball's attributes
    public void update() {
        if (xMax == 0 || yMax == 0){
        xMax = container.getWidth() - 50;
        yMax = container.getHeight() - 50;
        }

        float fric = 0.2f;
        float maxVel = 50f;

        //Remove inaccuracy from sensor by having a minimum acceleration
        if(-0.1 < this.acceleration[0] && this.acceleration[0] < 0.1){this.acceleration[0] = 0;}
        if(-0.1 < this.acceleration[1] && this.acceleration[1] < 0.1){this.acceleration[1] = 0;}


        // Apply acceleration to ball
        float frameTime = 0.666f;
        this.velocity[0] += (this.acceleration[0]* frameTime);
        this.velocity[1] += (this.acceleration[1]* frameTime);

        //Calc distance travelled in that time
        float xS = (this.velocity[0]/2)* frameTime;
        float yS = (this.velocity[1]/2)* frameTime;

        applyFrictionToBall(fric);
        checkSpeedLim(maxVel);
        checkCollision(xS, yS);

    }

    // Applies friction to ball to make the motion look more natural
    private void applyFrictionToBall(float fric) {
        if (this.velocity[0] > 0){
            if(this.velocity[0] - fric <= 0){
                this.velocity[0] = 0;
            }else{
                this.velocity[0] -= fric;
            }
        }else{
            if(this.velocity[0] + fric >= 0){
                this.velocity[0] = 0;
            }else{
                this.velocity[0] += fric;
            }
        }

        if (this.velocity[1] > 0){
            if(this.velocity[1] - fric <= 0){
                this.velocity[1] = 0;
            }else{
                this.velocity[1] -= fric;
            }
        }else{
            if(this.velocity[1] + fric >= 0){
                this.velocity[1] = 0;
            }else{
                this.velocity[1] += fric;
            }
        }
    }

    // Makes sure that the ball isn't moving too fast
    private void checkSpeedLim(float maxVel) {
        if (this.velocity[0] < -maxVel){
            this.velocity[0] = -maxVel;
        }else if(this.velocity[0] > maxVel){
            this.velocity[0] = maxVel;
        }

        if (this.velocity[1] < -maxVel){
            this.velocity[1] = -maxVel;
        }else if(this.velocity[1] > maxVel){
            this.velocity[1] = maxVel;
        }
    }

    // Checks if the ball is colliding with a border
    private void checkCollision(float xS, float yS) {
        this.position[0] += xS;
        this.position[1] += yS;

        if (this.position[0] > xMax) {
            this.position[0] = xMax;
            this.velocity[0] = -this.velocity[0]/1.5f;
            if(!leaningX){
                hasNewCollision = true;
            }
            leaningX = true;
        } else if (this.position[0] <= 0) {
            this.position[0] = 0;
            this.velocity[0] = -this.velocity[0]/1.5f;
            if(!leaningX){
                hasNewCollision = true;
            }
            leaningX = true;
        }else{
            leaningX = false;
        }
        if (this.position[1] > yMax) {
            this.position[1] = yMax;
            this.velocity[1] = -this.velocity[1]/1.5f;
            if(!leaningY){
                hasNewCollision = true;
            }
            leaningY = true;
        } else if (this.position[1] <= 0) {
            this.position[1] = 0;
            this.velocity[1] = -this.velocity[1]/1.5f;
            if(!leaningY){
                hasNewCollision = true;
            }
            leaningY = true;
        } else{
            leaningY = false;
        }
    }

    public boolean hasNewCollision(){
        if(this.hasNewCollision){
            this.hasNewCollision = false;
            return true;
        }
        return false;
    }

    public void setAcceleration(float dX, float dY){
        this.acceleration[0] = dX;
        this.acceleration[1] = dY;
    }

    public float getX(){
        return this.position[0];
    }

    public float getY(){
        return this.position[1];
    }



}
