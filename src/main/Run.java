package main;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Run {
    int HP = Config.Player_HP;
    int MONEY = Config.Player_Money;

    private static CreateEnemy ce = new CreateEnemy();

    static List<EnemyObject> enemy() {
        List<EnemyObject> e = new ArrayList<>();

        for (int i=0; i<5; i++) {
            e.add(ce.creNormalEnemy());
        }
        for (int i=0; i<3; i++) {
            e.add(ce.creTankerEnemy());
        }
        for (int i=0; i<3; i++) {
            e.add(ce.creSmallerEnemy());
        }
        for (int i=0; i<3; i++) {
            e.add(ce.creNormalEnemy());
            e.add(ce.creNormalEnemy());
            e.add(ce.creTankerEnemy());
            e.add(ce.creSmallerEnemy());
            e.add(ce.creSmallerEnemy());
        }
        for (int i=0; i<5; i++) {
            e.add(ce.creBossEnemy());
            e.add(ce.creNormalEnemy());
            e.add(ce.creNormalEnemy());
            e.add(ce.creTankerEnemy());
            e.add(ce.creSmallerEnemy());
            e.add(ce.creSmallerEnemy());
        }

        return e;
    }
}
