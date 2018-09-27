/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.scenariosimulation.client.widgets;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;

import com.ait.lienzo.client.core.event.NodeMouseOutEvent;
import com.ait.lienzo.client.core.event.NodeMouseOutHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import org.drools.workbench.screens.scenariosimulation.client.handlers.ScenarioSimulationGridPanelClickHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.GridLienzoPanel;

/**
 * ScenarioGridPanel implementation of <code>GridLienzoPanel</code>.
 * <p>
 * This panel contains a <code>ScenarioGridLayer</code> and it is instantiated only once.
 * The Clicks are managed by the injected <code>ScenarioSimulationGridPanelClickHandler</code>
 */
@Dependent
public class ScenarioGridPanel extends GridLienzoPanel implements NodeMouseOutHandler {

    public static final int LIENZO_PANEL_WIDTH = 1000;

    public static final int LIENZO_PANEL_HEIGHT = 800;

    private EventBus eventBus;
    private ScenarioSimulationGridPanelClickHandler clickHandler;

    public ScenarioGridPanel() {
        super(LIENZO_PANEL_WIDTH, LIENZO_PANEL_HEIGHT);
    }

    public Set<HandlerRegistration> addClickHandler(final ScenarioSimulationGridPanelClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        Set<HandlerRegistration> toReturn = new HashSet<>();
        toReturn.add(getDomElementContainer().addDomHandler(clickHandler,
                                                            ContextMenuEvent.getType()));
        toReturn.add(getDomElementContainer().addDomHandler(clickHandler,
                                                            ClickEvent.getType()));
        toReturn.add(getScenarioGridLayer().addNodeMouseOutHandler(this));
        return toReturn;
    }

    public ScenarioGridLayer getScenarioGridLayer() {
        return (ScenarioGridLayer) getDefaultGridLayer();
    }

    public ScenarioGrid getScenarioGrid() {
        return ((ScenarioGridLayer) getDefaultGridLayer()).getScenarioGrid();
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        getScenarioGrid().getModel().setEventBus(eventBus);
    }

    @Override
    public void onNodeMouseOut(NodeMouseOutEvent event) {
        final int height = getScenarioGridLayer().getHeight();
        final int width = getScenarioGridLayer().getWidth();
        final int x = event.getX();
        final int y = event.getY();
        final int screenY = event.getMouseEvent().getScreenY();
        if (x < 0 || x > width || y < 0 || screenY > height) {
           clickHandler.hideMenus();
       }
    }
}