package com.valkryst.VTerminal_Tutorial.gui.palette.rarity;

import com.valkryst.VTerminal.palette.ColorPalette;
import lombok.Getter;

import java.awt.*;

public class LegendaryColorPalette extends ColorPalette {
        @Getter private final Color radioButton_defaultBackground = new Color(45, 45, 45, 255);
        @Getter private final Color radioButton_defaultForeground = new Color(0xFF8000);
        @Getter private final Color radioButton_pressedBackground = radioButton_defaultBackground;
}