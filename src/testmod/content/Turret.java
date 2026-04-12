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

import testmod.content.FloatingWord;

public class Turret {
    public static Block PokerTurret;
    
    public static void load() {
        PokerTurret = new PowerTurret("PokerTurret") {
            {
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
                shootType = new BasicBulletType(0f, 0f) {{
                    lifetime = 0f;
                    keepVelocity = false;
                    hitEffect = Fx.none;
                    smokeEffect = Fx.none;
                    shootEffect = Fx.none;
                    despawnEffect = Fx.none;
                }};
            }
            @Override
            protected void shoot(BulletType type) {
                Seq<Card> hand = new Seq<>();
                for (int i = 0; i < 5; i++) {
                    int rank = Mathf.random(1, 13);
                    Suit suit = Suit.values()[Mathf.random(0, 3)];
                    hand.add(new Card(rank, suit));
                }
            }
    };
        
    }
    //usingvalue
    enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES}
        
    static class Card {
        int rank;
        Suit suit;
        Card(int rank, Suit suit) {
            this.rank = rank;
            this.suit = suit;
        }
    
        TextureRegion getRegion() {
            String regionName = suit.name() + rank;
            return Core.atlas.find(regionName);
        }
    }

}
