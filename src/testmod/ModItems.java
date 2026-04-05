package testmod;

import mindustry.type.Item;
import arc.graphics.Color;

public class ModItems {

    // 声明一个公共静态物品变量，这样在其他地方（比如方块配方）可以直接引用
    public static Item myFirstItem;

    // 这个 load 方法会被 TestMod.loadContent() 调用
    public static void load() {
        // 创建一个新物品，内部名字叫 "my-first-item"
        myFirstItem = new Item("my-first-item") {{
            // 设置物品显示名称（直接写中文，简单方便）
            localizedName = "我的第一个物品";
            // 设置物品描述
            description = "这是我模组里的第一个物品，可以用来合成。";
            // 可选：设置物品颜色（RGB，这里用青色）
            color = Color.valueOf("00FFFF");
            // 可选：设置物品硬度（影响钻头采矿时间，默认0）
            hardness = 1;
        }};
        
        // 如果你以后想添加更多物品，继续在这里写：
        // public static Item anotherItem;
        // anotherItem = new Item("another-item") {{ ... }};
    }
}