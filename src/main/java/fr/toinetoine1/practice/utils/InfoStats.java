package fr.toinetoine1.practice.utils;

import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Toinetoine1 on 31/05/2019.
 */


@Data
@AllArgsConstructor
public class InfoStats {

    private String playerName;
    private RankedPlayerModeInfo info;

}
