/**
 * Copyright 2016 Samebug, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.idea.ui.controller;

import com.intellij.openapi.diagnostic.Logger;
import com.samebug.clients.idea.ui.views.SearchGroupCardView;
import com.samebug.clients.search.api.entities.GroupedExceptionSearch;

import javax.swing.*;

/**
 * Created by poroszd on 3/4/16.
 */
public class SearchGroupCardController {
    final private static Logger LOGGER = Logger.getInstance(SearchGroupCardController.class);
    final private SearchGroupCardView view;
    final private GroupedExceptionSearch model;

    public SearchGroupCardController(GroupedExceptionSearch model) {
        this.view = new SearchGroupCardView(model);
        this.model = model;
    }

    public JPanel getControlPanel() {
        return view.controlPanel;
    }
}
