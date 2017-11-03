package wang.agent.assistent;



public class Princess extends Sprite
{
	
	private LevelScene world;
	private int runTime = 0;

	public Princess(LevelScene world, int x, int y, int mapX, int mapY)
	{
	    kind = KIND_PRINCESS;
	    sheet = Art.princess;

	    this.x = x;
	    this.y = y;
	    this.world = world;
	    this.mapX = mapX;
	    this.mapY = mapY;
	    yPic = 0;
	    xPic = 0;
	}

	public void collideCheck()
	{}

	public void move()
	{
	    runTime += 5;

	    xPic = (runTime / 20) % 2;
	}


}
