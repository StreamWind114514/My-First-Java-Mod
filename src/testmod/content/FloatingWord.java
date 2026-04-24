package testmod.content;

import arc.graphics.g2d.Font;
import arc.math.Interp;
import arc.graphics.Color;
import arc.util.Align;
import mindustry.entities.Effect;
import mindustry.ui.Fonts;

public class FloatingWord {
    public static Effect floatingText = new Effect(60f, 80f, e -> {
        lightOpacity = 0f;
        if (!(e.data instanceof Object[] data)) return;  
        String text = (String) data[0];  
        Color color = (Color) data[1];  
        float alpha = 1 - e.fin();  
        Font font = Fonts.outline;  
  
        boolean ints = font.usesIntegerPositions();  
        font.setUseIntegerPositions(false);  
        font.getData().setScale(0.25f);  // 调整这个值控制大小，默认是 1f  
  
        font.setColor(color.r, color.g, color.b, alpha);  
        float yOffset = e.fin(Interp.pow2Out) * 24f;  
        font.draw(text, e.x, e.y + yOffset, Align.center);  
  
        font.getData().setScale(1f);
        font.setColor(Color.white);  
        font.setUseIntegerPositions(ints);  
    });
}