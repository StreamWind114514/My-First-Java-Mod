package testmod.content;
/* 
* 谁说我英文差啊，这英文太好了我的天哪
* 多行注释是这样写的吗
* 这什么构思代码
*/
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.Interp;
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
import testmod.TestMod;

import java.util.*;

public class Turret {
    public static Block PokerTurret;
    public static Sound nonesound;
    public static Sound cardhit;
    public static Sound card;
    // 常量，卡牌长宽
    private static final float CARD_W = 12.125f;
    private static final float CARD_H = 15.875f;
    
    // 效果
    // 特效1:抽牌，持续30帧
    protected static Effect CardDeal = new Effect(30f, e -> {
        if (!(e.data instanceof Object[] data)) return;
        TextureRegion region = (TextureRegion) data[0];
        float startX = (float) data[1];
        float startY = (float) data[2];
        float endX = (float) data[3];
        float endY = (float) data[4];

        float progress = e.fin(Interp.pow2Out);
        float scale = progress;
        float x = startX + (endX - startX) * progress;
        float y = startY + (endY - startY) * progress;
        Draw.rect(region, x, y, CARD_W * scale, CARD_H * scale, 0);
    });

    // 特效2:翻牌，持续15帧
    protected static Effect CardFlip = new Effect(15f, e -> {
        if (!(e.data instanceof Object[] data)) return;
        TextureRegion region = (TextureRegion) data[0];
        float x = (float) data[1];
        float y = (float) data[2];

        float progress = e.fout();
        float width = CARD_W * progress;
        Draw.rect(region, x, y, width, CARD_H, 0);
    });
    
    // 加载音效
    public static void loadSounds() {
        nonesound = new Sound();
        String nonesoundpath = "sounds/nonesound.ogg";
        Core.assets.load(nonesoundpath, Sound.class, new SoundLoader.SoundParameter(nonesound));
        cardhit = new Sound();
        String cardhitpath = "sounds/cardhit.ogg";
        Core.assets.load(cardhitpath, Sound.class, new SoundLoader.SoundParameter(cardhit));
        card = new Sound();
        String cardpath = "sounds/card.ogg";
        Core.assets.load(cardpath, Sound.class, new SoundLoader.SoundParameter(card));
    }
    public static void load() {
        loadSounds();
        PokerTurret = new PokerTurretBlock("PokerTurret");
    }
    
    // 炮塔类
    public static class PokerTurretBlock extends PowerTurret {
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
    
    // 炮弹类，真正的炮弹
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
            hitSound = cardhit;
            homingPower = 0.1f;
            homingRange = 100f;
        }
        
        @Override
        public void draw(Bullet b) {
            Draw.rect(cardRegion, b.x, b.y, b.rotation());
        }
    }
    
    // 炮弹类，仅用来作触发的假子弹
    protected static class PokerBulletType extends BulletType {
        PokerBulletType() {
            super(0f, 0f);
            keepVelocity = false;
            hitEffect = Fx.none;
            smokeEffect = Fx.none;
            shootEffect = Fx.none;
            despawnEffect = Fx.none;
            hitSound = cardhit;
            lifetime = 100f;
        }
        
        // 主要
        @Override
        public void init(Bullet b) {
            TextureRegion cardBackRegion = TestMod.cardImages.get("cardback");
            // 获取炮塔坐标等
            if (!(b.owner instanceof Building turret)) return;
            float turretX = turret.x;
            float turretY = turret.y;
            float targetX = b.aimX;
            float targetY = b.aimY;
            
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
            
            // 开始动画
            
            // 依次抽牌（每隔4f抽一张）
            for (int i = 0; i < 5; i++) {
                final int idx = i;
                float delay = idx * 4f;   // 0, 4, 8, 12, 16
                Time.run(delay, () -> {
                    CardDeal.at(turretX, turretY, 0f, Color.white, new Object[]{cardBackRegion, turretX, turretY, turret + CARD_W * (idx - 2), turretY + CARD_H});
                });
            }

            // 最后一张牌抽完的延迟
            float lastDealFinishDelay = 16f + 30f;
            Time.run(lastDealFinishDelay, () -> {
            // 同时翻开所有牌
                CardFlip.at(turret + CARD_W * (idx - 2), turretY + CARD_H, 0f, Color.white, new Object[]{cardBackRegion, turret + CARD_W * (idx - 2), turretY + CARD_H});
            });
        }
    
        // 判断牌组的种类，倍数，每个牌的伤害映射数组
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
        
        // 内部类，卡牌类
        public static class Card {
            int value;
            Suit suit;
            Card(int value, Suit suit) {
                 this.value = value;
                 this.suit = suit;
            }
            public String getImgName() {
                return suit.name().toLowerCase() + value;
            }
        }
        // 花色枚举
        enum Suit{
            CLUBS, DIAMONDS, HEARTS, SPADES
        }
    }
}
    