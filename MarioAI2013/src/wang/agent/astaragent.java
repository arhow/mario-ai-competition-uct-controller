package wang.agent;


import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.MarioVisualComponent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;
import wang.agent.assistent.AStarSimulator;



public class astaragent implements Agent
{
	private String name;
	private boolean[] action = new boolean[Environment.numberOfKeys];;
	protected byte[][] levelScene;
	protected byte[][] levelSceneEx;
	protected byte[][] enemies;
	protected byte[][] mergedObservation;
	protected float[] marioFloatPos = null;
	protected float[] enemiesFloatPos = null;
	protected int[] marioState = null;
	protected int marioStatus;
	protected int marioMode;
	protected boolean isMarioOnGround;
	protected boolean isMarioAbleToJump;
	protected boolean isMarioAbleToShoot;
	protected boolean isMarioCarrying;
	protected int getKillsTotal;
	protected int getKillsByFire;
	protected int getKillsByStomp;
	protected int getKillsByShell;
	int zLevelScene = 1;
	int zLevelEnemies = 0;
	
	private AStarSimulator sim;
	int tmplog = 2;
	
	//GameViewer test;
	
	float tmpMarioX = 0;
	int index = 0;
	boolean isbigjump = false;
	
	public static int tmpflag = 0;
	
	public static int logout = 0;
	
	private int lost = 0;
	
	int allsimulationcount = 0;
	int allindex = 0;

	
	public boolean[] getAction()
	{

	    
		if (wang.agent.astaragent.logout == 1)
			System.out.println(" optimise");
		tmpflag = 2;
		if (lost == 0)
		{
			action = sim.optimise(st);
		}
		else
		{
			action = sim.optimise(st);//action = new boolean[] {false, false, false, false, false, false}; //the result is not good
		}
		
		allsimulationcount += sim.simulationcount;
		allindex++;
		//System.out.println("average = " + allsimulationcount/allindex);
		
		
	    if (wang.agent.astaragent.logout == 1)
	    	System.out.println(" end");

	    
	    return action;
        
        //return  action;
	}
	public long st;
	private float lastX;
	private float lastY;
	public void integrateObservation(Environment environment)
	{
		st = System.currentTimeMillis();
		
	    levelScene = environment.getLevelSceneObservationZ(1);//zLevelScene
	    levelSceneEx = environment.getLevelSceneObservationZ(0);
	    enemies = environment.getEnemiesObservationZ(zLevelEnemies);
	    mergedObservation = environment.getMergedObservationZZ(1, 0);
	    this.marioFloatPos = environment.getMarioFloatPos();
	    this.enemiesFloatPos = environment.getEnemiesFloatPos();
	    this.marioState = environment.getMarioState();
	    marioStatus = marioState[0];
	    marioMode = marioState[1];
	    isMarioOnGround = marioState[2] == 1;
	    isMarioAbleToJump = marioState[3] == 1;
	    isMarioAbleToShoot = marioState[4] == 1;
	    isMarioCarrying = marioState[5] == 1;
	    getKillsTotal = marioState[6];
	    getKillsByFire = marioState[7];
	    getKillsByStomp = marioState[8];
	    getKillsByShell = marioState[9];
	    
	   
	    
	    //System.out.println(" x " +  MarioVisualComponent.getInstance(null, null).getLocation().x  + " y " + MarioVisualComponent.getInstance(null, null).getLocation().y);


	    if (wang.agent.astaragent.logout == 1)
	    {
		    System.out.print("wangmario before tick enemy");
		    for (int i = 0 ; i < this.sim.levelScene.getEnemiesFloatPos().length; i+=3)
		    	System.out.print(" kind " + this.sim.levelScene.getEnemiesFloatPos()[i] + 
		    			" x " + (this.sim.levelScene.getEnemiesFloatPos()[i+1] + this.sim.levelScene.mario.x) + 
		    			" y " + (this.sim.levelScene.getEnemiesFloatPos()[i+2] + this.sim.levelScene.mario.y));
		    System.out.println();
	    }
	    tmpflag = 1;
    	sim.advanceStep(action); 
    	if (wang.agent.astaragent.logout == 1)
    	{
		    System.out.print("mario.x  " + " sim " + sim.levelScene.mario.x + " true " + this.marioFloatPos[0] + " ");
		    System.out.println(" mario.y  " + " sim " + sim.levelScene.mario.y + " true " + this.marioFloatPos[1] + " ");
		    
		    for (int i = 0 ; i < this.sim.levelScene.getEnemiesFloatPos().length; i+=3)
		    	System.out.print(" kind " + this.sim.levelScene.getEnemiesFloatPos()[i] + 
		    			" x " + (this.sim.levelScene.getEnemiesFloatPos()[i+1] + this.sim.levelScene.mario.x) + 
		    			" y " + (this.sim.levelScene.getEnemiesFloatPos()[i+2] + this.sim.levelScene.mario.y));
		    System.out.println();
    	}
    	//System.out.println("wang after sim.advancestep " + (System.currentTimeMillis()-st));
    	
    	
    	boolean tmp = false;
		if (sim.levelScene.mario.x != this.marioFloatPos[0])
		{
			
			if (Math.abs(sim.levelScene.mario.x - this.marioFloatPos[0]) > 1)
				System.out.println("mario.x not same " + sim.levelScene.mario.x + " " + this.marioFloatPos[0]);
			tmp = true;
		}

		if (sim.levelScene.mario.y != this.marioFloatPos[1])
		{
			if (Math.abs(sim.levelScene.mario.y - this.marioFloatPos[1]) > 1)
				System.out.println("mario.y not same " + sim.levelScene.mario.y + " " + this.marioFloatPos[1]);
			tmp = true;
		}
		if (sim.levelScene.mario.getMode() != marioMode)
		{
			System.out.println("mario mode not same" + " true mario mode " + marioMode + " sim mario " + sim.levelScene.mario.getMode() );
		}

		if (tmp)
		{
			lost = 1;
			wang.agent.assistent.Mario.printflag = true;
			if (this.logout == 1)
			{
				System.out.println("levelscreen.");
				for(int i = 0; i < 19; i++)
				{
					for(int j = 0; j < 19; j++)
					{
						System.out.print(levelScene[i][j] + " ");
					}
					System.out.println();
				}
				System.out.println();
			}
			tmp = false;
		}else
		{
			lost = 0;
			//System.out.println("back to normal");
		}
		/**/
    	
		//System.out.println("wang before sim.setLevelPart " + (System.currentTimeMillis()-st));
		
		
		
		if (lost == 1)//(sim.levelScene.mario.x != marioFloatPos[0] || sim.levelScene.mario.y != marioFloatPos[1])
		{
			
			// Stop planning when we reach the goal (just assume we're in the goal when we don't move)
			//if (marioFloatPos[0] == lastX && marioFloatPos[1] == lastY)
			//	return ac;

			// Set the simulator mario to the real coordinates (x and y) and estimated speeds (xa and ya)
			//System.out.println("reset x y");
			sim.levelScene.mario.x = marioFloatPos[0];
			sim.levelScene.mario.xa = (marioFloatPos[0] - lastX) *0.89f;
			if (Math.abs(sim.levelScene.mario.y - marioFloatPos[1]) > 0.1f)
				sim.levelScene.mario.ya = (marioFloatPos[1] - lastY) * 0.85f;// + 3f;

			sim.levelScene.mario.y = marioFloatPos[1];
			//System.out.println("reset the mario position");
		}
		if (sim.levelScene.mario.getMode() != marioMode)
		{
			if (marioMode == 1)
			{
				System.out.println("reset mario mode large");
				sim.levelScene.mario.large = true;
				sim.levelScene.mario.fire = false;
			}
			if (marioMode == 2)
			{
				System.out.println("reset mario mode fire");
				sim.levelScene.mario.large = true;
				sim.levelScene.mario.fire = true;
			}
			if (marioMode == 0)
			{
				System.out.println("reset mario small");
				sim.levelScene.mario.large = false;
				sim.levelScene.mario.fire = false;
			}
		}
		sim.setLevelPart(levelScene, levelSceneEx, enemiesFloatPos, marioFloatPos);
		
		lastX = marioFloatPos[0];
		lastY = marioFloatPos[1];
		
		//System.out.println("wang after sim.setLevelPart " + (System.currentTimeMillis()-st));
		
		/*
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
		 */
		/*
		boolean tmpforenemy;
		int tmpenemycount = 0;
		for(int i = 0 ; i < sim.levelScene.sprites.size(); i ++)
		{
			if (sim.levelScene.sprites.get(i).kind == Sprite.KIND_GOOMBA ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_BULLET_BILL ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_ENEMY_FLOWER ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_GOOMBA_WINGED ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_GREEN_KOOPA ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_GREEN_KOOPA_WINGED ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_RED_KOOPA ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_RED_KOOPA_WINGED ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_SPIKY ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_SPIKY_WINGED ||
					sim.levelScene.sprites.get(i).kind == Sprite.KIND_SHELL)
			{
				
				tmpenemycount ++;
				
				System.out.print("kind " + sim.levelScene.sprites.get(i).kind + " x " + sim.levelScene.sprites.get(i).x + " y " + sim.levelScene.sprites.get(i).y);
				
			}
			System.out.println();
			
			
		}
		
		
		for(int i = 0 ; i < enemiesFloatPos.length; i +=3)
		{
			if (enemiesFloatPos[i] == Sprite.KIND_GOOMBA ||
					enemiesFloatPos[i] == Sprite.KIND_BULLET_BILL ||
					enemiesFloatPos[i] == Sprite.KIND_ENEMY_FLOWER ||
					enemiesFloatPos[i] == Sprite.KIND_GOOMBA_WINGED ||
					enemiesFloatPos[i] == Sprite.KIND_GREEN_KOOPA ||
					enemiesFloatPos[i] == Sprite.KIND_GREEN_KOOPA_WINGED ||
					enemiesFloatPos[i] == Sprite.KIND_RED_KOOPA ||
					enemiesFloatPos[i] == Sprite.KIND_RED_KOOPA_WINGED ||
					enemiesFloatPos[i] == Sprite.KIND_SPIKY ||
					enemiesFloatPos[i] == Sprite.KIND_SPIKY_WINGED ||
					enemiesFloatPos[i] == Sprite.KIND_SHELL)
			{
				
				tmpenemycount ++;
				
				System.out.print("kind " + enemiesFloatPos[i]  + " x " + (enemiesFloatPos[i+1] + this.marioFloatPos[0]) + " y " + (enemiesFloatPos[i+2] + this.marioFloatPos[1]));
				
			}
			System.out.println();
			
			
		}
		
		
		if (tmpenemycount != enemiesFloatPos.length/3)
			System.out.println("enemiescount not same");
		*/
		
		//System.out.println("enemiescount" + " tmpenemycount = " + tmpenemycount + " enemiesFloatPos.length = " + enemiesFloatPos.length);
		
		
		
		
		//System.out.println("after setenemiesFloatPos " + sim.levelScene.enemiesFloatsList.size());
		
		/*
		System.out.println("levelscreen.");
		for(int i = 0; i < 19; i++)
		{
			for(int j = 0; j < 19; j++)
			{
				System.out.print(levelScene[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		*/
		
	    /*
	    System.out.println("");
	    System.out.println("levelScene.length = " + levelScene.length + " levelScene[0].length = " + levelScene[0].length);
	    System.out.println("enemies.length = " + enemies.length + " enemies[0].length = " + enemies[0].length);
	    System.out.println("mergedObservation.length = " + mergedObservation.length + " mergedObservation[0].length = " + mergedObservation[0].length);
	    System.out.println("");
	    
	    if (marioFloatPos != null)
	    {
	    for (int i = 0; i < marioFloatPos.length; i++)
	    	System.out.println(marioFloatPos[i]/16);
	    System.out.println();
	    }
	    
	    for (int i = 0 ; i < levelScene.length; i++)
	    {
	    	for (int j = 0; j < levelScene[0].length; j++)
	    	{
	    		if (mergedObservation[i][j] != 0)
	    			System.out.print("1" + "  ");
	    		else
	    			System.out.print("0" + "  ");
	    	}
	    	 System.out.println("");
	    }
	    System.out.println("");
	    */
	    /*
	    for(int i = 0; i < enemiesFloatPos.length; i+=3)
	    {
	    	System.out.println("type = " + enemiesFloatPos[i] + " x = " + enemiesFloatPos[i+1] + " y = " + enemiesFloatPos[i+2]);
	    }
	    System.out.println();
	    System.out.println();
	    
	    
	    */
	    

	}
	
	public void giveIntermediateReward(float intermediateReward)
	{
	
	}
	
	public void reset()
	{
        action = new boolean[Environment.numberOfKeys];
        sim = new AStarSimulator();

	}
	
	public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol){}
	public String getName() { return name; }
	public void setName(String Name) { this.name = Name; }
	public boolean[] getAction(Environment observation){ return null; }
	public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState){}
}
