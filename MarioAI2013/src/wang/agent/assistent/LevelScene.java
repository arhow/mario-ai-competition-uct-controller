package wang.agent.assistent;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.MarioAIOptions;




public class LevelScene implements SpriteContext, Cloneable
{

	public static final boolean[] defaultKeys = new boolean[Environment.numberOfKeys];
	public static final String[] keysStr = {"<<L ", "R>> ", "\\\\//", "JUMP", " RUN", "^UP^"};
	public static final int cellSize = 16;
	public List<Sprite> sprites = new ArrayList<Sprite>();
	private List<Sprite> spritesToAdd = new ArrayList<Sprite>();
	private List<Sprite> spritesToRemove = new ArrayList<Sprite>();
	public Level level;
	public Mario mario;
	public float xCam, yCam, xCamO, yCamO;
	public int tickCount;
	public int startTime = 0;
	private int timeLeft;
	private int width;
	private int height;
	public String memo = "";
	private Random randomGen = new Random(0);
	public List<Float> enemiesFloatsList = new ArrayList<Float>();
	public List<Float> fireFlowerFloatsList = new ArrayList<Float>();
	public List<Float> mushroomFloatsList = new ArrayList<Float>();
	public static int killedCreaturesTotal;
	public static int killedCreaturesByFireBall;
	public static int killedCreaturesByStomp;
	public static int killedCreaturesByShell;
	
	public static  int DISTANCE = 1;
	public static  int MODE = 32;
	public static  int COINS = 16;
	public static  int FLOWERFIRE = 64;
	public static  int KILLS = 42;
	public static  int KILLEDBYFIRE = 4;
	public static  int KILLEDBYSHELL = 17;
	public static  int KILLEDBYSTOMP = 12;
	public static  int MUSHROOM = 58;
	public static  int HIDDENBLOCK = 24;
	public static  int GREENMUSHROOM = 58;
	public static  int BRICKS = 2;
	public static  int DAMAGE = -1000;
	public static  int BUMP = 2;
	public static  int KICKS = 5;
	/*
	public static final int DISTANCE = 10;
	public static final int MODE = 0;
	public static final int COINS = 0;
	public static final int FLOWERFIRE = 0;
	public static final int KILLS = 0;
	public static final int KILLEDBYFIRE = 0;
	public static final int KILLEDBYSHELL = 0;
	public static final int KILLEDBYSTOMP = 0;
	public static final int MUSHROOM = 0;
	public static final int HIDDENBLOCK = 0;
	public static final int GREENMUSHROOM = 0;
	public static final int BRICKS = 0;
	public static final int DAMAGE = 0;
	public static final int BUMP = 0;
	public static final int KICKS = 0;
	*/
	public double reward;
	public double score;
	
	int debuglog = 100;
	
	// not be used *********************************
	 
	private static boolean onLadder = false;
	private float[] marioFloatPos = new float[2];
	private int[] marioState = new int[13];
	private int numberOfHiddenCoinsGained = 0;
	private int greenMushroomMode = 0;
	private Point marioInitialPos;
	private int bonusPoints = -1;
	private int timeLimit = 200;
	private long levelSeed;
	private int levelType;
	private int levelDifficulty;
	private int levelLength;
	private int levelHeight;
	
	//********************************************

//	    public int getTimeLimit() {  return timeLimit; }

	public void setTimeLimit(int timeLimit){ this.timeLimit = timeLimit; }
	
	public boolean isSame(LevelScene ls)
	{
		if (ls.mario.x != this.mario.x)
			return false;
		if (ls.mario.y != this.mario.y)
			return false;
		if (ls.mario.large != this.mario.large)
			return false;
		if (ls.mario.fire != this.mario.fire)
			return false;
		if (ls.sprites.size() != this.sprites.size())
			return false;
		

		for (int i = 0; i < this.sprites.size(); i++)
		{
			if (this.sprites.get(i).kind == Sprite.KIND_MARIO)
				continue;
			if (this.sprites.get(i).kind != ls.sprites.get(i).kind)
				return false;
			if (this.sprites.get(i).x != ls.sprites.get(i).x)
				return false;
			if (this.sprites.get(i).y != ls.sprites.get(i).y)
				return false;
		}
		
		for (int i = 0; i < this.level.map.length; i++)
		{
			for(int j = 0; j < this.level.map[0].length; j++)
			{
				if (this.level.map[i][j] != ls.level.map[i][j])
					return false;
			}
		}
		return true;
	}
	
	void resetReward()
	{
		this.reward = 0;
	}
	
	void resetScore()
	{
		this.score = 0;
	}

	
	public boolean setLevelScene(byte[][] levelPart, byte[][] levePartEx)  //added by wzx
	{

    	/*
        int HalfObsWidth = 11;
        int HalfObsHeight = 11;
        int MarioXInMap = (int)mario.x/16;
        int MarioYInMap = (int)mario.y/16;
        boolean gapAtLast = true;
        boolean gapAtSecondLast = true;
        int lastEventX = 0;
        int[] heights = new int[22];
        for(int i = 0; i < heights.length; i++)
        	heights[i] = 0;
        
        int gapBorderHeight = 0;
        int gapBorderMinusOneHeight = 0;
        int gapBorderMinusTwoHeight = 0;
        */
        
        //System.out.println("y form " + (MarioYInMap - HalfObsHeight) + " to " + (MarioYInMap + HalfObsHeight));
        //System.out.println("x from " + (MarioXInMap - HalfObsWidth) + " to " + (MarioXInMap + HalfObsWidth));
        
        int MarioXInMap = (int)mario.x/16;
        int MarioYInMap = (int)mario.y/16;
        int mCol = 9;
        int mRow = 9;
        int receptiveFieldHeight = 19;
        int receptiveFieldWidth = 19;
        
        boolean gapAtLast = true;
        boolean gapAtSecondLast = true;
        int lastEventX = 0;
        //int[] heights = new int[22];
        //for(int i = 0; i < heights.length; i++)
        //	heights[i] = 0;
        int gapBorderHeight = 0;
        int gapBorderMinusOneHeight = 0;
        int gapBorderMinusTwoHeight = 0;
        int HalfObsWidth = 9;
        int HalfObsHeight = 9;

        
        
        
        for (int y = MarioYInMap - mRow, row = 0; y <= MarioYInMap + (receptiveFieldHeight - mRow - 1); y++, row++)
        {
            for (int x = MarioXInMap - mCol, col = 0; x <= MarioXInMap + (receptiveFieldWidth - mCol - 1); x++, col++)
            {

	            	byte datum = levelPart[row][col];
	            	//byte datumEx = levePartEx[row][col];
	  
	            	if (x < 0) continue;
	            	if (y < 0) continue;
	            	if (y >= 15) continue;
	            	//level.map[x][y] = datum;
	            	level.setBlock(x, y, datum);


	            	if (debuglog == 5)
	            	{
	            		System.out.print(" " + datum);
	            	}

            }
        	if (debuglog == 5)
        		System.out.println();
        }
    	if (level.getBlock(MarioXInMap + (receptiveFieldWidth - mCol - 1), 15) == 0)
    	{
    		level.isGap[MarioXInMap + (receptiveFieldWidth - mCol - 1)] = true;
    		if (debuglog == 3)
    			System.out.println(" col "  + " marioinmap " + (MarioXInMap + (receptiveFieldWidth - mCol - 1)));
    	}
        
    	if (debuglog == 5)
    		System.out.println();

    	return true;
		
	}
	public void setSprite(byte[][] sprite,  float[]  marioFloatPos)
	{
        int MarioXInMap = (int)mario.x/16;
        int MarioYInMap = (int)mario.y/16;
        int mCol = 9;
        int mRow = 9;
        int receptiveFieldHeight = 19;
        int receptiveFieldWidth = 19;
        
        boolean gapAtLast = true;
        boolean gapAtSecondLast = true;
        int lastEventX = 0;
        //int[] heights = new int[22];
        //for(int i = 0; i < heights.length; i++)
        //	heights[i] = 0;
        int gapBorderHeight = 0;
        int gapBorderMinusOneHeight = 0;
        int gapBorderMinusTwoHeight = 0;
        int HalfObsWidth = 9;
        int HalfObsHeight = 9;
        

        
        
        for (int y = MarioYInMap - mRow, row = 0; y <= MarioYInMap + (receptiveFieldHeight - mRow - 1); y++, row++)
        {
            for (int x = MarioXInMap - mCol, col = 0; x <= MarioXInMap + (receptiveFieldWidth - mCol - 1); x++, col++)
            {
	            	byte kind = sprite[row][col];
	            	float fy = y*16+16;
	            	float fx = x*16+8;
	            	//boolean found = false;
	            	switch(kind)
	            	{
	            	case 3:
	            		/*
	                    for(Sprite tmpsprite:sprites)
	                    {
	                    	if (tmpsprite.kind == 3 && Math.abs(tmpsprite.x - fx) < 17 && Math.abs(tmpsprite.y - fy) < 17)
	                    		found = true;	
	                    }
	                    if (found == false)
	                    */
	                    	sprites.add(new FireFlower(this, (int)fx, (int)fy));
	                    	if (debuglog == 4)
	                    		System.out.println("add fireflower " + mario.x + " " + mario.y + " " +fx + " " + fy);
	                    	break;
	            	case 2:	
	            		/*
	                    for(Sprite tmpsprite:sprites)
	                    {
	                    	if (tmpsprite.kind == 2 && Math.abs(tmpsprite.x - fx) < 17 && Math.abs(tmpsprite.y - fy) < 17)
	                    		found = true;	
	                    }
	                    if (found == false)
	                    */
	                    	sprites.add(new Mushroom(this, (int)fx, (int)fy));
	                    	break;
	            	case 84:
	            		break;
	            		
	            	}
	            	

	    	        

	            	
            }

        }
	}
	
	public boolean setEnemies(float[] enemies, float[] realmariofloatpos) //added by wzx
	{

		boolean requireReplanning = false;
		
		List<Sprite> newSprites = new ArrayList<Sprite>();
		
		if (wang.agent.astaragent.logout == 1)
			System.out.print("setenemies mario x " + mario.x + " y " + mario.y);
		
		for (int i = 0; i < enemies.length; i += 3)
		{
			int kind = (int) enemies[i];
			float x = enemies[i+1] + realmariofloatpos[0];
			float y = enemies[i+2] + realmariofloatpos[1];
			
			//System.out.print(" kind " + enemies[i] + " x " + x + " y " + y );

	        int type = -1;
	        boolean winged = false;
	        
	        switch(kind)
	        {
		        case(Sprite.KIND_BULLET_BILL): type = -2; break;
		        //case(Sprite.KIND_GOOMBA): type = Enemy.ENEMY_GOOMBA; break;
		        case(Sprite.KIND_GOOMBA): type = Sprite.KIND_GOOMBA; break;
		        //case(Sprite.KIND_SHELL): type = Enemy.KIND_SHELL; break;
		        case(Sprite.KIND_SHELL): type = Sprite.KIND_SHELL; break;
		        //case(Sprite.KIND_GOOMBA_WINGED): type = Enemy.ENEMY_GOOMBA; winged = true; break;
		        case(Sprite.KIND_GOOMBA_WINGED): type = Sprite.KIND_GOOMBA_WINGED; winged = true; break;
		        //case(Sprite.KIND_GREEN_KOOPA): type = Enemy.ENEMY_GREEN_KOOPA; break;
		        case(Sprite.KIND_GREEN_KOOPA): type = Sprite.KIND_GREEN_KOOPA; break;
		        //case(Sprite.KIND_GREEN_KOOPA_WINGED): type = Enemy.ENEMY_GREEN_KOOPA; winged = true; break;
		        case(Sprite.KIND_GREEN_KOOPA_WINGED): type = Sprite.KIND_GREEN_KOOPA_WINGED; winged = true; break;
		        //case(Sprite.KIND_RED_KOOPA): type = Enemy.ENEMY_RED_KOOPA; break;
		        case(Sprite.KIND_RED_KOOPA): type = Sprite.KIND_RED_KOOPA; break;
		        //case(Sprite.KIND_RED_KOOPA_WINGED): type = Enemy.ENEMY_RED_KOOPA; winged = true; break;
		        case(Sprite.KIND_RED_KOOPA_WINGED): type = Sprite.KIND_RED_KOOPA_WINGED; winged = true; break;
		        //case(Sprite.KIND_SPIKY): type = Enemy.ENEMY_SPIKY; break;
		        case(Sprite.KIND_SPIKY): type = Sprite.KIND_SPIKY; break;
		        //case(Sprite.KIND_SPIKY_WINGED): type = Enemy.ENEMY_SPIKY; winged = true; break;
		        case(Sprite.KIND_SPIKY_WINGED): type = Sprite.KIND_SPIKY_WINGED; winged = true; break;
		        //case(Sprite.KIND_ENEMY_FLOWER): type = Enemy.ENEMY_FLOWER; break;
		        case(Sprite.KIND_ENEMY_FLOWER): type = Sprite.KIND_ENEMY_FLOWER; break;
		        //case(Sprite.KIND_FIRE_FLOWER): type = Sprite.KIND_FIRE_FLOWER;break;
		        //case(Sprite.KIND_MUSHROOM): type = Sprite.KIND_MUSHROOM;break;
		        case(Sprite.KIND_WAVE_GOOMBA): type = Sprite.KIND_WAVE_GOOMBA;break;
	        }
	        
	        if (debuglog == 2)
	        	if (kind == Sprite.KIND_WAVE_GOOMBA)
	        		System.out.println("wave goomba");


	        if (type == -1)
	        	continue;
	        
	        //Sprite sprite = new Enemy(this, x, y, -1, type, winged, (int) x/16, (int) y/16);
	        //sprite.spriteTemplate =  new SpriteTemplate(type, winged);
	        //newSprites.add(sprite);
	        
	        
	        // 
	        float maxDelta = 2.01f * 1.75f ;//0.1f;// 2.01f * 1.75f; why 2.01 * 1.75f ??
	        
	        boolean enemyFound = false;
	        
	        for (Sprite sprite:sprites)
	        {
	        	/*
	        	if (sprite.kind == Sprite.KIND_FIRE_FLOWER || sprite.kind == Sprite.KIND_MUSHROOM)
	        	{
	        		newSprites.add(sprite);
	        	}
	        	*/
	        	// check if object is of same kind and close enough
	        	if (sprite.kind == kind && Math.abs(sprite.x - x) < maxDelta && ((Math.abs(sprite.y - y) < maxDelta) || sprite.kind == Sprite.KIND_ENEMY_FLOWER))
	        	{
	        		if (Math.abs(sprite.x - x) > 0)
	        		{
	        			if (sprite.kind == Sprite.KIND_SHELL)
	        				((Shell)sprite).facing *= -1; //(shell)
	        			else if (sprite.kind == Sprite.KIND_BULLET_BILL)
	        				((BulletBill)sprite).facing *= -1; // 20140122
	        			else
	        				((Enemy) sprite).facing *= -1;
	        			if (debuglog == 1)
	        				System.out.println("set enemy facing*1 kind" + kind + " x " + x + " y " + y);
	        			requireReplanning = true;
		        		sprite.x = x;
	        		}
	        		if ((sprite.y - y) != 0 && sprite.kind == Sprite.KIND_ENEMY_FLOWER)
	        		{
	        			((Enemy) sprite).ya = (y - sprite.lastAccurateY) * 0.89f;//+= sprite.y - y;
		        		sprite.y = y;
	        		}
	        		enemyFound = true;
	        	}
	        	
	        	if (sprite.kind == kind && (sprite.x - x) == 0 && (sprite.y - y) != 0 && Math.abs(sprite.y - y) < 8 && sprite.kind != Sprite.KIND_SHELL &&sprite.kind != Sprite.KIND_BULLET_BILL && ((Enemy) sprite).winged)
	        	{
	        		// x accurate but y wrong. flying thing
	        		sprite.ya = (y - sprite.lastAccurateY) * 0.95f + 0.6f; // / 0.89f;
	        		sprite.y = y;
	        		sprite.unknownYA = false;
	        		enemyFound = true;
	        		requireReplanning = true;
	        	}
	        	if (sprite.kind == kind && (sprite.x - x) == 0 && (sprite.y - y) != 0 && Math.abs(sprite.y - y) <= 2 && sprite.unknownYA && sprite.lastAccurateY != 0)
	        	{
	        		// should be not winged, falling down a cliff

	        		sprite.ya = (y - sprite.lastAccurateY) * 0.85f + 2; // / 0.89f; 	        		
	        		sprite.y = y;	        		
	        		sprite.unknownYA = false;
	        		enemyFound = true;
	        	}
	        	
	        	if (enemyFound)
	        	{
	        		newSprites.add(sprite);
		        	sprite.lastAccurateX = x;
		        	sprite.lastAccurateY = y;
	        		break;
	        	}
	        }
	        
	        // didn't find a close enemy in our representation of the world,
	        // create a new one.
	        if (!enemyFound)
	        {

	        	requireReplanning = true;
	        	Sprite sprite;
	        	if (type == Enemy.KIND_ENEMY_FLOWER)
	        	{
	        		int flowerTileX = (int) x/16;
	        		int flowerTileY = (int) y/16;
	        		
			        sprite = new FlowerEnemy(this, flowerTileX*16+15, y, flowerTileX, flowerTileY);
	        	}
	        	else if (kind == Sprite.KIND_BULLET_BILL)
	        	{

	        		int dir = -1;
                    sprite = new BulletBill(this, x, y, dir);
	        	}
	        	else if (kind == Sprite.KIND_SHELL)
	        	{
	        		
	        		sprite = new Shell(this,  x,  y, 0,  -1);
	        		sprite.xa = 2;
	        		

	        	}else 
	        	{
	        		sprite = new Enemy(this, x, y, -1, type, winged, (int) x/16, (int) y/16);
	        		sprite.xa = 2;
	        	}
	        
	        	sprite.lastAccurateX = x;
	        	sprite.lastAccurateY = y;
	        	
		        sprite.spriteTemplate =  new SpriteTemplate(type, winged);
		        newSprites.add(sprite);

	        }
	        
	        
        
		}
		
		
		if (wang.agent.astaragent.logout == 1)
		{
			for(int i = 0; i < newSprites.size(); i++)
				System.out.print("set enemies kind " + newSprites.get(i).kind + " x " + newSprites.get(i).x + " y " + newSprites.get(i).y);
			System.out.println();
		}
		
		newSprites.add(mario);
		
		sprites = newSprites;
		
		
		return requireReplanning;
		
	}
	
	
    protected Object clone() throws CloneNotSupportedException 
    {

    	LevelScene c = (LevelScene) super.clone();
    	c.mario = (Mario) this.mario.clone();
    	c.level = (Level) this.level.clone();
    	/*
    	for(int i = 0; i < c.level.TILE_BEHAVIORS.length; i++)
    	{
    		if (c.level.TILE_BEHAVIORS[i] != this.level.TILE_BEHAVIORS[i])
    			System.out.println("Tile_Behaviors[" + i + "] not same" + "c." + c.level.TILE_BEHAVIORS[i] + " this." + this.level.TILE_BEHAVIORS[i]);
    		//System.out.println("Tile_Behaviors[" + i + "]  same" + "c." + c.level.TILE_BEHAVIORS[i] + " this." + this.level.TILE_BEHAVIORS[i]);
    	}
    	if (c.level.TILE_BEHAVIORS[196] != 2)
    		System.out.println("c.level.TILE_BEHAVIORS[196] != 2");
    	*/

    	//System.out.println("enemiesFloatsList length " + enemiesFloatsList.size());
    	
    	c.mario.world = c;
    	
    	List<Sprite> clone = new ArrayList<Sprite>(this.sprites.size());
        for(Sprite item: this.sprites) 
        {
        	if (item == mario)
        	{
        		clone.add(c.mario);
        	}
        	else
        	{
        		try{ //bug not sulove
        		Sprite s = (Sprite) item.clone();
        		if (s.kind == Sprite.KIND_SHELL && ((Shell) s).carried && c.mario.carried != null)
        			c.mario.carried = s;
        		s.world = c;
        		//s.world.mario = c.mario;
        		clone.add(s);
        		}catch(Exception e){
        		
        		}
        	}
        }
        c.sprites = clone;
    	return c;
    }
    /**/


	// TODO: !H!: Move to MarioEnvironment !!
	public float[] getEnemiesFloatPosinMarioZone(int zonelength)
	{
	    enemiesFloatsList.clear();
	    for (Sprite sprite : sprites)
	    {
	        // TODO:[M]: add unit tests for getEnemiesFloatPos involving all kinds of creatures
	    	if (Math.abs(sprite.x - mario.x) > zonelength * 16)
	    		continue;
	    	if (Math.abs(sprite.y - mario.y) > zonelength * 16)
	    		continue;
	        if (sprite.isDead()) continue;
	        switch (sprite.kind)
	        {
	            case Sprite.KIND_GOOMBA:
	            case Sprite.KIND_BULLET_BILL:
	            case Sprite.KIND_ENEMY_FLOWER:
	            case Sprite.KIND_GOOMBA_WINGED:
	            case Sprite.KIND_GREEN_KOOPA:
	            case Sprite.KIND_GREEN_KOOPA_WINGED:
	            case Sprite.KIND_RED_KOOPA:
	            case Sprite.KIND_RED_KOOPA_WINGED:
	            case Sprite.KIND_SPIKY:
	            case Sprite.KIND_SPIKY_WINGED:
	            case Sprite.KIND_SHELL:
	            {
	                enemiesFloatsList.add((float) sprite.kind);
	                enemiesFloatsList.add(sprite.x - mario.x);
	                enemiesFloatsList.add(sprite.y - mario.y);
	            }
	        }
	    }

	    float[] enemiesFloatsPosArray = new float[enemiesFloatsList.size()];

	    int i = 0;
	    for (Float F : enemiesFloatsList)
	        enemiesFloatsPosArray[i++] = F;

	    return enemiesFloatsPosArray;
	}

	public float[] getEnemiesFloatPos()
	{
	    enemiesFloatsList.clear();
	    for (Sprite sprite : sprites)
	    {
	        // TODO:[M]: add unit tests for getEnemiesFloatPos involving all kinds of creatures
	        if (sprite.isDead()) continue;
	        switch (sprite.kind)
	        {
	            case Sprite.KIND_GOOMBA:
	            case Sprite.KIND_BULLET_BILL:
	            case Sprite.KIND_ENEMY_FLOWER:
	            case Sprite.KIND_GOOMBA_WINGED:
	            case Sprite.KIND_GREEN_KOOPA:
	            case Sprite.KIND_GREEN_KOOPA_WINGED:
	            case Sprite.KIND_RED_KOOPA:
	            case Sprite.KIND_RED_KOOPA_WINGED:
	            case Sprite.KIND_SPIKY:
	            case Sprite.KIND_SPIKY_WINGED:
	            case Sprite.KIND_SHELL:
	            {
	                enemiesFloatsList.add((float) sprite.kind);
	                enemiesFloatsList.add(sprite.x - mario.x);
	                enemiesFloatsList.add(sprite.y - mario.y);
	            }
	        }
	    }

	    float[] enemiesFloatsPosArray = new float[enemiesFloatsList.size()];

	    int i = 0;
	    for (Float F : enemiesFloatsList)
	        enemiesFloatsPosArray[i++] = F;

	    return enemiesFloatsPosArray;
	}
	
	public float[] getEnemiesFloatPosFrontMario(int distancefrommario)
	{
	    enemiesFloatsList.clear();
	    for (Sprite sprite : sprites)
	    {
	        // TODO:[M]: add unit tests for getEnemiesFloatPos involving all kinds of creatures
	        if (sprite.isDead()) continue;
	        switch (sprite.kind)
	        {
	            case Sprite.KIND_GOOMBA:
	            case Sprite.KIND_BULLET_BILL:
	            case Sprite.KIND_ENEMY_FLOWER:
	            case Sprite.KIND_GOOMBA_WINGED:
	            case Sprite.KIND_GREEN_KOOPA:
	            case Sprite.KIND_GREEN_KOOPA_WINGED:
	            case Sprite.KIND_RED_KOOPA:
	            case Sprite.KIND_RED_KOOPA_WINGED:
	            case Sprite.KIND_SPIKY:
	            case Sprite.KIND_SPIKY_WINGED:
	            case Sprite.KIND_SHELL:
	            {
	            	
	            	if (sprite.x - mario.x > distancefrommario)
	            	{
		                enemiesFloatsList.add((float) sprite.kind);
		                enemiesFloatsList.add(sprite.x - mario.x);
		                enemiesFloatsList.add(sprite.y - mario.y);
	            	}
	            }
	        }
	    }

	    float[] enemiesFloatsPosArray = new float[enemiesFloatsList.size()];

	    int i = 0;
	    for (Float F : enemiesFloatsList)
	        enemiesFloatsPosArray[i++] = F;

	    return enemiesFloatsPosArray;
	}
	
	public float[] getFireFlowerorMushroomFloatPos()
	{
		if (debuglog == 6)
		{
		    for (Sprite sprite : sprites)
		    {
		    	System.out.print(" " + sprite.kind);
		    }
		    System.out.println();
		}
		fireFlowerFloatsList.clear();
	    for (Sprite sprite : sprites)
	    {
	        // TODO:[M]: add unit tests for getEnemiesFloatPos involving all kinds of creatures
	        //if (sprite.isDead()) continue;
	        switch (sprite.kind)
	        {
	            case Sprite.KIND_FIRE_FLOWER: 
	            case Sprite.KIND_MUSHROOM:

	            {
	            	fireFlowerFloatsList.add((float) sprite.kind);
	            	fireFlowerFloatsList.add(sprite.x - mario.x);
	            	fireFlowerFloatsList.add(sprite.y - mario.y);
	            	
	            	if (debuglog == 8)
	            		System.out.println("getFireFlowerorMushroomFloatPos");
	            }
	        }
	    }

	    float[] fireFlowerFloatsPosArray = new float[fireFlowerFloatsList.size()];

	    int i = 0;
	    for (Float F : fireFlowerFloatsList)
	    	fireFlowerFloatsPosArray[i++] = F;

	    return fireFlowerFloatsPosArray;
	}
	
	
	
	public float[] getFireFlowerorMushroomFloatPosinMarioZone(int zonelength)
	{
		fireFlowerFloatsList.clear();
	    for (Sprite sprite : sprites)
	    {
	    	if (Math.abs(sprite.x - mario.x) > (zonelength + 1) * 16)
	    		continue;
	    	if (Math.abs(sprite.y - mario.y) > (zonelength - 1) * 16)
	    		continue;

	        switch (sprite.kind)
	        {
	            case Sprite.KIND_FIRE_FLOWER: 
	            case Sprite.KIND_MUSHROOM:
	            	fireFlowerFloatsList.add((float) sprite.kind);
	            	fireFlowerFloatsList.add(sprite.x - mario.x);
	            	fireFlowerFloatsList.add(sprite.y - mario.y);	            		
	            	break;   
	        }
	    }

	    float[] fireFlowerFloatsPosArray = new float[fireFlowerFloatsList.size()];

	    int i = 0;
	    for (Float F : fireFlowerFloatsList)
	    	fireFlowerFloatsPosArray[i++] = F;

	    return fireFlowerFloatsPosArray;
	}


	
	public float[] getMushroomFloatPos()
	{
		mushroomFloatsList.clear();
	    for (Sprite sprite : sprites)
	    {
	        // TODO:[M]: add unit tests for getEnemiesFloatPos involving all kinds of creatures
	        if (sprite.isDead()) continue;
	        switch (sprite.kind)
	        {
	            case Sprite.KIND_MUSHROOM:

	            {
	            	mushroomFloatsList.add((float) sprite.kind);
	            	mushroomFloatsList.add(sprite.x - mario.x);
	            	mushroomFloatsList.add(sprite.y - mario.y);
	            }
	        }
	    }

	    float[] mushroomFloatsPosArray = new float[mushroomFloatsList.size()];

	    int i = 0;
	    for (Float F : mushroomFloatsList)
	    	mushroomFloatsPosArray[i++] = F;

	    return mushroomFloatsPosArray;
	}
	

	public int fireballsOnScreen = 0;

	List<Shell> shellsToCheck = new ArrayList<Shell>();

	public void checkShellCollide(Shell shell)
	{
	    shellsToCheck.add(shell);
	}

	List<Fireball> fireballsToCheck = new ArrayList<Fireball>();

	public void checkFireballCollide(Fireball fireball)
	{
	    fireballsToCheck.add(fireball);
	}
	
	int tmplog = 2;

	public void tick()
	{
		
	    if (GlobalOptions.isGameplayStopped)
	        return;
	    
	    this.resetReward();

	    timeLeft--;
	    if (timeLeft == 0)
	    {
	        mario.die("Time out!");
	    }
	    
	    
	    xCamO = xCam;
	    yCamO = yCam;
	    

	    if (startTime > 0)
	    {
	        startTime++;
	    }

	    float targetXCam = mario.x - 160;
	    
	    xCam = targetXCam;

	    if (xCam < 0) xCam = 0;
	    if (xCam > level.length * cellSize - GlobalOptions.VISUAL_COMPONENT_WIDTH)
	        xCam = level.length * cellSize - GlobalOptions.VISUAL_COMPONENT_WIDTH;

	    fireballsOnScreen = 0;
	    
	    for (Sprite sprite : sprites)
	    {
	        if (sprite != mario)
	        {
	            float xd = sprite.x - xCam;
	            float yd = sprite.y - yCam;
	            if (xd < -64 || xd > GlobalOptions.VISUAL_COMPONENT_WIDTH + 64 || yd < -64 || yd > GlobalOptions.VISUAL_COMPONENT_HEIGHT + 64)
	            {
	                removeSprite(sprite);
	            } 
	            else
	            {
	                if (sprite instanceof Fireball)
	                    fireballsOnScreen++;
	            }
	        }
	    }


	    tickCount++;
	    
	    //level.tick();
	    
        boolean hasShotCannon = false;
        int xCannon = 0;
	    for (int x = (int) xCam / cellSize - 1; x <= (int) (xCam + this.width) / cellSize + 1; x++)
	    {
	        for (int y = (int) yCam / cellSize - 1; y <= (int) (yCam + this.height) / cellSize + 1; y++)
	        {
	            int dir = 0;

	            if (x * cellSize + 8 > mario.x + cellSize) dir = -1;
	            if (x * cellSize + 8 < mario.x - cellSize) dir = 1;

	            SpriteTemplate st = level.getSpriteTemplate(x, y);

	            if (st != null)
	            {

	                if (st.lastVisibleTick != tickCount - 1)
	                {
	                    if (st.sprite == null || !sprites.contains(st.sprite))
	                        st.spawn(this, x, y, dir);
	                }

	                st.lastVisibleTick = tickCount;
	            }

	            if (dir != 0)
	            {
	                byte b = level.getBlock(x, y);
	  
	                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_ANIMATED) > 0)
	                {
	                    if ((b % cellSize) / 4 == 3 && b / cellSize == 0)
	                    {
	                        if ((tickCount - x * 2) % 100 == 0)
	                        {
	                            xCannon = x;
	                            for (int i = 0; i < 8; i++)
	                            {
	                                addSprite(new Sparkle(x * cellSize + 8, y * cellSize + (int) (Math.random() * cellSize), (float) Math.random() * dir, 0, 0, 1, 5));
	                            }
	                            addSprite(new BulletBill(this, x * cellSize + 8 + dir * 8, y * cellSize + 15, dir));

	                            hasShotCannon = true;
	                        }
	                    }
	                }
	            }
	        }
	    }

	    

	    for (Sprite sprite : sprites)
	    {
	    	sprite.tick();
	    }


	    byte levelElement = level.getBlock(mario.mapX, mario.mapY);
	    if (levelElement == (byte) (13 + 3 * 16) || levelElement == (byte) (13 + 5 * 16))
	    {
	        if (levelElement == (byte) (13 + 5 * 16))
	        {
	            mario.setOnTopOfLadder(true);
	        }
	        else
	        {
	            mario.setInLadderZone(true);
	        }
	    } 
	    else if (mario.isInLadderZone())
	    {
	        mario.setInLadderZone(false);
	    }
	    
	    

	    for (Sprite sprite : sprites)
	    {
	        sprite.collideCheck();
	    } 
	    
	    

	    for (Shell shell : shellsToCheck)
	    {
	        for (Sprite sprite : sprites)
	        {
	            if (sprite != shell && !shell.dead)
	            {
	                if (sprite.shellCollideCheck(shell))
	                {
	                    if (mario.carried == shell && !shell.dead)
	                    {
	                        mario.carried = null;
	                        mario.setRacoon(false);
	                        shell.die();
	                        ++this.killedCreaturesTotal;
	                        this.reward += LevelScene.KILLS;
	                    }
	                }
	            }
	        }
	    }
	    shellsToCheck.clear();

	    for (Fireball fireball : fireballsToCheck)
	        for (Sprite sprite : sprites)
	            if (sprite != fireball && !fireball.dead)
	                if (sprite.fireballCollideCheck(fireball))
	                {
	                	if (debuglog == 1)
	                		System.out.println("123");
	                    fireball.die();
	                }
	    fireballsToCheck.clear();

	    sprites.addAll(0, spritesToAdd);
	    sprites.removeAll(spritesToRemove);
	    spritesToAdd.clear();
	    spritesToRemove.clear();
	    

	    
	    this.score += this.reward;
	    
	    if (debuglog == 6)
	    	System.out.println("rewards " + this.reward + " score " + score);
	    
	}

	public void addSprite(Sprite sprite)
	{
	    spritesToAdd.add(sprite);
	    sprite.tick();
	}

	public void removeSprite(Sprite sprite)
	{
	    spritesToRemove.add(sprite);
	}

	public void bump(int x, int y, boolean canBreakBricks)
	{
	    byte block = level.getBlock(x, y);
	    
	    if (block == -20 || block == -22)
	    {
	    	this.mario.bricks++;
	    	this.reward += LevelScene.BUMP;
	    }
	    
	    if (debuglog == 5)
	    	if (block == -20 || block == -22)
	    		System.out.println("bump");

	    if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BUMPABLE) > 0)
	    {
	    	if (debuglog == 6)
	    		System.out.println(" bitbumpable");
	    	
	        if (block == 1)
	            this.mario.gainHiddenBlock();
	        bumpInto(x, y - 1);
	        level.setBlock(x, y, (byte) 4);


	        if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_SPECIAL) > 0)
	        {
		    	if (debuglog == 6)
		    		System.out.println(" special");
	        	/*
	        	
	            if (randomGen.nextInt(5) == 0 && level.difficulty > 4)
	            {
	                addSprite(new GreenMushroom(this, x * cellSize + 8, y * cellSize + 8));
	                ++level.counters.greenMushrooms;
	            } 
	            else
	            {
	                if (!this.mario.large)
	                {
	                    addSprite(new Mushroom(this, x * cellSize + 8, y * cellSize + 8));
	                    ++level.counters.mushrooms;
	                } else
	                {
	                    addSprite(new FireFlower(this, x * cellSize + 8, y * cellSize + 8));
	                    ++level.counters.flowers;
	                }
	            }
	            */
	            if (!mario.large)
	            {
                    addSprite(new Mushroom(this, x * cellSize + 8, y * cellSize + 8));
                    ++level.counters.mushrooms;
	            } else
	            {
                    addSprite(new FireFlower(this, x * cellSize + 8, y * cellSize + 8));
                    ++level.counters.flowers;
	            }
	        } 
	        else
	        {
		    	if (debuglog == 6)
		    		System.out.println(" coins");
	        	
	            this.mario.gainCoin();
	            //addSprite(new CoinAnim(x, y));
	        }
	    }

	    if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BREAKABLE) > 0)
	    {
	    	if (debuglog == 1)
	    		System.out.println("wang Level.BIT_BREAKABLE");
	    	
	        bumpInto(x, y - 1);
	        
	        if (canBreakBricks)
	        {
	            level.setBlock(x, y, (byte) 0); 
	            
	            for (int xx = 0; xx < 2; xx++)
	                for (int yy = 0; yy < 2; yy++)
	                    addSprite(new Particle(x * cellSize + xx * 8 + 4, y * cellSize + yy * 8 + 4, (xx * 2 - 1) * 4, (yy * 2 - 1) * 4 - 8));
	        } else
	        {
	            //level.setBlockData(x, y, (byte) 4);
	        }
	    }
	}

	public void bumpInto(int x, int y)
	{
	    byte block = level.getBlock(x, y);

	    if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_PICKUPABLE) > 0)
	    {
	        this.mario.gainCoin();
	        level.setBlock(x, y, (byte) 0); 
	        addSprite(new CoinAnim(x, y + 1));
	    }

	    for (Sprite sprite : sprites)
	    {
	        sprite.bumpCheck(x, y);
	    }
	}
	
	public int[] getMarioState()
	{
	    marioState[0] = this.getMarioStatus();
	    marioState[1] = this.getMarioMode();
	    marioState[2] = this.isMarioOnGround() ? 1 : 0;
	    marioState[3] = this.isMarioAbleToJump() ? 1 : 0;
	    marioState[4] = this.isMarioAbleToShoot() ? 1 : 0;
	    marioState[5] = this.isMarioCarrying() ? 1 : 0;
	    marioState[6] = this.getKillsTotal();
	    marioState[7] = this.getKillsByFire();
	    marioState[8] = this.getKillsByStomp();
	    marioState[9] = this.getKillsByShell();
	    marioState[10] = this.getTimeLeft();
	    marioState[11] = mario.coins;
	    marioState[12] = (int)mario.x;
	    
	    return marioState;
	}
	
	public int getMarioStatus(){ return mario.getStatus();}
	public int getTimeSpent()  { return startTime / GlobalOptions.mariosecondMultiplier; }
	public int getTimeLeft() { return timeLeft / GlobalOptions.mariosecondMultiplier; }
	public boolean isMarioOnGround() { return mario.isOnGround(); }
	public boolean isMarioAbleToJump() { return mario.mayJump(); }
	public int getKillsTotal() {return killedCreaturesTotal;}
	public int getKillsByFire(){return killedCreaturesByFireBall;}
	public int getKillsByStomp(){return killedCreaturesByStomp;}
	public int getKillsByShell(){return killedCreaturesByShell;}
	public boolean isMarioAbleToShoot(){return mario.isAbleToShoot();}
	public int getMarioMode(){ return mario.getMode(); }
	public boolean isMarioCarrying(){ return mario.carried != null; }




	

	/**
	 * first and second elements of the array are x and y Mario coordinates correspondingly
	 *
	 * @return an array of size 2*(number of creatures on screen) including mario
	 */
	
	/*
	public float[] getCreaturesFloatPos()
	{
	    float[] enemies = this.getEnemiesFloatPos();
	    float ret[] = new float[enemies.length + 2];
	    System.arraycopy(this.getMarioFloatPos(), 0, ret, 0, 2);
	    System.arraycopy(enemies, 0, ret, 2, enemies.length);
	    return ret;
	}


*/
	public void resetDefault()
	{
	    // TODO: set values to defaults
	    reset(MarioAIOptions.getDefaultOptions());
	}
	

	public void reset(MarioAIOptions marioAIOptions)
	{

	    killedCreaturesTotal = 0;
	    killedCreaturesByFireBall = 0;
	    killedCreaturesByStomp = 0;
	    killedCreaturesByShell = 0;
	    //marioInitialPos = marioAIOptions.getMarioInitialPos();
	    greenMushroomMode = marioAIOptions.getGreenMushroomMode(); //what means?
	    marioInitialPos = new Point(); //mario's initial position
	    marioInitialPos.x = 32;
	    marioInitialPos.y = 32;


	    level = new Level(1500,15);//null;
	    
	    Level.loadBehaviors();
	    Level.counters = new  Level.objCounters();
	    Level.counters.reset();
	    this.levelLength = level.length;
	    this.levelHeight = level.height;
	    Sprite.spriteContext = this;
	    sprites.clear();
	    this.width = GlobalOptions.VISUAL_COMPONENT_WIDTH;
	    this.height = GlobalOptions.VISUAL_COMPONENT_HEIGHT;

	    Sprite.setCreaturesGravity(marioAIOptions.getCreaturesGravity());
	    Sprite.setCreaturesWind(marioAIOptions.getWind());
	    Sprite.setCreaturesIce(marioAIOptions.getIce());
	    
	   
	    
	    

	    bonusPoints = -1;

	    mario = new Mario(this);
	    mario.resetStatic(marioAIOptions);
	    //System.out.println("mario = " + mario);
	    memo = "";

	    sprites.add(mario);
	    startTime = 1;
	    timeLeft = timeLimit * GlobalOptions.mariosecondMultiplier;

	    tickCount = 1;
	    
	}
	
	

	/*
	public float[] getMarioFloatPos()
	{
	    marioFloatPos[0] = this.mario.x;
	    marioFloatPos[1] = this.mario.y;
	    return marioFloatPos;
	}



	public boolean isMarioCarrying()
	{ return mario.carried != null; }

	public int getLevelDifficulty()
	{ return levelDifficulty; }

	public long getLevelSeed()
	{ return levelSeed; }

	public int getLevelLength()
	{ return levelLength; }

	public int getLevelHeight()
	{ return levelHeight; }

	public int getLevelType()
	{ return levelType; }


	public void addMemoMessage(final String memoMessage)
	{
	    memo += memoMessage;
	}

	public Point getMarioInitialPos() {return marioInitialPos;}

	/*
	public void setReplayer(Replayer replayer)
	{
	    this.replayer = replayer;
	}
	

	public int getGreenMushroomMode()
	{
	    return greenMushroomMode;
	}

	public int getBonusPoints()
	{
	    return bonusPoints;
	}

	public void setBonusPoints(final int bonusPoints)
	{
	    this.bonusPoints = bonusPoints;
	}

	public void appendBonusPoints(final int superPunti)
	{
	    bonusPoints += superPunti;
	}
	*/
    public void init()
    {
        this.resetDefault();
    }
    
	public void appendBonusPoints(final int superPunti)
	{
	    bonusPoints += superPunti;
	}
	public int getGreenMushroomMode()
	{
	    return greenMushroomMode;
	}
	public Point getMarioInitialPos() 
	{
		return marioInitialPos;
	}
	public void addMemoMessage(final String memoMessage)
	{
	    memo += memoMessage;
	}

}
