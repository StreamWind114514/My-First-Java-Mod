//i am bad in english
package testmod.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.*;
import arc.struct.Seq;
import arc.Core;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.Block;
import mindustry.Vars;
import mindustry.audio.SoundControl;
import arc.audio.Music;

public class Turret {
    public static Block PokerTurret;

    public static void load() {
        PokerTurret = new PokerTurretBlock("PokerTurret");
    }

    public static class PokerTurretBlock extends PowerTurret {
        private static final float CARD_W = 97f, CARD_H = 127f;
        public PokerTurretBlock(String name) {
            super(name);
            requirements(Category.turret, ItemStack.with(Items.copper, 500, Items.metaglass, 100, Items.silicon, 250));
            health = 1000;
            size = 2;
            targetAir = true;
            targetGround = true;
            range = 300;
            reload = 90f;
            shootCone = 360f;
            rotateSpeed = 8f;
            consumePower(3.3f);
            shootType = new PokerBulletType();
            recoil ＝ 0f
            shootSound = Sounds.none;
            
        }
    }
}
class PokerBulletType extends BulletType {
    PokerBulletType() {
        super(0f, 0f);
        keepVelocity = false;
        hitEffect = Fx.none;
        smokeEffect = Fx.none;
        shootEffect = Fx.none;
        despawnEffect = Fx.none;
    }
}
