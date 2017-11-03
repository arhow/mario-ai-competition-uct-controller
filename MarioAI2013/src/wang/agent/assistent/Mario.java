package wang.agent.assistent;





import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.MarioAIOptions;

class MarioState
{
	int mode ;
	int status ;
	double x;
	double y;
	int killsTotal;
	int killsByStomp;
	int killsByFire;
	int killsByShell;
	int coins;
	int bricks;
	int timeleft;
	int kickshells;
	int damage;
	float distance;
	int flowerfire;
	int mushroom;
	int enemycount;
	int flowerfireormushroomcount;
	float distanceXToFlower;
	float distanceYToFlower;
};

class sov
{
	public static int distance = 1;

	public static int mode = 1000;
	public static int coins = 16;
	public static int flowerFire = 30;
	public static int kills = 42;
	public static int killedByFire = 4;
	public static int killedByShell = 17;
	public static int kickshell = 0;
	public static int killedByStomp = 12;
	public static int mushroom = 30;
	public static int timeLeft = 8;
	public static int hiddenBlock = 24;
	public static int greenMushroom = 58;
	public static int stomp = 10;
	public static int brick = 30;
}



public class Mario extends Sprite implements Cloneable
{
	int debuglog = 8;

	int index = 0;
	public int betterthan(MarioState nowms, MarioState bestms, int searchcase) //1 good 2 bad
	{
		int score = 0;
		int bestscore = 0;

		//int fireflowerOrMushroomCount = this.levelScene.getFireFlowerorMushroomFloatPos().length/3;
		MarioState ms = this.getMarioState();

		index ++;
		
		if (debuglog==6)
			System.out.println("betterthan " + searchcase);
		
		switch (searchcase)
		{
		case 0:
			score = (int)(
					(ms.x - bestms.x) * 2 
					//(ms.y - bestms.y)*-1
					//+ (ms.damage - bestms.damage) * (-10000)
					+ (ms.bricks - bestms.bricks) *5
					+ (ms.status - bestms.status) * 100000
					+ (ms.mode - bestms.mode) * 10000
					+ (ms.coins - bestms.coins) * 10
					+ (ms.flowerfire - bestms.flowerfire) * 100
					+ (ms.mushroom - bestms.mushroom)*100 
					+ (ms.kickshells - bestms.kickshells)* 10
					+ (ms.enemycount - bestms.enemycount)* (-20)
					);//+
			break;
		case 1:
			if (debuglog==6)
				System.out.println("betterthan " + searchcase);
			score = (int)(
					(ms.distanceXToFlower - bestms.distanceXToFlower)*(-1)
					+(ms.distanceYToFlower - bestms.distanceYToFlower)*(-1)
					
					//+ (ms.damage - bestms.damage) * (-10000)
					+ (ms.bricks - bestms.bricks) *5
					+ (ms.status - bestms.status) * 100000
					+ (ms.mode - bestms.mode) * 10000
					+ (ms.coins - bestms.coins) * 10
					+ (ms.flowerfire - bestms.flowerfire) * 100
					+ (ms.mushroom - bestms.mushroom)*100 
					+ (ms.kickshells - bestms.kickshells)* 10
					+ (ms.enemycount - bestms.enemycount)* (-20)
					);//+
			break;
		case 2:
			score = (int)((ms.x - bestms.x) * 1 
					
					
					//+ (ms.damage - bestms.damage) * (-10000)
					+ (ms.bricks - bestms.bricks) *5
					+ (ms.status - bestms.status) * 100000
					+ (ms.mode - bestms.mode) * 10000
					+ (ms.coins - bestms.coins) * 10
					+ (ms.flowerfire - bestms.flowerfire) * 100
					+ (ms.mushroom - bestms.mushroom)*100 
					+ (ms.kickshells - bestms.kickshells)* 10
					+ (ms.enemycount - bestms.enemycount)* (-100)
					);//+
			break;
		}
		


		
			
		if (score > 0)
			return 1;
		else
			return 2;
		//return 0;
	}

	public MarioState getMarioState()
	{
		MarioState mariostate = new MarioState();
		mariostate.mode = this.getMode();
		mariostate.status = this.getStatus();
		mariostate.x = x;
		mariostate.y = y;
		mariostate.bricks = this.bricks;
		mariostate.coins = this.coins;
		mariostate.kickshells = this.kickshells;
		mariostate.killsByStomp = this.killsByStomp;
		mariostate.flowerfire = this.flowersDevoured;
		mariostate.mushroom = this.mushroomsDevoured;
		mariostate.enemycount = (int)this.world.getEnemiesFloatPos().length/3;
		mariostate.flowerfireormushroomcount = (int)this.world.getFireFlowerorMushroomFloatPos().length/3;
		if (this.world.getFireFlowerorMushroomFloatPosinMarioZone(4).length != 0)
		{
		mariostate.distanceXToFlower = Math.abs(this.world.getFireFlowerorMushroomFloatPos()[1] );
		mariostate.distanceYToFlower = Math.abs(this.world.getFireFlowerorMushroomFloatPos()[2] );
		if (debuglog==7)
			System.out.println("getMarioState " + mariostate.distanceXToFlower + " " + mariostate.distanceYToFlower);
		}else
		{
			mariostate.distanceXToFlower = 0;
			mariostate.distanceYToFlower = 0;
		}
		
		return mariostate;
	}
	public void setOnGround(boolean tmp)
	{
			onGround = tmp;		
	}
	
	public static boolean printflag = false;
	
	public static final String[] MODES = new String[]{"small", "Large", "FIRE"};

//  fire = (mode == MODE.MODE_FIRE);
public static final int KEY_LEFT = 0;
public static final int KEY_RIGHT = 1;
public static final int KEY_DOWN = 2;
public static final int KEY_JUMP = 3;
public static final int KEY_SPEED = 4;
public static final int KEY_UP = 5;

public static final int STATUS_RUNNING = 2;
public static final int STATUS_WIN = 1;
public static final int STATUS_DEAD = 0;

private static float marioGravity;

public  boolean large = false;
public  boolean fire = false;
public  int coins = 0;
public  int hiddenBlocksFound = 0;
public  int collisionsWithCreatures = 0;
public  int mushroomsDevoured = 0;
public  int greenMushroomsDevoured = 0;
public  int flowersDevoured = 0;

public  int bricks;
public  int killsTotal;
public  int killsByStomp;
public  int killsByFire;
public  int killsBuShell;
public int kickshells;

private  boolean isTrace;

private  boolean isMarioInvulnerable;

private int status = STATUS_RUNNING;
//for racoon when carrying the shell
private int prevWPic;
private int prevxPicO;
private int prevyPicO;
private int prevHPic;

private boolean isRacoon;
private float yaa = 1;

private static float windCoeff = 0f;
private static float iceCoeff = 0f;
private static float jumpPower;
private boolean inLadderZone;
private boolean onLadder;
private boolean onTopOfLadder = false;

public int damage = 0; // counts +1 everytime mario is hurt

public  void resetStatic(MarioAIOptions marioAIOptions)
{
	large = marioAIOptions.getMarioMode() > 0;
	fire = marioAIOptions.getMarioMode() == 2;
	
	hiddenBlocksFound = 0;

	collisionsWithCreatures = 0;
	
	coins = 0;
	mushroomsDevoured = 0;
	flowersDevoured = 0;
	
	bricks = 0;
	killsTotal = 0;
	killsByStomp = 0;
	killsByFire = 0;
	killsBuShell = 0;
	kickshells = 0;
	
	isMarioInvulnerable = marioAIOptions.isMarioInvulnerable();
	
	//System.out.println("wang resetstatic ismarioinvulnerable " + isMarioInvulnerable);
	marioGravity = marioAIOptions.getMarioGravity();
	jumpPower = marioAIOptions.getJumpPower();
	isTrace = marioAIOptions.isTrace();
	iceCoeff = marioAIOptions.getIce();
	windCoeff = marioAIOptions.getWind();
}
public void resetmariostate()
{
	coins = 0;
	mushroomsDevoured = 0;
	flowersDevoured = 0;
	bricks = 0;
	killsTotal = 0;
	killsByStomp = 0;
	killsByFire = 0;
	killsBuShell = 0;
	kickshells = 0;
	damage = 0;
	

}

public int getMode()
{
	return ((large) ? 1 : 0) + ((fire) ? 1 : 0);
}

//private static float GROUND_INERTIA = 0.89f;
//private static float AIR_INERTIA = 0.89f;

public boolean[] keys = new boolean[Environment.numberOfKeys];
public boolean[] cheatKeys;
private float runTime;
boolean wasOnGround = false;
boolean onGround = false;
private boolean mayJump = false;
private boolean ducking = false;
private boolean sliding = false;
public int jumpTime = 0;
private float xJumpSpeed;
private float yJumpSpeed;

private boolean ableToShoot = false;

int width = 4;
int height = 24;

//private static LevelScene levelScene;
//public  LevelScene levelScene;
//public LevelScene getlevelscene(){return this.levelScene;}
public int facing;
public int xDeathPos, yDeathPos;
public int deathTime = 0;
public int winTime = 0;
private int invulnerableTime = 0;
public Sprite carried = null;
//private static Mario instance;
public static float tmpya ; 
public static float tmpxa ;

public void setKeys(boolean action[]) // added by wzx
{
	for (int i = 0; i < 6; i++)
		keys[i] = action[i];
}

public Mario(LevelScene levelScene)
{
	kind = KIND_MARIO;
	//  Mario.instance = this;
	if (levelScene == null)
		System.out.println("levelScene == null");
	//this.levelScene = levelScene;
	this.world = levelScene;
	if (levelScene.getMarioInitialPos() == null)
	{
		System.out.println("levelScene.getMarioInitialPos() == null");
		x=32;//default
		y=32;//default
	}else
	{
		x = levelScene.getMarioInitialPos().x;
		y = levelScene.getMarioInitialPos().y;
		//System.out.println("123");
	}
	mapX = (int) (x / 16);
	mapY = (int) (y / 16);
	
	facing = 1;
	setMode(large, fire);
	yaa = marioGravity * 3;
	jT = jumpPower / (marioGravity);
	
	//System.out.println("jt" + jT);
	
	damage = 0;
	
    //kind = KIND_MARIO;
    //Mario.instance = this;
    //this.world = world;
    //keys = Scene.keys;      // SK: in fact, this is already redundant due to using Agent
    //cheatKeys = Scene.keys; // SK: in fact, this is already redundant due to using Agent
    //x = 32;
    //y = 0;

    //facing = 1;
    //setLarge(true, true);
}

private float jT;
private boolean lastLarge;
private boolean lastFire;
private boolean newLarge;
private boolean newFire;

private void blink(boolean on)
{
large = on ? newLarge : lastLarge;
fire = on ? newFire : lastFire;

//  System.out.println("on = " + on);
if (large)
{
  sheet = Art.mario;
  if (fire)
      sheet = Art.fireMario;

  xPicO = 16;
  yPicO = 31;
  wPic = hPic = 32;
} else
{
  sheet = Art.smallMario;

  xPicO = 8;
  yPicO = 15;
  wPic = hPic = 16;
}
savePrevState();
calcPic();
}


public Object clone() throws CloneNotSupportedException
{
	Mario m = (Mario) super.clone();

	
	boolean[] k = new boolean[6];
	for (int i = 0; i < k.length; i++)
		k[i] = keys[i];
	m.keys = k;
	return m; 
	


}

void setMode(boolean large, boolean fire)
{
//  System.out.println("large = " + large);
if (fire) large = true;
if (!large) fire = false;

lastLarge = this.large;
lastFire = this.fire;

this.large = large;
this.fire = fire;

newLarge = this.large;
newFire = this.fire;

blink(true);
}

public void setRacoon(boolean isRacoon)
{
//  if (true)
//  return;
this.isRacoon = isRacoon;
//  this.setMode(isRacoon, false);
//  System.out.println("isRacoon = " + isRacoon);
//  System.out.println("Art.racoonmario.length = " + Art.racoonmario.length);
//  System.out.println("Art.racoonmario[0].length = " + Art.racoonmario[0].length);
if (isRacoon)
{
  savePrevState();

  xPicO = 16;
  yPicO = 31;
  wPic = hPic = 32;
  this.sheet = Art.racoonmario;
} else
{

  this.sheet = prevSheet;
  this.xPicO = this.prevxPicO;
  this.yPicO = this.prevyPicO;
  wPic = prevWPic;
  hPic = prevHPic;
//      blink(false);
}
}

private void savePrevState()
{
this.prevSheet = this.sheet;
prevWPic = wPic;
prevHPic = hPic;
this.prevxPicO = xPicO;
this.prevyPicO = yPicO;
}

public void setMarioState(int[] mariostate)
{
    this.status = mariostate[0];
    //mariostate[1];
    if (mariostate[1] == 2)
    {
    	this.fire = true;
    	this.large = true;
    }
    else if(mariostate[1] == 1)
    {
    	this.large = true;
    	this.fire = false;
    }
    else
    {
    	this.fire = false;
    	this.large = true;
    }
    
    //this.isMarioOnGround() = mariostate[2];
    //this.isMarioAbleToJump() = mariostate[3];
    //this.isMarioAbleToShoot() = mariostate[4];
    //this.isMarioCarrying() = mariostate[5];
    //this.getKillsTotal() = mariostate[6];
    //this.getKillsByFire() = mariostate[7];
    //this.getKillsByStomp() = mariostate[8];
    //this.getKillsByShell() = mariostate[9];
    //this.getTimeLeft() = mariostate[10];
    //this.coins = mariostate[11];
    this.x = mariostate[12];
		
}
public void move()
{
	double tmpstartx = this.x;

	if (GlobalOptions.isFly)
	{
	  xa = ya = 0;
	  ya = keys[KEY_DOWN] ? 10 : ya;
	  ya = keys[KEY_UP] ? -10 : ya;
	  xa = keys[KEY_RIGHT] ? 10 : xa;
	  xa = keys[KEY_LEFT] ? -10 : xa;
	  
	}
	
	if (this.inLadderZone)
	{
		
	  if (keys[KEY_UP] && !onLadder)
	  {
	      onLadder = true;
	  }
	
	  if (!keys[KEY_UP] && !keys[KEY_DOWN] && onLadder)
	      ya = 0;
	
	  if (onLadder)
	  {
	      if (!onTopOfLadder)
	      {
	          ya = keys[KEY_UP] ? -10 : ya;
	      } else
	      {
	          ya = 0;
	          ya = keys[KEY_DOWN] ? 10 : ya;
	          if (keys[KEY_DOWN])
	              onTopOfLadder = false;
	      }
	      onGround = true;
	  }
	}
	
	
	if (mapY > -1 && isTrace)
	  ++world.level.marioTrace[this.mapX][this.mapY];
	
	if (winTime > 0)
	{
	  winTime++;
	  
	
	  xa = 0;
	  ya = 0;
	  return;
	}
	
	if (deathTime > 0)
	{
	  deathTime++;
	  if (deathTime < 11)
	  {
	      xa = 0;
	      ya = 0;
	  } else if (deathTime == 11)
	  {
	      ya = -15;
	  } else
	  {
	      ya += 2;
	  }
	  x += xa;
	  y += ya;
	  
	  return;
	}
	
	if (invulnerableTime > 0) invulnerableTime--;
	visible = ((invulnerableTime / 2) & 1) == 0;
	
	wasOnGround = onGround;
	float sideWaysSpeed = keys[KEY_SPEED] ? 1.2f : 0.6f;
	
	//        float sideWaysSpeed = onGround ? 2.5f : 1.2f;
	
	if (onGround)
	{
	  ducking = keys[KEY_DOWN] && large; //duobi
	}
	
	if (xa > 2)
	{
	  facing = 1;
	}
	if (xa < -2)
	{
	  facing = -1;
	}
	
	//float Wind = 0.2f;
	//float windAngle = 180;
	//xa += Wind * Math.cos(windAngle * Math.PI / 180);
	
	if (keys[KEY_JUMP] || (jumpTime < 0 && !onGround && !sliding))
	{
	  if (jumpTime < 0)
	  {
	      xa = xJumpSpeed;
	      ya = -jumpTime * yJumpSpeed;
	      jumpTime++;
	  } else if (onGround && mayJump)
	  {
	      xJumpSpeed = 0;
	      yJumpSpeed = -1.9f;
	      jumpTime = (int) jT;
	      ya = jumpTime * yJumpSpeed;
	      onGround = false;
	      sliding = false;
	  } else if (sliding && mayJump)
	  {
	      xJumpSpeed = -facing * 6.0f;
	      yJumpSpeed = -2.0f;
	      jumpTime = -6;
	      xa = xJumpSpeed;
	      ya = -jumpTime * yJumpSpeed;
	      onGround = false;
	      sliding = false;
	      facing = -facing;
	  } else if (jumpTime > 0)
	  {
	      xa += xJumpSpeed;
	      ya = jumpTime * yJumpSpeed;
	      jumpTime--;
	  }
	} else
	{
	  jumpTime = 0;
	}
	
	if (keys[KEY_LEFT] && !ducking)
	{
	  if (facing == 1) sliding = false;
	  xa -= sideWaysSpeed;
	  if (jumpTime >= 0) facing = -1;
	}
	
	if (keys[KEY_RIGHT] && !ducking)
	{
	  if (facing == -1) sliding = false;
	  xa += sideWaysSpeed;
	  if (jumpTime >= 0) facing = 1;
	}
	
	if ((!keys[KEY_LEFT] && !keys[KEY_RIGHT]) || ducking || ya < 0 || onGround)
	{
	  sliding = false;
	}
	
	if (keys[KEY_SPEED] && ableToShoot && this.fire && world.fireballsOnScreen < 2)
	{
	  world.addSprite(new Fireball(world, x + facing * 6, y - 20, facing));
	}
	// Cheats:
	if (GlobalOptions.isPowerRestoration && keys[KEY_SPEED] && (!this.large || !this.fire))
	{
		if (debuglog == 7)
			System.out.println("GlobalOptions.isPowerRestoration && keys[KEY_SPEED] && (!this.large || !this.fire)");
	  setMode(true, true);
	}
	//  if (cheatKeys[KEY_LIFE_UP])
	//      this.lives++;
	
	//  if (keys[KEY_DUMP_CURRENT_WORLD])
	//      try {
	//          System.out.println("DUMP:");
	////          levelScene.getObservationStrings(System.out);
	//          //levelScene.level.save(System.out);
	//          System.out.println("DUMPED:");
	//      } catch (IOException e) {
	//          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	//      }
	ableToShoot = !keys[KEY_SPEED];
	
	mayJump = (onGround || sliding) && !keys[KEY_JUMP];
	
	//xFlipPic = facing == -1;
	
	runTime += (Math.abs(xa)) + 5;  
	if (Math.abs(xa) < 0.5f)
	{
	  runTime = 0;
	  xa = 0;
	}
	
	//System.out.println("wang mario " + " islarge " + large + " isducking " + ducking);
	calcPic();
	
	if (sliding)
	{
	  for (int i = 0; i < 1; i++)
	  {
	      world.addSprite(new Sparkle((int) (x + Math.random() * 4 - 2) + facing * 8, (int) (y + Math.random() * 4) - 24, (float) (Math.random() * 2 - 1), (float) Math.random() * 1, 0, 1, 5));
	  }
	  ya *= 0.5f;
	}
	
	onGround = false;
	
	tmpxa = xa;
	tmpya = ya;

	move(xa, 0);
	move(0, ya);

	
	if (y > world.level.height * LevelScene.cellSize + LevelScene.cellSize)
	{
	  die("Gap");
	  if (debuglog == 2)
		  System.out.println("gap");
	}
	

	
	if (x < 0)
	{
	  x = 0;
	  xa = 0;
	}
	
	if (mapX >= world.level.xExit && mapY <= world.level.yExit)
	{
	  //x = (levelScene.level.xExit + 1) * LevelScene.cellSize;
	  //win();
	}
	
	if (x > world.level.length * LevelScene.cellSize)
	{
		
	  x = world.level.length * LevelScene.cellSize;
	  xa = 0;
	}

	ya *= 0.85f;
	if (onGround)
	{
	  xa *= (GROUND_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
	} else
	{
	  xa *= (AIR_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
	}

	if (!onGround)
	{
	  ya += yaa;
	}
	
	if (carried != null)
	{
	  carried.x = x + facing * 8; //TODO:|L| move to cellSize_2 = cellSize/2;
	  carried.y = y - 2;
	  if (!keys[KEY_SPEED])
	  {
	      carried.release(this);
	      carried = null;
	      setRacoon(false);
	  }
	
	}
	
	this.world.reward += (this.x - tmpstartx) * LevelScene.DISTANCE;

}

private void calcPic()
{
	int runFrame;
	
	if (large || isRacoon)
	{
	  runFrame = ((int) (runTime / 20)) % 4;
	  if (runFrame == 3) runFrame = 1;
	  if (carried == null && Math.abs(xa) > 10) runFrame += 3;
	  if (carried != null) runFrame += 10;
	  if (!onGround)
	  {
	      if (carried != null) runFrame = 12;
	      else if (Math.abs(xa) > 10) runFrame = 7;
	      else runFrame = 6;
	  }
	}
	else
	{
	  runFrame = ((int) (runTime / 20)) % 2;
	  if (carried == null && Math.abs(xa) > 10) runFrame += 2;
	  if (carried != null) runFrame += 8;
	  if (!onGround)
	  {
	      if (carried != null) runFrame = 9;
	      else if (Math.abs(xa) > 10) runFrame = 5;
	      else runFrame = 4;
	  }
	}
	
	if (onGround && ((facing == -1 && xa > 0) || (facing == 1 && xa < 0)))
	{
	  if (xa > 1 || xa < -1) runFrame = large ? 9 : 7;
	
	  if (xa > 3 || xa < -3)
	  {
	      for (int i = 0; i < 3; i++)
	      {
	          world.addSprite(new Sparkle((int) (x + Math.random() * 8 - 4), (int) (y + Math.random() * 4), (float) (Math.random() * 2 - 1), (float) Math.random() * -1, 0, 1, 5));
	      }
	  }
	}
	
	if (large)
	{
	  if (ducking) runFrame = 14;
	  height = ducking ? 12 : 24;
	} else
	{
	  height = 12;
	}
	
	xPic = runFrame;
}

private boolean move(float xa, float ya)
{
	//System.out.println("" + this.y);
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
	  sliding = true;
	  //System.out.println("1wang x=" + x + " width=" + width + " y=" + y + " ya=" + ya + " height=" + height + " xa=" + xa);
	  if (isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
	  else sliding = false;
	  if (isBlocking(x + xa + width, y + ya - height / 2, xa, ya)) collide = true;
	  else sliding = false;
	  if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;
	  else sliding = false;
	}
	if (xa < 0)
	{
	  sliding = true;
	  if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
	  else sliding = false;
	  if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
	  else sliding = false;
	  if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;
	  else sliding = false;
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
		  //System.out.println("mario move ya < 0");
	      y = (int) ((y - height) / 16) * 16 + height;
	      jumpTime = 0;
	      this.ya = 0;
	  }
	  if (ya > 0)
	  {
		  //System.out.println("mario move ya > 0");
	      y = (int) ((y - 1) / 16 + 1) * 16 - 1;
	      //System.out.println("move(float,float)");
	      onGround = true;
	  }
	  return false;
	} else
	{
	  x += xa;
	  y += ya;
	  return true;
	}
}

private boolean isBlocking(final float _x, final float _y, final float xa, final float ya)
{
	int x = (int) (_x / 16);
	int y = (int) (_y / 16);
	if (x == (int) (this.x / 16) && y == (int) (this.y / 16)) return false;
	
	boolean blocking = world.level.isBlocking(x, y, xa, ya);
	
	byte block = world.level.getBlock(x, y);

	if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_PICKUPABLE) > 0)
	{
	  gainCoin();
	  
	  world.level.setBlock(x, y, (byte) 0); 
	  
	  for (int xx = 0; xx < 2; xx++)
	      for (int yy = 0; yy < 2; yy++)
	          world.addSprite(new Sparkle(x * 16 + xx * 8 + (int) (Math.random() * 8), y * 16 + yy * 8 + (int) (Math.random() * 8), 0, 0, 0, 2, 5));
	}
	
	if (blocking && ya < 0)
	{
	  world.bump(x, y, large);
	}
	
	return blocking;
}

public void stomp(final Enemy enemy)
{
	if (deathTime > 0) return;
	this.killsByStomp ++ ;
	float targetY = enemy.y - enemy.height / 2;
	move(0, targetY - y);
	mapY = (int) y / 16;
	xJumpSpeed = 0;
	yJumpSpeed = -1.9f;
	jumpTime = (int) jT + 1;
	ya = jumpTime * yJumpSpeed;
	onGround = false;
	sliding = false;
	invulnerableTime = 1;
}

public void stomp(final Shell shell)
{
	if (deathTime > 0) return;
	
	if (keys[KEY_SPEED] && shell.facing == 0)
	{
	  carried = shell;
	  shell.carried = true;
	  setRacoon(true);
	}
	else
	{
	  float targetY = shell.y - shell.height / 2;
	  move(0, targetY - y);
	  mapY = (int) y / 16;
	
	  xJumpSpeed = 0;
	  yJumpSpeed = -1.9f;
	  jumpTime = (int) jT + 1;
	  ya = jumpTime * yJumpSpeed;
	  onGround = false;
	  sliding = false;
	  invulnerableTime = 1;
	}
	
}

public void getHurt(final int spriteKind)
{
	if (deathTime > 0 || isMarioInvulnerable) return;
	
	if (invulnerableTime > 0) return;
	
	damage++;
	this.world.reward += LevelScene.DAMAGE;
	//System.out.println("wang get hurt"); 
	
	++collisionsWithCreatures;
	world.appendBonusPoints(-MarioEnvironment.IntermediateRewardsSystemOfValues.kills);
	if (debuglog == 7)
		System.out.println("wang mario " + " large " + large + " fire " + fire);
	if (large)
	{
	//  levelScene.paused = true;
	//  powerUpTime = -3 * FractionalPowerUpTime;
	  if (fire)
	  {
	      world.mario.setMode(true, false);
	      if (wang.agent.astaragent.logout == 1)
	    	  System.out.println("became large");
	  } else
	  {
	      world.mario.setMode(false, false);
	      if (wang.agent.astaragent.logout == 1)
	    	  System.out.println("became small");
	  }
	  invulnerableTime = 32;
	} else
	{
		//System.out.println("Collision with a creature");
	  die("Collision with a creature [" + Sprite.getNameByKind(spriteKind) + "]");
	}
}
	
public void win()
{
	xDeathPos = (int) x;
	yDeathPos = (int) y;
	winTime = 1;
	status = Mario.STATUS_WIN;
	world.appendBonusPoints(MarioEnvironment.IntermediateRewardsSystemOfValues.win);
}

public void die(final String reasonOfDeath)
{
	damage+=2;
	this.world.reward += LevelScene.DAMAGE*2;
	xDeathPos = (int) x;
	yDeathPos = (int) y;
	deathTime = 25;
	status = Mario.STATUS_DEAD;
	world.addMemoMessage("Reason of death: " + reasonOfDeath);
	world.appendBonusPoints(-MarioEnvironment.IntermediateRewardsSystemOfValues.win / 2);
	if (debuglog == 1)
		System.out.println("Mario die?");
}

public void devourFlower()
{
	if (deathTime > 0) return;
	
	if (!fire)
	{
	  world.mario.setMode(true, true);
	} else
	{
	  gainCoin();
	}
	++flowersDevoured;
	world.reward += LevelScene.FLOWERFIRE;
	//levelScene.appendBonusPoints(MarioEnvironment.IntermediateRewardsSystemOfValues.flowerFire);
}

public void devourMushroom()
{
	if (deathTime > 0) return;
	
	if (!large)
	{
	  world.mario.setMode(true, false);
	} 
	else
	{
	  gainCoin();
	}
	++mushroomsDevoured;
	world.reward += LevelScene.MUSHROOM;
//levelScene.appendBonusPoints(MarioEnvironment.IntermediateRewardsSystemOfValues.mushroom);
}

public void devourGreenMushroom(final int mushroomMode)
{
	++greenMushroomsDevoured;
	if (mushroomMode == 0)
	  getHurt(Sprite.KIND_GREEN_MUSHROOM);
	else
	{
		//System.out.println("collision whith ");
	  die("Collision with a creature [" + Sprite.getNameByKind(Sprite.KIND_GREEN_MUSHROOM) + "]");
	}
}

public void kick(final Shell shell)
{
//  if (deathTime > 0 || levelScene.paused) return;
	if (debuglog == 2)
		System.out.println("kick shell");
	kickshells++;
	this.world.reward += LevelScene.KICKS;

	if (keys[KEY_SPEED])
	{
	  carried = shell;
	  shell.carried = true;
	  setRacoon(true);
	//  System.out.println("shell = " + shell);
	} else
	{
	  invulnerableTime = 1;
	}
}

public void stomp(final BulletBill bill)
{
	if (deathTime > 0)
	  return;
	
	float targetY = bill.y - bill.height / 2;
	move(0, targetY - y);
	mapY = (int) y / 16;
	
	xJumpSpeed = 0;
	yJumpSpeed = -1.9f;
	jumpTime = (int) jT + 1;
	ya = jumpTime * yJumpSpeed;
	onGround = false;
	sliding = false;
	invulnerableTime = 1;
	world.appendBonusPoints(MarioEnvironment.IntermediateRewardsSystemOfValues.stomp);
}

public  void gainCoin()
{
	coins++;
	this.world.reward += LevelScene.COINS;
}

public  void gainHiddenBlock()
{
	++hiddenBlocksFound;
}

public int getStatus()
{
	return status;
}

public boolean isOnGround()
{
	return onGround;
}

public boolean mayJump()
{
	return mayJump;
}

public boolean isAbleToShoot()
{
return ableToShoot;
}

public void setInLadderZone(final boolean inLadderZone)
{
	this.inLadderZone = inLadderZone;
	if (!inLadderZone)
	{
	  onLadder = false;
	  onTopOfLadder = false;
	}
}

public boolean isInLadderZone()
{
	return this.inLadderZone;
}

public void setOnTopOfLadder(final boolean onTop)
{
	this.onTopOfLadder = onTop;
}

public boolean isOnTopOfLadder()
{
	return this.onTopOfLadder;
}

}
