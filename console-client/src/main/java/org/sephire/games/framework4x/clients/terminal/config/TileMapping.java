package org.sephire.games.framework4x.clients.terminal.config;

import com.googlecode.lanterna.TextColor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TileMapping {
    @NonNull
    @Getter
    private Character character;
    @NonNull
    @Getter
    private TextColor color;
}
