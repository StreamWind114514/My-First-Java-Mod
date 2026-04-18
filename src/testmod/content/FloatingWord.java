package testmod.content;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.math.Interp;
import arc.util.*;
import arc.graphics.Color;
import mindustry.entities.Effect;
import arc.graphics.g2d.Lines;
import mindustry.ui.Fonts;
import mindustry.graphics.Pal;

public class FloatingWord {
    public static Effect floatingText = new Effect(60f, 80f, e -> {
        if (!(e.data instanceof Object[] data)) return;
        String text = (String) data[0];
        Color color = (Color) data[1];
        
        Font font = Fonts.outline;
        font.setColor(color);
        
        float textWidth = font.getBounds(text).width;
        float drawX = e.x - textWidth / 2; // 计算居中 X 坐标
        float yOffset = e.fout(Interp.pow2Out) * 24f; // 上浮效果，距离为 24 像素
        font.draw(text, drawX, e.y + yOffset);
    });
}