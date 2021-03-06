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
package org.gephi.io.processor.plugin;

import java.awt.Color;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.attribute.api.Origin;
import org.gephi.attribute.api.Table;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.ColumnDraft;
import org.gephi.io.importer.api.ContainerUnloader;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.project.api.Workspace;

/**
 *
 * @author Mathieu Bastian
 */
public abstract class AbstractProcessor {

    protected Workspace workspace;
    protected ContainerUnloader container;
    protected AttributeModel attributeModel;

    protected void flushColumns() {
        Table nodeTable = attributeModel.getNodeTable();
        for (ColumnDraft col : container.getNodeColumns()) {
            if (!nodeTable.hasColumn(col.getId())) {
                nodeTable.addColumn(col.getId(), col.getTitle(), col.getTypeClass(), Origin.DATA, col.getDefaultValue(), true);
            }
        }
        Table edgeTable = attributeModel.getEdgeTable();
        for (ColumnDraft col : container.getEdgeColumns()) {
            if (!edgeTable.hasColumn(col.getId())) {
                edgeTable.addColumn(col.getId(), col.getTitle(), col.getTypeClass(), Origin.DATA, col.getDefaultValue(), true);
            }
        }
    }

    protected void flushToNode(NodeDraft nodeDraft, Node node) {

        if (nodeDraft.getColor() != null) {
            node.setColor(nodeDraft.getColor());
        }

        if (nodeDraft.getLabel() != null) {
            node.setLabel(nodeDraft.getLabel());
        }

        if (node.getTextProperties() != null) {
            node.getTextProperties().setVisible(nodeDraft.isLabelVisible());
        }

        if (nodeDraft.getLabelColor() != null && node.getTextProperties() != null) {
            Color labelColor = nodeDraft.getLabelColor();
            node.getTextProperties().setColor(labelColor);
        }

        if (nodeDraft.getLabelSize() != -1f && node.getTextProperties() != null) {
            node.getTextProperties().setSize(nodeDraft.getLabelSize());
        }

        node.setX(nodeDraft.getX());
        node.setY(nodeDraft.getY());
        node.setZ(nodeDraft.getZ());

        if (nodeDraft.getSize() != 0 && !Float.isNaN(nodeDraft.getSize())) {
            node.setSize(nodeDraft.getSize());
        } else {
            node.setSize(10f);
        }

        //Attributes
        flushToNodeAttributes(nodeDraft, node);
    }

    protected void flushToNodeAttributes(NodeDraft nodeDraft, Node node) {
        for (ColumnDraft col : container.getNodeColumns()) {
            Object val = nodeDraft.getValue(col.getId());
            if (val != null) {
                node.setAttribute(col.getId(), val);
            }
        }
    }

    protected void flushToEdge(EdgeDraft edgeDraft, Edge edge) {
        if (edgeDraft.getColor() != null) {
            edge.setColor(edgeDraft.getColor());
        } else {
            edge.setR(-1f);
            edge.setG(-1f);
            edge.setB(-1f);
        }

        if (edgeDraft.getLabel() != null) {
            edge.setLabel(edgeDraft.getLabel());
        }

        if (edge.getTextProperties() != null) {
            edge.getTextProperties().setVisible(edgeDraft.isLabelVisible());
        }

        if (edgeDraft.getLabelSize() != -1f && edge.getTextProperties() != null) {
            edge.getTextProperties().setSize(edgeDraft.getLabelSize());
        }

        if (edgeDraft.getLabelColor() != null && edge.getTextProperties() != null) {
            Color labelColor = edgeDraft.getLabelColor();
            edge.getTextProperties().setColor(labelColor);
        }

        //Attributes
        flushToEdgeAttributes(edgeDraft, edge);
    }

    protected void flushToEdgeAttributes(EdgeDraft edgeDraft, Edge edge) {
        for (ColumnDraft col : container.getEdgeColumns()) {
            Object val = edgeDraft.getValue(col.getId());
            if (val != null) {
                edge.setAttribute(col.getId(), val);
            }
        }
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setContainer(ContainerUnloader container) {
        this.container = container;
    }
}
