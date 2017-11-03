package wang.agent.assistent;

import java.awt.Graphics;
import ch.idsia.benchmark.mario.engine.GlobalOptions;





public class Enemy extends Sprite {
	
	public static final int IN_FILE_POS_RED_KOOPA = 0;
	public static final int IN_FILE_POS_GREEN_KOOPA = 1;
	public static final int IN_FILE_POS_GOOMBA = 2;
	public static final int IN_FILE_POS_SPIKY = 3;
	public static final int IN_FILE_POS_FLOWER = 4;
	public static final int POSITION_WAVE_GOOMBA = 7;

	public float runTime;
	public boolean onGround = false;
//	    private boolean mayJump = false;
//	    private int jumpTime = 0;
//	    private float xJumpSpeed;
//	    private float yJumpSpeed;

	int width = 4;
	int height = 24;

	public float yaa = 1;

	//private LevelScene levelScene;
	public int facing;
	public int deadTime = 0;
	public boolean flyDeath = false;

	public boolean avoidCliffs = true;

	public boolean winged = true;
	public int wingTime = 0;

	public float yaw = 1;

	public boolean noFireballDeath;

	public Enemy(LevelScene levelScene, float x, float y, int dir, int type, boolean winged, int mapX, int mapY)
	{
	    kind = (byte) type;
	    sheet = Art.enemies;
	    this.winged = winged;

	    this.x = x;
	    this.y = y;
	    this.mapX = mapX;
	    this.mapY = mapY;

	    //this.levelScene = levelScene;
	    this.world = levelScene;
	    xPicO = 8;
	    yPicO = 31;

	    yaa = creaturesGravity * 2;
	    yaw = creaturesGravity == 1 ? 1 : 0.3f * creaturesGravity;

	    switch (type)
	    {
	        case KIND_GOOMBA:
	        case KIND_GOOMBA_WINGED:
	            yPic = IN_FILE_POS_GOOMBA;
	            break;
	        case KIND_RED_KOOPA:
	        case KIND_RED_KOOPA_WINGED:
	            yPic = IN_FILE_POS_RED_KOOPA;
	            break;
	        case KIND_GREEN_KOOPA:
	        case KIND_GREEN_KOOPA_WINGED:
	            yPic = IN_FILE_POS_GREEN_KOOPA;
	            break;
	        case KIND_SPIKY:
	        case KIND_SPIKY_WINGED:
	            yPic = IN_FILE_POS_SPIKY;
	            break;
	        case KIND_ENEMY_FLOWER:
	            yPic = IN_FILE_POS_FLOWER;
	            break;
	        case KIND_WAVE_GOOMBA:
	            yPic = POSITION_WAVE_GOOMBA;
	            break;
	    }

	    avoidCliffs = kind == KIND_RED_KOOPA;

	    noFireballDeath = (kind == KIND_SPIKY || kind == KIND_SPIKY_WINGED);
	    if (yPic > 1) height = 12;
	    facing = dir;
	    if (facing == 0) facing = 1;
	    this.wPic = 16;
	}

	public void collideCheck()
	{
	    if (deadTime != 0)
	    {
	        return;
	    }
  
	    float xMarioD = world.mario.x - x;
	    float yMarioD = world.mario.y - y;
	    
	    if (xMarioD > -width * 2 - 4 && xMarioD < width * 2 + 4)
	    { 	
	        if (yMarioD > -height && yMarioD < world.mario.height)
	        {
	            if ((kind != KIND_SPIKY && kind != KIND_SPIKY_WINGED && kind != KIND_ENEMY_FLOWER) && world.mario.ya > 0 && yMarioD <= 0 && (!world.mario.onGround || !world.mario.wasOnGround))
	            {	
	            	world.mario.stomp(this);
	            	
	                if (winged)
	                {
	                    winged = false;
	                    ya = 0;
	                } else
	                {
	                	//System.out.println(123);
	                    this.yPicO = 31 - (32 - 8);
	                    hPic = 8;
	                    if (spriteTemplate != null) spriteTemplate.isDead = true;
	                    deadTime = 10;
	                    winged = false;

	                    if (kind == KIND_RED_KOOPA || kind == KIND_RED_KOOPA_WINGED)
	                    {
	                        spriteContext.addSprite(new Shell(world, x, y, 0));
	                    } else if (kind == KIND_GREEN_KOOPA || kind == KIND_GREEN_KOOPA_WINGED)
	                    {
	                        spriteContext.addSprite(new Shell(world, x, y, 1));
	                    }
	                    ++LevelScene.killedCreaturesTotal;
	                    ++LevelScene.killedCreaturesByStomp;
	                    this.world.reward += LevelScene.KILLS;
	                    this.world.reward += LevelScene.KILLEDBYSTOMP;
	                }
	            } else
	            {
	            	world.mario.getHurt(this.kind);
	                
	            }
	        }
	    }
	}

	public void move()
	{
		//System.out.print(" wangenemy before move kind " + kind + " x " + x + " y " + y + " ya " + ya + " winged " + winged + " yaw " + yaw);
	    wingTime++;
	    if (deadTime > 0)
	    {
	        deadTime--;

	        if (deadTime == 0)
	        {
	            deadTime = 1;
	            for (int i = 0; i < 8; i++)
	            {
	                this.world.addSprite(new Sparkle((int) (x + Math.random() * 16 - 8) + 4, (int) (y - Math.random() * 8) + 4, (float) (Math.random() * 2 - 1), (float) Math.random() * -1, 0, 1, 5));
	            }
	            spriteContext.removeSprite(this);
	        }

	        if (flyDeath)
	        {
	            x += xa;
	            y += ya;
	            ya *= 0.95;
	            ya += 1;
	        }
	        return;
	    }

	    float sideWaysSpeed = 1.75f;
	    //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

	    if (xa > 2)
	        facing = 1;
	    else if (xa < -2)
	        facing = -1;

	    xa = facing * sideWaysSpeed;
//	    xa += facing == 1 ? -wind : wind;
//	        mayJump = (onGround);

	    xFlipPic = facing == -1;

	    runTime += (Math.abs(xa)) + 5;

	    int runFrame = ((int) (runTime / 20)) % 2;

	    if (!onGround)
	    {
	        runFrame = 1;
	    }
	    
	    //System.out.println("enemy xa " + xa);

	    if (!move(xa, 0)) facing = -facing;
	    onGround = false;
	    if (wang.agent.astaragent.logout ==1 && this.kind == 97)
	    	System.out.println("wangenemy before move kind  97 " + kind + " x " + x + " y " + y + " xa " + xa + " ya " + ya + " yaa " + yaa + " yaw " + yaw);
	    move(0, ya);
	    //if (this.kind == 97)
	    //	System.out.println("wangenemy kind == 97 " + " y " + y + " ya " + ya);

	    ya *= winged ? 0.95f : 0.85f;
	    if (onGround)
	    {
	        xa *= (GROUND_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
	    } else
	    {
	        xa *= (AIR_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
	    }

	    if (!onGround)
	    {
	        if (winged)
	        {
	            ya += 0.6f * yaw;
	        } else
	        {
	            ya += yaa;
	        }
	    } else if (winged)
	    {
	        ya = -10;
	    }

	    if (winged) runFrame = wingTime / 4 % 2;

	    xPic = runFrame;
	    
	    //if (this.kind == 97)
	    //	System.out.println("wangenemy  move kind == 97 " + " y " + y + " ya " + ya + " yaa " + yaa + " yaw " + yaw);
	    //System.out.println("wangenemy after move kind " + kind + " x " + x + " y " + y + " winged " + winged + " yaw " + yaw);
	}

	public boolean move(float xa, float ya)
	{

	    while (xa > 8)
	    {
	        if (!move(8, 0)) return false;
	        xa -= 8;
	    }
	    while (xa < -8)
	    {
	        if (!move(-8, 0)) return false;
	        xa += 8;
	    }
	    while (ya > 8)
	    {
	        if (!move(0, 8)) return false;
	        ya -= 8;
	    }
	    while (ya < -8)
	    {
	        if (!move(0, -8)) return false;
	        ya += 8;
	    }


	    boolean collide = false;
	    if (ya > 0)
	    {
	        if (isBlocking(x + xa - width, y + ya, xa, 0)) collide = true;
	        else if (isBlocking(x + xa + width, y + ya, xa, 0)) collide = true;
	        else if (isBlocking(x + xa - width, y + ya + 1, xa, ya)) collide = true;
	        else if (isBlocking(x + xa + width, y + ya + 1, xa, ya)) collide = true;
	    }
	    if (ya < 0)
	    {
	        if (isBlocking(x + xa, y + ya - height, xa, ya)) collide = true;
	        else if (collide || isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
	        else if (collide || isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
	    }
	    if (xa > 0)
	    {
	        if (isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
	        if (isBlocking(x + xa + width, y + ya - height / 2, xa, ya)) collide = true;
	        if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;

	        if (avoidCliffs && onGround && !world.level.isBlocking((int) ((x + xa + width) / 16), (int) ((y) / 16 + 1), xa, 1))
	            collide = true;
	    }
	    if (xa < 0)
	    {
	        if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
	        if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
	        if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;

	        if (avoidCliffs && onGround && !world.level.isBlocking((int) ((x + xa - width) / 16), (int) ((y) / 16 + 1), xa, 1))
	            collide = true;
	    }

	    if (collide)
	    {
	        if (xa < 0)
	        {
	            x = (int) ((x - width) / 16) * 16 + width;
	            this.xa = 0;
	        }
	        if (xa > 0)
	        {
	            x = (int) ((x + width) / 16 + 1) * 16 - width - 1;
	            this.xa = 0;
	        }
	        if (ya < 0)
	        {
	            y = (int) ((y - height) / 16) * 16 + height;
//	                jumpTime = 0;
	            this.ya = 0;
	        }
	        if (ya > 0)
	        {
	            y = (int) (y / 16 + 1) * 16 - 1;
	            onGround = true;
	        }
	        return false;
	    } else
	    {
	        if (GlobalOptions.areFrozenCreatures)
	            return true;

	        x += xa;
	        y += ya;
	        return true;
	    }
	}

	private boolean isBlocking(float _x, float _y, float xa, float ya)
	{
	    int x = (int) (_x / 16);
	    int y = (int) (_y / 16);
	    if (x == (int) (this.x / 16) && y == (int) (this.y / 16)) return false;

	    boolean blocking = world.level.isBlocking(x, y, xa, ya);

//	        byte block = levelScene.level.getBlock(x, y);

	    return blocking;
	}

	public boolean shellCollideCheck(Shell shell)
	{
	    if (deadTime != 0) return false;

	    float xD = shell.x - x;
	    float yD = shell.y - y;

	    if (xD > -16 && xD < 16)
	    {
	        if (yD > -height && yD < shell.height)
	        {
	            xa = shell.facing * 2;
	            ya = -5;
	            flyDeath = true;
	            if (spriteTemplate != null) spriteTemplate.isDead = true;
	            deadTime = 100;
	            winged = false;
	            hPic = -hPic;
	            yPicO = -yPicO + 16;
//	                System.out.println("shellCollideCheck");
	            ++LevelScene.killedCreaturesTotal;
	            ++LevelScene.killedCreaturesByShell;
	            world.reward += LevelScene.KILLEDBYSHELL;
	            world.reward += LevelScene.KILLS;
	            return true;
	        }
	    }
	    return false;
	}

	public boolean fireballCollideCheck(Fireball fireball)
	{
	    if (deadTime != 0) return false;

	    float xD = fireball.x - x;
	    float yD = fireball.y - y;

	    if (xD > -16 && xD < 16)
	    {
	        if (yD > -height && yD < fireball.height)
	        {
	            if (noFireballDeath) return true;

	            xa = fireball.facing * 2;
	            ya = -5;
	            flyDeath = true;
	            if (spriteTemplate != null) spriteTemplate.isDead = true;
	            deadTime = 100;
	            winged = false;
	            hPic = -hPic;
	            yPicO = -yPicO + 16;
//	                System.out.println("fireballCollideCheck");
	            ++LevelScene.killedCreaturesTotal;
	            ++LevelScene.killedCreaturesByFireBall;
	            this.world.reward += LevelScene.KILLEDBYFIRE;
	            this.world.reward += LevelScene.KILLS;
	            return true;
	        }
	    }
	    return false;
	}

	public void bumpCheck(int xTile, int yTile)
	{
	    if (deadTime != 0) return;

	    if (x + width > xTile * 16 && x - width < xTile * 16 + 16 && yTile == (int) ((y - 1) / 16))
	    {
	        xa = -world.mario.facing * 2;
	        ya = -5;
	        flyDeath = true;
	        if (spriteTemplate != null) spriteTemplate.isDead = true;
	        deadTime = 100;
	        winged = false;
	        hPic = -hPic;
	        yPicO = -yPicO + 16;
//	            System.out.println("bumpCheck: mostelikely shell killed other creature");
	    }
	}

	public void render(Graphics og)
	{
	    if (winged)
	    {
	        int xPixel = (int) (xOld + (x - xOld)) - xPicO;
	        int yPixel = (int) (yOld + (y - yOld)) - yPicO;

	        if (kind == KIND_GREEN_KOOPA ||
	                kind == KIND_RED_KOOPA ||
	                kind == KIND_GREEN_KOOPA_WINGED ||
	                kind == KIND_RED_KOOPA_WINGED)
	        {
	        } else
	        {
	            xFlipPic = !xFlipPic;
	            og.drawImage(sheet[wingTime / 4 % 2][4], xPixel + (xFlipPic ? wPic : 0) + (xFlipPic ? 10 : -10), yPixel + (yFlipPic ? hPic : 0) - 8, xFlipPic ? -wPic : wPic, yFlipPic ? -hPic : hPic, null);
	            xFlipPic = !xFlipPic;
	        }
	    }

	    super.render(og);

	    if (winged)
	    {
	        int xPixel = (int) (xOld + (x - xOld)) - xPicO;
	        int yPixel = (int) (yOld + (y - yOld)) - yPicO;

	        if (kind == KIND_GREEN_KOOPA ||
	                kind == KIND_RED_KOOPA ||
	                kind == KIND_GREEN_KOOPA_WINGED ||
	                kind == KIND_RED_KOOPA_WINGED)
	        {
	            og.drawImage(sheet[wingTime / 4 % 2][4], xPixel + (xFlipPic ? wPic : 0) + (xFlipPic ? 10 : -10), yPixel + (yFlipPic ? hPic : 0) - 10, xFlipPic ? -wPic : wPic, yFlipPic ? -hPic : hPic, null);
	        } else
	        {
	            og.drawImage(sheet[wingTime / 4 % 2][4], xPixel + (xFlipPic ? wPic : 0) + (xFlipPic ? 10 : -10), yPixel + (yFlipPic ? hPic : 0) - 8, xFlipPic ? -wPic : wPic, yFlipPic ? -hPic : hPic, null);
	        }
	    }
	}

}
