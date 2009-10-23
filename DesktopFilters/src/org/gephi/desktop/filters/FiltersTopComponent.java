/*
Copyright 2008 WebAtlas
Authors : Mathieu Bastian, Mathieu Jacomy, Julian Bilcke
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.desktop.filters;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gephi.filters.api.Filter;
import org.gephi.filters.api.FilterBuilder;
import org.gephi.filters.api.FilterModel;
import org.gephi.project.api.ProjectController;
import org.gephi.ui.components.JSqueezeBoxPanel;
import org.gephi.workspace.api.Workspace;
import org.gephi.workspace.api.WorkspaceListener;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

final class FiltersTopComponent extends TopComponent {

    private static FiltersTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "FiltersTopComponent";
    private List<FilterPanel> filtersPanels = new ArrayList<FilterPanel>();
    private FilterModel filterModel;
    private ChangeListener modelListener;

    private FiltersTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(FiltersTopComponent.class, "CTL_FiltersTopComponent"));
        setToolTipText(NbBundle.getMessage(FiltersTopComponent.class, "HINT_FiltersTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

        modelListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                refreshModel(filterModel);
            }
        };

        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.addWorkspaceListener(new WorkspaceListener() {

            public void initialize(Workspace workspace) {
            }

            public void select(Workspace workspace) {
                filterModel = workspace.getWorkspaceData().getData(Lookup.getDefault().lookup(FiltersWorkspaceDataProvider.class).getWorkspaceDataKey());
                filterModel.addChangeListener(modelListener);
                refreshModel(filterModel);
            }

            public void unselect(Workspace workspace) {
                filterModel.removeChangeListener(modelListener);
                filterModel = null;
            }

            public void close(Workspace workspace) {
            }

            public void disable() {
                filterModel = null;
                refreshModel(filterModel);
            }
        });
        refreshModel(filterModel);
    }

    public void refreshModel(FilterModel filterModel) {
        if (filterModel == null) {
            return;
        }
        refreshPanels(filterModel.getFilters());
    }

    private void refreshPanels(Filter[] filters) {
        JSqueezeBoxPanel sbp = (JSqueezeBoxPanel) squeezeBoxPanel;
        for (Iterator<FilterPanel> itr = filtersPanels.iterator(); itr.hasNext();) {
            FilterPanel fp = itr.next();
            boolean found = false;
            for (Filter f : filters) {
                if (fp.getFilter() == f) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                itr.remove();
                sbp.removePanel(fp);
            }
        }
        for (Filter f : filters) {
            boolean found = false;
            for (FilterPanel fp : filtersPanels) {
                if (fp.getFilter() == f) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                FilterBuilder[] filtersBuilder = Lookup.getDefault().lookupAll(FilterBuilder.class).toArray(new FilterBuilder[0]);
                FilterBuilder builder = null;
                for (FilterBuilder fb : filtersBuilder) {
                    if (fb.getFilterClass().isAssignableFrom(f.getClass())) {
                        builder = fb;
                    }
                }
                if (builder != null) {
                    FilterPanel fp = new FilterPanel(f, builder.getUI(f));
                    filtersPanels.add(fp);
                    sbp.addPanel(fp, builder.getName());
                } else {
                    throw new RuntimeException("FilterBuilder cannot be found for Filter class " + f.getClass());
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        filtersPanel = new javax.swing.JPanel();
        filtersToolbar = new FiltersToolbar();
        squeezeBoxPanel = new JSqueezeBoxPanel();
        libraryPanel = new javax.swing.JPanel();
        filtersLibraryExplorer = new org.gephi.desktop.filters.FiltersLibraryExplorer();
        infoPanel = new javax.swing.JPanel();
        contibuteLink = new org.jdesktop.swingx.JXHyperlink();

        setLayout(new java.awt.BorderLayout());

        filtersPanel.setLayout(new java.awt.BorderLayout());

        filtersToolbar.setRollover(true);
        filtersPanel.add(filtersToolbar, java.awt.BorderLayout.NORTH);
        filtersPanel.add(squeezeBoxPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(FiltersTopComponent.class, "FiltersTopComponent.filtersPanel.TabConstraints.tabTitle"), filtersPanel); // NOI18N

        libraryPanel.setLayout(new java.awt.BorderLayout());
        libraryPanel.add(filtersLibraryExplorer, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(FiltersTopComponent.class, "FiltersTopComponent.libraryPanel.TabConstraints.tabTitle"), libraryPanel); // NOI18N

        add(tabbedPane, java.awt.BorderLayout.CENTER);

        infoPanel.setBackground(new java.awt.Color(178, 223, 240));

        contibuteLink.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/desktop/filters/resources/info.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(contibuteLink, org.openide.util.NbBundle.getMessage(FiltersTopComponent.class, "FiltersTopComponent.contibuteLink.text")); // NOI18N
        infoPanel.add(contibuteLink);

        add(infoPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXHyperlink contibuteLink;
    private org.gephi.desktop.filters.FiltersLibraryExplorer filtersLibraryExplorer;
    private javax.swing.JPanel filtersPanel;
    private javax.swing.JToolBar filtersToolbar;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel libraryPanel;
    private javax.swing.JPanel squeezeBoxPanel;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized FiltersTopComponent getDefault() {
        if (instance == null) {
            instance = new FiltersTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the FiltersTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FiltersTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(FiltersTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof FiltersTopComponent) {
            return (FiltersTopComponent) win;
        }
        Logger.getLogger(FiltersTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return FiltersTopComponent.getDefault();
        }
    }

    private static class FilterPanel extends JPanel {

        private Filter filter;

        public FilterPanel(Filter filter, JPanel ui) {
            super(new BorderLayout());
            this.filter = filter;
            add(ui, BorderLayout.CENTER);
        }

        public Filter getFilter() {
            return filter;
        }
    }
}
