package testmod.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.*;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;

public class Turret {
    public static Block PokerTurret;
    
    public static void load() {
        PokerTurret = new PowerTurret("PokerTurret") {{
            requirements(Category.turret, ItemStark.with(Items.copper, 500, Items.metaglass, 100, Items.silicon, 250));
            health = 1000;
            size = 2;
            targetAir = true;
            range = 300;
            reload = 90f;
            shootCone = 0f;
            rotateSpeed = 8f;
        }};
    }
}


