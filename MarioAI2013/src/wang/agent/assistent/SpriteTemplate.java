package wang.agent.assistent;



public class SpriteTemplate implements Cloneable
{
	public int lastVisibleTick = -1;
	public Sprite sprite;
	public boolean isDead = false;
	private boolean winged;

	private static final long serialVersionUID = -6585112454240065011L;

	public int getType()
	{
	    return type;
	}

	private int type;
	
	public Object clone() throws CloneNotSupportedException
    {
    	return super.clone();
    	
    }

	public SpriteTemplate(int type)
	{
	    this.type = type;
	    switch (type)
	    {
	        case Sprite.KIND_GOOMBA:
	            this.winged = false;
	            break;
	        case Sprite.KIND_GREEN_KOOPA:
	            this.winged = false;
	            break;
	        case Sprite.KIND_RED_KOOPA:
	            this.winged = false;
	            break;
	        case Sprite.KIND_SPIKY:
	            this.winged = false;
	            break;
	        case Sprite.KIND_GOOMBA_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_GREEN_KOOPA_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_RED_KOOPA_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_SPIKY_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_ENEMY_FLOWER:
	            this.winged = false;
	            break;
	        case Sprite.KIND_BULLET_BILL:
	            this.winged = false;
	            break;
	        case Sprite.KIND_PRINCESS:
	            this.winged = false;
	            break;
	        case Sprite.KIND_WAVE_GOOMBA:
	            this.winged = true;
	            break;
	    }
	}
	
	public SpriteTemplate(int type, boolean winged)
	{
	    this.type = type;
	    this.winged = winged;
	    switch (type)
	    {
	        case Sprite.KIND_GOOMBA:
	            this.winged = false;
	            break;
	        case Sprite.KIND_GREEN_KOOPA:
	            this.winged = false;
	            break;
	        case Sprite.KIND_RED_KOOPA:
	            this.winged = false;
	            break;
	        case Sprite.KIND_SPIKY:
	            this.winged = false;
	            break;
	        case Sprite.KIND_GOOMBA_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_GREEN_KOOPA_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_RED_KOOPA_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_SPIKY_WINGED:
	            this.winged = true;
	            break;
	        case Sprite.KIND_ENEMY_FLOWER:
	            this.winged = false;
	            break;
	        case Sprite.KIND_BULLET_BILL:
	            this.winged = false;
	            break;
	        case Sprite.KIND_PRINCESS:
	            this.winged = false;
	            break;
	        case Sprite.KIND_WAVE_GOOMBA:
	            this.winged = true;
	            break;
	    }
	}

	public void spawn(LevelScene levelScene, int x, int y, int dir)
	{
	    if (isDead) return;

	    if (type == Sprite.KIND_ENEMY_FLOWER)
	    {
	        sprite = new FlowerEnemy(levelScene, x * 16 + 15, y * 16 + 24, x, y);
	    } else if (type == Sprite.KIND_WAVE_GOOMBA)
	    {
	        sprite = new WaveGoomba(levelScene, x * 16 + 8, y * 16 + 15 - 3 * 16, dir, x, y);
	    } else if (type == Sprite.KIND_PRINCESS)
	    {
	        sprite = new Princess(levelScene, x * 16 - 16, y * 16 - 15, x, y);
	    } else
	    {
//	            sprite = new Enemy(levelScene, x*16+8, y*16+15, dir, type, winged);
	        sprite = new Enemy(levelScene, x * 16 + 8, y * 16 + 15, dir, type, winged, x, y);
	    }
	    sprite.spriteTemplate = this;
	    levelScene.addSprite(sprite);
	}

}
