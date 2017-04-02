/*
 * Copyright 2017 Samebug, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.swing.ui.modules;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class FontService {
    private static final String Regular = "Montserrat Regular";
    private static final String Bold = "Montserrat Medium";

    public static void registerFonts() throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        final InputStream streamBold = FontService.class.getResourceAsStream("/com/samebug/fonts/Montserrat-Medium.ttf");
        final InputStream streamRegular = FontService.class.getResourceAsStream("/com/samebug/fonts/Montserrat-Regular.ttf");

        final Font bold = Font.createFont(Font.TRUETYPE_FONT, streamBold);
        final Font regular = Font.createFont(Font.TRUETYPE_FONT, streamRegular);

        streamBold.close();
        streamRegular.close();

        ge.registerFont(bold);
        ge.registerFont(regular);
    }

    public static Font regular(int size) {
        return new Font(Regular, Font.PLAIN, size - 1);
    }

    public static Font demi(int size) {
        return new Font(Bold, Font.PLAIN, size - 1);
    }
}
