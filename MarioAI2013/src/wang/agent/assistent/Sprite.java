package wang.agent.assistent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;







import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.MarioVisualComponent;



public class Sprite implements Cloneable
{
	public static final int KIND_NONE = 0;
	public static final int KIND_MARIO = -31;
	public static final int KIND_GOOMBA = 80;
	public static final int KIND_GOOMBA_WINGED = 95;
	public static final int KIND_RED_KOOPA = 82;
	public static final int KIND_RED_KOOPA_WINGED = 97;
	public static final int KIND_GREEN_KOOPA = 81;
	public static final int KIND_GREEN_KOOPA_WINGED = 96;
	public static final int KIND_BULLET_BILL = 84;
	public static final int KIND_SPIKY = 93;
	public static final int KIND_SPIKY_WINGED = 99;
//	    public static final int KIND_ENEMY_FLOWER = 11;
	public static final int KIND_ENEMY_FLOWER = 91;
	public static final int KIND_WAVE_GOOMBA = 98; // TODO: !H!: same
	public static final int KIND_SHELL = 13;
	public static final int KIND_MUSHROOM = 2;
	public static final int KIND_GREEN_MUSHROOM = 9;
	public static final int KIND_PRINCESS = 49;
	public static final int KIND_FIRE_FLOWER = 3;
	public static final int KIND_PARTICLE = 21;
	public static final int KIND_SPARCLE = 22;
	public static final int KIND_COIN_ANIM = 1;
	public static final int KIND_FIREBALL = 25;

	public static final int KIND_UNDEF = -42;

	public static SpriteContext spriteContext;
	public byte kind = KIND_UNDEF;

	protected static float GROUND_INERTIA = 0.89f;
	protected static float AIR_INERTIA = 0.89f;

	public float xOld, yOld, x, y, xa, ya;
	
	public float lastAccurateX, lastAccurateY;
    // if we've never seen the enemy jump/fall, we don't know how fast it'll fall. 
    public boolean unknownYA = true; 
	
	public int mapX, mapY;

	public int xPic, yPic;
	public int wPic = 32;
	public int hPic = 32;
	public int xPicO, yPicO;
	public boolean xFlipPic = false;
	public boolean yFlipPic = false;
	public Image[][] sheet;
	public Image[][] prevSheet;

	public boolean visible = true;

	public int layer = 1;
	
	public LevelScene world;

	public SpriteTemplate spriteTemplate;
	
	public Object clone() throws CloneNotSupportedException
    {
    	Sprite s = (Sprite) super.clone();
    	if (spriteTemplate != null)
    		s.spriteTemplate = (SpriteTemplate) this.spriteTemplate.clone();
    	return s;
    	
    	
    }

	public static void setCreaturesGravity(final float creaturesGravity)
	{
	    Sprite.creaturesGravity = creaturesGravity;
	}

	public static void setCreaturesWind(final float wind)
	{
	    Sprite.windCoeff = wind;
	}

	public static void setCreaturesIce(final float ice)
	{
	    Sprite.iceCoeff = ice;
	}

	protected static float creaturesGravity;
	protected static float windCoeff = 0;
	protected static float iceCoeff = 0;

	public static String getNameByKind(final int kind)
	{
	    switch (kind)
	    {
	        case Sprite.KIND_MARIO:
	            return "Mario";
	        case Sprite.KIND_GOOMBA:
	            return "Goomba";
	        case Sprite.KIND_GOOMBA_WINGED:
	            return "Goomba Winged";
	        case Sprite.KIND_RED_KOOPA:
	            return "Red Koopa";
	        case Sprite.KIND_RED_KOOPA_WINGED:
	            return "Red Koopa Winged";
	        case Sprite.KIND_GREEN_KOOPA:
	            return "Green Koopa";
	        case Sprite.KIND_GREEN_KOOPA_WINGED:
	            return "Green Koopa Winged";
	        case Sprite.KIND_SPIKY:
	            return "Spiky";
	        case Sprite.KIND_SPIKY_WINGED:
	            return "Spiky Winged";
	        case Sprite.KIND_BULLET_BILL:
	            return "Bullet";
	        case Sprite.KIND_ENEMY_FLOWER:
	            return "Flower";
	        case Sprite.KIND_SHELL:
	            return "Shell";
	        case Sprite.KIND_MUSHROOM:
	            return "Mushroom";
	        case Sprite.KIND_FIRE_FLOWER:
	            return "Power up Flower";
	        case Sprite.KIND_GREEN_MUSHROOM:
	            return "Green mushroom";
	        /*case Sprite.KIND_PRINCESS:
	            return "Princess";*/
	    }

	    return "Unknown";
	}

	public float iceScale(final float ice)
	{
	    return ice;
	}

	public float windScale(final float wind, int facing)
	{
	    return facing == 1 ? wind : -wind;
	}

	public void move()
	{
		System.out.println("sprite move");
	    x += xa;
	    y += ya;
	}

	public void render(final Graphics og)
	{
	    if (!visible) return;

//	        int xPixel = (int)(xOld+(x-xOld)*cameraOffSet)-xPicO;
//	        int yPixel = (int)(yOld+(y-yOld)*cameraOffSet)-yPicO;

	    int xPixel = (int) x - xPicO;
	    int yPixel = (int) y - yPicO;

//	        System.out.print("xPic = " + xPic);
//	        System.out.print(", yPic = " + yPic);
//	        System.out.println(", kind = " + this.kind);

	    try
	    {
	        og.drawImage(sheet[xPic][yPic],
	                xPixel + (xFlipPic ? wPic : 0),
	                yPixel + (yFlipPic ? hPic : 0),
	                xFlipPic ? -wPic : wPic,
	                yFlipPic ? -hPic : hPic, null);
	    } catch (ArrayIndexOutOfBoundsException ex)
	    {
//	        System.err.println("ok:" + this.kind + ", " + xPic);
	    }
	    // Labels
	    if (GlobalOptions.areLabels)
	        og.drawString("" + xPixel + "," + yPixel, xPixel, yPixel);

	    // Mario Grid Visualization Enable
	    if (GlobalOptions.isShowReceptiveField)
	    {
	        if (this.kind == KIND_MARIO)
	        {
//	                og.drawString("M", (int) x, (int) y);
	            og.drawString("Matrix View", xPixel - 40, yPixel - 20);
	            int width = GlobalOptions.receptiveFieldWidth;// * 16;
	            int height = GlobalOptions.receptiveFieldHeight;// * 16;

	            int rows = GlobalOptions.receptiveFieldHeight;
	            int columns = GlobalOptions.receptiveFieldWidth;

	            int marioCol = GlobalOptions.marioEgoCol;
	            int marioRow = GlobalOptions.marioEgoRow;

	            int htOfRow = 16;//height / (columns);
	            int k;
	            // horizontal lines
	            og.setColor(Color.BLACK);
	            for (k = -marioRow - 1 /*-rows / 2 - 1*/; k < rows - marioRow/*rows / 2*/; k++)
	                og.drawLine((int) x - marioCol * htOfRow - 8/*width / 2*/, (int) (y + k * htOfRow), (int) x + (columns - marioCol) * htOfRow - 8 /*(x + width / 2)*/, (int) (y + k * htOfRow));

//	                og.setColor(Color.RED);
	            // vertical lines
	            int wdOfRow = 16;// length / (rows);
	            for (k = -marioCol - 1 /*-columns / 2 - 1*/; k < columns - marioCol /*columns / 2 + 1*/; k++)
	                og.drawLine((int) (x + k * wdOfRow + 8), (int) y - marioRow * htOfRow - 16/*height / 2 - 8*/, (int) (x + k * wdOfRow + 8), (int) y + (height - marioRow) * htOfRow - 16 /*(y + height / 2 - 8)*/);
	        }
	        og.setColor(Color.GREEN);
	        MarioVisualComponent.drawString(og, String.valueOf(this.kind), (int) x - 4, (int) y - 8, 2);
	    }
	}

	public final void tick()
	{
		//System.out.println("sprite tick");
	    xOld = x;
	    yOld = y;
	    move();
	    mapY = (int) (y / 16);
	    mapX = (int) (x / 16);
	}

	public final void tickNoMove()
	{
	    xOld = x;
	    yOld = y;
	}

//	    public float getX(float alpha)
//	    {
//	        return (xOld+(x-xOld)*alpha)-xPicO;
//	    }
	//
//	    public float getY(float alpha)
//	    {
//	        return (yOld+(y-yOld)*alpha)-yPicO;
//	    }

	public void collideCheck()
	{
		//System.out.println("collidecheck");
	}

	public void bumpCheck(final int xTile, final int yTile)
	{
	}

	public boolean shellCollideCheck(final Shell shell)
	{
	    return false;
	}

	public void release(final Mario mario)
	{
	}

	public boolean fireballCollideCheck(Fireball fireball)
	{
	    return false;
	}

	public boolean isDead()
	{
	    return spriteTemplate != null && spriteTemplate.isDead;
	}
}
