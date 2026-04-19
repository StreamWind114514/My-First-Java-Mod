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
import arc.assets.loaders.SoundLoader;
import arc.audio.Sound;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.gen.Bullet;
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

import testmod.content.FloatingWord;

import java.util.*;

public class Turret {
    public static Block PokerTurret;
    public static Sound nonesound;
    public static Sound cardhit;
    private static final Map<String, TextureRegion> cardImages = new HashMap<>();
    public static void loadSounds() {
        nonesound = new Sound();
        String nonesoundpath = "sounds/nonesound.ogg";
        Core.assets.load(nonesoundpath, Sound.class, new SoundLoader.SoundParameter(nonesound));
        cardhit = new Sound();
        String cardhitpath = "sounds/cardhit.ogg";
        Core.assets.load(cardhitpath, Sound.class, new SoundLoader.SoundParameter(cardhit));
    }
    
    public static void loadAllCardImages() {
        String modName = Vars.mods.getMod(TestMod.class).name;
        String[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
        for (String suit : suits) {
            for (int value = 2; value <= 14; value++) {
                String regionName = modName + "-" + suit + value;
                TextureRegion region = Core.atlas.find(regionName);
                if (region.found()) {
                    cardImages.put(suit + value, region);
                } else {
                    Log.warn("Missing card image: " + regionName);
                }
            }
        }
        TextureRegion bregion = Core.atlas.find(modName + "-cardback");
        if (bregion.found()) {
        cardImages.put("cardback", bregion);
        } else {
            Log.warn("Missing cardback image: " + modName + "-cardback");
        }
    }

    public static void load() {
        loadSounds();
        loadAllCardImages();
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
            recoil = 0f;
            shootSound = nonesound;
            
        }
    }
    protected static class CardBulletType extends BasicBulletType {
        private final TextureRegion cardRegion;
        
        public CardBulletType(TextureRegion region, float damage) {
            super(8f, damage); // 速度8，伤害由参数决定
            this.cardRegion = region;
            keepVelocity = false;
            hitEffect = Fx.hitBulletSmall;
            smokeEffect = Fx.none;
            shootEffect = Fx.none;
            despawnEffect = Fx.none;
            width = 32f;
            height = 42f;
            collides = true;
            collidesGround = true;
            collidesAir = true;
            collidesTiles = false;
            absorbable = false;
        }
        
        @Override
        public void draw(Bullet b) {
            Draw.rect(cardRegion, b.x, b.y, b.rotation());
        }
    }
    protected static class PokerBulletType extends BulletType {
        PokerBulletType() {
            super(0f, 0f);
            keepVelocity = false;
            hitEffect = Fx.none;
            smokeEffect = Fx.none;
            shootEffect = Fx.none;
            despawnEffect = Fx.none;
            hitSound = cardhit;
        }
        
        @Override
        public void init(Bullet b) {
            Seq<Object> result;
            String kind;
            int multiply;
            int[] chooseCards;
            Seq<Card> card = new Seq<>();
            for (int i = 0; i < 5; i++) {
                int value = Mathf.random(2, 14);
                Suit suit = Suit.values()[Mathf.random(0, 3)]; // 随机抽牌
                card.add(new Card(value, suit));
            }
            // 判断，获取
            result = analysisCards(card);
            kind = (String) result.get(0);
            multiply = (int) result.get(1);
            int[] tempArray = (int[]) result.get(2);
            chooseCards = tempArray.clone();
            // 获取target
            
        }
    
        
        public Seq<Object> analysisCards(Seq<Card> cards) {
            Seq<Object> result = new Seq<>();
            int[] values = new int[5];
            int[] key = new int[13];
            for (int i = 0; i < 5; i++) {
                values[i] = cards.get(i).value;
                key[cards.get(i).value - 2] ++;
            }
            // 判断
            int[] sortedValues = values.clone();
            Arrays.sort(sortedValues);
            boolean straight = isStraight(sortedValues);
            boolean onePair = isOnePair(key);
            boolean twoPair = isTwoPair(key);
            boolean threeKind = isThreeKind(key);
            boolean fullHouse = isFullHouse(key);
            boolean fourKind = isFourKind(key);
            if (straight) {
                result.add("Straight! (*10)");
                result.add(10);
                result.add(values);
            } else if (fullHouse) {
                result.add("fullHouse! (*20)");
                result.add(20);
                result.add(values);
            } else if (fourKind) {
                result.add("Four of a kind! (*40)");
                result.add(40);
                int temp = -1;
                int[] choose = new int[5];
                for (int i = 0; i < 5; i++) {
                    if (key[i] == 4) {
                        temp = i + 2;
                        break;
                    }
                }
                for (int i = 0; i < 5; i++) {
                    choose[i] = (values[i] == temp) ? temp : 0;
                }
                result.add(choose);
            } else if (threeKind) {
                result.add("Three of a kind! (*10)");
                result.add(10);
                int temp = -1;
                int[] choose = new int[5];
                for (int i = 0; i < 5; i++) {
                    if (key[i] == 3) {
                        temp = i + 2;
                        break;
                    }
                }
                for (int i = 0; i < 5; i++) {
                    choose[i] = (values[i] == temp) ? temp : 0;
                }
                result.add(choose);
            } else if (twoPair) {
                result.add("Two pair! (*5)");
                result.add(5);
                int temp1 = -1;
                int temp2 = -1;
                int[] choose = new int[5];
                out:
                for (int i = 0; i < 5; i++) {
                    if (key[i] == 2) {
                        temp1 = i + 2;
                        for (int j = i + 1; j < 5; j++) {
                            if (key[j] == 2) {
                                temp2 = j + 2;
                                break out;
                            }
                        }
                    }
                }
                for (int i = 0; i < 5; i++) {
                    choose[i] = (values[i] == temp1) ? temp1: (values[i] == temp2) ? temp2: 0;
                }
                result.add(choose);
            } else if (onePair) {
                result.add("A pair! (*2)");
                result.add(2);
                int temp = -1;
                int[] choose = new int[5];
                for (int i = 0; i < 5; i++) {
                    if (key[i] == 2) {
                        temp = i + 2;
                        break;
                    }
                }
                for (int i = 0; i < 5; i++) {
                    choose[i] = (values[i] == temp) ? temp : 0;
                }
                result.add(choose);
            } else {
                result.add("High card! (*1)");
                result.add(1);
                int[] choose = new int[5];
                int maxIndex = 0;
                for (int i = 1; i < 5; i++) {
                    if (values[i] > values[maxIndex]) maxIndex = i;
                }
                choose[maxIndex] = values[maxIndex];
                result.add(choose);
            }
            
            
            return result;

        }
        // 顺子
        private boolean isStraight (int[] card) {
            for (int i = 0; i < 4; i++) {
                if (card[i + 1] != card[i] + 1) return false;
            }
            return true;
        }
        // 对子
        private boolean isOnePair(int[] cardKey) {
            int pair = 0;
            for (int i: cardKey) {
                if (i == 2) pair += 1;
            }
            if (pair == 1) return true;
            return false;
        }
        // 两对
        private boolean isTwoPair(int[] cardKey) {
            int pair = 0;
            for (int i: cardKey) {
                if (i == 2) pair += 1;
            }
            if (pair == 2) return true;
            return false;
        }
        // 三条
        private boolean isThreeKind(int[] cardKey) {
            int pair = 0;
            for (int i: cardKey) {
                if (i == 3) pair += 1;
            }
            if (pair == 1) return true;
            return false;
        }
        // 四条
        private boolean isFourKind(int[] cardKey) {
            int pair = 0;
            for (int i: cardKey) {
                if (i == 4) pair += 1;
            }
            if (pair == 1) return true;
            return false;
        }
        // 满堂红
        private boolean isFullHouse(int[] cardKey) {
            int pair2 = 0;
            int pair3 = 0;
            for (int i: cardKey) {
                if (i == 2) pair2 += 1;
                if (i == 3) pair3 += 1;
            }
            if (pair2 == 1 && pair3 == 1) return true;
            return false;
        }
        
        // 卡牌类
        public static class Card {
            int value;
            Suit suit;
            Card(int value, Suit suit) {
                 this.value = value;
                 this.suit = suit;
            }
            public String getImgName() {
                return suit.name() + value;
            }
        }
        // 花色枚举
        enum Suit{
            CLUBS, DIAMONDS, HEARTS, SPADES
        }
    }
}
