package com.edwinbustamante.gruposcochalos.Persistencia;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;

/**
 * Created by EDWIN on 4/6/2018.
 */
@Database(entities = {GrupoMusical.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public  abstract GrupoDAO getGrupoDAO();
}
