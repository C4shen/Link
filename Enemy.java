/**
 * Abstrakte Klasse Enemy
 * 
 * @author 
 * @version
 */
public abstract class Enemy extends Creature
{
    public Enemy(int x, int y, String name, SpriteSheet enemySprite, int health, int speed) 
    {
        super(name, enemySprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, health, speed);
    }
}
