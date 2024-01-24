package com.troylu.twenty.frame;

import java.awt.*;
import javax.swing.*;

public class ListPanel extends JPanel {

    final float ALIGNMENT_X;

    public ListPanel(float alignmentX) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.ALIGNMENT_X = alignmentX;
    }

    @Override
    public Component add(Component component) {
        ((JComponent) component).setAlignmentX(ALIGNMENT_X);

        return super.add(component);
    }

}
