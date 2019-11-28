package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

abstract class GameObject {
    int x, y;

    abstract void render(GraphicsContext gc);
    abstract void update();
}
abstract class EnemyObject extends GameObject {
    double x = Config.Spawner_x;
    double y = Config.Spawner_y;
    int ID;
    public int health;
    public int reward;
    public double speed;
    boolean pass = false;
    Image EnemyImg;
    Direction direction;
    int healthSpace;
}
abstract class TowerObject extends GameObject {
    int ID;
    public int damage;
    public int price;
    public double range;
    public double spaw;
    Image TowerImg;
    List<BulletObject> bulletofTower = new ArrayList<>();
}
abstract class BulletObject extends GameObject {
    boolean hit = false;
    int ID;
    public int damage;
    public double speed;
    public double range;
    Image BulletImg;
    Direction direction;
    EnemyObject enemy;
}
