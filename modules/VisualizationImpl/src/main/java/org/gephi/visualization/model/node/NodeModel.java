/*
 Copyright 2008-2010 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.visualization.model.node;

import java.awt.geom.Rectangle2D;
import org.gephi.graph.api.ElementProperties;
import org.gephi.graph.api.Node;
import org.gephi.lib.gleem.linalg.Vecf;
import org.gephi.visualization.model.Model;
import org.gephi.visualization.model.TextModel;
import org.gephi.visualization.octree.Octant;

/**
 *
 * @author mbastian
 */
public abstract class NodeModel implements Model, TextModel {

    protected final Node node;
    protected float viewportX;
    protected float viewportY;
    protected float cameraDistance;
    protected float viewportRadius;
    protected float[] dragDistance;
    //Octant
    protected Octant octant;
    protected int octantId;
    //Flags
    protected boolean selected;
    protected boolean highlight;
    public int markTime;
    public boolean mark;
    //Text
    protected Rectangle2D bounds;

    public NodeModel(Node node) {
        this.node = node;

        //Default
        dragDistance = new float[2];
        selected = false;
        mark = false;
        markTime = 0;
    }

    public int octreePosition(float centerX, float centerY, float centerZ, float size) {
        //float radius = obj.getRadius();
        int index = 0;

        if (node.y() < centerY) {
            index += 4;
        }
        if (node.z() > centerZ) {
            index += 2;
        }
        if (node.x() < centerX) {
            index += 1;
        }

        return index;
    }

    public boolean isInOctreeLeaf(Octant leaf) {
        float radius = node.size() / 2f;
        if (Math.abs(node.x() - leaf.getPosX()) > (leaf.getSize() / 2 - radius)
                || Math.abs(node.y() - leaf.getPosY()) > (leaf.getSize() / 2 - radius)
                || Math.abs(node.z() - leaf.getPosZ()) > (leaf.getSize() / 2 - radius)) {
            return false;
        }
        return true;
    }

    public abstract boolean selectionTest(Vecf distanceFromMouse, float selectionSize);

    public abstract float getCollisionDistance(double angle);

    public Node getNode() {
        return node;
    }

    public void setCameraDistance(float cameraDistance) {
        this.cameraDistance = cameraDistance;
    }

    public float getCameraDistance() {
        return cameraDistance;
    }

    public float getViewportRadius() {
        return viewportRadius;
    }

    public void setViewportRadius(float viewportRadius) {
        this.viewportRadius = viewportRadius;
    }

    public float getViewportX() {
        return viewportX;
    }

    public void setViewportX(float viewportX) {
        this.viewportX = viewportX;
    }

    public float getViewportY() {
        return viewportY;
    }

    public void setViewportY(float viewportY) {
        this.viewportY = viewportY;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Octant getOctant() {
        return octant;
    }

    public void setOctant(Octant octant) {
        this.octant = octant;
    }

    public void setOctantId(int octantId) {
        this.octantId = octantId;
    }

    public int getOctantId() {
        return octantId;
    }

    @Override
    public boolean hasCustomTextColor() {
        return node.getTextProperties().getR() > 0;
    }

    @Override
    public void setText(String text) {
        node.getTextProperties().setText(text);
    }

    @Override
    public float getTextWidth() {
        Rectangle2D rec = bounds;
        if (rec != null) {
            return (float) rec.getWidth();
        }
        return 0f;
    }

    @Override
    public float getTextHeight() {
        Rectangle2D rec = bounds;
        if (rec != null) {
            return (float) rec.getHeight();
        }
        return 0f;
    }

    @Override
    public void setTextBounds(Rectangle2D bounds) {
        this.bounds = bounds;
    }

    @Override
    public String getText() {
        String t = node.getTextProperties().getText();
        if (t == null) {
            return node.getLabel();
        }
        return t;
    }

    @Override
    public float getTextSize() {
        return node.getTextProperties().getSize();
    }

    @Override
    public float getTextR() {
        return node.getTextProperties().getR();
    }

    @Override
    public float getTextG() {
        return node.getTextProperties().getG();
    }

    @Override
    public float getTextB() {
        return node.getTextProperties().getB();
    }

    @Override
    public float getTextAlpha() {
        return node.getTextProperties().getAlpha();
    }

    @Override
    public boolean isTextVisible() {
        return node.getTextProperties().isVisible();
    }

    @Override
    public ElementProperties getElementProperties() {
        return node;
    }
}
