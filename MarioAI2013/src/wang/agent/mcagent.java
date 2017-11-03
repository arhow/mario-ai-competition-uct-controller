package wang.agent;

import java.util.ArrayList;
import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import wang.agent.assistent.*;

public class mcagent implements Agent
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
	//LevelScene levelscene = null;
	MCSimulator sim = null;
	public long st;
	private float lastX;
	private float lastY;
	int debuglog = 6;
	int lost = 0;
	
	public boolean[] getAction(){
		
		action = sim.search(st);
	    return action;
	}
	
	public void integrateObservation(Environment environment){
		st = System.currentTimeMillis();
	    levelScene = environment.getLevelSceneObservationZ(zLevelScene);
	    levelSceneEx = environment.getLevelSceneObservationZ(0);
	    enemies = environment.getEnemiesObservationZ(zLevelEnemies);
	    mergedObservation = environment.getMergedObservationZZ(1, 0);
	    this.marioFloatPos = environment.getMarioFloatPos();
	    this.enemiesFloatPos = environment.getEnemiesFloatPos();
	    this.marioState = environment.getMarioState();
	    //environment.getEnemiesObservationZ(0);
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
	    sim.advanceStep(action);
    	boolean tmp = false;
    	if ((boolean)isMarioOnGround != sim.levelScene.mario.isOnGround())
		if (sim.levelScene.mario.x != this.marioFloatPos[0])
		if (sim.levelScene.mario.y != this.marioFloatPos[1])
		if (sim.levelScene.mario.getMode() != marioMode){
			tmp = true;
		}
		if (tmp){
			lost = 1;
			wang.agent.assistent.Mario.printflag = true;
			tmp = false;
		}
		else{
			lost = 0;
		}
		if (lost == 1)
		{
			sim.levelScene.mario.x = marioFloatPos[0];
			sim.levelScene.mario.xa = (marioFloatPos[0] - lastX) *0.89f;
			if (Math.abs(sim.levelScene.mario.y - marioFloatPos[1]) > 0.1f)
				sim.levelScene.mario.ya = (marioFloatPos[1] - lastY) * 0.85f;// + 3f;
			sim.levelScene.mario.y = marioFloatPos[1];
			//System.out.println("reset the mario position");
			sim.levelScene.mario.setOnGround(isMarioOnGround);
			if (sim.levelScene.mario.getMode() != marioMode)
			{
				if (marioMode == 1)
				{
					sim.levelScene.mario.large = true;
					sim.levelScene.mario.fire = false;
				}
				if (marioMode == 2)
				{
					sim.levelScene.mario.large = true;
					sim.levelScene.mario.fire = true;
				}
				if (marioMode == 0)
				{
					sim.levelScene.mario.large = false;
					sim.levelScene.mario.fire = false;
				}
			}
		}
		sim.setLevelPart(levelScene, levelSceneEx, enemiesFloatPos, marioFloatPos);
		sim.setSprite(enemies, marioFloatPos);
		lastX = marioFloatPos[0];
		lastY = marioFloatPos[1];
	}
	
	public void giveIntermediateReward(float intermediateReward){
	
	}
	
	public void reset(){
		action = new boolean[Environment.numberOfKeys];
		sim = new MCSimulator();
	}
	
	public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol){}
	public String getName() { return name; }
	public void setName(String Name) { this.name = Name; }
	public boolean[] getAction(Environment observation){ return null; }
	public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState){}
}
